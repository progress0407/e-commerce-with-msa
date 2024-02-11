package io.philo.shop

import io.jsonwebtoken.security.Keys
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.philo.shop.JwtManager.Companion.ENCODING_ALGORITHM
import java.nio.charset.StandardCharsets

class JwtManagerTest:StringSpec( {

    val secretKey = Keys.hmacShaKeyFor(SECRET_KEY_STRING.toByteArray(StandardCharsets.UTF_8))
    val jwtManager = JwtManager(secretKey, ONE_DAY_IN_SECONDS)


    "토큰을_만들고_검증한다" {
        // given
        val 원본_문자열 = "유일한 어떤 것"

        // when
        val accessToken = jwtManager.createAccessToken(원본_문자열)
        val 복호한_값 = jwtManager.parse(accessToken)

        // then
        복호한_값 shouldBe 원본_문자열
    }

    "토큰의_유효성을_검증한다" {
        // given
        val 원본_문자열 = "유일한 어떤 것"
        val 정상_토큰 = jwtManager.createAccessToken(원본_문자열)
        val 변조된_토큰 = 정상_토큰 + "a"

        jwtManager.isValidToken(정상_토큰) shouldBe true
        jwtManager.isValidToken(변조된_토큰) shouldBe false
    }

    "만일 HS512로 만든 두 개의 SecretKey를 사용해서 암/복화하면 정상적으로 수행되지 않는다" {
        // given
        val jwtManager1 = JwtManager(Keys.secretKeyFor(ENCODING_ALGORITHM), ONE_DAY_IN_SECONDS)
        val jwtManager2 = JwtManager(Keys.secretKeyFor(ENCODING_ALGORITHM), ONE_DAY_IN_SECONDS)

        val 원본_문자열 = "유일한 어떤 것"
        val 정상_토큰 = jwtManager1.createAccessToken(원본_문자열)

        jwtManager2.isValidToken(정상_토큰) shouldBe false
    }
}) {
    companion object {
        private val SECRET_KEY_STRING = "abc 123 abc 123 abc 123 abc 123 abc 123 abc 123 abc 123 abc 123 abc 123 abc 123"
        private val ONE_DAY_IN_SECONDS = 86400000L // 하루를 초로 표현한 것
    }
}
