package com.ibm.uitests.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConn extends BaseTest {
	
	private Connection conn;
	private Statement stmt;
	private ResultSet rs;

	public static void main(String[] args) {
		DBConn conn = new DBConn();
		String name = "EVENT-AUTOMATION-DELETEME";
		conn.valueExists("SELECT ENDPOINT_ID FROM BPXDATA.BPX_ENDPOINTS WHERE ENDPOINT_NAME='"+name+"'");
	}
	
	public DBConn(){
		logInfo("Connecting to the database.");
		try {
			conn = connect();
		} catch (SQLException e) {
			logFatal(e.getMessage());
		}
	}
	
	public boolean valueExists(String query){
		logInfo("Checking if value exists in the database.");
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next()){
				logInfo("Value is found in database.");
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		logInfo("Value is NOT found in database");
		return false;
	}
	
	public String selectValue(String query, String column){
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
		    
	    	while(rs.next()){
	    		String result = rs.getString(column);
	    		logInfo("Found value in the database: "+ result);
	    		return result;
	    	}
	    	
		} catch (SQLException e) {
			logFatal(e.getMessage());
		}
    	
		return null;
	}
	
	public int getCount(String query){
		logInfo("Get count from the database.");
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next()){
				return rs.getInt("ENDPOINT_COUNT");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		logInfo("Value is NOT found in database");
		return -1;
	}
	
	public boolean updateQuery(String query){
		logInfo("Executing update query to the database.");
		int result = -1;
		try {
			stmt = conn.createStatement();
			result = stmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return result >=0;
	}

	private Connection connect() throws SQLException{
		String user="metaapi";
        String password="u13X0!pAP1";
        String serverName = "vubxdb01.wdc01np";
        
        Connection con = null;
        
        javax.sql.DataSource ds=null;
        ds=new com.ibm.db2.jcc.DB2SimpleDataSource();
        
        ((com.ibm.db2.jcc.DB2BaseDataSource) ds).setServerName(serverName);
        ((com.ibm.db2.jcc.DB2BaseDataSource) ds).setPortNumber(Integer.parseInt("50000"));
        ((com.ibm.db2.jcc.DB2BaseDataSource) ds).setDatabaseName("BPXQA1");
        ((com.ibm.db2.jcc.DB2BaseDataSource) ds).setDriverType(4);
       
        con = ds.getConnection(user,password);
        logInfo("Connected to database successfully.");
        return con;
	}
}
