package org.cs305.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.cs305.CommonTestClass;
import org.cs305.users.Student;
import org.junit.jupiter.api.Test;

public class StudentDetailsTest extends CommonTestClass {
  @Test
  void testGetCgpa_credits() {
    enrollTestStudent();
    StudentDetails.getCgpa_credits("2023csb1001@iitrpr.ac.in", null, false);
    StudentDetails.getCgpa_credits("2023csb1001@iitrpr.ac.in", null, true);
  }

  @Test
  void testGetGrade() {
    enrollTestStudent();
    assertEquals(StudentDetails.getGrade("2023csb1002@iitrpr.ac.in", "CS106", "CS"), "C");
    assertEquals(StudentDetails.getGrade("2023csb1002@iitrpr.ac.in", "CS401", "CS"), "A");

    assertEquals(StudentDetails.getGrade("2023csb1000@iitrpr.ac.in", "CS401", "CS"), "Student not enrolled");
    Student st = new Student("Test", "2023csb1002@iitrpr.ac.in");
    st.dropCourse("CS507", "CS", "2027-I");
    st.enrollCourse("CS507", "CS", "2027-I");

    assertEquals(StudentDetails.getGrade("2023csb1002@iitrpr.ac.in", "CS507", "CS"), "Grade Pending");
  }

  @Test
  void testPrintEnrollments() {
    enrollTestStudent();
    Student st = new Student("Test", "2023csb1002@iitrpr.ac.in");
    st.dropCourse("CS507", "CS", "2027-I");
    st.enrollCourse("CS507", "CS", "2027-I");
    testInput("", () -> {
      StudentDetails.printEnrollments("2023csb1002@iitrpr.ac.in");
    });
    ResultSet rs = StudentDetails.getEnrollments("2023csb1002@iitrpr.ac.in", null);
    try {
      int count = 0;
      while (rs.next()) {
        count++;
      }
      String[] lines = outputContent().split("\n");
      assertEquals(count, lines.length - 5);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    testInput("", () -> {
      StudentDetails.printEnrollments("2023csb1003@iitrpr.ac.in");
    });
    rs = StudentDetails.getEnrollments("2023csb1003@iitrpr.ac.in", null);
    try {
      int count = 0;
      while (rs.next()) {
        count++;
      }
      String[] lines = outputContent().split("\n");
      assertEquals(count, lines.length - 4);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void testPrintGraduationCheck() {
    enrollTestStudent();
    testInput("", () -> {
      StudentDetails.printGraduationCheck("2023csb1002@iitrpr.ac.in");
    });
  }
}
