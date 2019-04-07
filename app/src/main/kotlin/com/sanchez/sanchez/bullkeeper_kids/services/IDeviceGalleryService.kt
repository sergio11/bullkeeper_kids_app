package com.sanchez.sanchez.bullkeeper_kids.services

import com.sanchez.sanchez.bullkeeper_kids.data.entity.GalleryImageEntity


/**
 * Device Gallery Service
 */
interface IDeviceGalleryService {

    /**
     * Get Images Path
     */
    fun getImagesPath(): List<String>

    /**
     * Get Images
     */
    fun getImages(): List<GalleryImageEntity>

    /**
     * Delete Image
     * @param imageFile
     */
    fun deleteImage(imageFile: String)

    /**
     * Delete Image
     * @param imageFileList
     */
    fun deleteImage(imageFileList: List<String>)

}