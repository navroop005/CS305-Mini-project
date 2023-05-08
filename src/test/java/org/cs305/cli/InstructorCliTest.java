package org.cs305.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.cs305.CommonTestClass;
import org.cs305.Database;
import org.cs305.Main;
import org.cs305.common.StudentDetails;
import org.cs305.users.Instructor;
import org.cs305.users.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InstructorCliTest extends CommonTestClass {
  @BeforeEach
  void cleanBefore(){
    Database.clearData();
    enrollTestStudent();
  }

  // @AfterEach
  // void cleanAfter(){
  //   Database.clearData();
  // }

  String login = """
      l
      instructor0@iitrpr.ac.in
      iit
      """;
  String exit = """
      e
      e
      """;
  String input;

  @Test
  void testLogin() {
    assertTrue(testInput(login + exit, () -> {
      Main.main(null);
    }));
    assertEquals("Logged in: instructor0@iitrpr.ac.in", logLastNthLine(3));
    assertEquals("Logged out: instructor0@iitrpr.ac.in", logLastNthLine(2));
  }

  @Test
  void testRegisterDeregisterCourse(){
    input= """
        c
        rc
        cs110
        cs
        100
        0
        0
        2020
        cs
        pc
        c
        e
        """;

    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
    assertEquals("Added course to offerings", logLastNthLine(3));

    input = """
        c
        dc
        cs110
        cs
        2020

        c
        e
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
    assertEquals("Deleted offering for: CS110, session: 2023-II", logLastNthLine(3));

    input= """
        c
        rc
        cs110
        cs
        100
        0
        0
        2020
        iv
        pc
        c
        e
        """;

    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
    assertEquals("Register course: Invalid branch", logLastNthLine(3));
  }

  @Test
  void testViewCatalogueOffering(){
    input = """
        c
        vc
        all

        cs

        e
        vr

        e
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
  }

  @Test
  void testViewGrade(){
    input = """
        g
        vg
        ea
        2023csb1002
        ec
        2023csb1002
        cs101
        cs
        e
        e
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
    String output = outputContent();
    assertTrue(output.contains("Grade: A"));
  }

  @Test 
  void testUpdateGrade(){
    Instructor inst = new Instructor("TEST", "instructor0@iitrpr.ac.in");
    inst.registerCourse("CS110", "CS", "2023-II", 0, 2023, "CS", "PC");
    Student student = new Student("TEST", "2023csb1001@iitrpr.ac.in");
    student.enrollCourse("CS110", "CS", "2023-II");

    input = """
        g
        us
        CS110
        cs
        2023csb1001
        a-
        c
        e
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));

    assertEquals("A-", StudentDetails.getGrade("2023csb1001@iitrpr.ac.in", "CS110", "CS"));;
  }

  @Test 
  void testUploadGrade(){
    Instructor inst = new Instructor("TEST", "instructor0@iitrpr.ac.in");
    inst.registerCourse("CS110", "CS", "2023-II", 0, 2023, "CS", "PC");
    Student student = new Student("TEST", "2023csb1001@iitrpr.ac.in");
    student.enrollCourse("CS110", "CS", "2023-II");

    input = """
        g
        uc
        CS110
        CS
        invalid/path.csv
        uc
        CS110
        CS
        input/grade.csv
        c
        e
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));

    assertEquals("A-", StudentDetails.getGrade("2023csb1001@iitrpr.ac.in", "CS110", "CS"));
  }
}
