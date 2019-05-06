package Connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import TorrentDownload.DecodeChange;
import function.MyFunction;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ConnectionThread extends Thread {
	private Socket sk = null;
	private InputStream is = null;
	private OutputStream os = null;
	private String hash = null;
	private byte[] bitField = null;
	private JSONObject tinfo = null;
	private String path = null;

	public ConnectionThread(Socket sk, String hash, byte[] bitField, JSONObject tinfo, String path) {
		this.sk = sk;
		this.hash = hash;
		this.bitField = bitField;
		this.tinfo = tinfo;
		this.path = path;
		if (sk != null && !sk.isClosed()) {
			try {
				this.sk.setSoTimeout(100000);
				this.is = this.sk.getInputStream();
				this.os = this.sk.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void send(byte[] data) {
		try {
			synchronized (os) {
				os.write(data);
				os.flush();
			}
		} catch (IOException e) {
			System.out.println("发送消息出错!");
			e.printStackTrace();
		}
	}

	public void run() {
		new HandshakeThread(hash, bitField, tinfo, path).start();
		// 心跳包线程
		new KeepAliveThread().startHeartBeatThread();
	}

	// 握手消息线程，发送握手请求，接收握手请求并分析其正确性，如正确，发送bitField包,接收bitField包，匹配，检查，发送interested或uninterested包
	class HandshakeThread extends Thread {
		private ArrayList<Byte> commends = new ArrayList<Byte>();
		private HandshakePacket handShakePacketS;// 发送的握手包
		private HandshakePacket handShakePacketR;// 返回的握手包
		private ArrayList<Integer> requestPiece = new ArrayList<Integer>();// 待获取的片段
		private byte[] bitFieldR = null;
		private byte[] bitFieldbytesR = null;
		private byte[] bitFieldbytesS = null;
		private String infohash = null;
		private RequestsPacket rp = new RequestsPacket();

		private int pieceLen = 0;// 片段长度
		private JSONArray hashList = null;// 每个片段的hash校验值
		private String path = null;

		HandshakeThread(String hash, byte[] bitField, JSONObject tinfo, String path) {
			// 初始化握手请求包
			this.infohash = hash;
			this.path = path;
			this.bitFieldbytesS = bitField;
			this.bitFieldR = new byte[this.bitFieldbytesS.length / 8];
			this.handShakePacketS = new HandshakePacket(hash);
			this.handShakePacketR = new HandshakePacket();
			this.pieceLen = tinfo.getInt("piece length");// 片段长度
			this.hashList = tinfo.getJSONArray("pieces");
			// 设置读取超时时间
			initSocketIO();
		}

		// 检验握手信息包
		private Boolean checkHandshake(byte[] hspacket) {
			// 如果握手信息包为空
			if (hspacket == null) {
				System.out.println("握手消息出错,关闭socket");
				return false;
			}
			byte[] hash = new byte[20];
			MyFunction.byteCopy(hspacket, 28, hash, 0, 20);
			// 检验hash特征码是否和我需要的一致
			byte[] hashpacket = DecodeChange.HexToByte20(infohash);
			if (!Arrays.equals(hash, hashpacket)) {
				System.out.println("hash特征码不匹配!");
				return false;
			}
			return true;
		}

		// 从输入流中读取数据
		public byte[] readRet(int length) {
			byte[] get = new byte[length];
			int ret = 0;
			try {
				int index = 0;
				synchronized (is) {
					while ((ret = is.read()) != -1) {
						get[index++] = (byte)ret;
						if(index==length) {
							break;
						}
					}
				}
				if(index==length)
					return get;
				else
					return null;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		public void sendInfo(String type) throws IOException {
			synchronized (os) {
				if (type.equals("choke"))
					os.write(this.rp.getChoke());
				else if (type.equals("unchoke")) {
					os.write(this.rp.getUnchoke());
				} else if (type.equals("interested")) {
					os.write(this.rp.getInterested());
				} else if (type.equals("uninterested")) {
					os.write(this.rp.getUninterseted());
				} else if (type.equals("hava")) {
					os.write(this.rp.getHave());
				}
			}
		}

		// 从socket输入流中获取握手包信息
		private byte[] getHandshake(InputStream is) {
			int ret;
			int index = 0;
			byte[] handshakeTemp = new byte[68];
			if ((handshakeTemp = readRet(68)) == null) {
				System.out.println("返回的握手信息包有误!");
				return null;
			}
			return handshakeTemp;
		}

		// 比较自己的bitField和对方的区别,造出自己没有，对方有的包
		private Boolean compareSRBitField(byte[] bitFieldbytesS, byte[] bitFieldbytesR) {
			int count = 0;
			for (int i = 0; i < bitFieldbytesS.length; i++) {
				if (bitFieldbytesS[i] == 0 && bitFieldbytesR[i] == 1) {
					synchronized (requestPiece) {
						requestPiece.add(i);
					}
					count++;
				}
			}
			if (count == 0) {
				System.out.println("对方没有我需要的pieces");
				return false;
			}
			return true;

		}

		private int getPiece() {
			// 当向一个peer发送一个piece请求时,需要将本地的bitfield置为2;这样子其他线程就会跳过该请求
			this.bitFieldbytesS[this.requestPiece.get(0)] = 2;
			int remove = -1;
			synchronized (requestPiece) {
				if (requestPiece.size() > 0)
					remove = requestPiece.remove(0);
			}
			return remove;
		}

		private void initSocketIO() {
			try {
				synchronized (sk) {
					sk.setSoTimeout(100000);
					is = sk.getInputStream();
					os = sk.getOutputStream();
				}
			} catch (SocketException e) {
				System.out.println("设置套接字读取时间出错!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("初始化输入输出流出错!");
			}

		}

		private void socketClose() {
			try {
				synchronized (is) {
					is.close();
				}
				synchronized (os) {
					os.close();
				}
				synchronized (sk) {
					sk.close();
				}
			} catch (IOException e) {
				System.out.println("关闭套接字出错!");
				e.printStackTrace();
			}
		}

		public void run() {
			// 初始化套接字的读取端口和输出端口
			if (sk != null && !sk.isClosed()) {
				
				// 发送握手请求
				send(handShakePacketS.getPacket());
				// 接收握手请求
				byte[] handshakeTemp = null;
				if ((handshakeTemp = getHandshake(is)) == null)
					return;
				// 如果校验通过，则保存握手信息，并发送本地的bitfield信息给对方，如果校验不通过，则关闭socket连接
				if (checkHandshake(handshakeTemp)) {
					// 保存对方的握手包
					handShakePacketR.setPacket(handshakeTemp);
					/// 发送给对方自己的bitField
					send(DecodeChange.binaryToBytes(bitFieldbytesS));
				} else {
					socketClose();
					return;
				}

				System.out.print("\nhandshake:");
				for (byte b : handshakeTemp) {
					System.out.print(b);
					System.out.print(" ");
				}
				System.out.println();
			} else {
				return;
			}
			// 标记是否接收到bitfield数据
			byte[] getBitField = new byte[1];
			// 是否被阻塞
			byte[] isUnchoke = new byte[1];
			// requests请求是否已返回对应piece
			byte[] getPiece = new byte[] { 0 };
			new RequestThread(getBitField, isUnchoke, getPiece).start();
			new RecvThread(getBitField, isUnchoke, getPiece).start();
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}

		}

		// 发送请求消息
		class RequestThread extends Thread {
			private byte[] getBitField = null;
			private byte[] isUnchoke = null;
			private byte[] getRequest = null;

			RequestThread(byte[] getBitField, byte[] isUnchoke, byte[] getRequest) {
				this.getBitField = getBitField;
				this.isUnchoke = isUnchoke;
				this.getRequest = getRequest;
			}

			public void run() {
				RequestsPacket rp = new RequestsPacket();
				// 设置一次请求的长度为32kB
				int length = 32768;
				// 一个片段需要请求的次数
				int requestCount = pieceLen / length;
				// 当连接未断开
				while (!sk.isClosed()) {
					// 如果获取到对方发送过来的bitField
					if (this.getBitField[0] == 1) {
						synchronized (this.getBitField) {
							this.getBitField[0] = 0;
						}
						// 如果对方没有我需要的piece，那就发送uninterested信号，反之，发送interested信号
						if (!compareSRBitField(bitFieldbytesS, bitFieldbytesR)) {
							try {
								if (!sk.isClosed())
									sendInfo("uninterested");
							} catch (IOException e) {
								System.out.println("发送uninterested信号失败!");
								socketClose();
								return;
							}
						} else {
							try {
								if (!sk.isClosed()) {
									sendInfo("interested");
								}
							} catch (IOException e) {
								System.out.println("发送interested信号失败!");
								socketClose();
								return;
							}
						}
					}
					int count = 5;// 记录请求的数据数目
					if (requestPiece != null && requestPiece.size() > 0 && this.isUnchoke[0] == 1
							&& this.getRequest[0] == 0) {
						int pieceIndex = requestPiece.get(0) - 40;
						int begin = 0;
						int i = 0;
						// 从上次未下载完的地方继续下载
						File f = new File(path + "/" + pieceIndex + ".piece");
						if (f.exists()) {
							i = (int) Math.floor(f.length() / length);
						}
//						for (; i <= requestCount; i++) {
//							begin = length * i;
//							rp.setRequest(pieceIndex, begin, length);
//							send(rp.getRequest());
//							count++;
//							System.out.println("发送requests消息,piece index:" + pieceIndex);
//						}
						for (int j = 0; j < count; j++) {
							if (i < requestCount) {
								begin = length * i;
								rp.setRequest(pieceIndex, begin, length);
								send(rp.getRequest());
								i++;
								synchronized (this.getRequest) {
									this.getRequest[0]++;
								}
								System.out.println("发送requests消息,piece index:" + pieceIndex + " piece偏移" + begin);
							} else {
								System.out.println("pieces " + pieceIndex + "全部请求已发出");
							}
						}

					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("请求结束!");
			}
		}

		// 接收信息线程
		class RecvThread extends Thread {
			private byte[] getBitField;
			private byte[] isUnchoke;
			private byte[] getRequest;

			RecvThread(byte[] getBitField, byte[] isUnchoke, byte[] getRequest) {
				this.getBitField = getBitField;
				this.isUnchoke = isUnchoke;
				this.getRequest = getRequest;
			}

			private void chokeHandle() {
				System.out.println("对方choke了我方请求!");
				synchronized (this.isUnchoke) {
					this.isUnchoke[0] = 0;
				}
				// socketClose();
			}

			private void unchokeHandle() {
				synchronized (this.isUnchoke) {
					this.isUnchoke[0] = 1;
				}
				System.out.println("对方unchoke了我方请求!");
			}

			private void interestedHandle() {
				System.out.println("对方interest!");
			}

			private void uninterestedHandle() {
				System.out.println("对方uninterest!");
			}

			private void haveHandle() {
				System.out.println("对方发送了have请求!");
				byte[] get;
				if ((get = readRet(4)) != null) {
					requestPiece.add(DecodeChange.byteArrayToInt(get));
				} else {
					System.out.println("出错,have请求未发现下标");
					socketClose();
				}
			}

			private void bitFieldHandle(byte[] head) {
				System.out.println("对方发送了bitfield！");
				synchronized (bitFieldR) {
					int length = DecodeChange.byteArrayToInt(head);
					bitFieldR = new byte[length + 4];
					int ret = 0;
					int index = 0;
					MyFunction.byteCopy(head, 0, bitFieldR, 0, 4);
					bitField[4] = 5;
					byte[] block = new byte[length - 1];
					if ((block = readRet(length - 1)) == null) {
						return;
					}
					MyFunction.byteCopy(block, 0, bitFieldR, 5, length + 4);
					bitFieldbytesR = DecodeChange.bytesToBinary(bitFieldR);
				}
				System.out.print("\nbitFieldR:");
				for (byte b : bitFieldR) {
					System.out.print(b);
					System.out.print(" ");
				}
				System.out.println();
				synchronized (this.getBitField) {
					this.getBitField[0] = 1;
				}
			}

			private void requestHandle() {
				byte[] index = null;
				byte[] begin = null;
				byte[] length = null;
				if ((index = readRet(4)) == null) {
					System.out.println("request请求未发现下标!");
					socketClose();
					return;
				}
				if ((begin = readRet(4)) == null) {
					System.out.println("request请求未发现起始位置!");
					socketClose();
					return;
				}
				if ((length = readRet(4)) == null) {
					System.out.println("request请求未发现长度!");
					socketClose();
					return;
				}
				int indexInt = DecodeChange.byteArrayToInt(index);
				int lengthInt = DecodeChange.byteArrayToInt(length);
				int beginInt = DecodeChange.byteArrayToInt(begin);
				byte[] block = new byte[lengthInt];
				byte[] sendPiecePack = new byte[13 + lengthInt];
				int size = 0;
				if ((size = ReadFromFile(block, beginInt, lengthInt, path + "/" + indexInt + ".piece")) == 0) {
					return;
				}

				MyFunction.byteCopy(DecodeChange.intToByteArray(9 + lengthInt), 0, sendPiecePack, 0, 4);
				sendPiecePack[4] = 7;
				MyFunction.byteCopy(index, 0, sendPiecePack, 5, 9);
				MyFunction.byteCopy(begin, 0, sendPiecePack, 9, 13);
				MyFunction.byteCopy(block, 0, sendPiecePack, 13, 13 + lengthInt);
				send(sendPiecePack);
			}

			private void pieceHandle(byte[] head) {
				byte[] index = null;
				byte[] begin = null;
				int length = DecodeChange.byteArrayToInt(head) - 9;
				if ((index = readRet(4)) == null) {
					System.out.println("piece请求未发现下标!");
					socketClose();
					return;
				}
				if ((begin = readRet(4)) == null) {
					System.out.println("piece请求未发现起始位置!");
					socketClose();
					return;
				}
				int indexInt = DecodeChange.byteArrayToInt(index);
				int beginInt = DecodeChange.byteArrayToInt(begin);
				byte[] block = new byte[length];
				if ((block = readRet(length)) == null) {
					System.out.println("piece请求未发现block");
					return;
				}
				long size = 0;
				if (bitFieldbytesS[indexInt] != 1) {
					size = writeToFile(block, path + "/" + indexInt + ".piece");
					if (size / pieceLen == 1) {
						System.out.println("下载片段" + indexInt + "成功!");
						bitFieldbytesS[indexInt] = 1;
						getPiece();
					}
				}
				synchronized (this.getRequest) {
					this.getRequest[0]--;
				}
				// 将该片段的bitField置为1
			}

			private void cancelHandle() {
				byte[] index = null;
				byte[] begin = null;
				byte[] length = null;
				if ((index = readRet(4)) == null) {
					System.out.println("request请求未发现下标!");
					socketClose();
					return;
				}
				if ((begin = readRet(4)) == null) {
					System.out.println("request请求未发现起始位置!");
					socketClose();
					return;
				}
				if ((length = readRet(4)) == null) {
					System.out.println("request请求未发现长度!");
					socketClose();
					return;
				}
				System.out.println("对方下载完毕!");
			}

			private void extendedHandle(byte[] head) {
				int lengthInt = DecodeChange.byteArrayToInt(head);
				lengthInt = lengthInt > 0 ? lengthInt : 1;
				byte[] extend = new byte[lengthInt - 1];
				if ((extend = readRet(lengthInt - 1)) == null) {
					System.out.println("extended请求未发现extend!");
				}
			}

			private void portHandle(byte[] head) {
				int lengthInt = DecodeChange.byteArrayToInt(head);
				lengthInt = lengthInt > 0 ? lengthInt : 1;
				byte[] port;
				try {
					port = new byte[lengthInt - 1];
					if ((port = readRet(lengthInt - 1)) == null) {
						System.out.println("port请求未发现port!");
					}
				} catch (Exception e) {
					System.out.println(lengthInt + "===========================");
				}

			}

			private void unknownHandle(byte[] head) {
				int lengthInt = DecodeChange.byteArrayToInt(head);
				lengthInt = lengthInt > 0 ? lengthInt : 1;
				byte[] unknown;
				try {
					unknown = new byte[lengthInt - 1];
					if ((unknown = readRet(lengthInt - 1)) == null) {
						System.out.println("unkonwn请求未发现playout!");
					}
				} catch (Exception e) {
					System.out.println(lengthInt + "========================");
				}

			}

			private int ReadFromFile(byte[] piece, int begin, int length, String path) {
				File file = new File(path);
				// 如果文件不存在则创建一个
				if (!file.exists()) {
					System.out.println("没有该片段!");
					return 0;
				}
				int ret = 0;
				int count = 0;
				int index = 0;
				InputStream in;
				try {
					in = new FileInputStream(file);
					while ((ret = in.read()) != -1) {
						if (count >= begin && count < begin + length) {
							piece[index++] = (byte) ret;
						}
						count++;
					}
					return count;
				} catch (FileNotFoundException e) {
					System.out.println("找不到文件！");
					e.printStackTrace();
					return 0;
				} catch (IOException e) {
					System.out.println("读取磁盘文件出错!");
					e.printStackTrace();
					return 0;
				}
			}

			private long writeToFile(byte[] piece, String path) {
				File file = new File(path);
				// 如果文件不存在则创建一个
				if (!file.exists()) {
					try {
						file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					FileOutputStream fileOutputStream1 = new FileOutputStream(file, true);
					fileOutputStream1.write(piece);
					fileOutputStream1.flush();
					fileOutputStream1.close();
					return file.length();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					return 0;
				} catch (IOException e) {
					e.printStackTrace();
					return 0;
				}
			}

			private void receiveHandler(byte[] head) {
				int ret = 0;
				byte[] type = null;
				if ((type = readRet(1)) == null) {
					System.out.println("未发现type类型数,关闭套接字");
					socketClose();
					return;
				}
				switch (type[0]) {
				case 0:
					chokeHandle();
					break;
				case 1:
					unchokeHandle();
					break;
				case 2:
					interestedHandle();
					break;
				case 3:
					uninterestedHandle();
					break;
				case 4:
					haveHandle();
					break;
				case 5:
					bitFieldHandle(head);
					break;
				case 6:
					requestHandle();
					break;
				case 7:
					pieceHandle(head);
					break;
				case 8:
					cancelHandle();
					break;
				case 9:
					portHandle(head);
					break;
				case 20:
					extendedHandle(head);
					break;
				default:
					unknownHandle(head);
					System.out.println("其他类型!");
				}
			}

			public void run() {
				while (!sk.isClosed()) {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					int ret = 0;
					byte[] head = null;
					// 读取前4个字节
					if ((head = readRet(4)) != null) {
						// 如果前4个字节为0，表示为心跳包，可忽略
						if (!Arrays.equals(head, new byte[] { 0, 0, 0, 0 })) {
							System.out.println("读取到返回数据!");
							receiveHandler(head);
						} else {
							System.out.println("读取到心跳包!");
						}
					} else {
						System.out.println("读取返回数据出错!或超时未回复！");
					}
				}
				System.out.println("接收结束!");

			}
		}

	}

	// 心跳包线程
	class KeepAliveThread {
		private long timeInterval = 100000;
		// private OutputStream os = null;
		// private Socket sk = null;

		KeepAliveThread() {
			if (os==null && sk != null && !sk.isClosed()) {
				try {
					os = sk.getOutputStream();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void startHeartBeatThread() {
			// 启动心跳线程
			Timer heartBeatTimer = new Timer();
			TimerTask heartBeatTask = new TimerTask() {
				@Override
				public void run() {
					if (os != null && sk != null && !sk.isClosed()) {
						send(new byte[4]);
					}

				}
			};
			heartBeatTimer.schedule(heartBeatTask, 100000, 100000);
		}
	}
}
