package com.sanchez.sanchez.bullkeeper_kids.data.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

/**
 * Gallery Image Entity
 */
open class GalleryImageEntity(
        // Id
        @PrimaryKey var id: Long? = 0,
        // Display Name
        var displayName: String? = "",
        // Path
        var path: String? = "",
        // Date Added
        var dateAdded: Long? = 0,
        // Date Modified
        var dateModified: Long? = 0,
        // Date Taken
        var dateTaken: Long? = 0,
        // Height
        var height: Int? = 0,
        // Width
        var width: Int? = 0,
        // Orientation
        var orientation: Int? = 0,
        // Size
        var size: Int? = 0,
        // Sync
        var sync: Int = 0,
        // Server Id
        var serverId: String? = "",
        // Remove
        var remove: Int = 0
): RealmObject(), Serializable