package org.cs305.cli;

import org.cs305.common.AcademicCalender;
import org.cs305.common.StudentDetails;
import org.cs305.common.Utils;
import org.cs305.users.Student;

public class StudentCli extends Cli {
  final Student student;

  protected StudentCli(String userEmail, String userName) {
    student = new Student(userName, userEmail);
  }

  protected void run() {
    System.out.println("Logged in as student: " + student.userName);

    CliOptions options = new CliOptions();

    options.add("VC", "View Course catalogue",
        () -> CourseCli.viewCatalogue(this));
    options.add("VO", "View course Offerings",
        () -> CourseCli.showOfferings(this));
    options.add("VE", "View Enrolled courses",
        () -> {
          StudentDetails.printEnrollments(student.userEmail);
          waitInput();
        });
    options.add("EC", "Enroll Course",
        this::enrollCourse);
    options.add("DC", "Drop Course",
        this::dropCourse);
    options.add("CG", "View current CGPA",
        () -> System.out.println("CGPA: " +
            Utils.formatCG(StudentDetails.getCgpa_credits(student.userEmail, null, false))));

    options.add("CP", "Change Password",
        () -> changePassword(student));

    options.showOptions("Student: " + student.userName);
  }

  private void enrollCourse() {
    String session = AcademicCalender.getEnrollmentSession();
    if (session == null) {
      System.out.println("Enrollments not available!");
      return;
    }
    System.out.println("Enroll for session: " + session);

    String code = getStringUpper("Course Code");
    String dept = getStringUpper("Department");
    if (!Utils.getDepartments().containsKey(dept)) {
      System.out.println("Invalid department");
      return;
    }
    if (confirm()) {
      System.out.println(student.enrollCourse(code, dept, session));
    }
  }

  private void dropCourse() {
    String session = AcademicCalender.getDropSession();
    if (session == null) {
      System.out.println("Course drop not available!");
      return;
    }
    System.out.println("Drop course for session: " + session);

    String code = getStringUpper("Course Code");
    String dept = getStringUpper("Department");
    if (!Utils.getDepartments().containsKey(dept)) {
      System.out.println("Invalid department");
      return;
    }
    if (confirm()) {
      System.out.println(student.dropCourse(code, dept, session));
    }
  }
}
