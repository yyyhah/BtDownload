package TorrentDownload;
import java.io.IOException;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

//获取种子文件
public class GetTorrent {
	public static String baseUrl = "http://storetorrents.com/torrent/";
	public static String url = "https://storetorrents.xyz/download";
	public static byte[] downTorrent(String hash) {
		try {
			//请求获取密钥
			Document document = Jsoup.connect(baseUrl+hash+".html")
					.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.81 Safari/537.36")
					.get();
			String key = document.select("#downloadform div.downbox input[name='key']").attr("value");
			//请求种子文件
			Response response = Jsoup.connect(url)
					.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.81 Safari/537.36")
					.data("key",key)
					.data("infohash",hash)
					.ignoreContentType(true)
					.method(Method.POST)
					.execute();
			return response.bodyAsBytes();				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
