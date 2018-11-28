package com.sanchez.sanchez.bullkeeper_kids.data.net.service

import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.ChildrenOfSelfGuardianDTO
import kotlinx.coroutines.Deferred
import retrofit2.http.GET


/**
 * Guardians Service
 */
interface IGuardiansService {

    /**
     * Get Self Children
     */
    @GET("guardians/self/children")
    fun getSelfChildren(): Deferred<APIResponse<ChildrenOfSelfGuardianDTO>>
}