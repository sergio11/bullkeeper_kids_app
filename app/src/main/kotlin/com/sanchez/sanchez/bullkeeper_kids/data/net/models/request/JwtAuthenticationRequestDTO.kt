package com.sanchez.sanchez.bullkeeper_kids.data.net.models.request

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * JWT Authentication Request DTO
 */
class JwtAuthenticationRequestDTO {

    /**
     * Email
     */
    @JsonProperty("email")
    var email: String? = null

    /**
     * Password
     */
    @JsonProperty("password")
    var password: String? = null

    constructor(){}

    constructor(email: String, password: String) {
        this.email = email
        this.password = password
    }

    override fun toString(): String {
        return "JwtAuthenticationRequestDTO{" +
                "email='" + email + '\''.toString() +
                ", password='" + password + '\''.toString() +
                '}'.toString()
    }
}