package com.aws.cognito.repository;

import com.aws.cognito.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value= "SELECT * FROM USERS WHERE USERNAME = ?1",nativeQuery = true)
    User findByUserName(String userName);
}
