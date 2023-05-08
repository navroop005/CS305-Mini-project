package org.cs305.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class AcademicCalenderTest {
  @Test
  void testCheckSessionValid() {
    assertTrue(AcademicCalender.checkSessionValid("2023-I"));
    assertTrue(AcademicCalender.checkSessionValid("2020-II"));
    assertTrue(AcademicCalender.checkSessionValid("2027-S"));
    assertFalse(AcademicCalender.checkSessionValid("202-S"));
    assertFalse(AcademicCalender.checkSessionValid("2020-8"));
    assertFalse(AcademicCalender.checkSessionValid("2020-U"));
  }

  @Test
  void testDeregisterSession() {
    AcademicCalender.setDeregisterSession("2023-II");
    assertEquals("2023-II", AcademicCalender.getDeregisterSession());
  }

  @Test
  void testDropSession() {
    AcademicCalender.setDropSession("2023-II");
    assertEquals("2023-II", AcademicCalender.getDropSession());
  }

  @Test
  void testEnrollmentSession() {
    AcademicCalender.setEnrollmentSession("2023-II");
    assertEquals("2023-II", AcademicCalender.getEnrollmentSession());
  }

  @Test
  void testGradingSession() {
    AcademicCalender.setGradingSession("2023-II");
    assertEquals("2023-II", AcademicCalender.getGradingSession());
  }

  @Test
  void testRegisterSession() {
    AcademicCalender.setRegisterSession("2023-II");
    assertEquals("2023-II", AcademicCalender.getRegisterSession());
  }

  @Test
  void testInitialize() {
    assertThrows(RuntimeException.class, () -> {
      AcademicCalender.initialize("invalid/acad_calendar.csv");
    });

    AcademicCalender.initialize("acad_calendar.csv");
  }
}
