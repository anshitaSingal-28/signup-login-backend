package com.example.Login.service

import com.example.Login.model.User
import com.example.Login.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository, private val passwordEncoder: BCryptPasswordEncoder) {

    // Function to validate password strength
    fun validatePassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$".toRegex()
        return password.matches(passwordPattern)
    }

    // Register new user after password validation
    fun registerUser(user: User): User {
        if (!validatePassword(user.password)) {
            throw IllegalArgumentException("Password must be at least 8 characters long and contain both letters and numbers")
        }

        // Hash password before saving
        val hashedPassword = passwordEncoder.encode(user.password)
        val newUser = user.copy(password = hashedPassword)
        return userRepository.save(newUser)
    }

    // Login user with email and password verification
    fun loginUser(email: String, password: String): Boolean {
        val user = userRepository.findByEmail(email) ?: return false
        return passwordEncoder.matches(password, user.password)
    }
}
