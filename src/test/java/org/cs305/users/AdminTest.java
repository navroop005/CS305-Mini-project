package org.cs305.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class AdminTest {

  Admin admin = new Admin("ADMIN", "admin@iitrpr.ac.in");

  @Test
  void testAddInstructor() {
    admin.deleteUser("instructor0@iitrpr.ac.in");
    assertEquals("User added", admin.addInstructor("instructor0@iitrpr.ac.in", "INSTRUCTOR 0", "iit", "CS"));

    assertEquals("User already exist", admin.addInstructor("instructor0@iitrpr.ac.in", "INSTRUCTOR 0", "iit", "CS"));

    admin.deleteUser("instructor0@iitrpr.ac.in");
    assertEquals("Invalid department", admin.addInstructor("instructor0@iitrpr.ac.in", "INSTRUCTOR 0", "iit", "CD"));

    admin.deleteUser("instructor0@iitrpr.ac.in");
  }

  @Test
  void testAddStudent() {
    admin.deleteUser("2020csb1000@iitrpr.ac.in");

    assertEquals("User added", admin.addStudent("2020csb1000@iitrpr.ac.in", "TEST", "iit", "CS", "2020CSB1000", 2020));

    assertEquals("User already exist",
        admin.addStudent("2020csb1000@iitrpr.ac.in", "TEST", "iit", "CS", "2020CSB1000", 2020));

    admin.deleteUser("2020csb1000@iitrpr.ac.in");
    assertEquals("Invalid department",
        admin.addStudent("2020csb1000@iitrpr.ac.in", "TEST", "iit", "IV", "2020CSB1000", 2020));
  }

  @Test
  void testChangeUserPassword() {
    admin.addUser("test@iitrpr.ac.in", "TEST", "pass", "admin");
    assertTrue(admin.changeUserPassword("test@iitrpr.ac.in", "iit"));
    admin.deleteUser("test@iitrpr.ac.in");

    assertFalse(admin.changeUserPassword("test@iitrpr.ac.in", "iit"));
  }

  @Test
  void testDeleteUser() {
    admin.addUser("test@iitrpr.ac.in", "TEST", "pass", "admin");

    assertTrue(admin.deleteUser("test@iitrpr.ac.in"));
    assertFalse(admin.deleteUser("test@iitrpr.ac.in"));
  }
}
