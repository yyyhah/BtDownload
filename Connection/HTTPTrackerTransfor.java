package Connection;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import TorrentDownload.DecodeChange;
import TorrentDownload.ParseTorrent;

//HTTPTracker操作
public class HTTPTrackerTransfor {
	//创建一个20位的peerid
	public static String createPeerId() {
		String randomId = "";
		for(int i=0;i<10;i++) {
			randomId += String.valueOf((int)(Math.random()*10));
		}
		return "-SF1-0-0-R"+randomId;
	}
	//建立遵循http tracker的服务器链接
	public String setUpLink(String announce,String hash,int port) {
		String peerId = createPeerId();
		String url = announce+"?";
		hash = DecodeChange.toStringHexTest(hash);
		hash = URLEncoder.encode(hash);
		url += "peer_id="+peerId+"&info_hash="+hash+"&port="+port+"&uploaded=0&downloaded=0&left="+0;
		System.out.println(url);
		String responseData = null;
		try {
			Response response = Jsoup.connect(url)
					.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.81 Safari/537.36")
					.ignoreContentType(true)
					.execute();
			responseData = new String(response.bodyAsBytes(),"utf8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String result = ParseTorrent.ParseBencode(responseData);
		System.out.println(ParseTorrent.showTorrent(result).toString());
		return result;
	}
}
