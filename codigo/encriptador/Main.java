
public class Main {
	 public static void main(String[] args) {
	        String key = "Bar12345Bar12345"; // 128 bit key
	        String initVector = "RandomInitVector"; // 16 bytes IV

	        System.out.println(Encryptor.encrypt(key, initVector, "Mensaje a encriptar"));
	        
	        System.out.println(Encryptor.decrypt(key, initVector, "Mensaje a desencriptar"));
	    }
}

