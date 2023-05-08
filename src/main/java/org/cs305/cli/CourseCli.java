package org.cs305.cli;

import org.cs305.Log;
import org.cs305.common.Course;
import org.cs305.common.Utils;

public abstract class CourseCli extends Cli {

  protected static void viewCatalogue(Cli cli) {
    CliOptions options = cli.new CliOptions();
    options.add("ALL", "Show all courses ",
        () -> {
          Course.viewCatalogue(null);
          cli.waitInput();
        });

    Utils.getDepartments().forEach((code, name) -> {
      options.add(code, "Show " + name + " courses",
          () -> {
            Course.viewCatalogue(code);
            cli.waitInput();
          });
    });

    options.showOptions("View Course catalogue");
  }

  protected static void viewEditCatalogue(Cli cli) {
    CliOptions options = cli.new CliOptions();

    options.add("VC", "View Course catalogue",
        () -> viewCatalogue(cli));
    options.add("AC", "Add Course",
        () -> {
          String code = cli.getStringUpper("Course code");
          String name = cli.getStringUpper("Course name");
          String dept = cli.getStringUpper("Department");
          if (!Utils.getDepartments().containsKey(dept)) {
            System.out.println("Invalid department");
            return;
          }
          float credits = cli.getFloat("Credits", 0, 5);
          String in = cli.getStringUpper("Prerequisites (comma separated, leave empty if none)");
          String[] prereqs = null;
          if (in.length() > 0) {
            prereqs = in.replace(" ", "").split(",");
          }
          if (cli.confirm()) {
            System.out.println(Course.addCourse(code, name, dept, credits, prereqs));
          }
        });
    options.add("AM", "Add Multiple courses from csv",
        () -> {
          System.out.println("Expected CSV format: code,name,department,credits,prerequisites(space separated)");
          String filePath = cli.getString("File path");
          var data = Utils.readCSV(filePath);
          if (data == null) {
            System.out.println("Invalid file path");
            Log.error("Add from csv: Invalid file path");
            return;
          }
          if (cli.confirm()) {
            int added = 0, exist = 0;
            for (String[] inst : data) {
              String[] prereqs;
              try {
                prereqs = inst[4].split(" ");
              } catch (ArrayIndexOutOfBoundsException e) {
                prereqs = null;
              }
              String response;
              try {
                response = Course.addCourse(inst[0], inst[1], inst[2], Float.parseFloat(inst[3]), prereqs);
              } catch (Exception e) {
                response = "Invalid Data";
              }
              if (response.equals("Course added to catalogue")) {
                added++;
              } else if (response.equals("Course, department already exists")) {
                exist++;
              } else {
                System.out.println(inst[0] + " error: " + response);
              }
            }
            System.out.println("Added: " + added + ", Already exist: " + exist);
          }
        });
    options.add("DC", "Delete Course",
        () -> {
          String code = cli.getStringUpper("Course code");
          String dept = cli.getStringUpper("Department");
          if (!Utils.getDepartments().containsKey(dept)) {
            System.out.println("Invalid department");
            return;
          }
          if (cli.confirm()) {
            if (Course.deleteCourse(code, dept)) {
              System.out.println("Course deleted");
            } else {
              System.out.println("No course deleted");
            }
          }
        });

    options.showOptions("View/Edit Course catalogue");
  }

  protected static void showOfferings(Cli cli) {
    CliOptions options = cli.new CliOptions();
    options.add("ALL", "Show all course offerings ",
        () -> {
          Course.viewOfferings(null);
          cli.waitInput();
        });

    Utils.getDepartments().forEach((code, name) -> {
      options.add(code, "Show " + name + " offerings",
          () -> {
            Course.viewOfferings(code);
            cli.waitInput();
          });
    });

    options.showOptions("View Course Offerings");
  }
}
