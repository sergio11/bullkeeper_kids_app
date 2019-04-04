package com.sanchez.sanchez.bullkeeper_kids.data.net.models.request

import com.fasterxml.jackson.annotation.JsonProperty


/**
    Add Device Photo
 **/
data class AddDevicePhotoDTO (

        /**
         * Display Name
         */
        @get:JsonProperty("display_name")
        var displayName: String?,

        /**
         * Path
         */
        @get:JsonProperty("path")
        var path: String?,

        /**
         * Date Added
         */
        @get:JsonProperty("date_added")
        var dateAdded: Long?,

        /**
         * Date Modified
         */
        @get:JsonProperty("date_modified")
        var dateModified: Long?,

        /**
         * Date Taken
         */
        @get:JsonProperty("date_taken")
        var dateTaken: Long?,

        /**
         * height
         */
        @get:JsonProperty("height")
        var height: Int?,

        /**
         * width
         */
        @get:JsonProperty("width")
        var width: Int?,

        /**
         * orientation
         */
        @get:JsonProperty("orientation")
        var orientation: Int?,

        /**
         * size
         */
        @get:JsonProperty("size")
        var size: Int?,

        /**
         * Local Id
         */
        @get:JsonProperty("local_id")
        var localId: String?,

        /**
         * Kid
         */
        @get:JsonProperty("kid")
        var kid: String?,

        /**
         * Terminal
         */
        @get:JsonProperty("terminal")
        var terminal: String?
)