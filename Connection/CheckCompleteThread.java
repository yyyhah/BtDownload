package Connection;

//判断下载是否完成的线程
public class CheckCompleteThread extends Thread {
	private byte[] bitField;
	private byte[] complete;

	// 利用complete这个数组来表示当前下载完成与否
	public CheckCompleteThread(byte[] bitField, byte[] complete) {
		this.bitField = bitField;
		this.complete = complete;
	}

	public void run() {
		while (true) {
			int flag = 0;
			int num = 0;
			for (int i = 40; i < this.bitField.length; i++) {
				if (this.bitField[i] != 1)
					flag = 1;
				else
					num++;
			}
			if (flag == 0) {
				System.out.println("下载完成!");
				complete[0] = 1;
				break;
			} else {
				System.out.println("下载进度 " + 100 * (num * 1.0) / (bitField.length - 40));
			}
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
