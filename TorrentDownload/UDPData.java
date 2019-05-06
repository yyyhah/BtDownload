package TorrentDownload;

public class UDPData {
	public ConnectionOutput conn_out = null;
	public AnnounceInput ann_in = null;

	public UDPData() {
		conn_out = new ConnectionOutput();
		ann_in = new AnnounceInput();
	}

	public ConnectionOutput getConnOut() {
		return conn_out;
	}

	public AnnounceInput getAnnInput() {
		return ann_in;
	}

	public static final class ConnectionOutput {
		public byte[] action = new byte[4];
		public byte[] transaction_id = new byte[4];
		public byte[] connection_id = new byte[8];

		public void setAction(byte[] act) {
			action = act;
		}

		public void setTransId(byte[] trans) {
			transaction_id = trans;
		}

		public void setConnId(byte[] connid) {
			connection_id = connid;
		}

		public byte[] getAction() {
			return action;
		}

		public byte[] getTransId() {
			return transaction_id;
		}

		public byte[] getConnId() {
			return connection_id;
		}

	}

	public static final class AnnounceInput {
		// 一共98个字节
		public byte[] connection_id = new byte[8];
		public byte[] action = new byte[4];
		public byte[] transaction_id = new byte[4];
		public byte[] info_hash = new byte[20];
		public byte[] peer_id = new byte[20];
		public byte[] downloaded = new byte[8];
		public byte[] left = new byte[8];
		public byte[] uploaded = new byte[8];
		public byte[] event = new byte[4];
		public byte[] IpAddress = new byte[4];
		public byte[] key = new byte[4];
		public byte[] numWant = new byte[4];
		public byte[] port = new byte[2];
		public byte[] extensiosns = new byte[2];
//		public void setData(byte[] connId,byte[] act,byte[] transId,byte[] infoHash,byte[] peerId,byte[] download,byte[] l,byte[] upload,byte[] e,byte[] ip,byte[] k,byte[] num,byte[] p) {
//			connection_id = connId;
//			action = act;
//			transaction_id = transId;
//			info_hash = infoHash;
//			peer_id = peerId;
//			downloaded = download;
//			left = l;
//			uploaded = upload;
//			event = e;
//			IpAddress = ip;
//			key = k;
//			numWant = num;
//			port = p;
//		}
	}
}
