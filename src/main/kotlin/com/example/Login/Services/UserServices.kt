package com.example.Login.service

import com.example.Login.model.User
import com.example.Login.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository, private val passwordEncoder: BCryptPasswordEncoder) {

    fun registerUser(user: User): User {
        val hashedPassword = passwordEncoder.encode(user.password) // Hash password before saving
        val newUser = user.copy(password = hashedPassword)
        return userRepository.save(newUser)
    }

    fun loginUser(email: String, password: String): Boolean {
        val user = userRepository.findByEmail(email) ?: return false
        return passwordEncoder.matches(password, user.password) // Verify password
    }
}
