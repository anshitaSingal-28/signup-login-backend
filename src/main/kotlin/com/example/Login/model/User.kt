package com.example.Login.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "users") // Collection name in MongoDB
data class User(
    @Id val id: String? = null,
    val name: String,
    val email: String,
    val password: String,  // Hashed password
    val createdAt: Date = Date()
)
