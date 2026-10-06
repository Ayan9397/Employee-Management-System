package com.ems.dao.memory;

import com.ems.dao.DepartmentRepository;
import com.ems.model.Department;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDepartmentRepositoryImpl implements DepartmentRepository {

    private final Map<Integer, Department> store = new ConcurrentHashMap<>();
    private int idSeq = 1;

    public InMemoryDepartmentRepositoryImpl() {
        seed();
    }

    private void seed() {
        save(new Department(1, "DEPT-ENG", "Engineering", "Building A - Floor 4", 750000.00));
        save(new Department(2, "DEPT-HR", "Human Resources", "Building B - Floor 2", 200000.00));
        save(new Department(3, "DEPT-FIN", "Finance", "Building A - Floor 2", 350000.00));
        save(new Department(4, "DEPT-MKT", "Marketing", "Building C - Floor 1", 300000.00));
    }

    @Override
    public List<Department> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Department findById(int departmentId) {
        return store.get(departmentId);
    }

    @Override
    public boolean save(Department department) {
        if (department.getDepartmentId() <= 0) {
            department.setDepartmentId(idSeq++);
        } else if (department.getDepartmentId() >= idSeq) {
            idSeq = department.getDepartmentId() + 1;
        }
        store.put(department.getDepartmentId(), department);
        return true;
    }

    @Override
    public int getEmployeeCountByDepartment(int departmentId) {
        return 0; // calculated dynamically in service
    }
}

