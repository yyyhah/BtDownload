package Connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import TorrentDownload.DecodeChange;
import function.MyFunction;

public class TCPPeerConnection {
	private  Socket sendSocket = null;
	private  ServerSocket receiveSocket = null;
	private ServerSocket serverSocket = null;
	private int sendPort,receivePort;
	public TCPPeerConnection(String ipAddress,int sendPort,int receivePort) {
		//建立tcp连接
		this.sendPort = sendPort;
		this.receivePort = receivePort;
		this.sendSocket = new Socket();
		try {
			//绑定本地连接端口
			sendSocket.bind(new InetSocketAddress(InetAddress.getLocalHost(),receivePort));
			System.out.println("创建套接字成功！");
		} catch (IOException e) {
			System.out.println("本地端口已经被占用!");
		}
		try {
			//建立tcp连接
			sendSocket.connect(new InetSocketAddress(ipAddress, sendPort));
			System.out.println("建立连接成功！");
		} catch (IOException e) {
			System.out.println("建立tcp连接失败!");
		}
		
		//创建本地监听端口
//		try {
//			this.receiveSocket =new ServerSocket(receivePort);
//			System.out.println("创建本地监听端口 "+receivePort+" 成功！");
//		} catch (IOException e) {
//			System.out.println("创建本地监听端口 "+receivePort+" 失败！");
//		}
	}
	//握手请求包封装类
	class HandshakePacket{
		private byte pstrlen = (byte)19;
		private byte[] pstr = "BitTorrent protocol".getBytes();
		private byte[] reserved = new byte[8];
		private byte[] infoHash = new byte[20];
		private byte[] peer_id = new byte[20];
		private byte[] packet = new byte[68];
		HandshakePacket(String Hash) {
			peer_id = MyFunction.createPeerId().getBytes();
			infoHash = DecodeChange.HexToByte20(Hash);
			packet[0] = pstrlen;
			MyFunction.byteCopy(pstr, 0, packet, 1, 20);
			MyFunction.byteCopy(reserved, 0, packet, 20, 28);
			MyFunction.byteCopy(infoHash, 0, packet, 28, 48);
			MyFunction.byteCopy(peer_id, 0, packet, 48, 68);
		}
		HandshakePacket(){
			
		}
		public byte[] getPacket() {
			System.out.println("发送握手包");
			for(byte b:packet) {
				System.out.print(b);
				System.out.print(" ");
			}
			return packet;
		}
		public void setPacket(byte[] packet) {
			this.packet = packet;
		}
	}
	//发送数据包
	class SendThread extends Thread{
		private Socket sk;
		private ArrayList<Byte> bitField = new ArrayList<Byte>();
		private ArrayList<Byte> commends = new ArrayList<Byte>();
		private HandshakePacket handShakePacketS;//发送的握手包
		private HandshakePacket handShakePacketR;//返回的握手包
	    SendThread(Socket sk,String hash)
	    {
	        this.sk=sk;
	        //初始化握手请求包
	        this.handShakePacketS = new HandshakePacket(hash);
	        this.handShakePacketR = new HandshakePacket();
	    }
	    
		public void run() {
            try {
                OutputStream os = this.sk.getOutputStream();
                os.write(this.handShakePacketS.getPacket());
                os.flush();
                int ret;
                int index=0;
                int length = 0;
                this.sk.setSoTimeout(3000);
        		InputStream is = this.sk.getInputStream();
        		System.out.println("返回消息");
        		byte[] handshakeTemp = new byte[68];
        		
                try {
        			while((ret=is.read())!=-1) {
        				if(index < 68) {
        					handshakeTemp[index] = (byte)ret;
        				}else if(index >=68 && index <=71){
        					length += Math.pow(256,71-index)*ret;
        				}else if(index>=72 && index <72+length){
        					bitField.add((byte)ret);
        				}else {
        					commends.add((byte)ret);
        				}
        				System.out.print(ret);
    	                System.out.print(" ");
    	                index++;
        			   }
        			this.handShakePacketR.setPacket(handshakeTemp);
        		} catch (Exception e) {
        			System.out.println("读取结束!");
        		}
                finally {
        			is.close();
        		}  
                System.out.print("\nhandshake:");
	            for(byte b:handshakeTemp) {
	                System.out.print(b);
	                System.out.print(" ");
	            }
                System.out.print("\nbitField:");
	            for(byte b:bitField) {
	                System.out.print(b);
	                System.out.print(" ");
	              }
	            System.out.print("\nunkonwndata:");
	            for(byte b:commends) {
	                System.out.print(b);
	                System.out.print(" ");
	              }
	            System.out.println("数据总长:"+index);
	            System.out.println("数据总长:"+handshakeTemp.length +" "+bitField.size()+" "+commends.size());
            } catch (IOException e) {
            	System.out.println("信息发送失败!");
                e.printStackTrace();
            }
        }
	}
	
	//处理接收信息类
	class ServerThread extends Thread
	{
	    private Socket sk;
	    ServerThread(Socket sk)
	    {
	        this.sk=sk;
	    }
	    //线程运行实体
	    public void run() {
	    	byte[] data;
	    	InputStream is = null;
	    	OutputStream os = null;
	        try {
	        	//socket的输入接口
	            is = sk.getInputStream();
	            //socket的输出接口
	            os = sk.getOutputStream();
	            //接收消息
	            while(true)
	            {
	                int ret;
	                int index = 0;
	                data = new byte[2048];
	                while((ret=is.read())!=-1) {
	                	data[index] = (byte)ret;
	                	index++;
	                }
	                for(byte b:data) {
	                	System.out.print(b);
	                	System.out.print(" ");
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        finally{
	            try {
	                if (is!=null) {
	                    is.close();
	                }
	                if (os!=null) {
	                    os.close();
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}
	
	
	//作为接收方时
	public void peerReceive() {
		//接收端口数目
		int count = 0; 
        //处理socket请求
        Socket socket = null;
        while (this.receiveSocket!=null) {
            try {
				socket = receiveSocket.accept();
			} catch (IOException e) {
				System.out.println("监听到请求，创建新的socket端口！");
			}
            //分出一个线程处理连接请求
            ServerThread serverThread = new ServerThread(socket);
            System.out.println("client host address is: " + socket.getInetAddress().getHostAddress());
            serverThread.start();
            count++;
            System.out.println("now client count is: " + count);
        }
	}
	//发送握手请求
	public void peerHandshake(String hash) {
		//套接字建立
		if (sendSocket != null && sendSocket.isConnected()) {
            new SendThread(this.sendSocket,hash).start();
        }
	}
	
}

