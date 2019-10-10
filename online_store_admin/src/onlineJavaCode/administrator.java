package onlineJavaCode;

import org.apache.commons.codec.digest.DigestUtils;

public class administrator {
	
	private String name;
	private String mail;
	private String passHash;

	administrator(String name, String mail, String pass){
		this.name = name;
		this.mail = mail;
		this.passHash = DigestUtils.sha256Hex(pass);
	}
	
	public String getName() {
		return name;
	}
	
	public String getMail() {
		return mail;
	}
	
	public boolean checkPass(String pass) {
		String hash =  DigestUtils.sha256Hex(pass);
		return passHash.equals(hash);
	}
}
