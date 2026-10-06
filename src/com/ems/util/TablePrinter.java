package com.ems.util;

import com.ems.model.Employee;
import com.ems.model.Department;

import java.util.List;

/**
 * Utility for formatting tabular command-line output.
 */
public class TablePrinter {

    public static void printEmployeeTable(List<Employee> employees) {
        if (employees == null || employees.isEmpty()) {
            System.out.println("\n  [!] No employees found matching criteria.");
            return;
        }

        String border = "+-------------+----------------------+--------------+--------------------+----------------------+---------------+";
        System.out.println("\n" + border);
        System.out.printf("| %-11s | %-20s | %-12s | %-18s | %-20s | %13s |\n",
                "EMP ID", "NAME", "TYPE", "DEPARTMENT", "ROLE", "NET MONTHLY");
        System.out.println(border);

        for (Employee emp : employees) {
            System.out.printf("| %-11s | %-20s | %-12s | %-18s | %-20s | $%12.2f |\n",
                    emp.getEmployeeId(),
                    truncate(emp.getFullName(), 20),
                    emp.getEmploymentType(),
                    truncate(emp.getDepartmentName(), 18),
                    truncate(emp.getRoleTitle(), 20),
                    emp.calculateMonthlySalary());
        }
        System.out.println(border);
        System.out.println("  Total Employees: " + employees.size() + "\n");
    }

    public static void printDepartmentTable(List<Department> departments) {
        if (departments == null || departments.isEmpty()) {
            System.out.println("\n  [!] No departments found.");
            return;
        }

        String border = "+-----+------------+----------------------+----------------------+-----------------+";
        System.out.println("\n" + border);
        System.out.printf("| %-3s | %-10s | %-20s | %-20s | %15s |\n",
                "ID", "CODE", "NAME", "LOCATION", "BUDGET");
        System.out.println(border);

        for (Department d : departments) {
            System.out.printf("| %-3d | %-10s | %-20s | %-20s | $%14.2f |\n",
                    d.getDepartmentId(),
                    d.getDepartmentCode(),
                    truncate(d.getDepartmentName(), 20),
                    truncate(d.getLocation(), 20),
                    d.getBudget());
        }
        System.out.println(border + "\n");
    }

    private static String truncate(String text, int maxLen) {
        if (text == null) return "";
        if (text.length() <= maxLen) return text;
        return text.substring(0, maxLen - 3) + "...";
    }
}

