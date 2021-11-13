package org.headcrab.web.util;

import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.UUID;

public class Utils {

	public static String generateUUID() {
		return UUID.randomUUID().toString();
	}

	public static void sendEmail(String message) {
		System.out.println(message);
	}

	public static String hashPassword(String sourcePassword) {
		return BCrypt.hashpw(sourcePassword, BCrypt.gensalt(12));
	}

}
