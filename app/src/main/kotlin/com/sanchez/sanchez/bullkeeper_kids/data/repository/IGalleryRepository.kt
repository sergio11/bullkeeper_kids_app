package com.sanchez.sanchez.bullkeeper_kids.data.repository

import com.sanchez.sanchez.bullkeeper_kids.data.entity.GalleryImageEntity


/**
 * Gallery Repository
 */
interface IGalleryRepository: ISupportRepository<GalleryImageEntity> {

    /**
     * Find By Identity
     */
    fun findById(id: String): GalleryImageEntity?


    /**
     * Find By Ids
     */
    fun findByIds(ids: List<String>): List<GalleryImageEntity>


}