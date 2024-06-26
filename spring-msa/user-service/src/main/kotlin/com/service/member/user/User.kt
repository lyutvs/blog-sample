package com.service.member.user

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass
import javax.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Entity
@Table(name = "user")
class User(
    @Column(name = "email", nullable = false, length = 50)
    val email: String,
    @Column(name = "name", nullable = false, length = 50)
    val name: String,
    @Column(name = "userid", nullable = false, length = 50, unique = true)
    val userId: String,
    @Column(name = "password", nullable = false, length = 255)
    val password: String,
) : EntityAuditing() {
}

interface UserRepository : JpaRepository<User, Long> {
    fun findByUserId(userId: String): User
}

@Service
class UserSignUpService(
    val userRepository: UserRepository,
    val bCryptPasswordEncoder: BCryptPasswordEncoder
) {

    @Transactional
    fun signUp(dto: UserSignUpRequest): User {
        return userRepository.save(dto.toEntity(bCryptPasswordEncoder.encode(dto.password)))
    }
}


@EntityListeners(value = [AuditingEntityListener::class])
@MappedSuperclass
abstract class EntityAuditing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        internal set

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    lateinit var createdAt: LocalDateTime
        internal set

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    lateinit var updatedAt: LocalDateTime
        internal set
}