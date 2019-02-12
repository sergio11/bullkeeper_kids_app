package com.sanchez.sanchez.bullkeeper_kids.data.net.models.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * App Stats DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class AppStatsDTO(

    /**
     * Identity
     */
    @JsonProperty("identity")
    var identity: String? = null,

    /**
     * First Time
     */
    @JsonProperty("first_time")
    var firstTime: String? = null,

    /**
     * Last Time
     */
    @JsonProperty("last_time")
    var lastTime: String? = null,

    /**
     * Last Time Used
     */
    @JsonProperty("last_time_used")
    var lastTimeUsed: String? = null,

    /**
     * Total Time in foreground
     */
    @JsonProperty("total_time_in_foreground")
    var totalTimeInForeground: Long? = null,

    /**
     * Package Name
     */
    @JsonProperty("package_name")
    var packageName: String? = null,

    /**
     * Kid
     */
    @JsonProperty("id")
    var kid: String? = null,

    /**
     * Terminal
     */
    @JsonProperty("terminal")
    var terminal: String? = null

){}