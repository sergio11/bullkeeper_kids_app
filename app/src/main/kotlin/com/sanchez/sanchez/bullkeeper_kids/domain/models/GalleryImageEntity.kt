package com.sanchez.sanchez.bullkeeper_kids.domain.models

/**
 * Gallery Image Entity
 */
data class GalleryImageEntity(
        var id: Long,
        var path: String,
        var displayName: String,
        var dateAdded: Long,
        var dateModified: Long,
        var dateTaken: Long,
        var height: Int,
        var width: Int,
        var orientation: Int?,
        var size: Int
)