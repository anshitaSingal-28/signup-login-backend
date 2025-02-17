package com.example.Login.controller

import com.example.Login.model.User
import com.example.Login.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @PostMapping("/signup")
    fun signup(@RequestBody user: User): ResponseEntity<String> {
        val savedUser = userService.registerUser(user)
        return ResponseEntity.ok("User registered successfully with ID: ${savedUser.id}")
    }

    @PostMapping("/login")
    fun login(@RequestBody user: User): ResponseEntity<String> {
        return if (userService.loginUser(user.email, user.password)) {
            ResponseEntity.ok("Congratulations! You are logged in.")
        } else {
            ResponseEntity.status(401).body("Invalid credentials. Please sign up.")
        }
    }
}
