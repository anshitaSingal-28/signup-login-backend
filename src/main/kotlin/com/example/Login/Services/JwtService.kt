package com.example.Login.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtService {
    private val secretKey = "MySuperSecretKeyMySuperSecretKeyMySuperSecretKey"  // Move to environment variables in production
    private val expiration = 86400000  // 1 day in milliseconds

    fun generateToken(email: String): String {
        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + expiration))
            .signWith(SignatureAlgorithm.HS256, secretKey.toByteArray())
            .compact()
    }
}
