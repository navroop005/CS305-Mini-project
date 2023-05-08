package org.cs305;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

import org.postgresql.util.PSQLException;

public abstract class Database {
  private static Connection connection;

  public static void connect() {
    try {
      Class.forName("org.postgresql.Driver");
      Properties properties = new Properties();
      properties.setProperty("user", "postgres");
      properties.setProperty("password", "12345");
      properties.setProperty("escapeSyntaxCallMode", "callIfNoReturn");

      connection = DriverManager.getConnection(
          "jdbc:postgresql://localhost:5432/cs305_mini_project",
          properties);
      Log.write("Connected to database");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void initialize() {
    connect();

    runSqlFile("src/main/sql/schema.pgsql");
    Log.write("Schema created");
    runSqlFile("src/main/sql/data.pgsql");
    Log.write("Added data");
  }

  public static Connection getConnection() {
    if (connection == null) {
      initialize();
    }
    return connection;
  }

  public static void closeConnection() {
    try {
      if (connection != null) {
        connection.close();
      }
      connection = null;
      Log.write("Database closed");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private static void runSqlFile(String filePath) {
    try {
      String content = new String(Files.readAllBytes(Paths.get(filePath)));
      String[] queries = content.split(";");
      Statement stmt = connection.createStatement();
      for (String query : queries) {
        stmt.addBatch(query);
      }
      stmt.executeBatch();
      stmt.close();
    } catch (IOException e) {
      System.err.println("Invalid input file: " + filePath);
      throw new RuntimeException(e);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static String getSqlConstraint(SQLException e) {
    PSQLException pException = (PSQLException) e;
    try {
      return pException.getServerErrorMessage().getConstraint();
    } catch (NullPointerException a) {
      return null;
    }
  }

  public static void clearData() {
    try {
      String query = "TRUNCATE users, course_catalogue, course_offering, instructors, student_enrollments, students CASCADE";
      Statement stmt = getConnection().createStatement();
      stmt.execute(query);
      connection.close();
      connection = null;
      Log.write("Cleared database");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
