package org.cs305.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.cs305.CommonTestClass;
import org.cs305.Database;
import org.junit.jupiter.api.Test;

public class AcadOfficeTest extends CommonTestClass {

  AcadOffice acad = new AcadOffice("Academic Office", "acadoffice@iitrpr.ac.in");

  @Test
  void testAddInstructor() {
    String response = acad.addInstructor("testinst@iitrpr.ac.in", "Test Instructor", "test", "CS");
    assertEquals(response, "User added");

    response = acad.addInstructor("testinst@iitrpr.ac.in", "Test Instructor", "test", "CS");
    assertEquals(response, "User already exist");

    response = acad.addInstructor("testinst2@iitrpr.ac.in", "Test Instructor2", "test", "DP");
    assertEquals(response, "Invalid department");

    acad.deleteUser("testinst@iitrpr.ac.in");
  }

  @Test
  void testAddStudent() {
    String response = acad.addStudent("Test Student", "2000CSB0000", "test");
    assertEquals(response, "User added");

    response = acad.addStudent("Test Student", "2000CSB0000", "test");
    assertEquals(response, "User already exist");

    response = acad.addStudent("Test Student", "2000CPB0000", "test");
    assertEquals(response, "Invalid department");

    response = acad.addStudent("Test Student", "200DFFGE00", "test");
    assertEquals(response, "Invalid entry number");

    acad.deleteUser("2000csb0000@iitrpr.ac.in");
  }

  @Test
  void testDeleteUser(){
    acad.addInstructor("testinst@iitrpr.ac.in", "Test", "test", "CS");
    assertTrue(acad.deleteUser("testinst@iitrpr.ac.in"));
    assertFalse(acad.deleteUser("abc@iitrpr.ac.in"));
  }

  @Test
  void testGenerateTranscript() {
    enrollTestStudent();
    
    testInput("", ()-> {
      acad.generateTranscript("2023CSB1002");
    });
    assertEquals("Transcript written to: output/2023CSB1002.txt", outputLastNthLine(0));

    testInput("", ()-> {
      acad.generateTranscript("2000CSB1000");
    });
    assertEquals("Invalid entry number", outputLastNthLine(0));

    Database.clearData();
  }
}
