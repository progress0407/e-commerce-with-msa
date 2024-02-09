package io.philo.unit

import io.jsonwebtoken.security.Keys
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.philo.shop.JwtManager
import java.nio.charset.StandardCharsets

class JwtManagerTest:StringSpec( {

    val SECRET_KEY_STRING = "abc 123 abc 123 abc 123 abc 123 abc 123 abc 123 abc 123 abc 123 abc 123 abc 123"
    val ONE_DAY_IN_SECONDS = 86400000L // 하루를 초로 표현한 것

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
})
