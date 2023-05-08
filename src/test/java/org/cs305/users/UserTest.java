package org.cs305.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.cs305.Main;
import org.cs305.CommonTestClass;
import org.junit.jupiter.api.Test;

public class UserTest extends CommonTestClass {
  @Test
  void testChangePassword() {
    String input = """
        l
        acadoffice
        acadoffice
        cp
        wrongpassw
        newpassw
        e
        e
        """;
    assertTrue(testInput(input, () -> {
      Main.main(null);
    }));
    assertEquals(logLastNthLine(3), "Invalid password change request by: acadoffice@iitrpr.ac.in");

    input = """
        l
        acadoffice
        acadoffice
        cp
        acadoffice
        newpassw
        e
        e
        """;
    assertTrue(testInput(input, () -> {
      Main.main(null);
    }));

    input = """
        l
        acadoffice
        newpassw
        cp
        newpassw
        acadoffice
        e
        e
        """;
    assertTrue(testInput(input, () -> {
      Main.main(null);
    }));

    assertEquals(logLastNthLine(3), "Password changed by: acadoffice@iitrpr.ac.in");
  }

  @Test
  void testFindUser() {
    String[] details = User.findUser("admin@iitrpr.ac.in");
    assertEquals(details[0], "ADMIN");
    assertEquals(details[1], "admin");

    details = User.findUser("acadoffice@iitrpr.ac.in");
    assertEquals(details[0], "ACADEMICS OFFICE");
    assertEquals(details[1], "acad_office");

    details = User.findUser("abc@iitrpr.ac.in");
    assertNull(details);
  }

  @Test
  void testVerify_correct() {
    assertNotNull(User.verify("admin@iitrpr.ac.in", "admin"));
  }

  @Test
  void testVerify_incorrect() {
    assertNull(User.verify("admin@iitrpr.ac.in", "abc"));
    assertNull(User.verify("abc@iitrpr.ac.in", "admin"));
  }
}
