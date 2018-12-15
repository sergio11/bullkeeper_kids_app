package com.sanchez.sanchez.bullkeeper_kids.data.repository


/**
 * Support Repository
 */
interface ISupportRepository<T> {

    /**
     * Save
     */
    fun save(model: T)

    /**
     * Delete
     */
    fun delete(model: T)

    /**
     * Save
     */
    fun save(modelList: List<T>)

    /**
     * Delete
     */
    fun delete(modelList: List<T>)

    /**
     * List
     */
    fun list(): List<T>

}