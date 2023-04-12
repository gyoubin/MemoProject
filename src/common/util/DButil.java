package common.util;
import java.sql.*;
public class DButil {
	static String url="jdbc:oracle:thin:@localhost:1521:XE";
	static String user="scott", pwd="tiger";
	
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("Driver Loading Success!!");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getCon() throws SQLException{
		Connection con=DriverManager.getConnection(url, user, pwd);
		return con;
	}
}
