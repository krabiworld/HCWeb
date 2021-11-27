package org.headcrab.web.util;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class DB {

	private static DataSource dataSource;

	DB(DataSource dataSource) {
		DB.dataSource = dataSource;
	}

	public static Connection conn() throws Exception {
		return dataSource.getConnection();
    }

}
