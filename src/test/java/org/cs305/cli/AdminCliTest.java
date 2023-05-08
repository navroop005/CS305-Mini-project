package org.cs305.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.cs305.CommonTestClass;
import org.cs305.Main;
import org.junit.jupiter.api.Test;

public class AdminCliTest extends CommonTestClass {
  
  String login = """
      l
      admin@iitrpr.ac.in
      admin
      """;
  String exit = """
      e
      e
      """;

  @Test
  void testLogin() {
    assertTrue(testInput(login + exit, () -> {
      Main.main(null);
    }));
    assertEquals("Logged in: admin@iitrpr.ac.in", logLastNthLine(3));
    assertEquals("Logged out: admin@iitrpr.ac.in", logLastNthLine(2));
  }

  @Test
  void testAddDelete_admin() {
    // add User
    String input = """
        au
        testAdmin@iitrpr.ac.in
        test admin
        test
        admin
        c
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
    assertEquals("Added user (admin): TEST ADMIN (testAdmin@iitrpr.ac.in)", logLastNthLine(3));

    // add User already exist
    input = """
        au
        testAdmin@iitrpr.ac.in
        test admin
        test
        admin
        c
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
    assertEquals("Error: User testAdmin@iitrpr.ac.in already exist", logLastNthLine(3));

    // Delete User
    input = """
        du
        testAdmin@iitrpr.ac.in
        c
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
    assertEquals("Deleted user: testAdmin@iitrpr.ac.in", logLastNthLine(3));
  }

  @Test
  void testAddDelete_acad_office() {
    // add User
    String input = """
        au
        testacad@iitrpr.ac.in
        test acad
        test
        acad_office
        c
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
    assertEquals("Added user (acad_office): TEST ACAD (testacad@iitrpr.ac.in)", logLastNthLine(3));

    // Delete User
    input = """
        du
        testacad@iitrpr.ac.in
        c
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
    assertEquals("Deleted user: testacad@iitrpr.ac.in", logLastNthLine(3));
  }

  @Test
  void testAddDelete_instructor() {
    // add User
    String input = """
        au
        testinst@iitrpr.ac.in
        test inst
        test
        instructor
        cs
        c
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
   assertEquals("Added instructor: testinst@iitrpr.ac.in, department: CS", logLastNthLine(3));

    // Delete User
    input = """
        du
        testinst@iitrpr.ac.in
        c
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
    assertEquals("Deleted user: testinst@iitrpr.ac.in", logLastNthLine(3));
  }

  @Test
  void testAddDelete_student() {
    // add User
    String input = """
        au
        teststu@iitrpr.ac.in
        test stu
        test
        student
        cs
        2020csb1101
        2020
        c
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
    assertEquals("Added Student: teststu@iitrpr.ac.in, department: CS", logLastNthLine(3));

    // Delete User
    input = """
        du
        teststu@iitrpr.ac.in
        c
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
    assertEquals("Deleted user: teststu@iitrpr.ac.in", logLastNthLine(3));
  }

  @Test
  void testAdd_invalidRole() {
    // add User
    String input = """
        au
        invalid@iitrpr.ac.in
        test stu
        test
        invalid
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
    assertEquals("Error: Add user: Invalid role", logLastNthLine(3));
  }

  @Test
  void testAddInstt_invalidDept() {
    // add User
    String input = """
        au
        invalid@iitrpr.ac.in
        test stu
        test
        instructor
        iv
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
    assertEquals("Error: Add user: Invalid department", logLastNthLine(3));
  }

  @Test
  void testAddstudent_invalidDept() {
    // add User
    String input = """
        au
        invalid@iitrpr.ac.in
        test stu
        test
        student
        iv
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
    assertEquals("Error: Add user: Invalid department", logLastNthLine(3));
  }

  @Test
  void testDelete_invalid() {
    String input = """
        du
        invalid@iitrpr.ac.in
        c
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
    assertEquals("Error: Delete User: Invalid user", logLastNthLine(3));
  }

  @Test
  void testChangePasswordCurrent() {
    String input = """
        cpc
        admin
        iit
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
    assertEquals("Password changed by: admin@iitrpr.ac.in", logLastNthLine(3));

    // revert changes
    input = """
        cpc
        iit
        admin
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
  }

  @Test
  void testChangePasswordOther() {
    String input = """
        cpo
        acadoffice@iitrpr.ac.in
        iit
        c
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));    assertEquals("Password changed for: acadoffice@iitrpr.ac.in by :admin@iitrpr.ac.in", logLastNthLine(3));

    // revert changes
    input = """
        cpo
        acadoffice@iitrpr.ac.in
        acadoffice
        c
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
  }

  @Test 
  void testCourseCatalogue(){
    String input = """
        ec
        e
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
  }

  @Test 
  void testFindUser(){
    String input = """
        fu
        acadoffice@iitrpr.ac.in
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));

    assertTrue(outputContent().contains("Name: ACADEMICS OFFICE"));
  }
}
