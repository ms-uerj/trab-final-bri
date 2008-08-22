package br.ufrj.cos.bri.util.db.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlConnector {
	private String url = null;
	private String user = null;
	private String passwd = null;
	private Connection session = null;
	
	public MysqlConnector(String database, String user, String passwd) {
		this.user = user;
		this.passwd = passwd;
		
		this.url = new String("jdbc:mysql://localhost:3306/")+database;
		
	}
	
	public synchronized void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			session = DriverManager.getConnection(url, user, passwd);
		} catch (Exception e) {
			System.out.println("Erro de conexão com o banco");
		}
	}
	
	public boolean isConnected() {
		boolean status=false;
		
		if(session == null) {
			return false;
		}
		
		try {
			status = !(session.isClosed());
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return status;
	}
	
	public synchronized void disconnect() {
		try {
	        if(session != null) {
	          session.close();
	        }
	      } catch(SQLException e) {
	    	  e.printStackTrace();
	      }
	}
	
	public ResultSet query(String sql) {
		ResultSet set = null;
		try {
			Statement stm = session.createStatement();
			set = stm.executeQuery(sql);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return set;
	}
	
	public void exec(String sql) {
		
		try {
			Statement stm = session.createStatement();
			stm.execute(sql);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
