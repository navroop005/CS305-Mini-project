package org.cs305.common;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.TreeMap;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.*;

public class UtilsTest {
  @Test
  void testEmailToEntryNum() {
    String entryNumber = Utils.emailToEntryNum("2020csb1101@iitrpr.ac.in");
    assertEquals(entryNumber, "2020CSB1101");
  }

  @Test
  void testEntryNumToEmail() {
    String entryNumber = Utils.entryNumToEmail("2020CSB1101");
    assertEquals(entryNumber, "2020csb1101@iitrpr.ac.in");
  }

  @Test
  void testGetDepartments_size() {
    TreeMap<String, String> actual = Utils.getDepartments();
    assertEquals(actual.size(), 13);
  }

  @ParameterizedTest
  @CsvSource({
      "CS, Computer Science and Engineering",
      "EE, Electrical Engineering"
  })
  void testGetDepartments_data(String code, String name) {
    TreeMap<String, String> depts = Utils.getDepartments();
    assertEquals(depts.get(code), name);
  }

  @ParameterizedTest
  @CsvSource({
      "A,10",
      "A-,9",
      "B,8",
      "B-,7",
      "C,6",
      "C-,5",
      "D,4",
      "E,2",
      "F,0",
  })
  void testGradeValue(String grade, int value) {
    assertEquals(Utils.gradeValue(grade), value);
  }

  @Test
  void testReadCSV() {
    ArrayList<String[]> expectedData = new ArrayList<>();
    expectedData.add(new String[] { "A1", "B 1", "C1" });
    expectedData.add(new String[] { "A2", "B 2", "C2" });
    expectedData.add(new String[] { "A3", "B 3", "C3" });

    ArrayList<String[]> actualData = Utils.readCSV("input/test.csv");
    assertEquals(expectedData.size(), actualData.size());

    for (int i = 0; i < expectedData.size(); i++) {
      assertArrayEquals(expectedData.get(i), actualData.get(i));
    }
  }

  @Test
  void testReadCSV_nofile() {
    assertEquals(null, Utils.readCSV("input/nofile.csv"));
  }

  @Test
  void testGetBatch() {
    assertEquals(Utils.getBatch("2020csb1101"), 2020);
    assertEquals(Utils.getBatch("20csb1101"), -1);
  }

  @Test
  void testGetBranch() {
    assertEquals("CS", Utils.getBranch("2020CSB1101"));
    assertEquals("CS", Utils.getBranch("2020csb1101"));
  }

  @ParameterizedTest
  @CsvSource({
      "2020-I,2020",
      "2021-II,2021",
      "2023-S,2023",
  })
  void testGetSessionSem(String input, int output) {
    assertEquals(Utils.getSessionYear(input), output);
  }

  @ParameterizedTest
  @CsvSource({
      "2020-I,I",
      "2021-II,II",
      "2023-S,S",
  })
  void testGetSessionYear(String input, String output) {
    assertEquals(Utils.getSessionSem(input), output);
  }

  @ParameterizedTest
  @CsvSource({
      "2020-I,2019-II",
      "2021-II,2021-I",
      "2023-S,2023-I",
  })
  void testPrevSession(String input, String output) {
    assertEquals(Utils.prevSession(input), output);
  }
}
