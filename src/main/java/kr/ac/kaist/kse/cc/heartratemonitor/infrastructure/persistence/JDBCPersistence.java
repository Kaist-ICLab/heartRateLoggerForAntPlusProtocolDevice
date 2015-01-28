package kr.ac.kaist.kse.cc.heartratemonitor.infrastructure.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

abstract class JDBCPersistence {

	private final static String DB_SERVER_ID = "root";
	private final static String DB_SERVER_PW = "kimjw1100";
	private final static String DB_SERVER_URL = "jdbc:mysql://143.248.90.196:3306/kimauk_lectureplayer?characterEncoding=euckr";

	private static Connection connection = null;

	private void setConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(DB_SERVER_URL, DB_SERVER_ID, DB_SERVER_PW);
			//connection.setAutoCommit(false);  
		} catch (ClassNotFoundException e) {
			connection = null;
			e.printStackTrace();
		} catch (SQLException e) {
			connection = null;
			e.printStackTrace();
		}
	}

	Connection getConnection() {
		try {
			if (connection == null || connection.isClosed()) {
				setConnection();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

}
