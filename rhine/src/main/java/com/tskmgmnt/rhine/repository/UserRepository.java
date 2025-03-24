package com.tskmgmnt.rhine.repository;

import com.tskmgmnt.rhine.entity.User;
import com.tskmgmnt.rhine.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User, String> {
    Optional<User> findByEmail(String email);

    List<User> findByUserRoleNot(UserRole userRole);
}
