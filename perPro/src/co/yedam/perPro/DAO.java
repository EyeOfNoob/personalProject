package co.yedam.perPro;

import java.sql.Connection;
import java.sql.DriverManager;

//	DB와 연결
public class DAO {
//	Connection 생성
	public static Connection conn;
	
	public static Connection getConn() {
		Connection conn = null;
		try {
			Class.forName("oracle.jdbc.OracleDriver");
//			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "yedam", "yedam");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.19:1521:xe", "perpro", "1234");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
}
