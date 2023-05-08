package org.cs305.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.cs305.Database;

public abstract interface StudentDetails {
  public static String getGrade(String email, String code, String dept) {
    try {
      Connection conn = Database.getConnection();
      String query = "SELECT * FROM student_enrollments WHERE student_id=? AND code=? AND dept=?";
      PreparedStatement stmt = conn.prepareStatement(query);
      stmt.setString(1, email);
      stmt.setString(2, code);
      stmt.setString(3, dept);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        String grade = rs.getString("grade");
        if (grade == null) {
          return "Grade Pending";
        }
        return grade;
      } else {
        return "Student not enrolled";
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static ResultSet getEnrollments(String email, String session) {
    Connection conn = Database.getConnection();
    try {
      String query = "SELECT * FROM student_enrollments NATURAL JOIN course_catalogue WHERE student_id=?";
      if (session == null) {
        query += " ORDER BY session";
      } else {
        query += " AND session='" + session + "'";
      }

      PreparedStatement stmt = conn.prepareStatement(query);
      stmt.setString(1, email);
      return stmt.executeQuery();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static void printEnrollments(String email) {
    try {
      ResultSet rs = getEnrollments(email, null);
      String table_row = "| %-5s | %-30s | %-10s | %-7s | %-7s | %-5s |\n";
      String header = String.format(table_row, "Code", "Name", "Department",
          "Session", "Credits", "Grade");

      System.out.println("-".repeat(header.length()));
      System.out.print(header);
      System.out.println("-".repeat(header.length()));

      float cg = 0;
      float total_credits = 0;

      while (rs.next()) {
        String code = rs.getString("code");
        String name = rs.getString("name");
        String dept = rs.getString("dept");
        String session = rs.getString("session");
        float credits = rs.getFloat("credits");
        String grade = rs.getString("grade");

        System.out.printf(table_row, code, name, dept, session, String.format("%.1f", credits),
            (grade == null) ? "" : grade);

        if (!(grade == null || grade.equals("F") || grade.equals("E"))) {
          total_credits += credits;
          int grade_value = Utils.gradeValue(grade);
          cg += grade_value * credits;
        }
      }
      System.out.println("-".repeat(header.length()));
      if (total_credits != 0) {
        System.out.println("Total Credits: " + Utils.formatCG(total_credits)
            + ", CGPA: " + Utils.formatCG(cg / total_credits));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static float getCgpa_credits(String email, String session, boolean ret_credits) {
    try {
      float cg = 0;
      float total_credits = 0;
      ResultSet rs = getEnrollments(email, session);

      while (rs.next()) {
        String grade = rs.getString("grade");
        float credits = rs.getFloat("credits");
        if (!(grade == null || grade.equals("F") || grade.equals("E"))) {
          total_credits += credits;
          int grade_value = Utils.gradeValue(grade);
          cg += grade_value * credits;
        }
      }
      if (ret_credits) {
        return total_credits;
      }
      if (total_credits == 0) {
        return 0;
      }
      return cg / total_credits;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static void printGraduationCheck(String email) {
    try {
      float credits_pc = 0;
      float credits_pe = 0;
      float credits_oe = 0;
      float credits_cp = 0;
      float cg = 0;

      ResultSet rs = getEnrollments(email, null);

      while (rs.next()) {
        String grade = rs.getString("grade");
        float credits = rs.getFloat("credits");
        if (!(grade.equals("F") || grade.equals("E") || grade.equals(null))) {
          int grade_value = Utils.gradeValue(grade);
          cg += grade_value * credits;
          switch (rs.getString("course_type")) {
            case "PC":
              credits_pc++;
              break;
            case "PE":
              credits_pe++;
              break;
            case "OE":
              credits_oe++;
              break;
            case "cp":
              credits_cp++;
              break;
          }
        }
      }
      float total_credits = credits_cp + credits_oe + credits_pc + credits_pe;
      System.out.println("Total credits: " + Utils.formatCG(total_credits));
      if (total_credits >= 120) {
        System.out.println("Total credits satisfied");
      } else {
        System.out.println("Total credits not satisfied (required 120)");
      }
      System.out.println("\nCredit distribution:");
      System.out.println("Program core (" + (credits_pc > 60 ? "satisfied" : "unsatisfied")
          + "(required 60)): " + Utils.formatCG(credits_pc));
      System.out.println("Program elective (" + (credits_pe > 30 ? "satisfied" : "unsatisfied")
          + "(required 30)): " + Utils.formatCG(credits_pe));
      System.out.println("Capstone (" + (credits_cp > 9 ? "satisfied" : "unsatisfied")
          + "(required 9)): " + Utils.formatCG(credits_cp));
      System.out.println("Other elective (" + (credits_oe > 21 ? "satisfied" : "unsatisfied")
          + "(required 120)): " + Utils.formatCG(credits_oe));

      System.out.println("CGPA: " + Utils.formatCG(cg / total_credits));
      if (total_credits >= 120 && credits_pe > 30 && credits_cp > 9 && credits_oe > 21) {
        System.out.println("All requirements are satisfied");
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
