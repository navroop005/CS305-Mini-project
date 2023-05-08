package org.cs305.cli;

import org.cs305.Log;
import org.cs305.common.AcademicCalender;
import org.cs305.common.Utils;
import org.cs305.users.Instructor;

public class InstructorCli extends Cli {
  final Instructor instructor;

  protected InstructorCli(String userEmail, String userName) {
    instructor = new Instructor(userName, userEmail);
  }

  protected void run() {
    System.out.println("Logged in as instructor: " + instructor.userName);

    CliOptions options = new CliOptions();

    options.add("C", "Courses (register/deregister)", this::courses);
    options.add("G", "View/Update student grades", this::studentGrades);

    options.add("CP", "Change Password",
        () -> changePassword(instructor));
    options.showOptions("Instructor: " + instructor.userName);
  }

  private void studentGrades() {
    CliOptions options = new CliOptions();
    options.add("VG", "View Grade",
        () -> StudentDetailCli.viewGrade(this));
    options.add("US", "Update grade for a Student",
        this::updateGrade);
    options.add("UC", "Upload grades from Csv",
        this::uploadGrades);
    options.showOptions("View/update grades");
  }

  private void courses() {
    CliOptions options = new CliOptions();

    options.add("VC", "View Course catalogue",
        () -> CourseCli.viewCatalogue(this));
    options.add("VR", "View Registered courses",
        () -> {
          instructor.viewRegistered();
          waitInput();
        });
    options.add("RC", "Register Course",
        this::registerCourse);
    options.add("DC", "Deregister Course",
        this::deregisterCourse);

    options.showOptions("Register/Deregister courses");
  }

  private void registerCourse() {
    String session = AcademicCalender.getRegisterSession();
    if (session == null) {
      System.out.println("Course Registrations not available!");
      return;
    }
    System.out.println("Register for session: " + session);
    String code = getStringUpper("Course code");
    String dept = getStringUpper("Offering department");
    float min_cg = getFloat("Minimum CGPA", 0, 10);
    int batch = getInt("Batch", 2010, 2030);
    String branch = getStringUpper("Branch");
    if (!Utils.getDepartments().containsKey(branch)) {
      System.out.println("Invalid branch");
      Log.write("Register course: Invalid branch");
      return;
    }
    String type = getStringUpper("Type (PC (Program core), PE (Program elective), OE (Other elective), CP (Capstone))");
    if (confirm()) {
      System.out.println(instructor.registerCourse(code, dept, session, min_cg, batch, branch, type));
    }
  }

  private void deregisterCourse() {
    String session = AcademicCalender.getDeregisterSession();
    if (session == null) {
      System.out.println("Course deregistrations not available!");
      return;
    }
    System.out.println("Deregister for session: " + session);
    String code = getStringUpper("Course code");
    String dept = getStringUpper("Offering department");
    Integer batch = getInt("Batch", 2010, 2030);
    if (batch == 0) {
      batch = null;
    }
    String branch = getStringUpper("Branch (empty for all)");
    if (branch.equals("")) {
      branch = null;
    }
    System.out.println("Offerings to be deleted: ");
    int count = instructor.findRegistered(code, session, batch, branch);
    if (count == 0) {
      System.out.println("Nothing to be delete, exit!");
    } else if (confirm()) {
      int deleted = instructor.deregisterCourse(code, dept,session, batch, branch);
      System.out.println(deleted + " offerings deleted!");
    }
  }

  private void uploadGrades() {
    String session = AcademicCalender.getGradingSession();
    if (session == null) {
      System.out.println("Grade update not available!");
      return;
    }
    System.out.println("Grade update for session: " + session);
    String code = getStringUpper("Course code");
    String dept = getStringUpper("Course department");

    System.out.println("Expected CSV format: entry_number,grade");
    String filePath = getString("File path");
    var data = Utils.readCSV(filePath);
    if (data == null) {
      System.out.println("Invalid file path");
      return;
    }

    System.out.println("-".repeat(20));
    for (String[] inst : data) {
      System.out.printf("| %-11s | %-2s |\n", inst[0].toUpperCase(), inst[1].toUpperCase());
    }
    System.out.println("-".repeat(20) + '\n');
    System.out.println("To add data above:");
    if (confirm()) {
      int added = 0, exist = 0;
      for (String[] inst : data) {
        String response;
        try {
          response = instructor.updateGrade(inst[0].toUpperCase(),
              code, dept, session, inst[1].toUpperCase());
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

  private void updateGrade() {
    String session = AcademicCalender.getGradingSession();
    if (session == null) {
      System.out.println("Grade update not available!");
      return;
    }
    System.out.println("Grade update for session: " + session);

    String code = getStringUpper("Course code");
    String dept = getStringUpper("Course department");
    String entryNumber = getStringUpper("Entry Number");
    System.out.print("Current status: ");
    StudentDetailCli.printGrade(entryNumber, code, dept);
    String grade = getStringUpper("New grade");
    if (confirm()) {
      System.out.println(instructor.updateGrade(entryNumber, code, dept, session, grade));
    }
  }
}