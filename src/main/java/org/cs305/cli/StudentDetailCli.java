package org.cs305.cli;

import org.cs305.common.StudentDetails;
import org.cs305.common.Utils;

public abstract class StudentDetailCli extends Cli {
  protected static void viewGrade(Cli cli) {
    CliOptions options = cli.new CliOptions();

    options.add("EA", "view using Entry number for all course",
        () -> {
          String entryNum = cli.getStringUpper("Entry Number");
          StudentDetails.printEnrollments(Utils.entryNumToEmail(entryNum));
        });
    options.add("EC", "view using Entry number for a particular course",
        () -> {
          String entryNumber = cli.getStringUpper("Entry number");
          String code = cli.getStringUpper("Course Code");
          String dept = cli.getStringUpper("Course department");
          System.out.println();
          printGrade(entryNumber, code, dept);
        });
    options.add("CG", "view CGPA of student",
        () -> {
          String email = Utils.entryNumToEmail(cli.getStringUpper("Entry Number"));
          System.out.println("CGPA: " +
              Utils.formatCG(StudentDetails.getCgpa_credits(email, null, false)));
        });

    options.showOptions("View grades");
  }

  protected static void printGrade(String entryNumber, String code, String dept) {
    String res = StudentDetails.getGrade(Utils.entryNumToEmail(entryNumber), code, dept);
    if (res.length() > 2) {
      System.out.println(res);
    } else {
      System.out.println("Grade: " + res);
    }
  }
}
