package com.example.Login.Controller

import com.example.Login.model.User
import com.example.Login.repository.UserRepository
import com.example.Login.service.JwtService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*
import java.util.regex.Pattern

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = ["http://localhost:4200"]) // Enable CORS
class AuthController(
    private val userRepository: UserRepository,
    private val jwtService: JwtService
) {
    private val passwordEncoder = BCryptPasswordEncoder()

    // Regex for password strength validation
    private val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#\$%^&*]).{8,}$"

    @PostMapping("/signup")
    fun signup(@RequestBody user: User): Map<String, String> {
        // Check if any field is empty
        if (user.name.isBlank() || user.email.isBlank() || user.password.isBlank()) {
            return mapOf("error" to "Name, email, and password are required fields.")
        }

        // Check if email already exists
        if (userRepository.findByEmail(user.email) != null) {
            return mapOf("error" to "Email already exists")
        }

        // Password validation
        if (!Pattern.matches(passwordPattern, user.password)) {
            return mapOf("error" to "Password must be at least 8 characters long, contain at least one digit, one uppercase letter, one lowercase letter, and one special character.")
        }

        // Hash password and save the user
        val hashedPassword = passwordEncoder.encode(user.password)
        val newUser = user.copy(password = hashedPassword)
        userRepository.save(newUser)

        return mapOf("message" to "Signup successful! Please log in.")
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: Map<String, String>): Map<String, String> {
        val email = loginRequest["email"] ?: return mapOf("error" to "Email required")
        val password = loginRequest["password"] ?: return mapOf("error" to "Password required")

        val user = userRepository.findByEmail(email)
            ?: return mapOf("error" to "Invalid credentials")

        return if (passwordEncoder.matches(password, user.password)) {
            val token = jwtService.generateToken(user.email)
            mapOf("token" to token, "message" to "Login successful")
        } else {
            mapOf("error" to "Invalid credentials")
        }
    }
}
