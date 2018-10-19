package com.sanchez.sanchez.bullkeeper_kids.data.repository

import java.io.Serializable

/**
 * Support Repository
 */
interface ISupportRepository<T: Serializable> {

    /**
     * Save
     */
    fun save(model: T)

    /**
     * Save
     */
    fun save(modelList: List<T>)

    /**
     * List
     */
    fun list(): List<T>

}