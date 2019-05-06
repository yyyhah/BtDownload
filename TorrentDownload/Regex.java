package TorrentDownload;

import java.util.Stack;

public class Regex {
	static Stack<Character> stack = new Stack<Character>();
	private static Boolean isKey = true;

	// 处理除了pieces后面hash值情况外，其他属性为hash的情况
	public static Boolean isHash(String s) {
		String[] specific = { "\"filehash\"", "\"ed2k\"" };
		for (String c : specific) {
			if (c.equals(s)) {
				return true;
			}
		}
		return false;
	}

	public static String cleanData(String result) {
		result = result.replaceAll("(?s)\\d+:(.*?)￥", "￥\"$1\"￥");
		String result1[] = result.split("￥");
		String resultStr = "";
		int type = 0;
		Boolean flag = false;// 处理piece后面hash值乱码问题
		Boolean flag2 = false;// 处理filehash后面乱码问题
		for (String c : result1) {
			if (c.isEmpty()) {
				continue;
			}
			if (c.equals("{")) {
				type = 1;
				stack.push('{');
				resultStr += "{";
				isKey = true;
			} else if (c.equals("[")) {
				type = 2;
				stack.push('[');
				resultStr += "[";
				isKey = true;
			} else if (c.equals("]")) {
				stack.pop();
				type = getType();
				resultStr += "],";
			} else if (c.equals("}")) {
				stack.pop();
				type = getType();
				resultStr += "},";
			} else if (c.equals("\"pieces\"")) {
				resultStr += c + ":" + "[";
				flag = true;
			} else if (isHash(c)) {
				resultStr += c + ":\"";
				flag2 = true;
			} else {
				if (flag) {
					int count = 0;
					int index = 0;

					char[] arrays = c.substring(1, c.length() - 1).toCharArray();
					System.out.println("长度:" + arrays.length);
					for (char i : arrays) {
						if (count == 0) {
							resultStr += "\"" + (int) i;
							count += 1;
						} else if (count == 19) {
							resultStr += " " + (int) i + "\",";
							count = 0;
						} else {
							resultStr += " " + (int) i;
							count += 1;
						}
						index += 1;
						if (index == arrays.length) {
							resultStr = resultStr.substring(0, resultStr.length() - 1) + "],";
							break;
						}
					}
					flag = false;
				} else if (flag2) {
					String sha = "";
					for (char i : c.toCharArray()) {
						sha += " " + (int) i;
					}
					resultStr += sha.substring(4, sha.length() - 3) + "\",";
					flag2 = false;
				} else if (type == 0) {
					resultStr += c + " ";
				} else if (type == 1) {
					if (isKey) {
						resultStr += c + ":";
						isKey = false;
					} else {
						resultStr += c + ",";
						isKey = true;
					}
				} else if (type == 2) {
					resultStr += c + ",";
				}
			}
		}
		resultStr = resultStr.replaceAll(",([\\]\\}])", "$1");
		resultStr = resultStr.replaceAll("\\n", " ");
		return resultStr.substring(0, resultStr.length() - 1);
	}

	private static int getType() {
		int type = 0;
		if (stack.size() == 0) {
			type = 0;// 如果当前不在字典，列表中
		} else if (stack.peek() == '{') {
			type = 1;// 当前在字典中
		} else if (stack.peek() == '[') {
			type = 2;// 当前在列表中
		}
		isKey = true;
		return type;
	}
}
