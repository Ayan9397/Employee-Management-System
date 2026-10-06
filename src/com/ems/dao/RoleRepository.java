package com.ems.dao;

import com.ems.model.Role;
import java.util.List;

/**
 * Data Access Object (DAO) interface for Role management.
 */
public interface RoleRepository {
    List<Role> findAll();
    Role findById(int roleId);
}

