package com.example.booking_system.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service //contine Business logic si interactioneaza cu repository-ul pentru a accesa baza de date
//este responsabil de procesarea cererilor venite dintr Controller si gestionarea logicii aplicatiei
public class UserService {

    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Optional<User> findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    public List<User>getAllUsers(){
        return usersRepository.findAll();
    }

    public User registerUser(String fullName, String email, String rawPassword) {
        if (usersRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already registered!");
        }

        String hashedPassword = passwordEncoder.encode(rawPassword);
        User newUser = new User(fullName, email, hashedPassword);
        return usersRepository.save(newUser);
    }

    public boolean authenticateUser(String email, String rawPassword) {
        Optional<User> userOpt = usersRepository.findByEmail(email);
        if (userOpt.isEmpty()) return false;

        User user = userOpt.get();
        return passwordEncoder.matches(rawPassword, user.getPasswordHash());
    }

}
