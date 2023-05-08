package org.cs305.users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.cs305.Database;
import org.cs305.Log;
import org.cs305.common.Course;
import org.cs305.common.Utils;

public class Instructor extends User {
  final String department;

  public Instructor(String name, String email) {
    super(name, email);
    department = getInfo();
  }

  private String getInfo() {
    try {
      String query = "SELECT * FROM instructors WHERE email=?";
      PreparedStatement stmt = dbConnection.prepareStatement(query);
      stmt.setString(1, userEmail);
      ResultSet rs = stmt.executeQuery();
      rs.next();
      return rs.getString("dept");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void viewRegistered() {
    try {
      String query = "SELECT * FROM course_offering WHERE instructor=?";
      PreparedStatement stmt = dbConnection.prepareStatement(query);
      stmt.setString(1, userEmail);
      ResultSet rs = stmt.executeQuery();
      Course.printOfferings(rs);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public String registerCourse(String code, String dept, String session, float min_cg,
      int batch, String branch, String type) {
    String message;
    try {
      String query = "INSERT INTO course_offering VALUES (?,?,?,?,?,?,?,?)";
      PreparedStatement stmt = dbConnection.prepareStatement(query);
      stmt.setString(1, code);
      stmt.setString(2, dept);
      stmt.setString(3, session);
      stmt.setString(4, userEmail);
      stmt.setFloat(5, min_cg);
      stmt.setInt(6, batch);
      stmt.setString(7, branch);
      stmt.setString(8, type);
      stmt.executeUpdate();
      message = "Added course to offerings";
      Log.write(message);

      return message;
    } catch (SQLException e) {
      String constraint = Database.getSqlConstraint(e);
      if (constraint != null) {
        if (constraint
            .equals("course_offering_code_dept_fkey")) {
          return "Invalid course (Not in catalogue)";
        } else if (constraint
            .equals("course_offering_pkey")) {
          return "Course, department, session already exists";
        }
      }

      throw new RuntimeException(e);
    }
  }

  public int findRegistered(String code,
      String session, Integer batch, String branch) {
    try {
      String query = "SELECT * FROM course_offering WHERE code='" + code + "'";
      query += " AND dept='" + department + "'";
      query += " AND instructor='" + userEmail + "'";
      query += " AND session='" + session + "'";
      if (batch != null) {
        query += " AND batch=" + batch;
      }
      if (branch != null) {
        query += " AND branch='" + branch + "'";
      }
      Statement stmt = dbConnection.createStatement();
      ResultSet rs = stmt.executeQuery(query);
      return Course.printOfferings(rs);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public int deregisterCourse(String code, String dept,
      String session, Integer batch, String branch) {
    try {
      String query = "DELETE FROM course_offering WHERE code='" + code + "'";
      query += " AND dept='" + dept + "'";
      query += " AND instructor='" + userEmail + "'";
      query += " AND session='" + session + "'";
      if (batch != null) {
        query += " AND batch=" + batch;
      }
      if (branch != null) {
        query += " AND branch='" + branch + "'";
      }
      Statement stmt = dbConnection.createStatement();
      int deleted = stmt.executeUpdate(query);
      stmt.close();
      Log.write("Deleted offering for: " + code + ", session: " + session);
      return deleted;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean isOffering(String code, String dept, String session, int batch, String branch) {
    try {
      String query = "SELECT * FROM course_offering WHERE code='" + code + "'";
      query += " AND dept='" + dept + "'";
      query += " AND instructor='" + userEmail + "'";
      query += " AND session='" + session + "'";
      query += " AND batch=" + batch;
      query += " AND branch='" + branch + "'";

      Statement stmt = dbConnection.createStatement();
      ResultSet rs = stmt.executeQuery(query);

      int count = 0;
      while (rs.next()) {
        count++;
      }

      return count > 0;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public String updateGrade(String entryNumber, String code, String dept,
      String session, String grade) {
    String message;
    if (!isOffering(code, dept, session, Utils.getBatch(entryNumber), Utils.getBranch(entryNumber))) {
      message = "You have not offered this course!";
      return message;
    }
    try {
      String query = "UPDATE student_enrollments SET grade=? WHERE student_id=? AND code=? AND dept=? AND session=?";
      PreparedStatement stmt = dbConnection.prepareStatement(query);
      stmt.setString(1, grade);
      stmt.setString(2, Utils.entryNumToEmail(entryNumber));
      stmt.setString(3, code);
      stmt.setString(4, dept);
      stmt.setString(5, session);
      if (stmt.executeUpdate() == 1) {
        message = "Updated grade";
        Log.write(message + " for " + entryNumber + " course: " + code);
      } else {
        message = "Student not enrolled";
        Log.error(message);
      }
      return message;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
