package org.cs305.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.cs305.CommonTestClass;
import org.cs305.Database;
import org.cs305.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StudentCliTest extends CommonTestClass {
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
      2023csb1001@iitrpr.ac.in
      iit
      """;
  String exit = """
      e
      e
      """;
  String input;

  @Test
  void testLogin() {
    addData();
    assertTrue(testInput(login + exit, () -> {
      Main.main(null);
    }));
    assertEquals("Logged in: 2023csb1001@iitrpr.ac.in", logLastNthLine(3));
    assertEquals("Logged out: 2023csb1001@iitrpr.ac.in", logLastNthLine(2));
  }

  @Test
  void testViewCatalogue() {
    addData();
    input = """
        vc
        all

        e
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
  }

  @Test
  void testViewOffering() {
    enrollTestStudent();
    input = """
        vo
        all

        e
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
  }

  @Test
  void testEnrollDrop() {
    enrollTestStudent();
    input = """
        ec
        cs101
        iv
        ec
        cs101
        cs
        c
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
    assertEquals("Enrolled: CS101", logLastNthLine(3));

    input = """
        ve

        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
    assertTrue(outputContent().contains("CS101"));

    input = """
        dc
        cs101
        iv
        dc
        cs101
        cs
        c
        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
    assertEquals("Dropped enrollment", logLastNthLine(3));
  }
@Test 
  void testViewCG(){
    addData();
    input = """
        cg

        """;
    assertTrue(testInput(login + input + exit, () -> {
      Main.main(null);
    }));
    assertTrue(outputContent().contains("CGPA"));
  }
}
