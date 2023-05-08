package org.cs305.users;

import org.cs305.Database;
import org.cs305.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
  final Connection dbConnection;
  public final String userEmail;
  public final String userName;

  public User(String name, String email) {
    dbConnection = Database.getConnection();
    userName = name;
    userEmail = email;
  }

  static public String[] verify(String email, String password) {
    Connection conn = Database.getConnection();
    try {
      String query = "SELECT * FROM users WHERE email=? AND password=?";
      PreparedStatement stmt = conn.prepareStatement(query);
      stmt.setString(1, email);
      stmt.setString(2, password);
      ResultSet resultSet = stmt.executeQuery();
      if (resultSet.next()) {
        String name = resultSet.getString("name");
        String role = resultSet.getString("role");
        Log.write("verified: " + email);
        return new String[] { name, role };
      }
      Log.write("Invalid email/password: " + email);
      return null;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean changePassword(String oldPassword, String newPassword) {
    try {
      if (verify(userEmail, oldPassword) != null) {
        String query = "UPDATE users SET password=? WHERE email=?";
        PreparedStatement stmt = dbConnection.prepareStatement(query);
        stmt.setString(1, newPassword);
        stmt.setString(2, userEmail);
        stmt.executeUpdate();
        Log.write("Password changed by: " + userEmail);
        return true;
      }
      Log.write("Invalid password change request by: " + userEmail);
      return false;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  static public String[] findUser(String email) {
    Connection conn = Database.getConnection();
    try {
      String query = "SELECT * FROM users WHERE email=?";
      PreparedStatement stmt = conn.prepareStatement(query);
      stmt.setString(1, email);
      ResultSet resultSet = stmt.executeQuery();
      if (resultSet.next()) {
        String name = resultSet.getString("name");
        String role = resultSet.getString("role");
        return new String[] { name, role };
      } else {
        return null;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
