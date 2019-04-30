package TorrentDownload;

import java.io.UnsupportedEncodingException;

public class DecodeChange {
	//将字符串转化为16位字符串,等价于php的pack
	public static String toStringHexTest(String inHex) {
		byte[] hexBytes=new byte[inHex.length()/2];
		int index = 0;
		char[] inHexChr = inHex.toCharArray();
		for(int i=0;i<inHex.length();i=i+2) {
			int n = Integer.parseInt(inHex.substring(i,i+2), 16);
			hexBytes[index] = (byte)(n&0xff);
			index++;
		}
		String result = null;
		try {
			result =  new String(hexBytes,"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	public static byte[] HexToByte20(String inHex) {
		byte[] hexBytes=new byte[20];
		int index = 0;
		while(inHex.length()<40) {
			inHex = "0"+inHex;
		}
		for(int i=0;i<inHex.length();i=i+2) {
			int n = Integer.parseInt(inHex.substring(i,i+2), 16);
			hexBytes[index] = (byte)(n&0xff);
			index++;
		}
		return hexBytes;
    }
	public static byte[] HexToByte8(String inHex) {
		byte[] hexBytes=new byte[8];
		int index = 0;
		while(inHex.length()<16) {
			inHex = "0"+inHex;
		}
		for(int i=0;i<inHex.length();i=i+2) {
			int n = Integer.parseInt(inHex.substring(i,i+2), 16);
			hexBytes[index] = (byte)(n&0xff);
			index++;
		}
		return hexBytes;
    }
}
