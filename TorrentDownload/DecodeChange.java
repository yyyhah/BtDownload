package TorrentDownload;

import java.io.UnsupportedEncodingException;

public class DecodeChange {
	//将字符串转化为16位字符串,等价于php的pack
	public static String toStringHexTest(String inHex) {
//		byte[] baKeyword = new byte[s.length() / 2];
//		for (int i =0; i < baKeyword.length; i++) {
//		try {
//		baKeyword[i]=(byte)(0xff&Integer.parseInt(s.substring(i*2,i*2+2),16));
//		}catch (Exception e) {
//		e.printStackTrace();
//		}
//		}
//		try {
//		s= new String(baKeyword,"utf-8");// UTF-16le:Not
//		} catch (Exception e1) {
//		e1.printStackTrace();
//		}
//		return s;
		byte[] hexBytes=new byte[inHex.length()/2];
		int index = 0;
		char[] inHexChr = inHex.toCharArray();
		for(int i=0;i<inHex.length();i=i+2) {
			int n = inHexChr[i+1]-48 + (inHexChr[i]-48)*16;
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
}
