package org.cs305;

import java.util.Scanner;

import org.cs305.cli.Cli;
import org.cs305.common.AcademicCalender;

public class Main {
  public static void main(String[] args) {
    Log.openFile();
    Database.initialize();
    AcademicCalender.initialize("acad_calendar.csv");

    Scanner scanner = new Scanner(System.in);
    Cli.input = scanner;

    Cli cli = new Cli();
    cli.start();

    scanner.close();
    Database.closeConnection();
    Log.write("Exit");
    Log.closeFile();
  }
}