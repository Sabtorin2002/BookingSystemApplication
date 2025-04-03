package com.example.booking_system.user;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository //layer de interactiune cu baza de date, responsabil de operatiile CRUD
public interface UsersRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
