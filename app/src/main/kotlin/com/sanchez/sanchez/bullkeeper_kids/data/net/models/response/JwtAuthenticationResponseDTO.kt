package com.sanchez.sanchez.bullkeeper_kids.data.net.models.response

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

/**
 * JWT Authentication Response DTO
 */
class JwtAuthenticationResponseDTO: Serializable {

    /**
     * Token
     */
    @JsonProperty("token")
    var token: String? = null

    /**
     * To String
     */
    override fun toString(): String {
        return "JwtAuthenticationResponseDTO(token=$token)"
    }
}