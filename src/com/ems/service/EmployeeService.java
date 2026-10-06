package com.ems.service;

import com.ems.dao.DaoFactory;
import com.ems.dao.DepartmentRepository;
import com.ems.dao.EmployeeRepository;
import com.ems.dao.RoleRepository;
import com.ems.model.ContractEmployee;
import com.ems.model.Department;
import com.ems.model.Employee;
import com.ems.model.FullTimeEmployee;
import com.ems.model.Role;
import com.ems.util.ValidationUtil;

import java.time.LocalDate;
import java.util.List;

/**
 * Service Layer handling Employee business rules, validations, and orchestration.
 */
public class EmployeeService {

    private final EmployeeRepository employeeRepo;
    private final DepartmentRepository departmentRepo;
    private final RoleRepository roleRepo;

    public EmployeeService() {
        this.employeeRepo = DaoFactory.getEmployeeRepository();
        this.departmentRepo = DaoFactory.getDepartmentRepository();
        this.roleRepo = DaoFactory.getRoleRepository();
    }

    public Employee registerFullTimeEmployee(String firstName, String lastName, String email,
                                             String phone, LocalDate hireDate, int deptId, int roleId,
                                             double annualSalary, double monthlyBonus, double monthlyDeductions,
                                             String benefits) {
        validateCommonDetails(firstName, lastName, email, phone);

        if (!ValidationUtil.isPositive(annualSalary)) {
            throw new IllegalArgumentException("Annual salary must be positive.");
        }
        if (!ValidationUtil.isPositive(monthlyBonus)) {
            throw new IllegalArgumentException("Bonus must be positive.");
        }
        if (!ValidationUtil.isPositive(monthlyDeductions)) {
            throw new IllegalArgumentException("Deductions must be positive.");
        }

        String empId = employeeRepo.generateNextEmployeeId("FULL_TIME");
        FullTimeEmployee ft = new FullTimeEmployee(empId, firstName, lastName, email, phone,
                hireDate, deptId, roleId, annualSalary, monthlyBonus, monthlyDeductions, benefits);

        enrichDepartmentAndRole(ft);

        boolean success = employeeRepo.save(ft);
        if (!success) {
            throw new RuntimeException("Failed to save full-time employee record.");
        }
        return ft;
    }

    public Employee registerContractEmployee(String firstName, String lastName, String email,
                                             String phone, LocalDate hireDate, int deptId, int roleId,
                                             double hourlyRate, double hoursWorked, int durationMonths,
                                             double monthlyDeductions) {
        validateCommonDetails(firstName, lastName, email, phone);

        if (!ValidationUtil.isPositive(hourlyRate) || hourlyRate == 0) {
            throw new IllegalArgumentException("Hourly rate must be greater than zero.");
        }
        if (!ValidationUtil.isPositive(hoursWorked)) {
            throw new IllegalArgumentException("Hours worked must be non-negative.");
        }

        String empId = employeeRepo.generateNextEmployeeId("CONTRACT");
        ContractEmployee ct = new ContractEmployee(empId, firstName, lastName, email, phone,
                hireDate, deptId, roleId, hourlyRate, hoursWorked, durationMonths, monthlyDeductions);

        enrichDepartmentAndRole(ct);

        boolean success = employeeRepo.save(ct);
        if (!success) {
            throw new RuntimeException("Failed to save contract employee record.");
        }
        return ct;
    }

    public boolean updateEmployee(Employee employee) {
        validateCommonDetails(employee.getFirstName(), employee.getLastName(), employee.getEmail(), employee.getPhone());
        return employeeRepo.update(employee);
    }

    public boolean deleteEmployee(String employeeId) {
        if (!ValidationUtil.isNotEmpty(employeeId)) {
            throw new IllegalArgumentException("Employee ID cannot be empty.");
        }
        return employeeRepo.delete(employeeId);
    }

    public Employee getEmployeeById(String employeeId) {
        return employeeRepo.findById(employeeId);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepo.findAll();
    }

    public List<Employee> searchEmployees(String keyword) {
        if (!ValidationUtil.isNotEmpty(keyword)) {
            return getAllEmployees();
        }
        return employeeRepo.search(keyword);
    }

    public List<Employee> getEmployeesByDepartment(int departmentId) {
        return employeeRepo.findByDepartment(departmentId);
    }

    private void validateCommonDetails(String firstName, String lastName, String email, String phone) {
        if (!ValidationUtil.isNotEmpty(firstName)) {
            throw new IllegalArgumentException("First name cannot be empty.");
        }
        if (!ValidationUtil.isNotEmpty(lastName)) {
            throw new IllegalArgumentException("Last name cannot be empty.");
        }
        if (!ValidationUtil.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format (e.g. user@company.com).");
        }
        if (!ValidationUtil.isValidPhone(phone)) {
            throw new IllegalArgumentException("Invalid phone number format.");
        }
    }

    private void enrichDepartmentAndRole(Employee emp) {
        if (emp.getDepartmentId() > 0) {
            Department d = departmentRepo.findById(emp.getDepartmentId());
            if (d != null) {
                emp.setDepartmentName(d.getDepartmentName());
            }
        }
        if (emp.getRoleId() > 0) {
            Role r = roleRepo.findById(emp.getRoleId());
            if (r != null) {
                emp.setRoleTitle(r.getRoleTitle());
            }
        }
    }
}

