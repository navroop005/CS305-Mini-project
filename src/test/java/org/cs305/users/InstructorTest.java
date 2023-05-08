package org.cs305.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.cs305.CommonTestClass;
import org.cs305.common.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InstructorTest {

  Instructor inst;

  @BeforeEach
  void addTestInstructor() {
    CommonTestClass.addData();
    Course.addCourse("CS000", "TEST", "CS", 0, null);
    CommonTestClass.addData();
    inst = new Instructor("TEST", "instructor0@iitrpr.ac.in");
  }

  @Test
  void testDeregisterCourse() {
  inst.registerCourse("CS000","CS", "2023-II", 0, 2023, "CS", "PC");
    inst.registerCourse("CS000","CS", "2023-II", 0, 2024, "CS", "PC");
    inst.registerCourse("CS000","CS", "2023-II", 0, 2025, "CS", "PC");
    inst.registerCourse("CS000","CS", "2023-II", 0, 2023, "EE", "PC");

    assertEquals(inst.deregisterCourse("CS000","CS", "2023-II", 2025, "CS"), 1);

    inst.registerCourse("CS000","CS", "2023-II", 0, 2023, "CS", "PC");
    inst.registerCourse("CS000","CS", "2023-II", 0, 2024, "CS", "PC");
    inst.registerCourse("CS000","CS", "2023-II", 0, 2025, "CS", "PC");
    inst.registerCourse("CS000","CS", "2023-II", 0, 2023, "EE", "PC");
    assertEquals(inst.deregisterCourse("CS000","CS", "2023-II", 2023, null), 2);

    inst.registerCourse("CS000","CS", "2023-II", 0, 2023, "CS", "PC");
    inst.registerCourse("CS000","CS", "2023-II", 0, 2024, "CS", "PC");
    inst.registerCourse("CS000","CS", "2023-II", 0, 2025, "CS", "PC");
    inst.registerCourse("CS000","CS", "2023-II", 0, 2023, "EE", "PC");
    assertEquals(inst.deregisterCourse("CS000","CS", "2023-II", null, "CS"), 3);

    inst.registerCourse("CS000","CS", "2023-II", 0, 2023, "CS", "PC");
    inst.registerCourse("CS000","CS", "2023-II", 0, 2024, "CS", "PC");
    inst.registerCourse("CS000","CS", "2023-II", 0, 2025, "CS", "PC");
    inst.registerCourse("CS000","CS", "2023-II", 0, 2023, "EE", "PC");
    assertEquals(inst.deregisterCourse("CS000","CS", "2023-II", null, null), 4);
  }

  @Test
  void testFindRegistered() {
    inst.registerCourse("CS000","CS", "2023-II", 0, 2023, "CS", "PC");
    inst.registerCourse("CS000","CS", "2023-II", 0, 2024, "CS", "PC");
    inst.registerCourse("CS000","CS", "2023-II", 0, 2025, "CS", "PC");
    inst.registerCourse("CS000","CS", "2023-II", 0, 2023, "EE", "PC");
    assertEquals(inst.findRegistered("CS000", "2023-II", null, null), 4);
    assertEquals(inst.findRegistered("CS000", "2023-II", 2023, "CS"), 1);
    assertEquals(inst.findRegistered("CS000", "2023-II", 2023, null), 2);
    assertEquals(inst.findRegistered("CS000", "2023-II", null, "CS"), 3);

    inst.deregisterCourse("CS000","CS", "2023-II", null, null);
  }

  @Test
  void testIsOffering() {
    inst.registerCourse("CS000","CS", "2023-II", 0, 2023, "CS", "PC");
    assertTrue(inst.isOffering("CS000", "CS", "2023-II", 2023, "CS"));
    assertFalse(inst.isOffering("CS102", "CS", "2023-II", 2023, "CS"));
    assertFalse(inst.isOffering("CS000", "EE", "2023-II", 2023, "CS"));
    inst.deregisterCourse("CS000","CS", "2023-II", 2023, "CS");
  }

  @Test
  void testRegisterCourse() {
   inst.deregisterCourse("CS000","CS", "2023-II", 2023, "CS");
    assertEquals("Added course to offerings",
        inst.registerCourse("CS000","CS", "2023-II", 0, 2023, "CS", "PC"));
    assertEquals("Course, department, session already exists",
        inst.registerCourse("CS000","CS", "2023-II", 0, 2023, "CS", "PC"));

    assertEquals("Invalid course (Not in catalogue)",
        inst.registerCourse("CS607","CS", "2023-II", 0, 2023, "CS", "PC"));
        inst.deregisterCourse("CS000","CS", "2023-II", 2023, "CS");
  }

  @Test
  void testUpdateGrade() {
    inst.registerCourse("CS000","CS", "2023-II", 0, 2023, "CS", "PC");
    Student st = new Student("Test", "2023csb1001@iitrpr.ac.in");
    st.enrollCourse("CS000", "CS", "2023-II");

    assertEquals(inst.updateGrade("2023CSB1001", "CS000", "CS", "2023-II", "F"),
        "Updated grade");
    assertEquals(inst.updateGrade("2023CSB0000", "CS000", "CS", "2023-II", "F"),
        "Student not enrolled");
    assertEquals(inst.updateGrade("2023CSB0000", "GE101", "CS", "2023-II", "F"),
        "You have not offered this course!");

    inst.deregisterCourse("CS000","CS", "2023-II", 2023, "CS");
  }

  @Test
  void testViewRegistered() {
    inst.viewRegistered();
  }
}
