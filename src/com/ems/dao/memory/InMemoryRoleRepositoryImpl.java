package com.ems.dao.memory;

import com.ems.dao.RoleRepository;
import com.ems.model.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRoleRepositoryImpl implements RoleRepository {

    private final Map<Integer, Role> store = new ConcurrentHashMap<>();

    public InMemoryRoleRepositoryImpl() {
        seed();
    }

    private void seed() {
        store.put(1, new Role(1, "Software Engineer", "Develops and maintains core backend and frontend systems"));
        store.put(2, new Role(2, "Lead Architect", "Designs enterprise architecture and mentors developers"));
        store.put(3, new Role(3, "HR Specialist", "Handles recruitment, onboarding, and employee relations"));
        store.put(4, new Role(4, "Financial Analyst", "Monitors company expenses, financial planning, and budgets"));
        store.put(5, new Role(5, "Product Designer", "Creates user experience specifications and UI mockups"));
    }

    @Override
    public List<Role> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Role findById(int roleId) {
        return store.get(roleId);
    }
}

