package com.sanchez.sanchez.bullkeeper_kids.data.net.service

import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.AddMessageDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.ConversationDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.MessageDTO
import kotlinx.coroutines.Deferred
import retrofit2.http.*

/**
 * Conversation Service
 * DELETE /api/v1/conversations/self/{kid} DELETE_CONVERSATION
 * GET /api/v1/conversations/self/{kid} GET_CONVERSATION_DETAIL
 * DELETE /api/v1/conversations/self/{kid}/messages DELETE_CONVERSATION_MESSAGES
 * GET /api/v1/conversations/self/{kid}/messages GET_CONVERSATION_MESSAGES
 * POST /api/v1/conversations/self/{kid}/messages ADD_MESSAGE
 * DELETE /api/v1/conversations/{id} DELETE_CONVERSATION_BY_ID
 * GET /api/v1/conversations/{id} GET_CONVERSATION_BY_ID
 * DELETE /api/v1/conversations/{id}/messages DELETE_CONVERSATION_MESSAGES
 * GET /api/v1/conversations/{id}/messages GET_CONVERSATION_MESSAGES
 * POST /api/v1/conversations/{id}/messages ADD_MESSAGE
 */
interface IConversationService {

    /**
     * Delete Conversation
     * @param kid
     * @return
     */
    @DELETE("conversations/self/{kid}")
    fun deleteConversation(
            @Path("kid") kid: String): Deferred<APIResponse<String>>


    /**
     * Get Conversation
     * @param kid
     * @return
     */
    @GET("conversations/self/{kid}")
    fun getConversation(
            @Path("kid") kid: String): Deferred<APIResponse<ConversationDTO>>

    /**
     * Delete Conversation Messages
     * @param kid
     * @return
     */
    @DELETE("conversations/self/{kid}/messages")
    fun deleteConversationMessages(
            @Path("kid") kid: String,
            @Query("ids") messageIds: List<String>): Deferred<APIResponse<String>>

    /**
     * Get Conversation Messages
     * @param kid
     * @return
     */
    @GET("conversations/self/{kid}/messages")
    fun getConversationMessages(
            @Path("kid") kid: String): Deferred<APIResponse<List<MessageDTO>>>

    /**
     * Delete Conversation By Id
     * @param id
     * @return
     */
    @DELETE("conversations/{id}")
    fun deleteConversationById(
            @Path("id") id: String): Deferred<APIResponse<String>>

    /**
     * Get Conversation By Id
     * @param id
     * @return
     */
    @GET("conversations/{id}")
    fun getConversationById(
            @Path("id") id: String): Deferred<APIResponse<ConversationDTO>>

    /**
     * Delete Messages by conversation id
     * @param id
     * @return
     */
    @DELETE("conversations/{id}/messages")
    fun deleteMessagesByConversationId(
            @Path("id") id: String,
            @Query("ids") messageIds: List<String>): Deferred<APIResponse<String>>

    /**
     * Get Messages by conversation id
     * @param id
     * @return
     */
    @GET("conversations/{id}/messages")
    fun getMessagesByConversationId(
            @Path("id") id: String): Deferred<APIResponse<List<MessageDTO>>>

    /**
     * Add Message
     * @param kid
     * @param message
     * @return
     */
    @POST("conversations/self/{kid}/messages")
    fun addMessage(
            @Path("kid") kid: String,
            @Body message: AddMessageDTO): Deferred<APIResponse<MessageDTO>>

    /**
     * Add Message By Conversation Id
     * @param kid
     * @param messageDTO
     * @return
     */
    @POST("conversations/{id}/messages")
    fun addMessageByConversationId(
            @Path("id") kid: String,
            @Body messageDTO: AddMessageDTO): Deferred<APIResponse<MessageDTO>>
}