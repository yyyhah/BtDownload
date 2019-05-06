package Connection;

import java.io.IOException;
import java.net.URLEncoder;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import TorrentDownload.DecodeChange;
import TorrentDownload.ParseTorrent;
import TorrentDownload.Regex;
import function.MyFunction;

//HTTPTracker操作
public class HTTPTrackerTransfor {
	// 创建一个20位的peerid

	// 建立遵循http tracker的服务器链接
	public String setUpLink(String announce, String hash, int port) {
		String peerId = MyFunction.createPeerId();
		String url = announce + "?";
		hash = DecodeChange.toStringHexTest(hash);
		hash = URLEncoder.encode(hash);
		url += "peer_id=" + peerId + "&info_hash=" + hash + "&port=" + port + "&uploaded=0&downloaded=0&left="
				+ 1651507200;
		System.out.println(url);
		String responseData = null;
		try {
			Response response = Jsoup.connect(url).userAgent(
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.81 Safari/537.36")
					.ignoreContentType(true).execute();
			responseData = new String(response.bodyAsBytes(), "utf8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String result = ParseTorrent.ParseBencode(responseData);
		result = Regex.cleanData(result);
		System.out.println(ParseTorrent.showTorrent(result).toString());
		return result;
	}
}
