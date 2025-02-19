package com.example.Login.controller

import com.example.Login.model.User
import com.example.Login.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @PostMapping("/register")
    fun registerUser(@RequestBody user: User): User {
        return userService.registerUser(user)
    }

    @PostMapping("/login")
    fun loginUser(@RequestParam email: String, @RequestParam password: String): Boolean {
        return userService.loginUser(email, password)
    }
}
