package com.example.Login.Controller
import com.example.Login.model.User
import com.example.Login.repository.UserRepository
import com.example.Login.service.JwtService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = ["http://localhost:4200"]) // Enable CORS
class AuthController(
    private val userRepository: UserRepository,
    private val jwtService: JwtService
) {
    private val passwordEncoder = BCryptPasswordEncoder()

    @PostMapping("/signup")
    fun signup(@RequestBody user: User): Map<String, String> {
        if (userRepository.findByEmail(user.email) != null) {
            return mapOf("error" to "Email already exists")
        }
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
