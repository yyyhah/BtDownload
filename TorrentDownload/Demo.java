package TorrentDownload;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import Connection.HTTPTrackerTransfor;
import Connection.TCPPeerConnection;
import Connection.UDPTrackerTransfor;
import function.MyFunction;
import net.sf.json.JSONObject;
import java.util.Random;
public class Demo {
	
	public static void main(String[] args) {
		int port = 10086;
		String hash = "3afe5c4ae4ce9f136a289f30a530e5bbc400c99e";
//		byte[] getTorrent = GetTorrent.downTorrent(hash);
//		RWDTorrent.download(getTorrent, "E:/test2.torrent");
//		char[] torrentChar = RWDTorrent.read("E:/test2.torrent");
////		//char[] torrentChar = RWDTorrent.readFromGet(getTorrent);
//		if(torrentChar==null) {
//			System.out.println("该文件不存在");
//			return;
//		}
//		String torrent = new String(torrentChar);
//		//解析种子
//		String result1 = ParseTorrent.ParseBencode(torrent);
//		if(result1==null) {
//			System.out.println("文件解析出错!");
//		}else {
//			System.out.println("文件解析成功!");
//		}	
//		//将解析出来的种子文本存入txt文件
//		RWDTorrent.write(Regex.cleanData(result1),"E:/temp2.txt");
//		JSONObject torrentJson = null;
//		//显示种子结构
//		try {
//			torrentJson = ParseTorrent.showTorrent(result1);
//		}catch(Exception e) {
//			System.out.println("种子文件非标准!");
//		}
//		JSONObject info = (JSONObject)torrentJson.getJSONObject("info");
//		List pieces	= (List)info.getJSONArray("pieces");
//		//片段数目
//		int pieceLen = pieces.size();
		//torrentJson.get("announce-list");
		//String[] url1s = new String[] { "http://peersteers.org/announce",  "https://1.tracker.animmouse.tk/announce", "https://2.tracker.animmouse.tk/announce", "http://tracker.tfile.co/announce",  "https://tracker.fastdownload.xyz/announce", "http://open.trackerlist.xyz/announce", "https://t.quic.ws/announce", "http://t.acg.rip:6699/announce",  "http://torrent.nwps.ws/announce",   "https://opentracker.xyz/announce",            "http://vps02.net.orel.ru/announce", "http://t.nyaatracker.com/announce", "https://seeders-paradise.org/announce",        "http://tracker.internetwarriors.net:1337/announce", "http://explodie.org:6969/announce", "http://tracker1.itzmx.com:8080/announce", "http://tracker.port443.xyz:6969/announce",  "http://tracker3.itzmx.com:6961/announce", "http://retracker.telecom.by/announce", "http://opentracker.xyz/announce", "http://open.acgnxtracker.com/announce",       "http://tracker4.itzmx.com:2710/announce", "http://tracker2.itzmx.com:6961/announce", "http://tracker1.wasabii.com.tw:6969/announce", "http://tracker.tvunderground.org.ru:3218/announce", "http://tracker.torrentyorg.pl/announce", "http://tracker.city9x.com:2710/announce", "http://torrentclub.tech:6969/announce", "http://retracker.mgts.by/announce", "http://private.minimafia.nl:443/announce", "http://prestige.minimafia.nl:443/announce", "http://open.acgtracker.com:1096/announce", "http://fxtt.ru/announce", "http://bittracker.ru/announce", "http://0d.kebhana.mx:443/announce", "wss://tracker.openwebtorrent.com:443/announce", "wss://tracker.fastcast.nz:443/announce", "wss://tracker.btorrent.xyz:443/announce", "wss://ltrackr.iamhansen.xyz:443/announce",     "https://1337.abcvg.info/announce", "http://tracker.tfile.me/announce.php", "http://share.camoe.cn:8080/announce", "http://agusiq-torrents.pl:6969/announce",   "http://185.225.17.100:1337/announce",      "http://184.105.151.164:6969/announce", "http://128.1.203.23:8080/announce",        "http://51.38.184.185:6969/announce",         "https://104.31.70.251/announce", "https://104.28.2.5/announce", "http://45.119.209.194:6961/announce", "http://213.184.225.53/announce", "http://104.28.3.5/announce", "http://104.18.49.52/announce", "http://51.68.122.172/announce", "http://104.31.141.11/announce",       "http://91.121.255.214:2710/announce", "http://43.241.48.214:6961/announce", "http://210.244.71.25:6969/announce", "http://91.217.91.21:3218/announce", "http://62.210.202.61/announce", "http://64.78.163.242:2710/announce", "http://45.56.74.11:6969/announce", "http://54.39.98.124/announce", "http://82.209.230.66/announce", "http://5.2.70.184:443/announce", "http://62.210.177.88:1096/announce", "http://195.88.220.21/announce", "http://68.183.220.172/announce", "http://158.69.62.21:443/announce", "wss://51.15.179.14:443/announce", "wss://144.6.227.30:443/announce", "wss://45.32.219.126:443/announce", "wss://104.28.12.18:443/announce",     "https://104.31.91.56/announce", "http://185.217.0.76/announce.php", "http://185.217.0.76/announce", "http://185.217.0.78/announce", "http://198.251.81.243:8080/announce", "http://185.217.0.77/announce", "http://188.68.224.156:6969/announce",     "http://tracker.btsync.gq:233/announce", "http://amigacity.xyz:6969/announce"};
		//String[] url2s = new String[] {"udp://tracker.cyberia.is:6969/announce",     "udp://bittracker.ru:6969/announce",  "udp://open.demonii.si:1337/announce", "udp://exodus.desync.com:6969/announce",  "udp://denis.stalker.upeer.me:6969/announce", "udp://bigfoot1942.sektori.org:6969/announce", "udp://retracker.hotplug.ru:2710/announce", "udp://tracker.port443.xyz:6969/announce", "udp://tracker.birkenwald.de:6969/announce", "udp://tracker.filemail.com:6969/announce", "udp://tracker.torrent.eu.org:451/announce", "udp://chihaya.toss.li:9696/announce", "udp://tracker.nyaa.uk:6969/announce", "udp://explodie.org:6969/announce", "udp://retracker.lanta-net.ru:2710/announce",   "udp://prestige.minimafia.nl:443/announce", "udp://hk1.opentracker.ga:6969/announce", "udp://ipv4.tracker.harry.lu:80/announce", "udp://tw.opentracker.ga:36920/announce", "udp://tracker.uw0.xyz:6969/announce", "udp://tracker.opentrackr.org:1337/announce", "udp://tracker.iamhansen.xyz:2000/announce", "udp://tracker.coppersurfer.tk:6969/announce", "udp://tracker.internetwarriors.net:1337/announce", "udp://thetracker.org:80/announce",  "udp://9.rarbg.to:2710/announce", "udp://9.rarbg.me:2710/announce", "udp://tracker1.itzmx.com:8080/announce", "udp://tracker.vanitycore.co:6969/announce", "udp://open.stealth.si:80/announce", "udp://bt.xxx-tracker.com:2710/announce", "udp://tracker.tiny-vps.com:6969/announce",     "udp://zephir.monocul.us:6969/announce",     "udp://tracker1.wasabii.com.tw:6969/announce", "udp://tracker.tvunderground.org.ru:3218/announce", "udp://tracker.kamigami.org:2710/announce", "udp://tracker.dler.org:6969/announce", "udp://torrentclub.tech:6969/announce", "udp://pubt.in:2710/announce",               "wss://tracker.openwebtorrent.com:443/announce", "wss://tracker.fastcast.nz:443/announce", "wss://tracker.btorrent.xyz:443/announce", "wss://ltrackr.iamhansen.xyz:443/announce", "udp://tracker4.itzmx.com:2710/announce", "udp://tracker.justseed.it:1337/announce", "udp://tracker.filepit.to:6969/announce", "udp://packages.crunchbangplusplus.org:6969/announce",     "udp://62.138.0.158:6969/announce", "udp://185.225.17.100:1337/announce",  "udp://51.15.4.13:1337/announce", "udp://151.80.120.113:2710/announce", "udp://208.83.20.20:6969/announce", "udp://128.1.203.23:8080/announce", "udp://184.105.151.164:6969/announce",   "udp://51.15.40.114:80/announce", "udp://5.2.79.22:6969/announce", "udp://89.234.156.205:451/announce", "udp://51.38.184.185:6969/announce", "udp://176.31.106.35:80/announce", "udp://5.2.79.219:1337/announce", "udp://95.211.168.204:2710/announce",  "udp://51.15.76.199:6969/announce", "udp://188.246.227.212:80/announce", "udp://185.83.215.123:6969/announce", "udp://8.9.31.140:2000/announce", "udp://37.235.174.46:2710/announce", "udp://212.47.227.58:6969/announce", "udp://5.206.28.90:6969/announce", "udp://159.100.245.181:6969/announce", "udp://210.244.71.25:6969/announce", "udp://91.217.91.21:3218/announce", "udp://37.187.123.8:2710/announce", "udp://45.56.74.11:6969/announce", "udp://95.211.195.88:2710/announce", "udp://68.183.220.172:6969/announce",               "wss://51.15.179.14:443/announce", "wss://144.6.227.30:443/announce", "wss://45.32.219.126:443/announce", "wss://104.28.12.18:443/announce", "udp://91.121.255.214:2710/announce", "udp://94.23.217.90:1337/announce", "udp://85.202.163.5:6969/announce", "udp://138.68.225.164:6969/announce",        "udp://tracker2.itzmx.com:6961/announce", "udp://tracker.swateam.org.uk:2710/announce", "udp://tracker.btsync.gq:233/announce", "udp://amigacity.xyz:6969/announce",  "http://amigacity.xyz:6969/announce"};
//		HTTPTrackerTransfor httpTf = new HTTPTrackerTransfor();
//		try {
//			String data = httpTf.setUpLink("http://tracker.supertracker.net:1337/announce", hash, port);
//			System.out.println(data);
//		}catch(Exception e) {
//			System.out.println("该链接无法访问！！！");
//		}
//		UDPTrackerTransfor udpTf = new UDPTrackerTransfor(10000,"052ef38011e34ef27e58391da13a327eb88323a3");
//		byte[] bytes = udpTf.setUpLink("udp://tracker.blackunicorn.xyz:6969");
//		System.out.println(bytes.length);
//		for(byte b:bytes) {
//			System.out.print(b);
//			System.out.print(" ");
//		}
//		byte[] bytes2 = udpTf.startAnnounceRequest();
//		for(byte b:bytes2) {
//			System.out.print(b);
//			System.out.print(" ");
//		}
		
		
		
//		int pieceLen = 2975;
//		byte pstrlen = (byte)19;
//		byte[] pstr = "BitTorrent protocol".getBytes();
//		System.out.println(pstr.length);
//		byte[] reverse = new byte[] {0,0,0,0,0,0,0,0};
//		byte[] info_hash = DecodeChange.HexToByte20(hash);
//		byte[] peer_id = MyFunction.createPeerId().getBytes();
//		System.out.println(info_hash.length);
//		
//		//握手包
//		byte[] handShakepacket = new byte[68];
//		handShakepacket[0] = pstrlen;
//		MyFunction.byteCopy(pstr, 0, handShakepacket, 1, 20);
//		MyFunction.byteCopy(reverse, 0, handShakepacket, 20, 28);
//		MyFunction.byteCopy(info_hash, 0, handShakepacket, 28, 48);
//		MyFunction.byteCopy(peer_id, 0, handShakepacket, 48, 68);
//		
//		//进行握手
		TCPPeerConnection peerConn = new TCPPeerConnection("183.157.169.2",8999,18888);
		peerConn.peerHandshake(hash);
		//peerConn.peerReceive();
//		//bitField包
//		byte[] bitFieldPacket =  new byte[pieceLen+5];
//		byte[] lenBit = new byte[] {(byte)((pieceLen+1)/(256*256*256)),(byte)((pieceLen+1)/(256*256)),(byte)((pieceLen+1)/256),(byte)((pieceLen+1)%256)};
//		bitFieldPacket[4] = 5;
//		//发送bit Field
//		peerConn.peerSend(bitFieldPacket);
//		//peerConn.peerReceive();
	}
	
}
