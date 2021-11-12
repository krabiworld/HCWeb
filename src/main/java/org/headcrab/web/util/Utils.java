package org.headcrab.web.util;

import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Properties;
import java.util.UUID;

public class Utils {

	public static Properties getProp(String file) throws Exception {
		Properties properties = new Properties();
		properties.load(Utils.class.getClassLoader().getResourceAsStream(file));
		return properties;
	}

	public static String generateUUID() {
		return UUID.randomUUID().toString();
	}

	public static void sendEmail(String message) {
		try {
			System.out.println(message);
		} catch (Exception exception) {
			System.out.println("Error! In sendEmail() method!\n" + exception.getMessage());
		}
	}

	public static String hashPassword(String sourcePassword) {
		return BCrypt.hashpw(sourcePassword, BCrypt.gensalt(12));
	}

}
