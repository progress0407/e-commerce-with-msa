package io.philo.shop

import io.jsonwebtoken.*
import io.jsonwebtoken.io.DecodingException
import io.jsonwebtoken.security.SignatureException
import mu.KotlinLogging
import java.util.*
import javax.crypto.SecretKey


class JwtManager(
    private val secretKey: SecretKey,
    private val expirationDurationTime: Long
) {

    companion object {
        @JvmStatic
        val ENCODING_ALGORITHM = SignatureAlgorithm.HS512
    }

    private val log = KotlinLogging.logger { }


    /**
     * JWT 생성
     */
    fun createAccessToken(tokenSubject: String): String {

        return Jwts.builder()
            .signWith(secretKey, ENCODING_ALGORITHM)
            .setSubject(tokenSubject)
            .setIssuedAt(Date())
            .setExpiration(createExpirationDateTime())
            .compact()
    }

    /**
     * 유효한 토큰인지 검증
     */
    fun isValidToken(accessToken: String): Boolean {

        return try {
            tryParseJwt(accessToken)
            true
        } catch (e: Exception) {
            when (e) {
                is IllegalArgumentException ,
                is SignatureException,
                is MalformedJwtException,
                is ExpiredJwtException,
                is UnsupportedJwtException,
                is DecodingException -> {
                    log.info { e }
                    false
                }
                else -> {
                    log.error { e } // 예측하지 못한 예외
                    throw e
                }
            }
        }
    }

    /**
     * JWT의 Subject 추출
     */
    fun parse(accessToken: String?): String {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(accessToken)
            .body
            .subject
    }

    /**
     * 유요한 토큰인지 확인하는데 사용
     */
    private fun tryParseJwt(accessToken: String) {
        Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(accessToken)
    }

    /**
     * 만료 일시 생성
     */
    private fun createExpirationDateTime() =
        Date(System.currentTimeMillis() + expirationDurationTime)
}
