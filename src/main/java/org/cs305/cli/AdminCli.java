package org.cs305.cli;

import org.cs305.Database;
import org.cs305.Log;
import org.cs305.common.Utils;
import org.cs305.users.Admin;

public class AdminCli extends Cli {
  final Admin admin;

  protected AdminCli(String userEmail, String userName) {
    admin = new Admin(userName, userEmail);
  }

  protected void run() {
    System.out.println("Logged in as admin!");
    CliOptions options = new CliOptions();

    options.add("AU", "Add User",
        this::addUser);
    options.add("FU", "Find User",
        this::findUser);
    options.add("DU", "Delete User",
        this::deleteUser);
    options.add("EC", "view/Edit Course catalogue",
        () -> CourseCli.viewEditCatalogue(this));
    options.add("CPO", "Change Password of Other user",
        () -> {
          String email = getString("Email");
          String password = getPassword();
          admin.changeUserPassword(email, password);
          System.out.println("Password changed");
        });
    options.add("CPC", "Change Password for Current user",
        () -> changePassword(admin));
    options.add("DD", "Delete Database and exit", () -> {
      if (confirm()) {
        Database.clearData();
        System.exit(0);
      }
    });
    options.showOptions("Admin");
  }

  private void addUser() {
    String email = getString("Email");
    String name = getStringUpper("Name");
    String password = getPassword();
    String role = getStringLower("Role");
    if (!(role.equals("admin") ||
        role.equals("acad_office") ||
        role.equals("instructor") ||
        role.equals("student"))) {
      System.out.println("Invalid role (admin, acad_office, instructor, student)");
      Log.error("Add user: Invalid role");
      return;
    }

    if (role.equals("instructor")) {
      String dept = getStringUpper("Department");
      if (!Utils.getDepartments().containsKey(dept)) {
        System.out.println("Invalid department");
        Log.error("Add user: Invalid department");
        return;
      }
      if (confirm()) {
        System.out.println(admin.addInstructor(email, name, password, dept));

      }
    } else if (role.equals("student")) {
      String dept = getStringUpper("Department");
      if (!Utils.getDepartments().containsKey(dept)) {
        System.out.println("Invalid department");
        Log.error("Add user: Invalid department");
        return;
      }
      String entryNumber = getStringUpper("Entry Number");
      int batch = getInt("Batch", 2010, 2030);
      if (confirm()) {
        System.out.println(admin.addStudent(email, name, password, dept, entryNumber, batch));
      }
    } else {
      if (confirm()) {
        if (admin.addUser(email, name, password, role)) {
          System.out.println("User added");
        } else {
          System.out.println("User already exist");
        }
      }
    }
  }

  private void deleteUser() {
    String email = getString("Email");
    if (confirm()) {
      if (admin.deleteUser(email)) {
        System.out.println("User deleted");
      } else {
        System.out.println("User does not exist");
      }
    }
  }
}