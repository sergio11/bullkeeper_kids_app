package com.sanchez.sanchez.bullkeeper_kids.data.net.models.request

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * JWT Social Authentication Request DTO
 */
class JwtSocialAuthenticationRequestDTO {

    /**
     * Token
     */
    @JsonProperty("token")
    var token: String? = null

    constructor(){}


    constructor(token: String) {
        this.token = token
    }

    /**
     * To String
     */
    override fun toString(): String {
        return "JwtSocialAuthenticationRequestDTO{" +
                "token='" + token + '\''.toString() +
                '}'.toString()
    }
}