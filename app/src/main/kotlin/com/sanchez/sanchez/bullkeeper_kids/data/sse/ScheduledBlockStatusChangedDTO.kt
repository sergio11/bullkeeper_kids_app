package com.sanchez.sanchez.bullkeeper_kids.data.sse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Scheduled Block Status Changed DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class ScheduledBlockStatusChangedDTO(

        /**
         * Kid
         */
        @JsonProperty("kid")
        var kid: String? = null,

        /**
         * Scheduled Block Status
         */
        @JsonProperty("scheduled_block_status")
        var scheduledBlockStatusList: Iterable<ScheduledBlockStatusDTO>

)