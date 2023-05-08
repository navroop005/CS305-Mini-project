package org.cs305.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

import org.cs305.Database;

public abstract class Utils {
  private static TreeMap<String, String> departments;// = new String[] { "CS", "EE" };

  public static TreeMap<String, String> getDepartments() {
    if (departments == null) {
      try {
        Connection conn = Database.getConnection();
        String query = "SELECT * FROM department";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        departments = new TreeMap<>();
        while (rs.next()) {
          String code = rs.getString("code");
          String name = rs.getString("name");
          departments.put(code, name);
        }
      } catch (SQLException e) {
        
        throw new RuntimeException(e);
      }
    }
    return departments;
  }

  public static int gradeValue(String grade) {
    return switch (grade) {
      case "A" -> 10;
      case "A-" -> 9;
      case "B" -> 8;
      case "B-" -> 7;
      case "C" -> 6;
      case "C-" -> 5;
      case "D" -> 4;
      case "E" -> 2;
      default -> 0;
    };
  }

  public static String entryNumToEmail(String entryNumber) {
    return entryNumber.toLowerCase() + "@iitrpr.ac.in";
  }

  public static String emailToEntryNum(String email) {
    return email.replace("@iitrpr.ac.in", "").toUpperCase();
  }

  public static int getBatch(String entry_num) {
    try {
      return Integer.parseInt(entry_num.substring(0, 4));
    } catch (NumberFormatException e) {
      return -1;
    }
  }

  public static String getBranch(String entry_num) {
    return entry_num.substring(4, 6).toUpperCase();
  }

  public static String formatCG(float cg) {
    return String.format("%.2f", cg);
  }

  public static int getSessionYear(String session) {
    return Integer.parseInt(session.split("-")[0]);
  }

  public static String getSessionSem(String session) {
    return session.split("-")[1].strip();
  }

  public static String prevSession(String session) {
    int year = getSessionYear(session);
    String sem = getSessionSem(session);
    switch (sem) {
      case "I":
        return year - 1 + "-II";
      case "II":
        return year + "-I";
      default:
        return year + "-I";
    }
  }

  public static ArrayList<String[]> readCSV(String filePath) {

    Scanner sc;
    try {
      sc = new Scanner(new File(filePath));
    } catch (FileNotFoundException e) {
      return null;
    }

    ArrayList<String[]> data = new ArrayList<>();

    while (sc.hasNextLine()) {
      String line = sc.nextLine().strip();
      if (line.length() > 0) {
        String[] lineData = line.split("\\s*,\\s*");
        data.add(lineData);
      }
    }

    sc.close();
    return data;
  }
}
