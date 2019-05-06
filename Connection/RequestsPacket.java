package Connection;

import TorrentDownload.DecodeChange;
import function.MyFunction;

//ÇëÇó°ü·â×°
public class RequestsPacket {
	private byte[] choke = new byte[5];
	private byte[] unchoke = new byte[5];
	private byte[] interested = new byte[5];
	private byte[] uninterseted = new byte[5];
	private byte[] have = new byte[9];
	private byte[] request = new byte[17];

	RequestsPacket() {
		choke[3] = 1;
		choke[4] = 0;
		unchoke[3] = 1;
		unchoke[4] = 1;
		interested[3] = 1;
		interested[4] = 2;
		uninterseted[3] = 1;
		uninterseted[4] = 3;
		have[3] = 5;
		have[4] = 4;
		request[3] = (byte) 13;
		request[4] = 6;
	}

	public void setHave(int index) {
		byte[] havebytes = DecodeChange.intToByteArray(index);
		MyFunction.byteCopy(havebytes, 0, have, 5, 9);
	}

	public void setRequest(int index, int begin, int length) {
		byte[] indexBytes = DecodeChange.intToByteArray(index);
		byte[] beginBytes = DecodeChange.intToByteArray(begin);
		byte[] lengthBytes = DecodeChange.intToByteArray(length);
		MyFunction.byteCopy(indexBytes, 0, request, 5, 9);
		MyFunction.byteCopy(beginBytes, 0, request, 9, 13);
		MyFunction.byteCopy(lengthBytes, 0, request, 13, 17);
	}

	public byte[] getRequest() {
		return request;
	}

	public byte[] getChoke() {
		return choke;
	}

	public byte[] getUnchoke() {
		return unchoke;
	}

	public byte[] getInterested() {
		return interested;
	}

	public byte[] getUninterseted() {
		return uninterseted;
	}

	public byte[] getHave() {
		return have;
	}
}
