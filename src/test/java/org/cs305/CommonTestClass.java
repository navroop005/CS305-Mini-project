package org.cs305;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

import org.cs305.common.Utils;
import org.cs305.users.Instructor;
import org.cs305.users.Student;

public class CommonTestClass {
  private static String outFile = "output/test/test.txt";
  private static String logFile = "output/log.txt";

  public static boolean testInput(String input, Runnable method) {
    boolean exit = false;
    InputStream systemin = System.in;
    PrintStream systemout = System.out;
    try {
      System.setIn(new ByteArrayInputStream(input.getBytes()));
      System.setOut(new PrintStream(outFile));
      try {
        method.run();
        exit = true;
      } catch (NoSuchElementException e) {
        System.in.close();
        System.out.close();
      }
      System.setIn(systemin);
      System.setOut(systemout);
      return exit;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static String readFile(String filePath) {
    String content;
    try {
      content = new String(Files.readAllBytes(Paths.get(filePath)));
      return content;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String outputContent() {
    return readFile(outFile);
  }

  public static String logContent() {
    return readFile(logFile);
  }

  public static String outputLastNthLine(int n) {
    String output = readFile(outFile);
    String[] lines = output.split("\n");
    return lines[lines.length - 1 - n];
  }

  public static String logLastNthLine(int n) {
    String output = readFile(logFile);
    String[] lines = output.split("\n");
    return lines[lines.length - 1 - n];
  }

  public static String streamLastNthLine(InputStream stream, int n) throws IOException {
    String output = new String(stream.readNBytes(stream.available()));
    String[] lines = output.split("\n");
    return lines[lines.length - 1 - n];
  }

  public static void addData() {
    String input = """
        l
        acadoffice
        acadoffice
        as
        c
        input/students.csv
        c
        e
        ai
        c
        input/instructors.csv
        c
        e
        cc
        am
        input/course.csv
        c
        e
        e
        e

        """;

    testInput(input, () -> {
      Main.main(null);
    });
  }

  public static void studentEnrollCSV(String[] arguments) {
    String code = arguments[0];
    String dept = arguments[1];
    int inst_id = Integer.parseInt(arguments[2]);
    String type = arguments[3];
    String grade = arguments[4];
    String session = arguments[5];
    Student student = new Student("TEST", "2023csb1002@iitrpr.ac.in");
    Instructor inst = new Instructor("INSTRUCTOR " + inst_id, "instructor" + inst_id + "@iitrpr.ac.in");
    inst.registerCourse(code, dept, session, 0, 2023, "CS", type);
    student.enrollCourse(code, dept, session);
    inst.updateGrade("2023CSB1002", code, dept, session, grade);
  }

  public static void runFromCsv(String source, Consumer<String[]> method) {
    var csvContent = Utils.readCSV(source);

    for (String[] strings : csvContent) {
      method.accept(strings);
    }
  }

  public static void enrollTestStudent() {
    addData();
    runFromCsv("input/enroll_test.csv", CommonTestClass::studentEnrollCSV);
  }
}
