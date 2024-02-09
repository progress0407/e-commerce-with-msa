package io.philo.support

import io.jsonwebtoken.*
import io.jsonwebtoken.io.DecodingException
import io.jsonwebtoken.security.SignatureException
import java.util.*
import javax.crypto.SecretKey


class JwtManager(
    private val secretKey: SecretKey,
    private val expirationDurationTime: Long
) {

    fun createAccessToken(tokenSubject: String): String {
        return Jwts.builder()
            .signWith(secretKey, ENCODING_ALGORITHM)
            .setSubject(tokenSubject)
            .setIssuedAt(Date())
            .setExpiration(createExpirationDateTime())
            .compact()
    }

    fun isValidToken(accessToken: String): Boolean {
        return try {
            tryParseJwt(accessToken)
            true
        } catch (e: Exception) {
            when (e) {
                is IllegalArgumentException,
                is SignatureException,
                is MalformedJwtException,
                is ExpiredJwtException,
                is UnsupportedJwtException,
                is DecodingException -> false
                else -> throw e
            }
        }
    }

    fun parse(accessToken: String?): String {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(accessToken)
            .body
            .subject
    }

    private fun tryParseJwt(accessToken: String) {
        Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(accessToken)
    }

    private fun createExpirationDateTime(): Date {
        return Date(System.currentTimeMillis() + expirationDurationTime)
    }

    companion object {
        private val ENCODING_ALGORITHM = SignatureAlgorithm.HS512
    }
}
