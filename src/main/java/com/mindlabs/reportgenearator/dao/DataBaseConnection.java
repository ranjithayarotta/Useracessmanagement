package com.mindlabs.reportgenearator.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DataBaseConnection {

	public static Connection getResult() throws SQLException {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/aml_db", "root", "root");
			// here sonoo is database name, root is username and passwor
		} catch (Exception e) {
			System.out.println(e);
		}
		return con;

	}
}
