package io.philo.shop.domain.entity

import io.philo.shop.PasswordEncoder
import io.philo.shop.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "users",
    uniqueConstraints = [
        UniqueConstraint(name = "unique__users__email", columnNames = ["email"])
    ]
)
class UserEntity private constructor(

    @Column(nullable = false)
    var email: String = "",

    @Column(nullable = false)
    var name: String = "",

    @Column(nullable = false)
    var address: String = "",

    @Column(nullable = false)
    var encodedPassword: String = "",

    ) : BaseEntity() {

    protected constructor() : this("", ", ", "")

    fun isSamePassword(rawPassword: String?): Boolean {
        return PasswordEncoder.isSamePassword(rawPassword, encodedPassword)
    }

    override fun toString(): String {
        return "UserEntity(id=$id, email='$email', name='$name', address='$address')"
    }

    companion object {

        @JvmStatic
        fun of(email: String, name: String, address: String, rawPassword: String) =
            UserEntity(
                email = email,
                name = name,
                address = address,
                encodedPassword = PasswordEncoder.encodePassword(rawPassword)
            )
    }
}
