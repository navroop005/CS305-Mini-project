package org.cs305.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.cs305.CommonTestClass;
import org.cs305.users.AcadOffice;
import org.cs305.users.Instructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CourseTest {

  @BeforeAll
  static void addCourse() {
    AcadOffice acad = new AcadOffice("test", "test@iitrpr.ac.in");
    acad.addInstructor("testinst@iitrpr.ac.in", "TEST", "test", "CS");
    Course.addCourse("CS000", "TEST COURSE", "CS", 2, null);
    Instructor inst = new Instructor("TEST", "testinst@iitrpr.ac.in");
    inst.registerCourse("CS000", "CS", "2000-I", 0, 0, "CS", "PC");
    Course.addCourse("CS001", "TEST COURSE", "CS", 2, new String[] { "CS000" });
    inst.registerCourse("CS000", "CS", "2000-I", 0, 0, "CS", "PC");
  }

  @Test
  void testAddCourse() {
    Course.deleteCourse("CS002", "CS");
    String response;
    response = Course.addCourse("CS002", "TEST COURSE", "CS", 2, new String[] { "CS000" });
    assertEquals(response, "Course added to catalogue");

    response = Course.addCourse("CS002", "TEST COURSE", "CS", 2, null);
    assertEquals(response, "Course, department already exists");

    response = Course.addCourse("CS003", "TEST COURSE", "TC", 2, null);
    assertEquals(response, "Invalid department");

    Course.deleteCourse("CS001", "CS");
  }

  @Test
  void testDeleteCourse() {
    Course.addCourse("CS000", "TEST COURSE", "CS", 2, null);
    assertTrue(Course.deleteCourse("CS000", "CS"));
    assertFalse(Course.deleteCourse("CS000", "CS"));
  }

  @Test
  void testViewCatalogue() {
    assertTrue(CommonTestClass.testInput("", () -> {
      Course.viewCatalogue(null);
    }));
    assertTrue(CommonTestClass.testInput("", () -> {
      Course.viewCatalogue("CS");
    }));
  }

  @Test
  void testViewOfferings() {
    assertTrue(CommonTestClass.testInput("", () -> {
      Course.viewOfferings(null);
    }));
    assertTrue(CommonTestClass.testInput("", () -> {
      Course.viewOfferings("CS");
    }));
  }

  @AfterAll
  static void clean() {
    AcadOffice acad = new AcadOffice("test", "test@iitrpr.ac.in");
    acad.addInstructor("testinst@iitrpr.ac.in", "TEST", "test", "CS");
    acad.deleteUser("testinst@iitrpr.ac.in");
    Course.deleteCourse("CS000", "CS");
  }
}
