package io.philo.shop.domain.entity

import io.philo.shop.PasswordEncoder
import io.philo.shop.entity.BaseEntity
import jakarta.persistence.*
import jakarta.persistence.GenerationType.IDENTITY

@Entity
@Table(
    name = "users",
    uniqueConstraints = [
        UniqueConstraint(name = "unique__users__email", columnNames = ["email"])
    ]
)
class UserEntity protected constructor(

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column
    val id: Long? = null,

    @Column(nullable = false)
    var email: String = "",

    @Column(nullable = false)
    var name: String = "",

    @Column(nullable = false)
    var address: String = "",

    @Column(nullable = false)
    var encodedPassword: String = "",

) : BaseEntity() {

    protected constructor() : this(email = "")

    constructor(
        email: String,
        name: String,
        address: String,
        rawPassword: String,
    ) :
            this(
                email = email,
                name = name,
                address = address,
                encodedPassword = PasswordEncoder.encodePassword(rawPassword)
            )

    fun isSamePassword(rawPassword: String?): Boolean {
        return PasswordEncoder.isSamePassword(rawPassword, encodedPassword)
    }

    override fun toString(): String {
        return "UserEntity(id=$id, email='$email', name='$name', address='$address')"
    }

    companion object
}
