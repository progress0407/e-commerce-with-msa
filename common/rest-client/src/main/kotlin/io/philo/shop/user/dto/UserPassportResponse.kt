package io.philo.shop.user.dto

//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
class UserPassportResponse private constructor(
    val id: Long = -1,
    val name: String = "",
    val email: String = "",
    val isValid: Boolean = true
) {

    companion object {
        fun OfInvalid() = UserPassportResponse(isValid = false)

        fun OfValid(id: Long, name: String, email: String) =
            UserPassportResponse(id, name, email, isValid = true)
    }
}

//data class ValidUserPassportResponse(val id: Long, val name: String, val email: String) : UserPassportResponse {
//    constructor(): this(0L, "", "")
//}

//class InValidUserPassportResponse : UserPassportResponse
