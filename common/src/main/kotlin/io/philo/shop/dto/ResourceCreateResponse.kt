package io.philo.shop.dto

/**
 * 자원 생성에 대한 DTO
 *
 * 단일 자원에 대한 응답 포맷이 모두 같기에 공통 클래스로 추출함
 */
data class ResourceCreateResponse(val id:Long)