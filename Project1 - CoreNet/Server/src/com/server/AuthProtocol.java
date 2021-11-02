package com.server;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class AuthProtocol {
	public static char byteToPhase(byte[] msg) {
		return (char) msg[0];
	}

	public static char byteToType(byte[] msg) {
		return (char) msg[1];
	}

	public static int byteToSize(byte[] msg) {
		int size = ((msg[2] & 0xFF) << 24) |
        ((msg[3] & 0xFF) << 16) |
        ((msg[4] & 0xFF) << 8 ) |
        ((msg[5] & 0xFF) << 0 );
		return size;
	}

	public static String byteToPayload(byte[] msg) {
		byte[] message = Arrays.copyOfRange(msg, 6, 6 + byteToSize(msg));
		return new String(message);
	}

	public static byte[] messageToByte(char phase, char type, String payload) {
		byte bytePhase = (byte) phase;
		byte byteType = (byte) type;
		byte[] byteSize = ByteBuffer.allocate(4).putInt(payload.length()).array();
		byte[] bytePayload = payload.getBytes();
		byte[] byteMessage = ByteBuffer.allocate(2 + byteSize.length + bytePayload.length)
				.put(bytePhase)
				.put(byteType)
				.put(byteSize)
				.put(bytePayload).array();
		
		return byteMessage;
	}
	
	/*
	public byte[] messageToByte(char phase, char type, byte[] byteSize, ) {
		return null;
	}*/
}
