package com.sanchez.sanchez.bullkeeper_kids.data.net.models.response

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

/**
 * JWT Authentication Response DTO
 */
class JwtAuthenticationResponseDTO: Serializable {

    /**
     * Identity
     */
    @JsonProperty("identity")
    var identity: String? = null

    /**
     * Token
     */
    @JsonProperty("token")
    var token: String? = null

    /**
     *
     */
    override fun toString(): String {
        return "JwtAuthenticationResponseDTO(identity=$identity, token=$token)"
    }


}