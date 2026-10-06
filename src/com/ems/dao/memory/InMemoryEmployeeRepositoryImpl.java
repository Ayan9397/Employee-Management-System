package com.ems.dao.memory;

import com.ems.dao.EmployeeRepository;
import com.ems.model.ContractEmployee;
import com.ems.model.Employee;
import com.ems.model.FullTimeEmployee;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryEmployeeRepositoryImpl implements EmployeeRepository {

    private final Map<String, Employee> store = new ConcurrentHashMap<>();
    private int ftCounter = 1002;
    private int ctCounter = 2002;

    public InMemoryEmployeeRepositoryImpl() {
        seed();
    }

    private void seed() {
        FullTimeEmployee ft1 = new FullTimeEmployee("EMP-FT-1001", "Ayan", "Khan", "ayan.khan@company.com",
                "+91-9876543210", LocalDate.of(2024, 1, 15), 1, 1, 960000.0, 5000.0, 2500.0, "Standard Health & 401(k)");
        ft1.setDepartmentName("Engineering");
        ft1.setRoleTitle("Software Engineer");
        save(ft1);

        FullTimeEmployee ft2 = new FullTimeEmployee("EMP-FT-1002", "Sarah", "Jenkins", "sarah.j@company.com",
                "+91-9812345678", LocalDate.of(2023, 8, 10), 2, 3, 600000.0, 3000.0, 1500.0, "Standard Health & 401(k)");
        ft2.setDepartmentName("Human Resources");
        ft2.setRoleTitle("HR Specialist");
        save(ft2);

        ContractEmployee ct1 = new ContractEmployee("EMP-CT-2001", "Rahul", "Verma", "rahul.v@contractor.com",
                "+91-9723456789", LocalDate.of(2024, 5, 1), 1, 2, 1200.0, 160.0, 6, 5000.0);
        ct1.setDepartmentName("Engineering");
        ct1.setRoleTitle("Lead Architect");
        save(ct1);

        ContractEmployee ct2 = new ContractEmployee("EMP-CT-2002", "Emily", "Chen", "emily.c@contractor.com",
                "+91-9654321876", LocalDate.of(2024, 6, 12), 4, 5, 950.0, 140.0, 6, 3000.0);
        ct2.setDepartmentName("Marketing");
        ct2.setRoleTitle("Product Designer");
        save(ct2);
    }

    @Override
    public boolean save(Employee employee) {
        store.put(employee.getEmployeeId(), employee);
        return true;
    }

    @Override
    public boolean update(Employee employee) {
        if (store.containsKey(employee.getEmployeeId())) {
            store.put(employee.getEmployeeId(), employee);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(String employeeId) {
        return store.remove(employeeId) != null;
    }

    @Override
    public Employee findById(String employeeId) {
        return store.get(employeeId);
    }

    @Override
    public List<Employee> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<Employee> search(String keyword) {
        List<Employee> result = new ArrayList<>();
        String kw = keyword.toLowerCase().trim();
        for (Employee e : store.values()) {
            if (e.getEmployeeId().toLowerCase().contains(kw) ||
                e.getFullName().toLowerCase().contains(kw) ||
                e.getEmail().toLowerCase().contains(kw) ||
                e.getDepartmentName().toLowerCase().contains(kw)) {
                result.add(e);
            }
        }
        return result;
    }

    @Override
    public List<Employee> findByDepartment(int departmentId) {
        List<Employee> result = new ArrayList<>();
        for (Employee e : store.values()) {
            if (e.getDepartmentId() == departmentId) {
                result.add(e);
            }
        }
        return result;
    }

    @Override
    public int countTotalEmployees() {
        return store.size();
    }

    @Override
    public String generateNextEmployeeId(String employmentType) {
        if ("FULL_TIME".equalsIgnoreCase(employmentType)) {
            ftCounter++;
            return "EMP-FT-" + ftCounter;
        } else {
            ctCounter++;
            return "EMP-CT-" + ctCounter;
        }
    }
}
