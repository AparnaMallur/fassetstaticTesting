package com.fasset.utils;
//STEP 1. Import required packages
import java.sql.*;

class TraialBalanceReportEntity
{
	String particulars;
	long   openingBalance;
	long   debit;
	long   credit;
	long   closingBalance;
	public String getParticulars() {
		return particulars;
	}
	public void setParticulars(String particulars) {
		this.particulars = particulars;
	}
	public long getOpeningBalance() {
		return openingBalance;
	}
	public void setOpeningBalance(long openingBalance) {
		this.openingBalance = openingBalance;
	}
	public long getDebit() {
		return debit;
	}
	public void setDebit(long debit) {
		this.debit = debit;
	}
	public long getCredit() {
		return credit;
	}
	public void setCredit(long credit) {
		this.credit = credit;
	}
	public long getClosingBalance() {
		return closingBalance;
	}
	public void setClosingBalance(long closingBalance) {
		this.closingBalance = closingBalance;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (closingBalance ^ (closingBalance >>> 32));
		result = prime * result + (int) (credit ^ (credit >>> 32));
		result = prime * result + (int) (debit ^ (debit >>> 32));
		result = prime * result + (int) (openingBalance ^ (openingBalance >>> 32));
		result = prime * result + ((particulars == null) ? 0 : particulars.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TraialBalanceReportEntity other = (TraialBalanceReportEntity) obj;
		if (closingBalance != other.closingBalance)
			return false;
		if (credit != other.credit)
			return false;
		if (debit != other.debit)
			return false;
		if (openingBalance != other.openingBalance)
			return false;
		if (particulars == null) {
			if (other.particulars != null)
				return false;
		} else if (!particulars.equals(other.particulars))
			return false;
		return true;
	}
}
public class FirstExample {
   // JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:mysql://localhost/fasssetProd";

   //  Database credentials
   static final String USER = "root";
   static final String PASS = "root";
   
   public static void main(String[] args) {
   Connection conn = null;
   Statement stmt = null;
   try{
      //STEP 2: Register JDBC driver
      Class.forName("com.mysql.jdbc.Driver");

      //STEP 3: Open a connection
      System.out.println("Connecting to database...");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);

      //STEP 4: Execute a query
      System.out.println("Creating statement...");
      stmt = conn.createStatement();
      String sql;
      //sql = "SELECT * from subledger_master";
      sql = "SELECT * from unit_of_measurement";
      ResultSet rs = stmt.executeQuery(sql);

      //STEP 5: Extract data from result set
      while(rs.next()){
         //Retrieve by column name
         int id  = rs.getInt("uom_id");
         int from_mobile = rs.getInt("from_mobile");
         int status = rs.getInt("status");
         String unit = rs.getString("unit");
         int created_by=rs.getInt("created_by");
         int updated_by=rs.getInt("updated_by");
         String ip_address = rs.getString("ip_address");System.out.print("ID: " + id);
				
				  System.out.print("from_mobile: " +from_mobile );
				  System.out.print(", status: " + status); System.out.print(", unit: " + unit);
				  System.out.println(", created_by: " + created_by);
				  System.out.print(", updated_by: " + updated_by);
				  System.out.println(", ip_address: " + ip_address);
				 

      }
      //STEP 6: Clean-up environment
      rs.close();
      stmt.close();
      conn.close();
   }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }// nothing we can do
      try{
         if(conn!=null)
            conn.close();
      }catch(SQLException se){
         se.printStackTrace();
      }
   }
   System.out.println("Code End!");
}
   
   
}