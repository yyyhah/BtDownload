package Connection;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

//寻找Peer的线程
public class FindPeerThread extends Thread {
	// 当前可用ip
	private JSONObject use = null;
	// 尝试连接过不可用的ip
	private JSONObject unuse = null;
	// tracker地址列表
	private JSONArray announce = null;
	// hash特征码
	private String hash = null;
	// 当前使用的连接
	private ArrayList<Socket> sockets = null;

	public FindPeerThread(JSONObject use, JSONObject unuse, JSONArray announce, String hash,
			ArrayList<Socket> sockets) {
		this.use = use;
		this.unuse = unuse;
		this.hash = hash;
		// 获取announce
		this.announce = announce;
		this.sockets = sockets;
	}

	// 判断是否可连接
	private Boolean canConnect(String ip, int port) {
		try {
			// 如果该连接可连接，则将该连接加入list中
			Socket socket = new Socket(ip, port);
			this.sockets.add(socket);
			return true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private void udpfind() {
		UDPTrackerTransfor udpTf = new UDPTrackerTransfor(10000, this.hash);
		for (int i = 0; i < this.announce.size(); i++) {
			String url = this.announce.getString(i);
			if (url.matches("udp")) {
				byte[] bytes = udpTf.setUpLink(url);
				JSONObject ipAndPort = udpTf.startAnnounceRequest();
				Iterator it = ipAndPort.keys();
				while (it.hasNext()) {
					String key = (String) it.next();
					int port = ipAndPort.getInt(key);
					if (!canConnect(key, port)) {
						this.unuse.put(key, port);
					} else {
						this.use.put(key, port);
					}
				}
			}
		}
	}

	public void run() {
		while (true) {
			if (sockets != null) {
				// 如果该连接断开， 将其从use中移除，从unuse中加入
				for (int i = 0; i < sockets.size(); i++) {
					if (!sockets.get(i).isConnected()) {
						sockets.remove(i);
					}
				}
			}
			udpfind();
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
