package org.cs305.users;

import org.cs305.Database;
import org.cs305.Log;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Admin extends User {

  public Admin(String name, String email) {
    super(name, email);
  }

  public boolean addUser(String email, String name, String password, String role) {
    try {
      String query = "INSERT INTO users VALUES (?,?,?,?)";
      PreparedStatement stmt = dbConnection.prepareStatement(query);
      stmt.setString(1, email);
      stmt.setString(2, name);
      stmt.setString(3, password);
      stmt.setString(4, role);
      stmt.executeUpdate();
      stmt.close();
      Log.write("Added user (" + role + "): " + name + " (" + email + ")");
      return true;
    } catch (SQLException e) {
      if (e.getSQLState().equals("23505")) {
        Log.error("User " + email + " already exist");
        return false;
      }
      throw new RuntimeException(e);
    }
  }

  public String addInstructor(String email, String name, String password, String dept) {
    try {
      boolean added = addUser(email, name, password, "instructor");
      if (!added) {
        return "User already exist";
      }
      String query = "INSERT INTO instructors VALUES (?,?)";
      PreparedStatement stmt = dbConnection.prepareStatement(query);
      stmt.setString(1, email);
      stmt.setString(2, dept);
      stmt.executeUpdate();
      stmt.close();
      Log.write("Added instructor: " + email + ", department: " + dept);
      return "User added";
    } catch (SQLException e) {
      deleteUser(email);
      String constraint = Database.getSqlConstraint(e);
      if (constraint != null) {
        if (constraint.equals("instructors_dept_fkey")) {
          return "Invalid department";
        }
      }
      throw new RuntimeException(e);
    }
  }

  public String addStudent(String email, String name, String password, String dept, String entryNum, int batch) {
    try {
      boolean added = addUser(email, name, password, "student");
      if (!added) {
        return "User already exist";
      }
      String query = "INSERT INTO students VALUES (?,?,?,?)";
      PreparedStatement stmt = dbConnection.prepareStatement(query);
      stmt.setString(1, email);
      stmt.setString(2, entryNum);
      stmt.setInt(3, batch);
      stmt.setString(4, dept);
      stmt.executeUpdate();
      stmt.close();
      Log.write("Added Student: " + email + ", department: " + dept);
      return "User added";
    } catch (SQLException e) {
      deleteUser(email);
      String constraint = Database.getSqlConstraint(e);
      if (constraint != null) {
        if (constraint
            .equals("students_dept_fkey")) {
          return "Invalid department";
        }
      }
      throw new RuntimeException(e);
    }
  }

  /**
   * Delete user from everywhere
   * 
   * @param email email of user to be deleted
   * @return
   */
  public boolean deleteUser(String email) {
    try {
      String query = "DELETE FROM users WHERE email=?";
      PreparedStatement stmt = dbConnection.prepareStatement(query);
      stmt.setString(1, email);
      int deleted = stmt.executeUpdate();
      stmt.close();
      if (deleted>=1) {
        Log.write("Deleted user: " + email);
      return true;
      }
      else{
        Log.error("Delete User: Invalid user");
        return false;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean changeUserPassword(String email, String password) {
    try {
      String query = "UPDATE users SET password=? WHERE email=?";
      PreparedStatement stmt = dbConnection.prepareStatement(query);
      stmt.setString(1, password);
      stmt.setString(2, email);
      if (stmt.executeUpdate() == 1) {
        Log.write("Password changed for: " + email + " by :" + userEmail);
        return true;
      } else {
        return false;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
