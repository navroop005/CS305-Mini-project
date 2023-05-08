package org.cs305;

import java.io.FileWriter;
import java.io.IOException;

public abstract class Log {
  static private FileWriter fileWriter;

  static void openFile() {
    try {
      fileWriter = new FileWriter("output/log.txt");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void write(String log) {
    if (fileWriter == null) {
      openFile();
    }
    try {
      fileWriter.write(log + '\n');
      fileWriter.flush();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void error(String log) {
    if (fileWriter == null) {
      openFile();
    }
    try {
      fileWriter.write("Error: " + log + '\n');
      fileWriter.flush();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  static void closeFile() {
    if (fileWriter != null) {
      try {
        fileWriter.close();
        fileWriter = null;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
