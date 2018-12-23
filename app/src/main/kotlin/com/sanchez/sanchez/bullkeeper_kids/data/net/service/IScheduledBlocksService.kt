package com.sanchez.sanchez.bullkeeper_kids.data.net.service

import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.ScheduledBlockDTO
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * GET /api/v1/children/{kid}/scheduled-blocks GET_ALL_SCHEDULED_BLOCKS
 * GET /api/v1/children/{kid}/scheduled-blocks/{block} GET_SCHEDULED_BLOCK_BY_ID
 */
interface IScheduledBlocksService {

    /**
     * Get All Scheduled Blocks
     */
    @GET("children/{kid}/scheduled-blocks")
    fun getAllScheduledBlocks(
            @Path("kid")  kid: String
    ) : Deferred<APIResponse<List<ScheduledBlockDTO>>>

    /**
     * Get Scheduled Block By Id
     */
    @GET("children/{kid}/scheduled-blocks/{block}")
    fun getScheduledBlockById(
            @Path("kid")  kid: String,
            @Path("block") block: String
    ) : Deferred<APIResponse<ScheduledBlockDTO>>

}