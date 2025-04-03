package com.example.booking_system.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

//controleaza cererile HTTP si le redirectioneaza catre serviciile corespunzatoare
//primeste cererile de la client, apeleaza SERVICE-ul, prelucreaza raspunsul si il trimite inapoi.
@CrossOrigin(origins = "http://localhost:3000")  // Allow React frontend to access
@RestController //combina @Controller si @ResponseBody returnand direct un JSON sau XML
@RequestMapping("/api/users") //o adresa specifica un se fac cereri


public class UserController {

    private final UserService userService;
    @Autowired //este folosit pentru dependency injection, adica pentru a permite Spring-ului sa creeze automat instante
    //ale componenetelor necesare, injecteaza automat dependentele(Repository in Service, Service in Controller)

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        try {
            User registeredUser = userService.registerUser(
                    signupRequest.getFullName(),
                    signupRequest.getEmail(),
                    signupRequest.getPassword() // Raw password
            );

            // Return the saved user (excluding password)
            return ResponseEntity.ok("User registered successfully: " + registeredUser.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        boolean isAuthenticated = userService.authenticateUser(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

        if (isAuthenticated) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }



    @GetMapping
    public ResponseEntity<?>getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }



    @GetMapping("/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.findByEmail(email);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
