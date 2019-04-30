package Connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
//UDP发送接收数据包的类
public class UDPConnection {
	//建立UDP链接，参数为udp地址，以及端口号
	private DatagramSocket s;
	private int receivePort;
	//初始化本地端口,建立套接字
	public UDPConnection(int receivePort) {
		this.receivePort = receivePort;
		try {
			this.s = new DatagramSocket();
		} catch (SocketException e) {
			System.out.println("创建套接字失败，请检查端口是否被占用!");
			e.printStackTrace();
		}
	}
	//创建套接字
	public void setSocket(int receivePort) {
		try {
			this.s = new DatagramSocket();
		} catch (SocketException e) {
			System.out.println("创建套接字失败，请检查端口是否被占用!");
			e.printStackTrace();
		}
	}
	//关闭套接字
	public void close() {
		this.s.close();
	}
	//获取套接字端口
	public int getReceivePort() {
		return this.receivePort;
	}
	//发送数据包
	public int send(byte[] data,String udp_url,Integer targetPort) {
		//将数据报发送到目标
		try {
			//创建数据包
			DatagramPacket send_data = new DatagramPacket(data,data.length,InetAddress.getByName(udp_url),targetPort);
			//发送数据包
			this.s.send(send_data);
		} catch (IOException e) {
			System.out.println("发送数据包出错!");
			return 4;
		}
		return 0;
	}
	//接收数据包
	public byte[] receive() {
		while(true){ 
            byte[] buf = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
				this.s.receive(packet);
			} catch (IOException e) {
				System.out.println("接受数据包出错！");
				e.printStackTrace();	
			}
            byte[] data = packet.getData();
            System.out.println("发送方的IP地址:"+packet.getAddress());
            System.out.println("发送方的端口号:"+packet.getPort());
            return data;
        }
	}
}
