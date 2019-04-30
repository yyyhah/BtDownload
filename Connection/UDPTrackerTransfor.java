package Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import TorrentDownload.DecodeChange;
import TorrentDownload.UDPData;
import function.MyFunction;
//遵循UDP tracker协议的地址信息传输
public class UDPTrackerTransfor {
	//将16进制数转化为长度为8个字节的byte数组
	private static byte[] transaction_id =new byte[4];
	public static UDPData udpData = new UDPData();
	public static String udpUrl;//tracker地址
	public static int targetPort;//tracker端口号
	private static int port;//本地监听端口号
	private byte[] infoHashBytes;
	UDPConnection conn;//udp发送接收端口
	static {
		Random rand =new Random(25);
		for(int i=0;i<4;i++)
			transaction_id[i] = (byte) rand.nextInt(128);
	}
	public UDPTrackerTransfor(int port,String hexHash) {
		this.port = port;
		this.infoHashBytes = DecodeChange.HexToByte20(hexHash);
		conn = new UDPConnection(port);
	}
	
	
	//检查建立连接时的返回包数据是否正确
	public Boolean check1(byte[] action,byte[] transaction_id) {
		if(Arrays.equals(action,new byte[]{0,0,0,0})&&Arrays.equals(transaction_id, this.transaction_id)) {
			return true;
		}
		return false;
	}
	public Boolean check2(byte[] action,byte[] transaction_id) {
		if(Arrays.equals(action,new byte[]{0,0,0,1})&&Arrays.equals(transaction_id, this.transaction_id)) {
			return true;
		}
		return false;
	}
	public byte[] startAnnounceRequest() {
		byte[] announce = new byte[100];
		udpData.ann_in.action = new byte[] {0,0,0,1};
		udpData.ann_in.connection_id = udpData.conn_out.connection_id;
		udpData.ann_in.transaction_id = udpData.conn_out.transaction_id;
		udpData.ann_in.downloaded = new byte[] {0,0,0,0,0,0,0,0};
		udpData.ann_in.uploaded = new byte[] {0,0,0,0,0,0,0,0};
		udpData.ann_in.peer_id = MyFunction.createPeerId().getBytes();
		udpData.ann_in.extensiosns = new byte[] {0,0};
		udpData.ann_in.port = new byte[] {(byte) (port/256),(byte) (port%256)};
		udpData.ann_in.IpAddress = new byte[] {0,0,0,0};
		udpData.ann_in.key = new byte[] {1,1,1,1};
		udpData.ann_in.event = new byte[] {0,0,0,(byte)2};
		udpData.ann_in.numWant = new byte[] {0,0,0,(byte)5};
		udpData.ann_in.info_hash = this.infoHashBytes;
		//1651507200
		udpData.ann_in.left = new byte[] {6,2,7,0,0,0,0,0};
		MyFunction.byteCopy(udpData.ann_in.connection_id,0,announce,0,8);
		MyFunction.byteCopy(udpData.ann_in.action,0,announce,8,12);
		MyFunction.byteCopy(udpData.ann_in.transaction_id,0,announce,12,16);
		MyFunction.byteCopy(udpData.ann_in.info_hash,0,announce,16,36);
		MyFunction.byteCopy(udpData.ann_in.peer_id,0,announce,36,56);
		MyFunction.byteCopy(udpData.ann_in.downloaded,0,announce,56,64);
		MyFunction.byteCopy(udpData.ann_in.left,0,announce,64,72);
		MyFunction.byteCopy(udpData.ann_in.uploaded,0,announce,72,80);
		MyFunction.byteCopy(udpData.ann_in.event,0,announce,80,84);
		MyFunction.byteCopy(udpData.ann_in.IpAddress,0,announce,84,88);
		MyFunction.byteCopy(udpData.ann_in.key,0,announce,88,92);
		MyFunction.byteCopy(udpData.ann_in.numWant,0,announce,92,96);
		MyFunction.byteCopy(udpData.ann_in.port,0,announce,96,98);
		MyFunction.byteCopy(udpData.ann_in.extensiosns,0,announce,98,100);
		conn.send(announce,udpUrl,targetPort);
		byte[] data = conn.receive();
		byte[] action_return = new byte[4];
		byte[] transcation_id_return = new byte[4];
		byte[] interval_return = new byte[4];
		byte[] leechers = new byte[4];
		byte[] seeders = new byte[4];
		byte[] ip= new byte[4];
		byte[] port = new byte[2];
		ArrayList<byte[]> ips = new ArrayList<byte[]>();
		ArrayList<byte[]> ports = new ArrayList<byte[]>();
		MyFunction.byteCopy(data, 0, action_return,0,4);
		MyFunction.byteCopy(data, 4, transcation_id_return,0,4);
		MyFunction.byteCopy(data, 8, interval_return,0,4);
		MyFunction.byteCopy(data, 12, leechers,0,4);
		MyFunction.byteCopy(data, 16, seeders,0,4);
		MyFunction.byteCopy(data, 20, ip,0,4);
		MyFunction.byteCopy(data, 24, port,0,2);
		int index = 20;
		while((!Arrays.equals(ip, new byte[] {0,0,0,0}))&&index<data.length-6) {
			ips.add(ip);
			ports.add(port);
			ip= new byte[4];
			port = new byte[2];
			index += 6;
			MyFunction.byteCopy(data, index, ip,0,4);
			MyFunction.byteCopy(data, index+4, port,0,2);
		}
		MyFunction.byteCopy(data, 0, udpData.conn_out.action,0,4);
		MyFunction.byteCopy(data, 4, udpData.conn_out.transaction_id,0,4);
		MyFunction.byteCopy(data, 8, udpData.conn_out.connection_id,0,8);
		if(check2(action_return, transcation_id_return)) {
			System.out.println("建立连接成功！");
			for(int i=0;i<ips.size();i++) {
				byte[] ipAddress = ips.get(i);
				String ip1 = "";
				for(int j=0;j<4;j++) {
					ip1 += ipAddress[j]& 0xff;//无符号二进制转10进制
					if(j!=3)
						ip1 += ".";
				}
				String port1 = "";
				port1 += (ports.get(i)[0]& 0xff)*128+(ports.get(i)[1]& 0xff); 
				System.out.println("ip"+i+":"+ip1+" port:"+port1);
			}
		}else {
			System.out.println("建立连接失败！");
		}
		return data;
	}
	//和tracker服务器建立链接,返回获取的byte数组,announce为种子中的服务器地址，port为与tracker传输的端口号
	public byte[] setUpLink(String announce) {
		byte[] connection_id = DecodeChange.HexToByte8("41727101980");
		byte[] action = {0,0,0,0};
		byte[] packetData = new byte[16];
		MyFunction.byteCopy(connection_id,0,packetData,0,8);
		MyFunction.byteCopy(action,0,packetData,8,12);
		MyFunction.byteCopy(transaction_id,0,packetData,12,16);
		String regEx = "//(.*?):(\\d*)";  
		Pattern pat = Pattern.compile(regEx);  
        Matcher mat = pat.matcher(announce);  
        if(mat.find()){
        	udpUrl = mat.group(1);
    		targetPort = Integer.parseInt(mat.group(2));
    		System.out.println("udpUrl "+udpUrl+",targetPort "+targetPort);
        }else {
        	System.out.println("未匹配到有效链接");
        	return null;
        }
		conn.send(packetData,udpUrl, targetPort);
		byte[] data = conn.receive();
		MyFunction.byteCopy(data, 0, udpData.conn_out.action,0,4);
		MyFunction.byteCopy(data, 4, udpData.conn_out.transaction_id,0,4);
		MyFunction.byteCopy(data, 8, udpData.conn_out.connection_id,0,8);
		if(check1(udpData.conn_out.action, udpData.conn_out.transaction_id)) {
			System.out.println("建立连接成功！");
		}else {
			System.out.println("建立连接失败！");
		}
		return data;
	}
	//public byte[] sendAnnounce(String announce) {
	//}
}
