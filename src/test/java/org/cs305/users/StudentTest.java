package org.cs305.users;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.cs305.CommonTestClass;
import org.cs305.Database;
import org.junit.jupiter.api.Test;

public class StudentTest extends CommonTestClass {

  @Test
  void testDropCourse() {
    addData();
    Student student = new Student("TEST", "2023csb1001@iitrpr.ac.in");
    Instructor inst = new Instructor("INSTRUCTOR 0", "instructor0@iitrpr.ac.in");
    inst.registerCourse("CS000", "CS", "2023-II", 0, 2023, "CS", "PC");
    student.enrollCourse("CS000", "CS", "2023-II");
    assertEquals("Dropped enrollment!", student.dropCourse("CS000", "CS", "2023-II"));
    assertEquals("No course dropped", student.dropCourse("CS000", "CS", "2023-II"));
  }

  @Test
  void testEnrolls() {
    Database.clearData();
    addData();
    enrollTestStudent();
    Database.clearData();
  }
}
