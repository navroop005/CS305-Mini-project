package org.cs305.users;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import org.cs305.Database;
import org.cs305.Log;
import org.cs305.common.StudentDetails;
import org.cs305.common.Utils;

public class AcadOffice extends User {

  public AcadOffice(String name, String email) {
    super(name, email);
  }

  /**
   * Adds user to users table
   * 
   * @param email    email of user to be added
   * @param name     name of user to be added
   * @param password password of user to be added
   * @param role     role (admin, acad_office, instructor, student) of user to be
   *                 added
   * @return true if added successfully, false if user with same email already
   *         exist
   */
  private boolean addUser(String email, String name, String password, String role) {
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

  /**
   * Adds instructor to instructor table including
   * adding it to users table.
   * 
   * @param email    email of instructor to be added
   * @param name     name of instructor to be added
   * @param password password of instructor to be added
   * @param dept     department of instructor to be added
   * @return
   *         <ul>
   *         <li>"User added" if successfully added,</li>
   *         <li>"User already exist" if there is entry in users
   *         table with same email exist in users table</li>
   *         <li>"Invalid department" if department code is not in departments
   *         table</li>
   *         </ul>
   */
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
      // Undo add to user table (if added)
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

  /**
   * Adds instructor to instructor table including
   * adding it to users table. Interprets email, batch and department from it.
   * 
   * @param name     name of student to be added
   * @param entryNum entry number of student to be added
   * @param password name of student to be added
   * @return
   *         <ul>
   *         <li>"User added" if successfully added,</li>
   *         <li>"User already exist" if there is entry in users
   *         table with same email exist in users table</li>
   *         <li>"Invalid department" if department code is not
   *         in departments table</li>
   *         </ul>
   */
  public String addStudent(String name, String entryNum, String password) {
    if (!Pattern.matches("\\d{4}[A-Z]{3}\\d{4}", entryNum)) {
      Log.error("Add student: invalid entry number");
      return "Invalid entry number";
    }
    String email = Utils.entryNumToEmail(entryNum);
    int batch = Utils.getBatch(entryNum);
    String branch = Utils.getBranch(entryNum);
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
      stmt.setString(4, branch);
      stmt.executeUpdate();
      stmt.close();
      Log.write("Added student: " + email + ", department: " + branch);
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
      Log.write("Deleted user: " + email);
      return deleted >= 1;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void generateTranscript(String entryNumber) {
    try {
      String email = Utils.entryNumToEmail(entryNumber);
      String query = "SELECT * FROM students NATURAL JOIN users WHERE email=?";
      PreparedStatement stmt = dbConnection.prepareStatement(query);
      stmt.setString(1, email);
      ResultSet details = stmt.executeQuery();
      if (!details.next()) {
        System.out.println("Invalid entry number");
        return;
      }
      String filePath = "output/" + entryNumber + ".txt";
      PrintWriter writer = new PrintWriter(filePath);

      writer.println("Name: " + details.getString("name"));
      writer.println("Entry number: " + details.getString("entry_number"));

      writer.println("Branch: " + Utils.getDepartments().get(details.getString("dept")));

      ResultSet rs = StudentDetails.getEnrollments(email, null);

      String table_row = "| %-5s | %-30s | %-10s | %-7s | %-7s | %-5s |\n";
      String header = String.format(table_row, "Code", "Name", "Department",
          "Session", "Credits", "Grade");

      writer.println('\n' + "-".repeat(header.length()));
      writer.print(header);
      writer.println("-".repeat(header.length()));

      float total_cg = 0;
      float total_credits = 0;
      String prevSession = null;
      float session_cg = 0;
      float session_credits = 0;
      while (rs.next()) {
        String code = rs.getString("code");
        String name = rs.getString("name");
        String dept = rs.getString("dept");
        String session = rs.getString("session");
        float credits = rs.getFloat("credits");
        String grade = rs.getString("grade");

        writer.printf(table_row, code, name, dept, session, String.format("%.1f", credits),
            (grade == null) ? "" : grade);

        if (!(grade == null || grade.equals("F") || grade.equals("E"))) {
          total_credits += credits;
          session_credits += credits;
          int grade_value = Utils.gradeValue(grade);
          session_cg += grade_value * credits;
          total_cg += grade_value * credits;
        }
        if (prevSession != null && !session.equals(prevSession)) {
          writer.println("-".repeat(header.length()));
          if (session_credits > 0) {
            writer.println("\nSGPA = " + Utils.formatCG(session_cg / session_credits) +
                ", Semester credits = " + Utils.formatCG(session_credits));
          }
          writer.println();
          writer.println("-".repeat(header.length()));
          session_cg = 0;
          session_credits = 0;
        }
        prevSession = session;
      }
      writer.println("-".repeat(header.length()));
      if (session_credits > 0) {
        writer.println("\nSGPA = " + Utils.formatCG( session_cg / session_credits) +
            ", Semester credits = " + Utils.formatCG( session_credits));
      }
      if (total_credits > 0) {
        writer.println("\nCGPA = " + Utils.formatCG( total_cg / total_credits) +
            ", Total credits = " + Utils.formatCG( total_credits));
      }
      writer.close();
      System.out.println("Transcript written to: " + filePath);
    } catch (IOException | SQLException e) {
      throw new RuntimeException(e);
    }
  }
}