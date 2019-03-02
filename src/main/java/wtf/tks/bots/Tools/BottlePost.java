package wtf.tks.bots.Tools;

import java.util.Random;

public class BottlePost {
	
	private String msg;
	private int encryption;
	private Random rng;
	
	public BottlePost(String msg) {
		
		this.msg = msg;
		this.encryption = setEncryption();
		
		rng = new Random();
	}
	
	
	private int setEncryption() {
		
		Random rng = new Random();
		int low = 5;
		int high = 30;
		int rngResult = rng.nextInt(high - low) + low;
		return rngResult;
	}
	
	
	public String getEncryptText() {
		
		int encryptChars = msg.length() * encryption / 100;
		StringBuilder strBuilder = new StringBuilder(msg);
		
		for (int i = 0; i <= encryptChars; i++) {
			int rngLocation = rng.nextInt(msg.length() - 1) + 1;
			if (strBuilder.charAt(0) == '█') {
				i--;
			} else {
				strBuilder.setCharAt(rngLocation, '█');
			}
		}
		return strBuilder.toString();
	}
	
	public int getEncryption() {
		return encryption;
	}
}