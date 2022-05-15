package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

public class PaymentManagement {

	private Connection connect() {
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Provide the correct details: DBServer/DBName, username, password
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/EGCompDb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
					"root", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	public String insertProject(String CardType, String AmountPaid, String ArrearsAmount, String PaidDate, String AccNumber) {
		String output = "";
		try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for inserting.";
			}
			// create a prepared statement
			String query = " insert into payment(`PaymentId`,`CardType`,`AmountPaid`,`ArrearsAmount`,`PaidDate`,`AccNumber`)"
					+ " values (?, ?, ?, ?, ?, ?)";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			// binding values
			preparedStmt.setInt(1, 0);
			preparedStmt.setString(2, CardType);
			preparedStmt.setString(3, AmountPaid);
			preparedStmt.setString(4, ArrearsAmount);
			preparedStmt.setString(5, PaidDate);
			preparedStmt.setString(6, AccNumber);

			// execute the statement
			preparedStmt.execute();
			con.close();

			String newProject = readProject();
			output = "{\"status\":\"success\", \"data\": \"" + newProject + "\"}";
		} catch (Exception e) {
			output = "{\"status\":\"error\", \"data\": \"Error while inserting the project.\"}";
			System.err.println(e.getMessage());
		}

		return output;
	}

	public String readProject() {
		String output = "";
		try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for reading.";
			}
			// Prepare the html table to be displayed
			output = "<table border=\'1\'><tr><th>Card Type</th><th>Amount Paid</th><th>Arrears Amount</th><th>Paid date</th><th>Acc Number</th><th>Update</th><th>Delete</th></tr>";
			String query = "select * from payment";

			Statement stmt = (Statement) con.createStatement();
			ResultSet rs = ((java.sql.Statement) stmt).executeQuery(query);
			// iterate through the rows in the result set
			while (rs.next()) {
				String PaymentId = rs.getString("PaymentId");
				String CardType = rs.getString("CardType");
				String AmountPaid = rs.getString("AmountPaid");
				String ArrearsAmount = rs.getString("ArrearsAmount");
				String PaidDate = rs.getString("PaidDate");
				String AccNumber = rs.getString("AccNumber");
				

				// Add into the html table
				output += "<tr><td><input id=\'hidProjectIDUpdate\' name=\'hidProjectIDUpdate\' type=\'hidden\' value=\'"
						+ PaymentId + "'>" + CardType + "</td>";
				output += "<td>" + AmountPaid + "</td>";
				output += "<td>" + ArrearsAmount + "</td>";
				output += "<td>" + PaidDate + "</td>";
				output += "<td>" + AccNumber + "</td>";
				

				
				output += "<td><input name='btnUpdate' type='button' value='Update' class='btnUpdate btn btn-secondary'></td>"
						+ "<td><input name='btnRemove' type='button' value='Remove' class='btnRemove btn btn-danger' data-payid='"
						+ PaymentId + "'>" + "</td></tr>";
			}

			con.close();

			// Complete the html table
			output += "</table>";
		} catch (Exception e) {
			output = "Error while reading the projects.";
			System.err.println(e.getMessage());
		}

		return output;
	}

	public String updateProject(String PaymentId,String CardType, String AmountPaid,String ArrearsAmount, String PaidDate, String AccNumber) {
		String output = "";

		try {
			Connection con = connect();

			if (con == null) {
				return "Error while connecting to the database for updating.";
			}

			// create a prepared statement
			String query = "UPDATE payment SET CardType=?,AmountPaid=?,ArrearsAmount=?,PaidDate=?,AccNumber=? WHERE PaymentId=?";

			PreparedStatement preparedStmt = con.prepareStatement(query);

			// binding values

			preparedStmt.setString(1, CardType);
			preparedStmt.setString(2, AmountPaid);
			preparedStmt.setString(3, ArrearsAmount);
			preparedStmt.setString(4, PaidDate);
			preparedStmt.setString(5, AccNumber);
			preparedStmt.setInt(6, Integer.parseInt(PaymentId));

			// execute the statement
			preparedStmt.execute();
			con.close();

			String newProject = readProject();
			output = "{\"status\":\"success\", \"data\": \"" + newProject + "\"}";
		} catch (Exception e) {
			output = "{\"status\":\"error\", \"data\": \"Error while updating the project.\"}";
			System.err.println(e.getMessage());
		}

		return output;
	}

	public String deleteProject(String PaymentId) {

		String output = "";

		try {
			Connection con = connect();

			if (con == null) {
				return "Error while connecting to the database for deleting.";
			}

			// create a prepared statement
			String query = "delete from payment where PaymentId=?";

			PreparedStatement preparedStmt = con.prepareStatement(query);

			// binding values
			preparedStmt.setInt(1, Integer.parseInt(PaymentId));

			// execute the statement
			preparedStmt.execute();
			con.close();

			String newProject = readProject();
			output = "{\"status\":\"success\", \"data\": \"" + newProject + "\"}";
		} catch (Exception e) {
			output = "Error while deleting the project.";
			System.err.println(e.getMessage());
		}

		return output;
	}

}
