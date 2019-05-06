package Connection;

import TorrentDownload.DecodeChange;
import function.MyFunction;

//握手消息包
class HandshakePacket {
	private byte pstrlen = (byte) 19;
	private byte[] pstr = "BitTorrent protocol".getBytes();
	private byte[] reserved = new byte[8];
	private byte[] infoHash = new byte[20];
	private byte[] peer_id = new byte[20];
	private byte[] packet = new byte[68];

	// 初始化握手包
	HandshakePacket(String Hash) {
		peer_id = MyFunction.createPeerId().getBytes();
		infoHash = DecodeChange.HexToByte20(Hash);
		packet[0] = pstrlen;
		MyFunction.byteCopy(pstr, 0, packet, 1, 20);
		MyFunction.byteCopy(reserved, 0, packet, 20, 28);
		MyFunction.byteCopy(infoHash, 0, packet, 28, 48);
		MyFunction.byteCopy(peer_id, 0, packet, 48, 68);
	}

	HandshakePacket() {

	}

	public byte[] getPacket() {
		for (byte b : packet) {
			System.out.print(b);
			System.out.print(" ");
		}
		return packet;
	}

	public void setPacket(byte[] packet) {
		this.packet = packet;
	}
}
