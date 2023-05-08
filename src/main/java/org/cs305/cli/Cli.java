package org.cs305.cli;

import java.io.Console;
import java.util.LinkedHashMap;
import java.util.Scanner;

import org.cs305.Log;
import org.cs305.common.AcademicCalender;
import org.cs305.common.Utils;
import org.cs305.users.User;

public class Cli {
  public static Scanner input;

  static class DescribedRunnable {
    public String description;
    public Runnable runnable;

    DescribedRunnable(String description, Runnable runnable) {
      this.description = description;
      this.runnable = runnable;
    }
  }

   class CliOptions extends LinkedHashMap<String, DescribedRunnable> {
    public void add(String input, String description, Runnable method) {
      this.put(input, new DescribedRunnable(description, method));
    }

    public void showOptions(String title) {
      while (true) {
        System.out.println("\n**** " + title + " ****");
  
        this.forEach((key, value) -> System.out.println(key + "\t: " + value.description));
        System.out.println("E" + "\t: " + "Exit");
  
        String in = getStringUpper("Input");
        System.out.println();
        if (in.equals("E")) {
          System.out.println("Exit");
          break;
        } else {
          Runnable run;
          try {
            run = this.get(in).runnable;
          } catch (Exception e) {
            System.out.println("Invalid Option");
            continue;
          }
          run.run();
        }
      }
    }
  }

  protected String getString(String text) {
    System.out.print(text + ": ");
    return input.nextLine().strip();
  }

  protected String getStringUpper(String text) {
    return getString(text).toUpperCase();
  }

  protected String getStringLower(String text) {
    return getString(text).toLowerCase();
  }

  protected int getInt(String text, int from, int to) {
    while (true) {
      try {
        String in = getString(text + " (" + from + " to " + to + ")");
        int out = Integer.parseInt(in);
        if (out <= to && out >= from) {
          return out;
        }
      } catch (NumberFormatException e) {
      }
      clearLine();
      System.out.print("Invalid input! Retry: ");
    }
  }

  protected float getFloat(String text, int from, int to) {
    while (true) {
      try {
        String in = getString(text + " (" + from + " to " + to + ")");
        float out = Float.parseFloat(in);
        if (out <= to && out >= from) {
          return out;
        }
      } catch (NumberFormatException e) {
      }
      clearLine();
      System.out.print("Invalid input! Retry: ");
    }
  }

  protected String getPassword() {
    Console console = System.console();
    if (console == null) {
      return getString("Password");
    }
    System.out.print("Password: ");
    return new String(console.readPassword());
  }

  protected boolean confirm() {
    String in = getStringUpper("C to confirm, E to exit");
    if (in.equals("C")) {
      return true;
    } else {
      System.out.println("Exit");
      return false;
    }
  }

  private void clearLine() {
    System.out.print("\033[1A\033[2K");
  }

  protected void waitInput() {
    System.out.print("\nPress enter to continue");
    input.nextLine();
    clearLine();
  }

  public void start() {
    CliOptions options = new CliOptions();

    options.add("L", "Login", this::login);
    options.showOptions("Login");

    input.close();
  }

  private void login() {
    String email = getString("Email");
    if (email.indexOf("@iitrpr.ac.in") == -1) {
      email = Utils.entryNumToEmail(email);
    }

    String password = getPassword();

    String[] details = User.verify(email, password);
    if (details == null) {
      System.out.println("Invalid email/password");
    } else {
      Log.write("Logged in: " + email);
      String name = details[0];
      String role = details[1];

      switch (role) {
        case "admin" -> {
          AdminCli admin = new AdminCli(email, name);
          admin.run();
        }
        case "acad_office" -> {
          AcadOfficeCli acadOfficeCli = new AcadOfficeCli(email, name);
          acadOfficeCli.run();
        }
        case "instructor" -> {
          InstructorCli instructorCli = new InstructorCli(email, name);
          instructorCli.run();
        }
        case "student" -> {
          StudentCli studentCli = new StudentCli(email, name);
          studentCli.run();
        }
      }

      System.out.println("Logged out");
      Log.write("Logged out: " + email);
    }
  }

  protected void changePassword(User user) {
    System.out.print("Old ");
    String oldPassword = getPassword();
    System.out.print("New ");
    String newPassword = getPassword();
    boolean changed = user.changePassword(oldPassword, newPassword);
    if (changed) {
      System.out.println("Password changed");
    } else {
      System.out.println("Incorrect password");
    }
  }

  protected void findUser() {
    String email = getString("Email");
    String[] details = User.findUser(email);
    if (details == null) {
      System.out.println("User does not exist");
    } else {
      System.out.println("Name: " + details[0]);
      System.out.println("Role: " + details[1]);
    }
  }

  protected static void printAcademicCalendar() {
    System.out.println("Course enrollment session: " + AcademicCalender.getEnrollmentSession());
    System.out.println("Course drop session: " + AcademicCalender.getDropSession());
    System.out.println("Course registration session session: " + AcademicCalender.getRegisterSession());
    System.out.println("Course deregistration session: " + AcademicCalender.getDeregisterSession());
    System.out.println("Grade update session: " + AcademicCalender.getGradingSession());
  }
}
