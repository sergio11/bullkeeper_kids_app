package com.sanchez.sanchez.bullkeeper_kids.data.net.service

import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.AddMessageDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.ConversationDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.MessageDTO
import kotlinx.coroutines.Deferred
import retrofit2.http.*
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.GET



/**
 * Conversation Service
 * GET -> /api/v1/conversations/{id}
 * DELETE -> /api/v1/conversations/{id}
 * GET -> /api/v1/conversations/{id}/messages
 * DELETE -> /api/v1/conversations/{id}/messages
 * POST -> /api/v1/conversations/{id}/messages
 * GET -> /api/v1/conversations/members/self
 * GET -> /api/v1/conversations/members/{member}
 * GET -> /api/v1/conversations/members/{memberOne}/{memberTwo}
 * POST -> /api/v1/conversations/members/{memberOne}/{memberTwo}
 * DELETE -> /api/v1/conversations/members/{memberOne}/{memberTwo}
 * GET -> /api/v1/conversations/members/{memberOne}/{memberTwo}/messages
 * DELETE -> /api/v1/conversations/members/{memberOne}/{memberTwo}/messages
 * POST -> /api/v1/conversations/members/{memberOne}/{memberTwo}/messages
 */
interface IConversationService {


    /**
     * Get Conversation By Id
     * @param id
     * @return
     */
    @GET("conversations/{id}")
    fun getConversationById(
            @Path("id") id: String): Deferred<APIResponse<ConversationDTO>>


    /**
     * Delete Conversation By Id
     * @param id
     * @return
     */
    @DELETE("conversations/{id}")
    fun deleteConversationById(
            @Path("id") id: String): Deferred<APIResponse<String>>

    /**
     * Get Conversation Messages
     * @param id
     * @return
     */
    @GET("conversations/{id}/messages")
    fun getConversationMessages(
            @Path("id") id: String): Deferred<APIResponse<List<MessageDTO>>>


    /**
     * Delete Conversation
     * @param id
     * @return
     */
    @DELETE("conversations/{id}/messages")
    fun deleteConversation(
            @Path("id") id: String): Deferred<APIResponse<String>>

    /**
     * Delete Conversation Messages
     * @param id
     * @param messages
     * @return
     */
    @DELETE("conversations/{id}/messages")
    fun deleteConversationMessage(
            @Path("id") id: String,
            @Query("messages") messages: List<String>
    ): Deferred<APIResponse<String>>


    /**
     * Add Message
     * @param id
     * @param message
     * @return
     */
    @POST("conversations/{id}/messages")
    fun addMessage(
            @Path("id") id: String,
            @Body message: AddMessageDTO): Deferred<APIResponse<MessageDTO>>


    /**
     * Get Conversations For Member
     * @return
     */
    @GET("conversations/members/{member}")
    fun getConversationsForMember(
            @Path("member") member: String
    ): Deferred<APIResponse<List<ConversationDTO>>>

    /**
     * Delete Conversations For Member
     * @return
     */
    @DELETE("conversations/members/{member}")
    fun deleteConversationsForMember(
            @Path("member") member: String
    ): Deferred<APIResponse<String>>

    /**
     * Get Conversation For Members
     * @param memberOne
     * @param memberTwo
     * @return
     */
    @GET("conversations/members/{memberOne}/{memberTwo}")
    fun getConversationForMembers(
            @Path("memberOne") memberOne: String,
            @Path("memberTwo") memberTwo: String
    ): Deferred<APIResponse<ConversationDTO>>

    /**
     * Create Conversation
     * @param memberOne
     * @param memberTwo
     * @return
     */
    @POST("conversations/members/{memberOne}/{memberTwo}")
    fun createConversation(
            @Path("memberOne") memberOne: String,
            @Path("memberTwo") memberTwo: String
    ): Deferred<APIResponse<ConversationDTO>>

    /**
     * Delete Conversatioin For Members
     * @param memberOne
     * @param memberTwo
     * @return
     */
    @DELETE("conversations/members/{memberOne}/{memberTwo}")
    fun deleteConversationForMembers(
            @Path("memberOne") memberOne: String,
            @Path("memberTwo") memberTwo: String
    ): Deferred<APIResponse<String>>

    /**
     * Get Conversation Messages For Members
     * @param memberOne
     * @param memberTwo
     * @return
     */
    @GET("conversations/members/{memberOne}/{memberTwo}/messages")
    fun getConversationMessagesForMembers(
            @Path("memberOne") memberOne: String,
            @Path("memberTwo") memberTwo: String
    ): Deferred<APIResponse<List<MessageDTO>>>

    /**
     * Delete Conversation Messages For Members
     * @param memberOne
     * @param memberTwo
     * @return
     */
    @DELETE("conversations/members/{memberOne}/{memberTwo}/messages")
    fun deleteConversationMessagesForMembers(
            @Path("memberOne") memberOne: String,
            @Path("memberTwo") memberTwo: String
    ): Deferred<APIResponse<String>>

    /**
     * Add Message DTO
     * @param memberOne
     * @param memberTwo
     * @param addMessageDTO
     * @return
     */
    @POST("conversations/members/{memberOne}/{memberTwo}/messages")
    fun addMessage(
            @Path("memberOne") memberOne: String,
            @Path("memberTwo") memberTwo: String,
            addMessageDTO: AddMessageDTO
    ): Deferred<APIResponse<MessageDTO>>

}