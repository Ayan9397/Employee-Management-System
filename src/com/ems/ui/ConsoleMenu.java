package com.ems.ui;

import com.ems.dao.DaoFactory;
import com.ems.model.ContractEmployee;
import com.ems.model.Department;
import com.ems.model.Employee;
import com.ems.model.FullTimeEmployee;
import com.ems.service.DepartmentService;
import com.ems.service.EmployeeService;
import com.ems.service.SalaryService;
import com.ems.util.DatabaseConnection;
import com.ems.util.TablePrinter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Command-Line Interface providing an interactive menu for the Employee Management System.
 */
public class ConsoleMenu {

    private final Scanner scanner;
    private EmployeeService employeeService;
    private DepartmentService departmentService;
    private SalaryService salaryService;

    public ConsoleMenu() {
        this.scanner = new Scanner(System.in);
        reinitializeServices();
    }

    private void reinitializeServices() {
        this.employeeService = new EmployeeService();
        this.departmentService = new DepartmentService();
        this.salaryService = new SalaryService();
    }

    public void run() {
        boolean running = true;
        while (running) {
            printBanner();
            printMenu();
            System.out.print("  Select an option [1-9]: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleAddEmployee();
                    break;
                case "2":
                    handleUpdateEmployee();
                    break;
                case "3":
                    handleDeleteEmployee();
                    break;
                case "4":
                    handleSearchEmployee();
                    break;
                case "5":
                    handleViewAllEmployees();
                    break;
                case "6":
                    handleDepartmentManagement();
                    break;
                case "7":
                    handleSalaryManagement();
                    break;
                case "8":
                    handleDatabaseSettings();
                    break;
                case "9":
                    System.out.println("\n  Thank you for using Employee Management System. Goodbye!\n");
                    running = false;
                    break;
                default:
                    System.out.println("\n  [!] Invalid selection. Please enter a number between 1 and 9.");
            }

            if (running) {
                System.out.print("\n  Press [Enter] to return to main menu...");
                scanner.nextLine();
            }
        }
    }

    private void printBanner() {
        String dbMode = DaoFactory.isUsingJdbc() ? "MySQL (Live JDBC)" : "In-Memory (Demo Mode)";
        System.out.println("\n==========================================================================");
        System.out.println("                 EMPLOYEE MANAGEMENT SYSTEM (EMS)                         ");
        System.out.println("              Stack: Java + OOP + JDBC + MySQL Database                   ");
        System.out.println("              Storage Mode: [" + dbMode + "]");
        System.out.println("==========================================================================");
    }

    private void printMenu() {
        System.out.println("  1. Add Employee (Full-Time / Contract)");
        System.out.println("  2. Update Employee");
        System.out.println("  3. Delete Employee");
        System.out.println("  4. Search Employee (by ID, Name, Department)");
        System.out.println("  5. View All Employees");
        System.out.println("  6. Department Management");
        System.out.println("  7. Salary & Payroll Analytics");
        System.out.println("  8. Database Settings & Connection Test");
        System.out.println("  9. Exit");
        System.out.println("--------------------------------------------------------------------------");
    }

    private void handleAddEmployee() {
        System.out.println("\n--- [ Add New Employee ] ---");
        System.out.println("Select Employment Type:");
        System.out.println("  1. Full-Time Employee (Salaried + Bonus)");
        System.out.println("  2. Contract Employee (Hourly Rate + Hours Worked)");
        System.out.print("Choice [1/2]: ");
        String typeChoice = scanner.nextLine().trim();

        if (!typeChoice.equals("1") && !typeChoice.equals("2")) {
            System.out.println("  [!] Invalid employment type selection.");
            return;
        }

        try {
            System.out.print("Enter First Name: ");
            String firstName = scanner.nextLine().trim();

            System.out.print("Enter Last Name: ");
            String lastName = scanner.nextLine().trim();

            System.out.print("Enter Email Address: ");
            String email = scanner.nextLine().trim();

            System.out.print("Enter Phone Number: ");
            String phone = scanner.nextLine().trim();

            // Display departments to choose from
            List<Department> depts = departmentService.getAllDepartments();
            System.out.println("\nAvailable Departments:");
            for (Department d : depts) {
                System.out.printf("  [%d] %s (%s)\n", d.getDepartmentId(), d.getDepartmentName(), d.getDepartmentCode());
            }
            System.out.print("Enter Department ID (or 0 for unassigned): ");
            int deptId = Integer.parseInt(scanner.nextLine().trim());

            int roleId = 1; // Default software engineer
            LocalDate hireDate = LocalDate.now();

            if (typeChoice.equals("1")) {
                // Full Time
                System.out.print("Enter Annual Base Salary ($): ");
                double annualSalary = Double.parseDouble(scanner.nextLine().trim());

                System.out.print("Enter Monthly Bonus ($) [e.g. 500]: ");
                String bonusInput = scanner.nextLine().trim();
                double bonus = bonusInput.isEmpty() ? 0.0 : Double.parseDouble(bonusInput);

                System.out.print("Enter Monthly Deductions ($) [e.g. 250]: ");
                String dedInput = scanner.nextLine().trim();
                double deductions = dedInput.isEmpty() ? 0.0 : Double.parseDouble(dedInput);

                Employee created = employeeService.registerFullTimeEmployee(
                        firstName, lastName, email, phone, hireDate, deptId, roleId,
                        annualSalary, bonus, deductions, "Full Health + Retirement Plan"
                );
                System.out.println("\n  [SUCCESS] Full-Time Employee registered successfully!");
                System.out.printf("  Assigned Employee ID: %s\n", created.getEmployeeId());
                System.out.printf("  Calculated Net Monthly Salary: $%,.2f\n", created.calculateMonthlySalary());

            } else {
                // Contract
                System.out.print("Enter Hourly Rate ($): ");
                double hourlyRate = Double.parseDouble(scanner.nextLine().trim());

                System.out.print("Enter Estimated Hours Worked per Month [e.g. 160]: ");
                double hoursWorked = Double.parseDouble(scanner.nextLine().trim());

                System.out.print("Enter Contract Duration in Months [e.g. 6]: ");
                int duration = Integer.parseInt(scanner.nextLine().trim());

                System.out.print("Enter Monthly Deductions/Taxes ($) [e.g. 200]: ");
                String dedInput = scanner.nextLine().trim();
                double deductions = dedInput.isEmpty() ? 0.0 : Double.parseDouble(dedInput);

                Employee created = employeeService.registerContractEmployee(
                        firstName, lastName, email, phone, hireDate, deptId, roleId,
                        hourlyRate, hoursWorked, duration, deductions
                );
                System.out.println("\n  [SUCCESS] Contract Employee registered successfully!");
                System.out.printf("  Assigned Employee ID: %s\n", created.getEmployeeId());
                System.out.printf("  Calculated Net Monthly Salary: $%,.2f\n", created.calculateMonthlySalary());
            }

        } catch (NumberFormatException e) {
            System.out.println("  [!] Invalid numeric format entered: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("  [!] Error: " + e.getMessage());
        }
    }

    private void handleUpdateEmployee() {
        System.out.println("\n--- [ Update Employee Record ] ---");
        System.out.print("Enter Employee ID to update (e.g. EMP-FT-1001): ");
        String empId = scanner.nextLine().trim();

        Employee emp = employeeService.getEmployeeById(empId);
        if (emp == null) {
            System.out.println("  [!] Employee not found with ID: " + empId);
            return;
        }

        System.out.println("\nCurrent Details: " + emp);
        try {
            System.out.print("New First Name [" + emp.getFirstName() + "]: ");
            String fName = scanner.nextLine().trim();
            if (!fName.isEmpty()) emp.setFirstName(fName);

            System.out.print("New Last Name [" + emp.getLastName() + "]: ");
            String lName = scanner.nextLine().trim();
            if (!lName.isEmpty()) emp.setLastName(lName);

            System.out.print("New Email [" + emp.getEmail() + "]: ");
            String email = scanner.nextLine().trim();
            if (!email.isEmpty()) emp.setEmail(email);

            System.out.print("New Phone [" + emp.getPhone() + "]: ");
            String phone = scanner.nextLine().trim();
            if (!phone.isEmpty()) emp.setPhone(phone);

            if (emp instanceof FullTimeEmployee) {
                FullTimeEmployee ft = (FullTimeEmployee) emp;
                System.out.print("New Annual Salary [$" + ft.getAnnualSalary() + "]: ");
                String sal = scanner.nextLine().trim();
                if (!sal.isEmpty()) ft.setAnnualSalary(Double.parseDouble(sal));

                System.out.print("New Monthly Bonus [$" + ft.getMonthlyBonus() + "]: ");
                String bon = scanner.nextLine().trim();
                if (!bon.isEmpty()) ft.setMonthlyBonus(Double.parseDouble(bon));
            } else if (emp instanceof ContractEmployee) {
                ContractEmployee ct = (ContractEmployee) emp;
                System.out.print("New Hourly Rate [$" + ct.getHourlyRate() + "]: ");
                String rate = scanner.nextLine().trim();
                if (!rate.isEmpty()) ct.setHourlyRate(Double.parseDouble(rate));

                System.out.print("New Hours Worked [" + ct.getHoursWorked() + "]: ");
                String hrs = scanner.nextLine().trim();
                if (!hrs.isEmpty()) ct.setHoursWorked(Double.parseDouble(hrs));
            }

            boolean updated = employeeService.updateEmployee(emp);
            if (updated) {
                System.out.println("  [SUCCESS] Employee updated successfully!");
                System.out.println("  New Net Monthly Salary: $" + String.format("%,.2f", emp.calculateMonthlySalary()));
            } else {
                System.out.println("  [!] Update operation failed.");
            }
        } catch (Exception e) {
            System.out.println("  [!] Error updating employee: " + e.getMessage());
        }
    }

    private void handleDeleteEmployee() {
        System.out.println("\n--- [ Delete Employee ] ---");
        System.out.print("Enter Employee ID to delete: ");
        String empId = scanner.nextLine().trim();

        Employee emp = employeeService.getEmployeeById(empId);
        if (emp == null) {
            System.out.println("  [!] Employee not found with ID: " + empId);
            return;
        }

        System.out.printf("Are you sure you want to delete '%s' (%s)? (y/N): ", emp.getFullName(), emp.getEmployeeId());
        String confirm = scanner.nextLine().trim().toLowerCase();
        if (confirm.equals("y") || confirm.equals("yes")) {
            boolean deleted = employeeService.deleteEmployee(empId);
            if (deleted) {
                System.out.println("  [SUCCESS] Employee deleted successfully.");
            } else {
                System.out.println("  [!] Deletion failed.");
            }
        } else {
            System.out.println("  Deletion canceled.");
        }
    }

    private void handleSearchEmployee() {
        System.out.println("\n--- [ Search Employees ] ---");
        System.out.print("Enter search keyword (ID, Name, Email, or Department): ");
        String keyword = scanner.nextLine().trim();

        List<Employee> results = employeeService.searchEmployees(keyword);
        TablePrinter.printEmployeeTable(results);
    }

    private void handleViewAllEmployees() {
        System.out.println("\n--- [ All Registered Employees ] ---");
        List<Employee> list = employeeService.getAllEmployees();
        TablePrinter.printEmployeeTable(list);
    }

    private void handleDepartmentManagement() {
        System.out.println("\n--- [ Department Management ] ---");
        List<Department> departments = departmentService.getAllDepartments();
        TablePrinter.printDepartmentTable(departments);

        System.out.println("Options:");
        System.out.println("  1. Add New Department");
        System.out.println("  2. View Employees in a Department");
        System.out.println("  3. Return to Main Menu");
        System.out.print("Choice: ");
        String opt = scanner.nextLine().trim();

        if (opt.equals("1")) {
            try {
                System.out.print("Enter Department Code (e.g. DEPT-QA): ");
                String code = scanner.nextLine().trim();
                System.out.print("Enter Department Name: ");
                String name = scanner.nextLine().trim();
                System.out.print("Enter Location/Building: ");
                String loc = scanner.nextLine().trim();
                System.out.print("Enter Allocated Budget ($): ");
                double budget = Double.parseDouble(scanner.nextLine().trim());

                boolean ok = departmentService.createDepartment(code, name, loc, budget);
                if (ok) {
                    System.out.println("  [SUCCESS] Department created successfully!");
                } else {
                    System.out.println("  [!] Failed to create department.");
                }
            } catch (Exception e) {
                System.out.println("  [!] Error: " + e.getMessage());
            }
        } else if (opt.equals("2")) {
            System.out.print("Enter Department ID: ");
            try {
                int deptId = Integer.parseInt(scanner.nextLine().trim());
                List<Employee> deptEmps = employeeService.getEmployeesByDepartment(deptId);
                TablePrinter.printEmployeeTable(deptEmps);
            } catch (NumberFormatException e) {
                System.out.println("  [!] Invalid department ID.");
            }
        }
    }

    private void handleSalaryManagement() {
        System.out.println("\n--- [ Salary & Payroll Analytics ] ---");
        System.out.println("SQL Aggregations Demonstrated: SUM, AVG, MIN, MAX, GROUP BY");

        Map<String, Object> stats = salaryService.getPayrollStatistics();

        int totalRec = (int) stats.getOrDefault("totalRecords", 0);
        double totalPayroll = (double) stats.getOrDefault("totalMonthlyPayroll", 0.0);
        double avgSalary = (double) stats.getOrDefault("averageMonthlySalary", 0.0);
        double minSalary = (double) stats.getOrDefault("minMonthlySalary", 0.0);
        double maxSalary = (double) stats.getOrDefault("maxMonthlySalary", 0.0);

        System.out.println("+------------------------------------------------------------+");
        System.out.println("|                   COMPANY PAYROLL SUMMARY                  |");
        System.out.println("+------------------------------------------------------------+");
        System.out.printf("| Total Salaried Employees      : %-26d |\n", totalRec);
        System.out.printf("| Total Monthly Payroll Expense : $%-25.2f |\n", totalPayroll);
        System.out.printf("| Average Net Monthly Salary    : $%-25.2f |\n", avgSalary);
        System.out.printf("| Minimum Monthly Take-Home     : $%-25.2f |\n", minSalary);
        System.out.printf("| Maximum Monthly Take-Home     : $%-25.2f |\n", maxSalary);
        System.out.println("+------------------------------------------------------------+");

        @SuppressWarnings("unchecked")
        Map<String, double[]> deptBreakdown = (Map<String, double[]>) stats.get("departmentBreakdown");
        if (deptBreakdown != null && !deptBreakdown.isEmpty()) {
            System.out.println("\n  Department-wise Payroll Breakdown (SQL GROUP BY):");
            System.out.println("  +--------------------+-------+--------------------+--------------------+");
            System.out.printf("  | %-18s | %-5s | %-18s | %-18s |\n", "DEPARTMENT", "COUNT", "TOTAL PAYROLL", "AVG SALARY");
            System.out.println("  +--------------------+-------+--------------------+--------------------+");
            for (Map.Entry<String, double[]> entry : deptBreakdown.entrySet()) {
                double[] arr = entry.getValue();
                System.out.printf("  | %-18s | %-5.0f | $%17.2f | $%17.2f |\n",
                        entry.getKey(), arr[0], arr[1], arr[2]);
            }
            System.out.println("  +--------------------+-------+--------------------+--------------------+");
        }
    }

    private void handleDatabaseSettings() {
        System.out.println("\n--- [ Database Connection & Configuration ] ---");
        System.out.println("Current URL : " + DatabaseConnection.getUrl());
        System.out.println("Current User: " + DatabaseConnection.getUser());

        boolean isConnected = DatabaseConnection.testConnection();
        System.out.println("Live MySQL Connection Status: " + (isConnected ? "[CONNECTED]" : "[NOT CONNECTED]"));
        System.out.println("Active Mode: " + (DaoFactory.isUsingJdbc() ? "MySQL JDBC" : "In-Memory Demo"));

        System.out.println("\nOptions:");
        System.out.println("  1. Test Connection to MySQL");
        System.out.println("  2. Update MySQL Credentials (host/port/user/password)");
        System.out.println("  3. Switch to MySQL JDBC Mode");
        System.out.println("  4. Switch to In-Memory Demo Mode");
        System.out.println("  5. Return to Main Menu");
        System.out.print("Choice: ");
        String opt = scanner.nextLine().trim();

        if (opt.equals("1")) {
            if (DatabaseConnection.testConnection()) {
                System.out.println("  [SUCCESS] Successfully connected to MySQL database!");
            } else {
                System.out.println("  [!] Connection failed. Please verify credentials in db.properties.");
            }
        } else if (opt.equals("2")) {
            System.out.print("Enter MySQL Username [default: root]: ");
            String user = scanner.nextLine().trim();
            if (user.isEmpty()) user = "root";

            System.out.print("Enter MySQL Password: ");
            String pass = scanner.nextLine().trim();

            String newUrl = "jdbc:mysql://localhost:3306/employee_management_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&createDatabaseIfNotExist=true";
            DatabaseConnection.setCredentials(newUrl, user, pass);

            if (DatabaseConnection.testConnection()) {
                DatabaseConnection.initializeDatabase();
                DaoFactory.enableJdbc();
                reinitializeServices();
                System.out.println("  [SUCCESS] Connected to MySQL and switched to live JDBC mode!");
            } else {
                System.out.println("  [!] Connection failed with provided credentials.");
            }
        } else if (opt.equals("3")) {
            if (DatabaseConnection.testConnection()) {
                DaoFactory.enableJdbc();
                reinitializeServices();
                System.out.println("  [SUCCESS] Switched to MySQL JDBC mode.");
            } else {
                System.out.println("  [!] Cannot switch to MySQL: Connection test failed.");
            }
        } else if (opt.equals("4")) {
            DaoFactory.enableInMemory();
            reinitializeServices();
            System.out.println("  [SUCCESS] Switched to In-Memory Demo mode.");
        }
    }
}

