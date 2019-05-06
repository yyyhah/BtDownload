//package Connection;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.InetAddress;
//import java.net.InetSocketAddress;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.net.SocketException;
//import java.util.ArrayList;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import TorrentDownload.DecodeChange;
//import function.MyFunction;
//
//public class TCPPeerConnection {
//	private  Socket sendSocket = null;
//	private  ServerSocket receiveSocket = null;
//	private ServerSocket serverSocket = null;
//	private int sendPort,receivePort;
//	private byte[] bitField = null;
//	public TCPPeerConnection(String ipAddress,int sendPort,int receivePort) {
//		//建立tcp连接
//		this.sendPort = sendPort;
//		this.receivePort = receivePort;
//		this.sendSocket = new Socket();
//		try {
//			//绑定本地连接端口
//			sendSocket.bind(new InetSocketAddress(InetAddress.getLocalHost(),receivePort));
//			System.out.println("创建套接字成功！");
//		} catch (IOException e) {
//			System.out.println("本地端口已经被占用!");
//		}
//		try {
//			//建立tcp连接
//			sendSocket.connect(new InetSocketAddress(ipAddress, sendPort));
//			System.out.println("建立连接成功！");
//		} catch (Exception e) {
//			System.out.println("建立tcp连接失败!");
//			try {
//				this.sendSocket.close();
//			} catch (IOException e1) {
//				System.out.println("关闭套接字失败！");
//			}
//		}		
//	}
//	
//	public void reset(String ipAddress,int sendPort) {
//		try {
//			//关闭原套接字连接
//			this.sendSocket.close();
//		} catch (IOException e1) {
//			System.out.println("套接字关闭失败!");
//		}
//		try {
//			//绑定本地连接端口
//			sendSocket.bind(new InetSocketAddress(InetAddress.getLocalHost(),receivePort));
//			System.out.println("创建套接字成功！");
//		} catch (IOException e) {
//			System.out.println("本地端口已经被占用!");
//		}
//		try {
//			//建立tcp连接
//			sendSocket.connect(new InetSocketAddress(ipAddress, sendPort));
//			System.out.println("建立连接成功！");
//		} catch (Exception e) {
//			System.out.println("建立tcp连接失败!");
//		}finally {
//			try {
//				this.sendSocket.close();
//			} catch (IOException e) {
//				System.out.println("关闭套接字失败！");
//			}
//		}
//	}
//	
//	
//	//发送握手请求
//	public void peerHandshake(String hash,int pieceNum) {
//		//如果刚开始未下载片段，初始化bitField
//		if(bitField==null) {
//			this.bitField = new byte[pieceNum+5];
//			bitField[4] = (byte)5;
//			bitField[0] = (byte) (pieceNum/Math.pow(256, 3));
//			bitField[1] = (byte) (pieceNum/Math.pow(256, 2));
//			bitField[2] = (byte) (pieceNum/Math.pow(256, 1));
//			bitField[3] = (byte) (pieceNum/Math.pow(256, 0));
//		}
//		if (sendSocket != null && sendSocket.isConnected()) {
//            new HandshakeThread(this.sendSocket,hash,bitField).start();
//            
////            if(sendSocket.isConnected()) {
////            	new SendThread(this.sendSocket,bitField).start();
////            	new RecvThread(this.sendSocket,bitField).start();
////            	new KeepAliveThread(this.sendSocket).start();
////            }
//        }
//	}
//	
//}
//
