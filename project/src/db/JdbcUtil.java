package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;



public class JdbcUtil {
	static {
	/*	try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	public static Connection getConnection() {
		Connection con =null;
	/*	try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:XE", "java", "java");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		try {
			Context ctx =  new InitialContext();
			//tomcat 자체의 context를 얻어옴
			DataSource dataFactory = (DataSource)ctx.lookup("java:comp/env/jdbc/jsptest");
			//java:comp/env/ :리소스 정의 건텍스트를 얻어오는 JNDI
			con = dataFactory.getConnection();
			con.setAutoCommit(false);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return con;
	}
	public static void close(Connection con) {
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void close(Statement stmt) {
		try {
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void close(ResultSet rs) {
		try {
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//=====transaction 메소드 ===
	public static void commit(Connection con) {
		try {
			con.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void rollback(Connection con) {
		try {
			con.rollback();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
}
