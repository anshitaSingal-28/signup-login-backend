package com.example.Login.repository

import com.example.Login.model.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, String> {
    fun findByEmail(email: String): User?
}
