package com.ems.dao.memory;

import com.ems.dao.SalaryRepository;
import com.ems.model.SalaryRecord;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemorySalaryRepositoryImpl implements SalaryRepository {

    private final Map<String, SalaryRecord> store = new ConcurrentHashMap<>();
    private int idSeq = 1;

    public InMemorySalaryRepositoryImpl() {
        seed();
    }

    private void seed() {
        saveOrUpdate(new SalaryRecord(1, "EMP-FT-1001", 960000.00, 0.0, 0.0, 5000.0, 2500.0, 82500.00, null));
        saveOrUpdate(new SalaryRecord(2, "EMP-FT-1002", 600000.00, 0.0, 0.0, 3000.0, 1500.0, 51500.00, null));
        saveOrUpdate(new SalaryRecord(3, "EMP-CT-2001", 0.0, 1200.0, 160.0, 0.0, 5000.0, 187000.00, null));
        saveOrUpdate(new SalaryRecord(4, "EMP-CT-2002", 0.0, 950.0, 140.0, 0.0, 3000.0, 130000.00, null));
    }

    @Override
    public SalaryRecord findByEmployeeId(String employeeId) {
        return store.get(employeeId);
    }

    @Override
    public boolean saveOrUpdate(SalaryRecord record) {
        if (record.getSalaryId() <= 0) {
            record.setSalaryId(idSeq++);
        }
        store.put(record.getEmployeeId(), record);
        return true;
    }

    @Override
    public Map<String, Object> getSalaryStatistics() {
        Map<String, Object> stats = new HashMap<>();
        int count = store.size();
        double sum = 0.0;
        double min = Double.MAX_VALUE;
        double max = 0.0;

        for (SalaryRecord r : store.values()) {
            double sal = r.getNetMonthlySalary();
            sum += sal;
            if (sal < min) min = sal;
            if (sal > max) max = sal;
        }

        if (count == 0) {
            min = 0.0;
        }

        stats.put("totalRecords", count);
        stats.put("totalMonthlyPayroll", sum);
        stats.put("averageMonthlySalary", count > 0 ? (sum / count) : 0.0);
        stats.put("minMonthlySalary", min);
        stats.put("maxMonthlySalary", max);
        stats.put("departmentBreakdown", new LinkedHashMap<String, double[]>());

        return stats;
    }
}
