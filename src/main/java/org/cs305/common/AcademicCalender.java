package org.cs305.common;

import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

import org.cs305.Log;

public abstract class AcademicCalender {
  private static String enrollmentSession = "2023-II";
  private static String dropSession = "2023-II";
  private static String registerSession = "2023-II";
  private static String deregisterSession = "2023-II";
  private static String gradingSession = "2023-II";
  private static String filePath;

  public static void initialize(String path) {
    filePath = path;
    var data = Utils.readCSV(path);
    if (data == null) {
      writeCalendar();
    }
    for (String[] row : data) {
      if (checkSessionValid(row[1])||row[1].equals("null")) {
        if (row[1].equals("null")) {
          row[1]=null;
        }
        switch (row[0]) {
          case "enroll_session":
            enrollmentSession = row[1];
            break;
          case "drop_session":
            dropSession = row[1];
            break;
          case "register_session":
            registerSession = row[1];
            break;
          case "deregister_session":
            deregisterSession = row[1];
            break;
          case "grading_session":
            gradingSession = row[1];
            break;
        }
      }
    }
  }

  public static boolean checkSessionValid(String session) {
    return Pattern.matches("[0-9]{4}-(I|II|S)", session);
  }

  public static void writeCalendar() {
    try {
      FileWriter writer = new FileWriter(filePath);
      writer.write("enroll_session," + enrollmentSession);
      writer.write("\ndrop_session," + dropSession);
      writer.write("\nregister_session," + registerSession);
      writer.write("\nderegister_session," + deregisterSession);
      writer.write("\ngrading_session," + gradingSession);
      writer.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    Log.write("Academic calendar updated");
  }

  public static String getEnrollmentSession() {
    return enrollmentSession;
  }

  public static void setEnrollmentSession(String enrollmentSession) {
    AcademicCalender.enrollmentSession = enrollmentSession;
    writeCalendar();
  }

  public static String getDropSession() {
    return dropSession;
  }

  public static void setDropSession(String dropSession) {
    AcademicCalender.dropSession = dropSession;
    writeCalendar();
  }

  public static String getRegisterSession() {
    return registerSession;
  }

  public static void setRegisterSession(String registerSession) {
    AcademicCalender.registerSession = registerSession;
    writeCalendar();
  }

  public static String getDeregisterSession() {
    return deregisterSession;
  }

  public static void setDeregisterSession(String deregisterSession) {
    AcademicCalender.deregisterSession = deregisterSession;
    writeCalendar();
  }

  public static String getGradingSession() {
    return gradingSession;
  }

  public static void setGradingSession(String gradeUpdateSession) {
    AcademicCalender.gradingSession = gradeUpdateSession;
    writeCalendar();
  }
}
