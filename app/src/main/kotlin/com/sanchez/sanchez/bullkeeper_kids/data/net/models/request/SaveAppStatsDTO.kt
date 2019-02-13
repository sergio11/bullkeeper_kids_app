package com.sanchez.sanchez.bullkeeper_kids.data.net.models.request


import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Save App Stats DTO
 */
data class SaveAppStatsDTO(

    /**
     * Identity
     */
    @get:JsonProperty("identity")
    var identity: String = "",

    /**
     * First Time
     */
    @get:JsonProperty("first_time")
    var firstTime: String? = null,

    /**
     * Last Time
     */
    @get:JsonProperty("last_time")
    var lastTime: String? = null,

    /**
     * Last Time Used
     */
    @get:JsonProperty("last_time_used")
    var lastTimeUsed: String? = null,

    /**
     * Total Time in foreground
     */
    @get:JsonProperty("total_time_in_foreground")
    var totalTimeInForeground: Long? = null,

    /**
     * Package Name
     */
    @get:JsonProperty("package_name")
    var packageName: String? = null,

    /**
     * Kid
     */
    @get:JsonProperty("kid")
    var kid: String? = null,

    /**
     * Terminal
     */
    @get:JsonProperty("terminal")
    var terminal: String? = null

){}