package org.cs305.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.cs305.CommonTestClass;
import org.cs305.Database;
import org.cs305.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class AcadOfficeCliTest extends CommonTestClass {
  @BeforeEach
  void cleanBefore() {
    Database.clearData();
  }
  @AfterEach
  void cleanAfter() {
    Database.clearData();
  }

  String login = """
      l
      acadoffice@iitrpr.ac.in
      acadoffice
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
    assertEquals("Logged in: acadoffice@iitrpr.ac.in", logLastNthLine(3));
    assertEquals("Logged out: acadoffice@iitrpr.ac.in", logLastNthLine(2));
  }

  @Test
  void testCourseCatalogue() {
    input = """
        cc
        e
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
  }

  @ParameterizedTest
  @ValueSource(strings = { "ce", "cd", "rs", "ds", "gu" })
  void testAcadCalendar(String s) {
    input = """
        ac
        """
        + s +
        """


            e
            """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
    assertEquals("Academic calendar updated", logLastNthLine(3));

    input = """
        ac
        """
        + s +
        """

            2023-II
            e
            """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
    assertEquals("Academic calendar updated", logLastNthLine(3));

    input = """
        ac
        """
        + s +
        """

            202ss
            e
            """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
    assertNotEquals("Academic calendar updated", logLastNthLine(3));
  }

  @Test
  void testAddStudent() {
    input = """
        as
        s
        Test
        2020csb1101
        iit
        c
        e
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
    assertEquals("Added student: 2020csb1101@iitrpr.ac.in, department: CS", logLastNthLine(3));
  }

  @Test
  void testAddInstructor() {
    input = """
        ai
        s
        Test
        testInst@iitrpr.ac.in
        cs
        iit
        c
        e
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
    assertEquals("Added instructor: testInst@iitrpr.ac.in, department: CS", logLastNthLine(3));

    input = """
        ai
        s
        Test
        testInst@iitrpr.ac.in
        iv
        e
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
    assertEquals("Error: Add Instructor: Invalid department", logLastNthLine(3));
  }

  @Test
  void testAddFromCSV() {
    addData();
    assertEquals("Course added to catalogue", logLastNthLine(3));

    input = """
        ai
        c
        invalid/path
        c
        e
        e
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
    assertEquals("Error: Add from csv: Invalid file path", logLastNthLine(3));

    input = """
        as
        c
        invalid/path
        c
        e
        e
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));

    input = """
        cc
        am
        c
        invalid/path
        c
        e
        e
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
    assertEquals("Error: Add from csv: Invalid file path", logLastNthLine(3));
  }

  @Test
  void testGraduationCheck() {
    enrollTestStudent();
    input = """
        gc
        2023csb1002


        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
  }

  @Test 
  void testAddDeleteCourse(){
    input = """
        cc
        ac
        cs000
        test course
        cs
        3

        c
        e
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
    assertEquals("Course added to catalogue", logLastNthLine(3));
    
    input = """
        cc
        dc
        cs000
        cs
        c
        e
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
    assertEquals("Deleted course: CS000", logLastNthLine(3));
  }
}
