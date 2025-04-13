package com.ahmed.Spring_Security.dao;

import com.ahmed.Spring_Security.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role , Integer> {

    Optional<Role> findByName(String name);

}
