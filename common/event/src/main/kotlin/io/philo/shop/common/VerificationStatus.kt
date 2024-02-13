package io.philo.shop.common

import io.philo.shop.error.InAppException

enum class VerificationStatus(val description: String) {

    PENDING("대기 중"),
    SUCCESS("검증 결과 정상"),
    FAIL("검증 실패");

    val toBool: Boolean
        get() = when (this) {
            PENDING -> throw InAppException(message = "PENDING 상태는 참/거짓 값으로 반환되지 않습니다.")
            SUCCESS -> true
            FAIL -> false
        }

    companion object {

        @JvmStatic
        fun of(verification: Boolean) = if (verification) SUCCESS else FAIL
    }
}