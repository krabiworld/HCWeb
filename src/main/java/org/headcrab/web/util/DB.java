package org.headcrab.web.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;


public class DB {
	public static Connection conn() throws Exception {
		Properties properties = Utils.getProp("application.properties");

		return DriverManager.getConnection(
			properties.getProperty("spring.datasource.url"),
			properties.getProperty("spring.datasource.username"),
			properties.getProperty("spring.datasource.password")
		);
    }
}
