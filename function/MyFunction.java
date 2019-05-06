package function;

public class MyFunction {
	// 将一段byte数组拷贝到另一端byte数组上
	public static void byteCopy(byte[] array1, int start, byte[] array2, int begin, int end) {
		for (int i = begin; i < end; i++) {
			if (start >= array1.length) {
				array2[i] = 0;
				continue;
			}
			array2[i] = array1[start];
			start++;
		}
	}

	public static String createPeerId() {
		String randomId = "";
		for (int i = 0; i < 10; i++) {
			randomId += String.valueOf((int) (Math.random() * 10));
		}
		return "-SF1-0-0-R" + randomId;
	}
}
