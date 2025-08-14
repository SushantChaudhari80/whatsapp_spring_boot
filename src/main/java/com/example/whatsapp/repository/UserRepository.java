package com.example.whatsapp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import com.example.whatsapp.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	@Query(value = "SELECT * FROM user WHERE username = :username AND password = :pass", nativeQuery = true)
	User getUser(@Param("username") String username, @Param("pass") String password);
	
	@Query(value = "SELECT * FROM user WHERE username = :username ", nativeQuery = true)
	User getUserDetails(@Param("username") String username);
    
}