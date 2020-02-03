package com.unizef.login.repository;

import com.unizef.login.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import java.util.List;


@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Query(value = "SELECT u.name,u.last_name FROM user u inner join user_role ur on u.user_id=ur.user_id where ur.role_id=2 ", nativeQuery = true)
	List<User> getALLEmployees();
}