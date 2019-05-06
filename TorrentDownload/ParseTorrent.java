package TorrentDownload;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;

import Connection.UDPConnection;
import net.sf.json.JSONObject;
<<<<<<< HEAD

//该程序未考虑例如i23e4:adsf这种语法的可能性。因为对于种子，一般是一段字典编码。不会出现以上情况。出现以上情况则解析出错
public class ParseTorrent {
	// bencode的预测分析表
=======
//璇ョ▼搴忔湭鑰冭檻渚嬪i23e4:adsf杩欑璇硶鐨勫彲鑳芥�с�傚洜涓哄浜庣瀛愶紝涓�鑸槸涓�娈靛瓧鍏哥紪鐮併�備笉浼氬嚭鐜颁互涓婃儏鍐点�傚嚭鐜颁互涓婃儏鍐靛垯瑙ｆ瀽鍑洪敊
public class ParseTorrent {
	//bencode鐨勯娴嬪垎鏋愯〃
>>>>>>> 8b38b83700e6210d2c0bd26d9bc479bd4aa8f8f5
	private static JSONObject grammer = new JSONObject();
	private static String[][] grammerArray = {
			{ "iMe", "lAe", "dCe", "N:S", "null", "null", "null", "null", "null", "null", "null" },
			{ "null", "null", "null", "DF", "null", "null", "null", "null", "null", "null", "null" },
			{ "null", "null", "null", "N", "null", "null", "-N", "0", "null", "null", "null" },
			{ "EB", "EB", "EB", "EB", "null", "null", "null", "null", "null", "null", "EB" },
			{ "A", "A", "A", "A", "null", "null", "null", "null", "null", "null", "@" },
			{ "null", "null", "null", "N:SEG", "null", "null", "null", "null", "null", "null", "null" },
			{ "null", "null", "null", "C", "null", "null", "null", "null", "null", "null", "@" },
			{ "null", "null", "null", "N", "null", "null", "null", "null", "null", "@", "@" },
			{ "null", "null", "null", "null", "cS", "null", "null", "null", "z", "null", "null" } };
	private static Character[] terminal = { 'i', 'l', 'd', 'D', 'c', '$', '-', '0', 'z', ':', 'e' };

	private static String result2 = "";
<<<<<<< HEAD
	private static Stack<Character> result = new Stack<Character>();// 通过将d，l，i这三类数据压栈，出栈的方法，实现便分析，边编译
	private static Character[] noTerminal = { 'E', 'N', 'M', 'A', 'B', 'C', 'G', 'F', 'S' };
=======
	private static Stack<Character> result = new Stack<Character>();//閫氳繃灏哾锛宭锛宨杩欎笁绫绘暟鎹帇鏍堬紝鍑烘爤鐨勬柟娉曪紝瀹炵幇渚垮垎鏋愶紝杈圭紪璇�
	private static Character[] noTerminal = {'E','N','M','A','B','C','G','F','S'};
>>>>>>> 8b38b83700e6210d2c0bd26d9bc479bd4aa8f8f5
	static {
		for (int nt = 0; nt < noTerminal.length; nt++) {
			for (int t = 0; t < terminal.length; t++) {
				grammer.put("" + noTerminal[nt] + terminal[t], grammerArray[nt][t]);
			}
		}
	}
<<<<<<< HEAD
	private static int StringIndex = -1;// 字符串类型结尾的下标
	private static Boolean isZero = true;// 判断数据是否为0
	private static List<Character> noTerminaList = Arrays.asList(noTerminal);

	// 匹配字符的真实类型
	private static Character getType(char s, int index) {
		// 这个是判断当前字符是否属于字符串
		if (index < StringIndex) {
=======
	private static int StringIndex = -1;//瀛楃涓茬被鍨嬬粨灏剧殑涓嬫爣
	private static Boolean isZero = true;//鍒ゆ柇鏁版嵁鏄惁涓�0
	private static List<Character> noTerminaList = Arrays.asList(noTerminal);
	//鍖归厤瀛楃鐨勭湡瀹炵被鍨�
	private static Character getType(char s,int index) {
		//杩欎釜鏄垽鏂綋鍓嶅瓧绗︽槸鍚﹀睘浜庡瓧绗︿覆
		if(index < StringIndex) {
>>>>>>> 8b38b83700e6210d2c0bd26d9bc479bd4aa8f8f5
			isZero = true;
			return 'c';
		} else if (index == StringIndex) {
			isZero = true;
			return 'z';
		}
<<<<<<< HEAD
		if (Character.isDigit(s)) {
			// 如果为0-9的数字那么返回D类型
			int value = s - 48;
			if (!isZero) {
=======
		if(Character.isDigit(s)) {
			//濡傛灉涓�0-9鐨勬暟瀛楅偅涔堣繑鍥濪绫诲瀷
			int value = s-48;
			if(!isZero) {
>>>>>>> 8b38b83700e6210d2c0bd26d9bc479bd4aa8f8f5
				return 'D';
			}
			if (value > 0 && value <= 9) {
				isZero = false;
				return 'D';
			} else {
				isZero = true;
				return '0';
			}
		}
		isZero = true;
		return s;
	}

	public static String BDecode(String text) {
		char[] textArray = text.toCharArray();
<<<<<<< HEAD
		int listenState = 0;// 当弹出N:的时候开始监听后方的字符串长度,1表示准备监听，2表示开始监听，0表示不监听
		int index = 0;
		String result1 = "";
		Stack<Character> stack = new Stack<Character>();
		String stringLength = "";// 保存字符串长度
		stack.add('$');
		stack.add('E');
		// 若栈顶非空
		while (stack.peek() != '$') {
			char top = stack.peek();
			char indexChar = getType(textArray[index], index);
			// 如果是终结符
			if (!noTerminaList.contains(top)) {
				// 如果栈顶字符为空，弹出空字符
				if (top == '@') {
					stack.pop();
				}
				// 如果该终结符和当前读取字符一致,读取下一个字符，将栈顶弹出,不一致则解析出错
				else if (top == indexChar) {
					index += 1;
					char remove = stack.pop();
					result2 += remove;
					if (remove == 'd') {
						result1 += "￥{";
=======
		int listenState = 0;//褰撳脊鍑篘:鐨勬椂鍊欏紑濮嬬洃鍚悗鏂圭殑瀛楃涓查暱搴�,1琛ㄧず鍑嗗鐩戝惉锛�2琛ㄧず寮�濮嬬洃鍚紝0琛ㄧず涓嶇洃鍚�
		int index = 0;
		String result1 = "";
		Stack<Character> stack = new Stack<Character>();
		String stringLength = "";//淇濆瓨瀛楃涓查暱搴�
		stack.add('$');
		stack.add('E');
		//鑻ユ爤椤堕潪绌�
		while(stack.peek() != '$') {
			char top = stack.peek();
			char indexChar = getType(textArray[index],index);
			//濡傛灉鏄粓缁撶
			if(!noTerminaList.contains(top)) {
				//濡傛灉鏍堥《瀛楃涓虹┖锛屽脊鍑虹┖瀛楃
				if(top == '@') {
					stack.pop();
				}
				//濡傛灉璇ョ粓缁撶鍜屽綋鍓嶈鍙栧瓧绗︿竴鑷�,璇诲彇涓嬩竴涓瓧绗︼紝灏嗘爤椤跺脊鍑�,涓嶄竴鑷村垯瑙ｆ瀽鍑洪敊
				else if(top == indexChar) {
					index += 1;
					char remove = stack.pop();
					result2 += remove;
					if(remove=='d') {
						result1 += "锟";
>>>>>>> 8b38b83700e6210d2c0bd26d9bc479bd4aa8f8f5
						result.push('d');
					} else if (remove == 'l') {
						result.push('l');
<<<<<<< HEAD
						result1 += "￥[";
					} else if (remove == 'i') {
						result.push('i');
						result1 += '￥';
					} else if (remove == ':') {
=======
						result1 += "锟";
					}else if(remove=='i') {
						result.push('i');
						result1 += '锟�';
					}else if(remove==':') {
>>>>>>> 8b38b83700e6210d2c0bd26d9bc479bd4aa8f8f5
						result1 += ":";
					} else if (remove == 'e') {
						char type = result.pop();
<<<<<<< HEAD
						if (type == 'i') {
							result1 += '￥';
						}
						if (type == 'd') {
							result1 += "}￥";
						} else if (type == 'l') {
							result1 += "]￥";
						}
					} else if (remove == 'z') {
						result1 += textArray[index - 1] + "￥";
					} else {
						result1 += textArray[index - 1];
					}
					if (remove == ':' && listenState == 1) {
						// 当读到:，结束监听,计算得到的字符串长度
						StringIndex = index + Integer.valueOf(stringLength) - 1;
=======
						if(type == 'i') {
							result1 += '锟�';
						}if(type == 'd') {
							result1 += "}锟�";
						}else if(type=='l') {
							result1 += "]锟�";
						}
					}else if(remove=='z') {
						result1 += textArray[index-1]+"锟�";
					}else {
						result1 += textArray[index-1];
					}
					if(remove == ':' && listenState==1) {
						//褰撹鍒�:锛岀粨鏉熺洃鍚�,璁＄畻寰楀埌鐨勫瓧绗︿覆闀垮害
						StringIndex = index + Integer.valueOf(stringLength)-1;
>>>>>>> 8b38b83700e6210d2c0bd26d9bc479bd4aa8f8f5
						stringLength = "";
						listenState = 0;
					} else if (remove == 'e' && listenState == 1) {
						stringLength = "";
						listenState = 0;
					} else if (listenState == 1) {
						stringLength += textArray[index - 1];
					}
				} else {
					return null;
				}
<<<<<<< HEAD
			} else {
				// 如果当前栈顶是非终结符,如果不存在。从非终结符到当前字符的映射，则解析出错，如果存在映射，将栈顶弹出，将新的产生式压入栈
=======
			}else {
				//濡傛灉褰撳墠鏍堥《鏄潪缁堢粨绗�,濡傛灉涓嶅瓨鍦ㄣ�備粠闈炵粓缁撶鍒板綋鍓嶅瓧绗︾殑鏄犲皠锛屽垯瑙ｆ瀽鍑洪敊锛屽鏋滃瓨鍦ㄦ槧灏勶紝灏嗘爤椤跺脊鍑猴紝灏嗘柊鐨勪骇鐢熷紡鍘嬪叆鏍�
>>>>>>> 8b38b83700e6210d2c0bd26d9bc479bd4aa8f8f5
				String tempStr = "";
				String grammerTemp = null;
				tempStr += top;
				tempStr += indexChar;
				try {
					grammerTemp = grammer.get(tempStr).toString();
				} catch (Exception e) {
					System.out.println(tempStr + " " + index);
					System.out.println(result1);
					System.out.println(result2);
					System.out.println(stack);
				}
				if (grammerTemp.equals("null")) {
					return null;
				} else {
					System.out.println(top + "->" + grammerTemp);
					char remove = stack.pop();
					if (remove == 'N') {
						listenState = 1;
					}
					char[] charList = grammerTemp.toCharArray();
					for (int i = charList.length - 1; i >= 0; i--) {
						stack.push(charList[i]);
					}
				}

			}
		}
		if (index != text.length()) {
			return null;
		}
		return result1;
	}

	public static String ParseBencode(String text) {
		ArrayList<String> results = new ArrayList<String>();
		String result1 = "";
		result1 = BDecode(text);
<<<<<<< HEAD
		if (result1 == null) {
			System.out.println("解析出错!");
=======
		if(result1==null) {
			System.out.println("瑙ｆ瀽鍑洪敊!");
>>>>>>> 8b38b83700e6210d2c0bd26d9bc479bd4aa8f8f5
			return null;
		}
		return result1;
	}

	public static JSONObject showTorrent(String result1) {
		JSONObject torrentJson = new JSONObject();
		torrentJson = JSONObject.fromObject(Regex.cleanData(result1));
		Iterator iterator = torrentJson.keys();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			String value = torrentJson.getString(key);
			if (key.equals("peers")) {
				byte[] bytes = value.getBytes();
				String ip1 = "";
				for (int j = 0; j < bytes.length; j++) {
					if (j == 0) {
						ip1 += bytes[j] & 0xff;
					} else if (j < 3 && j > 0) {
						ip1 += ".";
<<<<<<< HEAD
						ip1 += bytes[j] & 0xff;// 无符号二进制转10进制
					} else if (j == 3) {
						ip1 += ".";
						ip1 += bytes[j] & 0xff;// 无符号二进制转10进制
						ip1 += ":";
					} else {
						int k = (bytes[j] & 0xff);
						int y = (bytes[j + 1] & 0xff);
						ip1 += (bytes[j] & 0xff) * 128 + (bytes[j + 1] & 0xff);// 无符号二进制转10进制
=======
						ip1 += bytes[j]& 0xff;//鏃犵鍙蜂簩杩涘埗杞�10杩涘埗
					}
					else if(j==3) {
						ip1 += ".";
						ip1 += bytes[j]& 0xff;//鏃犵鍙蜂簩杩涘埗杞�10杩涘埗
						ip1 += ":";
					}else {
						int k = (bytes[j]& 0xff);
						int y = (bytes[j+1]&0xff);
						ip1 += (bytes[j]& 0xff)*128+(bytes[j+1]&0xff);//鏃犵鍙蜂簩杩涘埗杞�10杩涘埗
>>>>>>> 8b38b83700e6210d2c0bd26d9bc479bd4aa8f8f5
						break;
					}

				}
				System.out.println(key + " : " + ip1);
			} else {
				System.out.println(key + " : " + value);
			}
		}
		return torrentJson;
	}
<<<<<<< HEAD
//解析种子示例
=======
//瑙ｆ瀽绉嶅瓙绀轰緥
>>>>>>> 8b38b83700e6210d2c0bd26d9bc479bd4aa8f8f5
//	public static void main(String args[]) throws IOException{
//		String hash = "cfbc6db371632c90439d99c7a7b8bba530d9e54c";
//		//涓嬭浇绉嶅瓙鏂囦欢
//		byte[] getTorrent = GetTorrent.downTorrent(hash);
//		//灏嗙瀛愭枃浠跺瓨鍏ユ湰鍦�
//		RWDTorrent.download(getTorrent, "E:/test3.torrent");
//		//浠庢湰鍦拌鍙栫瀛愭枃浠�
//		char[] torrentChar = RWDTorrent.read("E:/test.torrent");
//		if(torrentChar==null) {
//			System.out.println("璇ユ枃浠朵笉瀛樺湪");
//			return;
//		}
//		String torrent = new String(torrentChar);
//		//瑙ｆ瀽绉嶅瓙
//		String result1 = ParseBencode(torrent);
//		if(result1==null) {
//			System.out.println("鏂囦欢瑙ｆ瀽鍑洪敊!");
//		}else {
//			System.out.println("鏂囦欢瑙ｆ瀽鎴愬姛!");
//		}
//		//鏄剧ず绉嶅瓙缁撴瀯
//		JSONObject torrentJson = showTorrent(result1);
//		//灏嗚В鏋愬悗鐨勭瀛愭枃浠跺啓鍏ユ枃浠�
//		RWDTorrent.write(Regex.cleanData(result1),"E:/temp3.txt");
<<<<<<< HEAD
	// 发送UDP
//	}
//b解码示例
=======
		//鍙戦�乁DP
//	}
	
//璋冪敤b瑙ｇ爜绀轰緥
>>>>>>> 8b38b83700e6210d2c0bd26d9bc479bd4aa8f8f5
//	public static void main(String[] args) {
//		UDPConnection send_conn = new UDPConnection();
//		UDPConnection receive_conn = new UDPConnection(12700);
//		byte[] data = null;
//		send_conn.send(data, url, targetPort);
//		data = receive_conn.receive();
//		
//	}
	public static String bencode(JSONObject obj) {
		String result = "";
		String str = obj.toString();
<<<<<<< HEAD
		// 标记回填，主要是针对字符串长度回填
=======
		//鏍囪鍥炲～锛屼富瑕佹槸閽堝瀛楃涓查暱搴﹀洖濉�
>>>>>>> 8b38b83700e6210d2c0bd26d9bc479bd4aa8f8f5
		int Rindex = -1;
		int index = 0;
		char top = '!';
		Boolean isStr = false;
		Boolean isInt = false;
<<<<<<< HEAD
		int lenght = 0;// 表示字符串的长度
=======
		int lenght = 0 ;//琛ㄧず瀛楃涓茬殑闀垮害
>>>>>>> 8b38b83700e6210d2c0bd26d9bc479bd4aa8f8f5
		char bchar[] = str.toCharArray();
		for (char c : bchar) {
			if (c == '{') {
				top = '{';
				result += 'd';
				index += 1;
			} else if (c == '[') {
				top = '[';
				result += 'l';
				index += 1;
<<<<<<< HEAD
			} else if (c == '"') {
				// 回填
				if (isStr) {
=======
			}else if(c=='"') {
				//鍥炲～
				if(isStr) {
>>>>>>> 8b38b83700e6210d2c0bd26d9bc479bd4aa8f8f5
					String num = String.valueOf(lenght);
					result = result.substring(0, Rindex) + num + ":" + result.substring(Rindex);
					index += num.length() + 1;
					lenght = 0;
				} else {
					Rindex = index;
				}
				isStr = !isStr;
			} else if (c == ']' || c == '}') {
				result += 'e';
				index += 1;
			} else {
				if (isStr) {
					lenght += 1;
					result += c;
					index += 1;
				} else if (c - 48 > 0 && c - 48 <= 9 && !isInt) {
					result += "i" + c;
					isInt = true;
					index += 2;
				} else if (c - 48 > 0 && c - 48 <= 9 && isInt) {
					result += c;
					isInt = true;
					index += 1;
				} else if (isInt) {
					result += 'e';
					isInt = false;
					index += 1;
				}
			}

		}
		return result;
	}

	public static JSONObject standerdJson(JSONObject in) {
		Iterator iterator = in.keys();
		JSONObject out = new JSONObject();
		ArrayList<String> temp = new ArrayList<String>();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			temp.add(key);
		}
		Collections.sort(temp);
		for (String str : temp) {
			out.put(str, in.get(str));
		}
		return out;
	}
<<<<<<< HEAD
//b编码示例
=======
//璋冪敤b缂栫爜绀轰緥
>>>>>>> 8b38b83700e6210d2c0bd26d9bc479bd4aa8f8f5
//	public static void main(String[] args) {
//		String code = "{\"t\":\"tx1\", \"y\":\"q\", \"q\":\"ping\", \"a\":{\"id\":\"abcdefghij0123456789\"}}";
//		//System.out.println(bencode(JSONObject.fromObject(code)));
//		JSONObject standJson = standerdJson(JSONObject.fromObject(code));
//		System.out.println(bencode(standJson));
//	}
}
