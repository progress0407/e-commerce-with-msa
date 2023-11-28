package io.philo.domain.entity

import io.philo.support.PasswordEncoder
import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    val id: Long? = null,

    @Column(nullable = false)
    private var email: String = "",

    @Column(nullable = false)
    private var name: String = "",

    @Column(nullable = false)
    private var address: String = "",

    @Column(nullable = false)
    var encodedPassword: String = ""
) {
    constructor() : this(email = "")

    constructor(
        email: String,
        name: String,
        address: String,
        rawPassword: String
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
}
