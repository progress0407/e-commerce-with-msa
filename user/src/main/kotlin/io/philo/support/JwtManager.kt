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
        try {
            tryParseJwt(accessToken)
        } catch (e: IllegalArgumentException) {
            return false
        } catch (e: SignatureException) {
            return false
        } catch (e: MalformedJwtException) {
            return false
        } catch (e: ExpiredJwtException) {
            return false
        } catch (e: UnsupportedJwtException) {
            return false
        } catch (e: DecodingException) {
            return false
        }
        return true
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
