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

//¸Ã³ÌĞòÎ´¿¼ÂÇÀıÈçi23e4:adsfÕâÖÖÓï·¨µÄ¿ÉÄÜĞÔ¡£ÒòÎª¶ÔÓÚÖÖ×Ó£¬Ò»°ãÊÇÒ»¶Î×Öµä±àÂë¡£²»»á³öÏÖÒÔÉÏÇé¿ö¡£³öÏÖÒÔÉÏÇé¿öÔò½âÎö³ö´í
public class ParseTorrent {
	// bencodeµÄÔ¤²â·ÖÎö±í
=======
//è¯¥ç¨‹åºæœªè€ƒè™‘ä¾‹å¦‚i23e4:adsfè¿™ç§è¯­æ³•çš„å¯èƒ½æ€§ã€‚å› ä¸ºå¯¹äºç§å­ï¼Œä¸€èˆ¬æ˜¯ä¸€æ®µå­—å…¸ç¼–ç ã€‚ä¸ä¼šå‡ºç°ä»¥ä¸Šæƒ…å†µã€‚å‡ºç°ä»¥ä¸Šæƒ…å†µåˆ™è§£æå‡ºé”™
public class ParseTorrent {
	//bencodeçš„é¢„æµ‹åˆ†æè¡¨
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
	private static Stack<Character> result = new Stack<Character>();// Í¨¹ı½«d£¬l£¬iÕâÈıÀàÊı¾İÑ¹Õ»£¬³öÕ»µÄ·½·¨£¬ÊµÏÖ±ã·ÖÎö£¬±ß±àÒë
	private static Character[] noTerminal = { 'E', 'N', 'M', 'A', 'B', 'C', 'G', 'F', 'S' };
=======
	private static Stack<Character> result = new Stack<Character>();//é€šè¿‡å°†dï¼Œlï¼Œiè¿™ä¸‰ç±»æ•°æ®å‹æ ˆï¼Œå‡ºæ ˆçš„æ–¹æ³•ï¼Œå®ç°ä¾¿åˆ†æï¼Œè¾¹ç¼–è¯‘
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
	private static int StringIndex = -1;// ×Ö·û´®ÀàĞÍ½áÎ²µÄÏÂ±ê
	private static Boolean isZero = true;// ÅĞ¶ÏÊı¾İÊÇ·ñÎª0
	private static List<Character> noTerminaList = Arrays.asList(noTerminal);

	// Æ¥Åä×Ö·ûµÄÕæÊµÀàĞÍ
	private static Character getType(char s, int index) {
		// Õâ¸öÊÇÅĞ¶Ïµ±Ç°×Ö·ûÊÇ·ñÊôÓÚ×Ö·û´®
		if (index < StringIndex) {
=======
	private static int StringIndex = -1;//å­—ç¬¦ä¸²ç±»å‹ç»“å°¾çš„ä¸‹æ ‡
	private static Boolean isZero = true;//åˆ¤æ–­æ•°æ®æ˜¯å¦ä¸º0
	private static List<Character> noTerminaList = Arrays.asList(noTerminal);
	//åŒ¹é…å­—ç¬¦çš„çœŸå®ç±»å‹
	private static Character getType(char s,int index) {
		//è¿™ä¸ªæ˜¯åˆ¤æ–­å½“å‰å­—ç¬¦æ˜¯å¦å±äºå­—ç¬¦ä¸²
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
			// Èç¹ûÎª0-9µÄÊı×ÖÄÇÃ´·µ»ØDÀàĞÍ
			int value = s - 48;
			if (!isZero) {
=======
		if(Character.isDigit(s)) {
			//å¦‚æœä¸º0-9çš„æ•°å­—é‚£ä¹ˆè¿”å›Dç±»å‹
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
		int listenState = 0;// µ±µ¯³öN:µÄÊ±ºò¿ªÊ¼¼àÌıºó·½µÄ×Ö·û´®³¤¶È,1±íÊ¾×¼±¸¼àÌı£¬2±íÊ¾¿ªÊ¼¼àÌı£¬0±íÊ¾²»¼àÌı
		int index = 0;
		String result1 = "";
		Stack<Character> stack = new Stack<Character>();
		String stringLength = "";// ±£´æ×Ö·û´®³¤¶È
		stack.add('$');
		stack.add('E');
		// ÈôÕ»¶¥·Ç¿Õ
		while (stack.peek() != '$') {
			char top = stack.peek();
			char indexChar = getType(textArray[index], index);
			// Èç¹ûÊÇÖÕ½á·û
			if (!noTerminaList.contains(top)) {
				// Èç¹ûÕ»¶¥×Ö·ûÎª¿Õ£¬µ¯³ö¿Õ×Ö·û
				if (top == '@') {
					stack.pop();
				}
				// Èç¹û¸ÃÖÕ½á·ûºÍµ±Ç°¶ÁÈ¡×Ö·ûÒ»ÖÂ,¶ÁÈ¡ÏÂÒ»¸ö×Ö·û£¬½«Õ»¶¥µ¯³ö,²»Ò»ÖÂÔò½âÎö³ö´í
				else if (top == indexChar) {
					index += 1;
					char remove = stack.pop();
					result2 += remove;
					if (remove == 'd') {
						result1 += "£¤{";
=======
		int listenState = 0;//å½“å¼¹å‡ºN:çš„æ—¶å€™å¼€å§‹ç›‘å¬åæ–¹çš„å­—ç¬¦ä¸²é•¿åº¦,1è¡¨ç¤ºå‡†å¤‡ç›‘å¬ï¼Œ2è¡¨ç¤ºå¼€å§‹ç›‘å¬ï¼Œ0è¡¨ç¤ºä¸ç›‘å¬
		int index = 0;
		String result1 = "";
		Stack<Character> stack = new Stack<Character>();
		String stringLength = "";//ä¿å­˜å­—ç¬¦ä¸²é•¿åº¦
		stack.add('$');
		stack.add('E');
		//è‹¥æ ˆé¡¶éç©º
		while(stack.peek() != '$') {
			char top = stack.peek();
			char indexChar = getType(textArray[index],index);
			//å¦‚æœæ˜¯ç»ˆç»“ç¬¦
			if(!noTerminaList.contains(top)) {
				//å¦‚æœæ ˆé¡¶å­—ç¬¦ä¸ºç©ºï¼Œå¼¹å‡ºç©ºå­—ç¬¦
				if(top == '@') {
					stack.pop();
				}
				//å¦‚æœè¯¥ç»ˆç»“ç¬¦å’Œå½“å‰è¯»å–å­—ç¬¦ä¸€è‡´,è¯»å–ä¸‹ä¸€ä¸ªå­—ç¬¦ï¼Œå°†æ ˆé¡¶å¼¹å‡º,ä¸ä¸€è‡´åˆ™è§£æå‡ºé”™
				else if(top == indexChar) {
					index += 1;
					char remove = stack.pop();
					result2 += remove;
					if(remove=='d') {
						result1 += "ï¿¥{";
>>>>>>> 8b38b83700e6210d2c0bd26d9bc479bd4aa8f8f5
						result.push('d');
					} else if (remove == 'l') {
						result.push('l');
<<<<<<< HEAD
						result1 += "£¤[";
					} else if (remove == 'i') {
						result.push('i');
						result1 += '£¤';
					} else if (remove == ':') {
=======
						result1 += "ï¿¥[";
					}else if(remove=='i') {
						result.push('i');
						result1 += 'ï¿¥';
					}else if(remove==':') {
>>>>>>> 8b38b83700e6210d2c0bd26d9bc479bd4aa8f8f5
						result1 += ":";
					} else if (remove == 'e') {
						char type = result.pop();
<<<<<<< HEAD
						if (type == 'i') {
							result1 += '£¤';
						}
						if (type == 'd') {
							result1 += "}£¤";
						} else if (type == 'l') {
							result1 += "]£¤";
						}
					} else if (remove == 'z') {
						result1 += textArray[index - 1] + "£¤";
					} else {
						result1 += textArray[index - 1];
					}
					if (remove == ':' && listenState == 1) {
						// µ±¶Áµ½:£¬½áÊø¼àÌı,¼ÆËãµÃµ½µÄ×Ö·û´®³¤¶È
						StringIndex = index + Integer.valueOf(stringLength) - 1;
=======
						if(type == 'i') {
							result1 += 'ï¿¥';
						}if(type == 'd') {
							result1 += "}ï¿¥";
						}else if(type=='l') {
							result1 += "]ï¿¥";
						}
					}else if(remove=='z') {
						result1 += textArray[index-1]+"ï¿¥";
					}else {
						result1 += textArray[index-1];
					}
					if(remove == ':' && listenState==1) {
						//å½“è¯»åˆ°:ï¼Œç»“æŸç›‘å¬,è®¡ç®—å¾—åˆ°çš„å­—ç¬¦ä¸²é•¿åº¦
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
				// Èç¹ûµ±Ç°Õ»¶¥ÊÇ·ÇÖÕ½á·û,Èç¹û²»´æÔÚ¡£´Ó·ÇÖÕ½á·ûµ½µ±Ç°×Ö·ûµÄÓ³Éä£¬Ôò½âÎö³ö´í£¬Èç¹û´æÔÚÓ³Éä£¬½«Õ»¶¥µ¯³ö£¬½«ĞÂµÄ²úÉúÊ½Ñ¹ÈëÕ»
=======
			}else {
				//å¦‚æœå½“å‰æ ˆé¡¶æ˜¯éç»ˆç»“ç¬¦,å¦‚æœä¸å­˜åœ¨ã€‚ä»éç»ˆç»“ç¬¦åˆ°å½“å‰å­—ç¬¦çš„æ˜ å°„ï¼Œåˆ™è§£æå‡ºé”™ï¼Œå¦‚æœå­˜åœ¨æ˜ å°„ï¼Œå°†æ ˆé¡¶å¼¹å‡ºï¼Œå°†æ–°çš„äº§ç”Ÿå¼å‹å…¥æ ˆ
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
			System.out.println("½âÎö³ö´í!");
=======
		if(result1==null) {
			System.out.println("è§£æå‡ºé”™!");
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
						ip1 += bytes[j] & 0xff;// ÎŞ·ûºÅ¶ş½øÖÆ×ª10½øÖÆ
					} else if (j == 3) {
						ip1 += ".";
						ip1 += bytes[j] & 0xff;// ÎŞ·ûºÅ¶ş½øÖÆ×ª10½øÖÆ
						ip1 += ":";
					} else {
						int k = (bytes[j] & 0xff);
						int y = (bytes[j + 1] & 0xff);
						ip1 += (bytes[j] & 0xff) * 128 + (bytes[j + 1] & 0xff);// ÎŞ·ûºÅ¶ş½øÖÆ×ª10½øÖÆ
=======
						ip1 += bytes[j]& 0xff;//æ— ç¬¦å·äºŒè¿›åˆ¶è½¬10è¿›åˆ¶
					}
					else if(j==3) {
						ip1 += ".";
						ip1 += bytes[j]& 0xff;//æ— ç¬¦å·äºŒè¿›åˆ¶è½¬10è¿›åˆ¶
						ip1 += ":";
					}else {
						int k = (bytes[j]& 0xff);
						int y = (bytes[j+1]&0xff);
						ip1 += (bytes[j]& 0xff)*128+(bytes[j+1]&0xff);//æ— ç¬¦å·äºŒè¿›åˆ¶è½¬10è¿›åˆ¶
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
//½âÎöÖÖ×ÓÊ¾Àı
=======
//è§£æç§å­ç¤ºä¾‹
>>>>>>> 8b38b83700e6210d2c0bd26d9bc479bd4aa8f8f5
//	public static void main(String args[]) throws IOException{
//		String hash = "cfbc6db371632c90439d99c7a7b8bba530d9e54c";
//		//ä¸‹è½½ç§å­æ–‡ä»¶
//		byte[] getTorrent = GetTorrent.downTorrent(hash);
//		//å°†ç§å­æ–‡ä»¶å­˜å…¥æœ¬åœ°
//		RWDTorrent.download(getTorrent, "E:/test3.torrent");
//		//ä»æœ¬åœ°è¯»å–ç§å­æ–‡ä»¶
//		char[] torrentChar = RWDTorrent.read("E:/test.torrent");
//		if(torrentChar==null) {
//			System.out.println("è¯¥æ–‡ä»¶ä¸å­˜åœ¨");
//			return;
//		}
//		String torrent = new String(torrentChar);
//		//è§£æç§å­
//		String result1 = ParseBencode(torrent);
//		if(result1==null) {
//			System.out.println("æ–‡ä»¶è§£æå‡ºé”™!");
//		}else {
//			System.out.println("æ–‡ä»¶è§£ææˆåŠŸ!");
//		}
//		//æ˜¾ç¤ºç§å­ç»“æ„
//		JSONObject torrentJson = showTorrent(result1);
//		//å°†è§£æåçš„ç§å­æ–‡ä»¶å†™å…¥æ–‡ä»¶
//		RWDTorrent.write(Regex.cleanData(result1),"E:/temp3.txt");
<<<<<<< HEAD
	// ·¢ËÍUDP
//	}
//b½âÂëÊ¾Àı
=======
		//å‘é€UDP
//	}
	
//è°ƒç”¨bè§£ç ç¤ºä¾‹
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
		// ±ê¼Ç»ØÌî£¬Ö÷ÒªÊÇÕë¶Ô×Ö·û´®³¤¶È»ØÌî
=======
		//æ ‡è®°å›å¡«ï¼Œä¸»è¦æ˜¯é’ˆå¯¹å­—ç¬¦ä¸²é•¿åº¦å›å¡«
>>>>>>> 8b38b83700e6210d2c0bd26d9bc479bd4aa8f8f5
		int Rindex = -1;
		int index = 0;
		char top = '!';
		Boolean isStr = false;
		Boolean isInt = false;
<<<<<<< HEAD
		int lenght = 0;// ±íÊ¾×Ö·û´®µÄ³¤¶È
=======
		int lenght = 0 ;//è¡¨ç¤ºå­—ç¬¦ä¸²çš„é•¿åº¦
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
				// »ØÌî
				if (isStr) {
=======
			}else if(c=='"') {
				//å›å¡«
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
//b±àÂëÊ¾Àı
=======
//è°ƒç”¨bç¼–ç ç¤ºä¾‹
>>>>>>> 8b38b83700e6210d2c0bd26d9bc479bd4aa8f8f5
//	public static void main(String[] args) {
//		String code = "{\"t\":\"tx1\", \"y\":\"q\", \"q\":\"ping\", \"a\":{\"id\":\"abcdefghij0123456789\"}}";
//		//System.out.println(bencode(JSONObject.fromObject(code)));
//		JSONObject standJson = standerdJson(JSONObject.fromObject(code));
//		System.out.println(bencode(standJson));
//	}
}
