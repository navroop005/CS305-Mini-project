package org.cs305.common;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.cs305.Database;
import org.cs305.Log;

public abstract class Course {

  public static void viewCatalogue(String dept) {
    Connection conn = Database.getConnection();
    try {
      String query = "SELECT * FROM course_catalogue";
      if (dept != null) {
        query += " WHERE dept=?";
      }
      PreparedStatement stmt = conn.prepareStatement(query);
      if (dept != null) {
        stmt.setString(1, dept);
      }
      ResultSet rs = stmt.executeQuery();
      printCatalogue(rs);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private static void printCatalogue(ResultSet rs) {
    try {
      String table_row = "| %-5s | %-30s | %-10s | %-7s | %-21s|\n";
      String header = String.format(table_row, "Code", "Name", "Department", "Credits", "Prerequisites");

      System.out.println("-".repeat(header.length()));
      System.out.print(header);
      System.out.println("-".repeat(header.length()));

      while (rs.next()) {
        String code = rs.getString("code");
        String name = rs.getString("name");
        String dept = rs.getString("dept");
        float credits = rs.getFloat("credits");
        Array prereqs = rs.getArray("prerequisites");

        String prereqString = "";
        if (prereqs != null) {
          String[] prerequisites = (String[]) prereqs.getArray();
          for (String prereq_code : prerequisites) {
            prereqString += prereq_code + ", ";
          }
        }
        System.out.printf(table_row, code, name, dept, String.format("%.1f", credits), prereqString);
      }
      System.out.println("-".repeat(header.length()));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Adds course to course catalogue
   * 
   * @param code    Course code of course to be added
   * @param name    Name of course to be added
   * @param dept    Offering department for the course
   * @param credits Credits of the course
   * @param prereqs Prerequisites for the course
   * @return
   *         <ul>
   *         <li>"Course added to catalogue" if added successfully</li>
   *         <li>"Course, department already exists" if course
   *         is already in the catalogue</li>
   *         <li>"Invalid department" if department not in
   *         departments table</li>
   *         </ul>
   */
  public static String addCourse(String code, String name, String dept, float credits, String[] prereqs) {
    Connection conn = Database.getConnection();
    String message = "";
    try {
      String query = "INSERT INTO course_catalogue VALUES (?,?,?,?,?)";
      PreparedStatement stmt = conn.prepareStatement(query);
      stmt.setString(1, code);
      stmt.setString(2, name);
      stmt.setString(3, dept);
      stmt.setFloat(4, credits);
      Array prereqArray = conn.createArrayOf("VARCHAR", prereqs);
      stmt.setArray(5, prereqArray);
      stmt.executeUpdate();
      message = "Course added to catalogue";
      Log.write(message);
      return message;
    } catch (SQLException e) {
      String constraint = Database.getSqlConstraint(e);
      if (constraint != null) {
        if (constraint
            .equals("course_catalogue_dept_fkey")) {
          return "Invalid department";
        } else if (constraint
            .equals("course_catalogue_pkey")) {
          return "Course, department already exists";
        }
      }
      
      throw new RuntimeException(e);
    }
  }

  /**
   * Deletes given course from everywhere
   * 
   * @param code Course code of the course to be deleted
   * @param dept offering department
   * @return true if course is deleted, false if no such course
   */
  public static boolean deleteCourse(String code, String dept) {
    Connection conn = Database.getConnection();
    try {
      String query = "DELETE FROM course_catalogue WHERE code=? AND dept=?";
      PreparedStatement stmt = conn.prepareStatement(query);
      stmt.setString(1, code);
      stmt.setString(2, dept);
      int deleted = stmt.executeUpdate();
      stmt.close();
      Log.write("Deleted course: " + code);
      return deleted >= 1;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static void viewOfferings(String dept) {
    Connection conn = Database.getConnection();
    try {
      String query = "SELECT * FROM course_offering";
      if (dept != null) {
        query += " WHERE dept=?";
      }
      PreparedStatement stmt = conn.prepareStatement(query);
      if (dept != null) {
        stmt.setString(1, dept);
      }
      ResultSet rs = stmt.executeQuery();
      printOfferings(rs);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Prints course offerings in resultset to System.out
   * 
   * @param rs ResultSet for course offerings table
   */
  public static int printOfferings(ResultSet rs) {
    try {
      String table_row = "| %-5s | %-10s | %-7s | %-30s | %-6s | %-5s | %-6s | %-8s |\n";
      String header = String.format(table_row, "Code", "Department",
          "Session", "Instructor", "Min CG", "Batch", "branch", "Type");

      System.out.println("-".repeat(header.length()));
      System.out.print(header);
      System.out.println("-".repeat(header.length()));

      int count = 0;
      while (rs.next()) {
        String code = rs.getString("code");
        String dept = rs.getString("dept");
        String session = rs.getString("session");
        String inst = rs.getString("instructor");
        float min_cg = rs.getFloat("min_cg");
        Integer batch = rs.getInt("batch");
        String branch = rs.getString("branch");
        String type = rs.getString("course_type");

        System.out.printf(table_row, code, dept, session, inst,
            Utils.formatCG( min_cg), batch.toString(),
            branch, type);
        count++;
      }
      System.out.println("-".repeat(header.length()));

      return count;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
