package com.client;

public class AuthClient {
	private String token;
	
	public byte[] createCliAuthResponse(String msg) {
		byte[] response = AuthProtocol.messageToByte('0', '0', msg);
		return response;
	}

	public boolean isAuth(byte[] msg) {
		if (AuthProtocol.byteToType(msg) == '3') {
			setToken(AuthProtocol.byteToPayload(msg));
			return true;
		} else {
			return false;
		}
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String addToken(String msg) {
		return msg+ " "+ token;
	}
}

