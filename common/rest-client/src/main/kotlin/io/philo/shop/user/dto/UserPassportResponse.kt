package io.philo.shop.user.dto

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

    override fun toString(): String {
        return "UserPassportResponse(id=$id, name='$name', email='$email', isValid=$isValid)"
    }
}
