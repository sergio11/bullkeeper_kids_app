package com.sanchez.sanchez.bullkeeper_kids.data.net.models.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * API Response
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class APIResponse<T> {

    /**
     * Response Code
     */
    @JsonProperty("response_code")
    var code: String? = null

    /**
     * Response Code Name
     */
    @JsonProperty("response_code_name")
    var codeName: String? = null

    /**
     * Response HTTP Status
     */
    @JsonProperty("response_http_status")
    var httpStatus: String? = null

    /**
     * Response Info URL
     */
    @JsonProperty("response_info_url")
    var infoUrl: String? = null

    /**
     * Response Status
     */
    @JsonProperty("response_status")
    var status: String? = null

    /**
     * Response Data
     */
    @JsonProperty("response_data")
    var data: T? = null
}