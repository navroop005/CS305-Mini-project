package org.cs305.cli;

import org.cs305.Log;
import org.cs305.common.AcademicCalender;
import org.cs305.common.StudentDetails;
import org.cs305.common.Utils;
import org.cs305.users.AcadOffice;

public class AcadOfficeCli extends Cli {
  final AcadOffice acad;

  protected AcadOfficeCli(String userEmail, String userName) {
    acad = new AcadOffice(userName, userEmail);
  }

  protected void run() {
    System.out.println("Logged in as academic office!");

    CliOptions options = new CliOptions();
    options.add("CC", "view/edit Course catalogue",
        () -> CourseCli.viewEditCatalogue(this));
    options.add("AC", "view/edit Academic Calendar",
        this::editCalender);
    options.add("AS", "Add Students",
        this::addStudent);
    options.add("AI", "Add Instructors",
        this::addInstructor);
    options.add("SG", "view Student Grade",
        () -> StudentDetailCli.viewGrade(this));
    options.add("ST", "generate Student Transcript",
        () -> {
          String entryNumber = getStringUpper("Entry number");
          acad.generateTranscript(entryNumber);
        });
    options.add("GC", "Student graduation check",
        () -> {
          String entryNum = getStringUpper("Entry Number");
          StudentDetails.printGraduationCheck(Utils.entryNumToEmail(entryNum));
          waitInput();
        });
    options.add("CP", "Change Password",
        () -> changePassword(acad));

    options.showOptions("Academic Office");
  }

  private void editCalender() {
    printAcademicCalendar();
    CliOptions options = new CliOptions();
    options.add("CE", "edit Course Enrollment session",
        () -> {
          String session = getStringUpper("New session (empty to end)");
          if (session.equals("")) {
            session = null;
          } else if (!AcademicCalender.checkSessionValid(session)) {
            System.out.println("Invalid session");
            return;
          }
          AcademicCalender.setEnrollmentSession(session);
          printAcademicCalendar();
        });
    options.add("CD", "edit Course Drop session",
        () -> {
          String session = getStringUpper("New session (empty to end)");
          if (session.equals("")) {
            session = null;
          } else if (!AcademicCalender.checkSessionValid(session)) {
            System.out.println("Invalid session");
            return;
          }
          AcademicCalender.setDropSession(session);
          printAcademicCalendar();
        });
    options.add("RS", "edit course Registration Session",
        () -> {
          String session = getStringUpper("New session (empty to end)");
          if (session.equals("")) {
            session = null;
          } else if (!AcademicCalender.checkSessionValid(session)) {
            System.out.println("Invalid session");
            return;
          }
          AcademicCalender.setRegisterSession(session);
          printAcademicCalendar();
        });
    options.add("DS", "edit course Deregistration Session",
        () -> {
          String session = getStringUpper("New session (empty to end)");
          if (session.equals("")) {
            session = null;
          } else if (!AcademicCalender.checkSessionValid(session)) {
            System.out.println("Invalid session");
            return;
          }
          AcademicCalender.setDeregisterSession(session);
          printAcademicCalendar();
        });
    options.add("GU", "edit Grade Update session",
        () -> {
          String session = getStringUpper("New session (empty to end)");
          if (session.equals("")) {
            session = null;
          } else if (!AcademicCalender.checkSessionValid(session)) {
            System.out.println("Invalid session");
            return;
          }
          AcademicCalender.setGradingSession(session);
          printAcademicCalendar();
        });

    options.showOptions("Edit academic calendar");
  }

  private void addInstructor() {
    CliOptions options = new CliOptions();
    options.add("S", "Single instructor",
        () -> {
          String name = getStringUpper("Name");
          String email = getString("Email");
          String dept = getStringUpper("Department");
          if (!Utils.getDepartments().containsKey(dept)) {
            System.out.println("Invalid department");
            Log.error("Add Instructor: Invalid department");
            return;
          }
          String password = getPassword();
          if (confirm()) {
            System.out.println(acad.addInstructor(email, name, password, dept));
          }
        });
    options.add("C", "add from CSV file",
        this::addInstructorCSV);
    options.showOptions("Add Instructors");
  }

  private void addInstructorCSV() {
    System.out.println("Expected CSV format: name,email,department,password");
    String filePath = getString("File path");
    var data = Utils.readCSV(filePath);
    if (data == null) {
      System.out.println("Invalid file path");
      Log.error("Add from csv: Invalid file path");
      return;
    }

    System.out.println("-".repeat(68));
    for (String[] inst : data) {
      System.out.printf("%-30s| %-30s | %-3s\n", inst[0], inst[1].toUpperCase(), inst[2].toUpperCase());
    }
    System.out.println("-".repeat(68) + '\n');

    System.out.println("To add data above:");
    if (confirm()) {
      int added = 0, exist = 0;
      for (String[] inst : data) {
        String response = acad.addInstructor(inst[0], inst[1].toUpperCase(), inst[3], inst[2].toUpperCase());
        if (response.equals("User added")) {
          added++;
        } else if (response.equals("User already exist")) {
          exist++;
        } else {
          System.out.println(inst[0] + " error: " + response);
        }
      }
      System.out.println("Added: " + added + ", Already exist: " + exist);
    }
  }

  private void addStudent() {
    CliOptions options = new CliOptions();
    options.add("S", "Single student",
        () -> {
          String name = getStringUpper("Name");
          String entryNumber = getStringUpper("Entry Number");
          String password = getPassword();
          if (confirm()) {
            System.out.println(acad.addStudent(name, entryNumber, password));
          }
        });
    options.add("C", "add from CSV file",
        this::addStudentCSV);
    options.showOptions("Add Student");
  }

  private void addStudentCSV() {
    System.out.println("Expected CSV format: name,entrynumber,branch,batch,password");
    String filePath = getString("File path");
    var data = Utils.readCSV(filePath);
    if (data == null) {
      System.out.println("Invalid file path");
      Log.error("Add from csv: Invalid file path");
      return;
    }

    System.out.println("-".repeat(60));
    for (String[] stud : data) {
      System.out.printf("| %-30s| %-10s | %-3s| %s |\n", stud[0].toUpperCase(), stud[1].toUpperCase(),
          stud[2].toUpperCase(), stud[3]);
    }
    System.out.println("-".repeat(60) + '\n');

    System.out.println("To add data above:");
    if (confirm()) {
      int added = 0, exist = 0;
      for (String[] inst : data) {
        String response;
        try {
          response = acad.addStudent(inst[0], inst[1], inst[4]);
        } catch (Exception e) {
          response = "Invalid data";
        }
        if (response.equals("User added")) {
          added++;
        } else if (response.equals("User already exist")) {
          exist++;
        } else {
          System.out.println(inst[0] + " error: " + response);
        }
      }
      System.out.println("Added: " + added + ", Already exist: " + exist);
    }
  }
}
