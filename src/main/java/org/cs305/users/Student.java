package org.cs305.users;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.cs305.Database;
import org.cs305.Log;
import org.cs305.common.StudentDetails;
import org.cs305.common.Utils;

public class Student extends User {
  String userBranch;
  int userBatch;
  String userEntryNumber;

  public Student(String name, String email) {
    super(name, email);
    getInfo();
  }

  private void getInfo() {
    try {
      String query = "SELECT * FROM students WHERE email=?";
      PreparedStatement stmt = dbConnection.prepareStatement(query);
      stmt.setString(1, userEmail);
      ResultSet rs = stmt.executeQuery();
      rs.next();
      userBranch = rs.getString("dept");
      userBatch = rs.getInt("batch");
      userEntryNumber = rs.getString("entry_number");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public String enrollCourse(String code, String dept, String session) {
    String message;
    try {
      ResultSet rs = getCourseOffering(code, dept, session, userBatch, userBranch);
      if (rs == null) {
        message = "Enrollment not valid! (Check offerings)";
        Log.write(message + ": " + code);
        return message;
      }
      if (!checkCG(rs)) {
        message = "Enrollment not valid! (CG criteria not satisfied)";
        Log.write(message + ": " + code);
        return message;
      }
      if (!checkPrereqs(rs)) {
        message = "Enrollment not valid! (Prerequisites not satisfied)";
        Log.write(message + ": " + code);
        return message;
      }
      if (!checkCredits(rs)) {
        message = "Enrollment not valid! (Credit limit not satisfied)";
        Log.write(message + ": " + code);
        return message;
      }

      String query = "INSERT INTO student_enrollments VALUES (?,?,?,?,?,?,?)";
      PreparedStatement stmt = dbConnection.prepareStatement(query);
      stmt.setString(1, code);
      stmt.setString(2, userEmail);
      stmt.setString(3, dept);
      stmt.setString(4, session);
      stmt.setInt(5, userBatch);
      stmt.setString(6, userBranch);
      stmt.setString(7, rs.getString("course_type"));

      stmt.executeUpdate();
      message = "Enrolled: " + code;
      Log.write(message);
      return message;
    } catch (SQLException e) {
      String constraint = Database.getSqlConstraint(e);
      if (constraint != null) {
        if (constraint
            .equals("student_enrollments_pkey")) {
          return "Already enrolled";
        }
      }

      throw new RuntimeException(e);
    }
  }

  private boolean checkCredits(ResultSet course_offering) {
    try {
      String session = course_offering.getString("session");
      int sessionYear = Utils.getSessionYear(session);
      String sessionSem = Utils.getSessionSem(session);
      float courseCredits = course_offering.getFloat("credits");
      float enrolledCredits = StudentDetails.getCgpa_credits(userEmail, session, true);
      float totalEnroll = courseCredits + enrolledCredits;
      float prevAvg;
      if (sessionYear == userBatch) {
        return totalEnroll <= 18;
      } else if (sessionYear == userBatch + 1 && sessionSem.equals("I")) {
        float prev_credits = StudentDetails.getCgpa_credits(userEmail,
            Utils.prevSession(session), true);
        prevAvg = (prev_credits + 18) / 2;
      } else {
        float prev_credits = StudentDetails.getCgpa_credits(userEmail,
            Utils.prevSession(session), true);
        float prev_prev_credits = StudentDetails.getCgpa_credits(userEmail,
            Utils.prevSession(Utils.prevSession(session)), true);
        prevAvg = (prev_credits + prev_prev_credits) / 2;
      }
      return totalEnroll < prevAvg * 1.25;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private ResultSet getCourseOffering(String code, String dept, String session, int batch, String branch) {
    try {
      String query = "SELECT * FROM course_offering NATURAL JOIN course_catalogue WHERE code=? AND dept=? AND session=? AND batch=? AND branch=?";
      PreparedStatement stmt = dbConnection.prepareStatement(query);
      stmt.setString(1, code);
      stmt.setString(2, dept);
      stmt.setString(3, session);
      stmt.setInt(4, batch);
      stmt.setString(5, branch);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return rs;
      }
      return null;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private boolean checkCG(ResultSet rs) {
    try {
      return rs.getFloat("min_cg") <= StudentDetails.getCgpa_credits(userEmail, null, false);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private boolean checkPrereqs(ResultSet course_offering) {
    try {
      Array prereqsArr = course_offering.getArray("prerequisites");
      if (prereqsArr == null) {
        return true;
      }
      String[] prereqs = (String[]) prereqsArr.getArray();

      for (String prerequisite : prereqs) {
        String query = "SELECT * FROM student_enrollments WHERE student_id=? AND code=?";
        PreparedStatement stmt = dbConnection.prepareStatement(query);
        stmt.setString(1, userEmail);
        stmt.setString(2, prerequisite);
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
          return false;
        }
        String grade = rs.getString("grade");
        if (grade == null || grade.equals("F")) {
          return false;
        }
      }
      return true;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public String dropCourse(String code, String dept, String session) {
    try {
      String query = "DELETE FROM student_enrollments WHERE code=? AND student_id=? AND dept=? AND session=?";

      PreparedStatement stmt = dbConnection.prepareStatement(query);
      stmt.setString(1, code);
      stmt.setString(2, userEmail);
      stmt.setString(3, dept);
      stmt.setString(4, session);
      int deleted = stmt.executeUpdate();
      stmt.close();
      if (deleted == 1) {
        String message = "Dropped enrollment";
        Log.write(message);
        return message + "!";
      } else {
        return "No course dropped";
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
