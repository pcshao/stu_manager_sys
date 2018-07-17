package cn.pcshao.study.zx.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {

	private static String driver = "oracle.jdbc.driver.OracleDriver";
	private static String url = "jdbc:oracle:thin:@";
	private static String ip = "127.0.0.1:1521";
	private static String instance = "orcl";
	private static String user = "stu_manager_system";
	private static String password = "123";
	private static Connection conn = null;
	
	static {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getOracleConnection() {
		try {
			conn = DriverManager.getConnection(url+ip+":"+instance,user,password);
//			System.out.println("数据库连接成功");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void main(String[] args) {
		conn = getOracleConnection();
	}
	
}
