package com.sanchez.sanchez.bullkeeper_kids.data.net.service

import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.SaveContactDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.ContactDTO
import kotlinx.coroutines.Deferred
import retrofit2.http.*

/**
 * Contacts Service
 * DELETE /api/v1/children/{id}/terminal/{terminal}/contacts DELETE_ALL_CONTACTS_FROM_TERMINAL
 * GET /api/v1/children/{id}/terminal/{terminal}/contacts GET_ALL_CONTACTS_FROM_TERMINAL
 * POST /api/v1/children/{id}/terminal/{terminal}/contacts SAVE_CONTACTS_FROM_TERMINAL
 * POST /api/v1/children/{id}/terminal/{terminal}/contacts/add ADD_CONTACT_FROM_TERMINAL
 * DELETE /api/v1/children/{id}/terminal/{terminal}/contacts/{contact} DELETE_SINGLE_CONTACT
 * GET /api/v1/children/{id}/terminal/{terminal}/contacts/{contact} GET_CONTACT_DETAIL
 * POST /api/v1/children/{id}/terminal/{terminal}/contacts/delete DELETE_CONTACTS_FROM_TERMINAL
 */
interface IContactsService {

    /**
     * Delete All contacts from terminal
     */
    @DELETE("children/{id}/terminal/{terminal}/contacts")
    fun deleteAllContactsFromTerminal(
            @Path("id")  kid: String,
            @Path("terminal") terminal: String) : Deferred<APIResponse<String>>

    /**
     * Get All Contacts from terminal
     */
    @GET("children/{id}/terminal/{terminal}/contacts")
    fun getAllContactsFromTerminal(
            @Path("id")  kid: String,
            @Path("terminal") terminal: String
    ) : Deferred<APIResponse<ContactDTO>>


    /**
     * Save Contacts From Terminal
     */
    @POST("children/{id}/terminal/{terminal}/contacts")
    fun saveContactsFromTerminal(
            @Path("id")  kid: String,
            @Path("terminal") terminal: String,
            @Body contacts: List<SaveContactDTO>
    ): Deferred<APIResponse<List<ContactDTO>>>

    /**
     * Add Contact From Terminal
     */
    @POST("children/{id}/terminal/{terminal}/contacts/add")
    fun addContactFromTerminal(
            @Path("id")  kid: String,
            @Path("terminal") terminal: String,
            @Body contact: SaveContactDTO
    ): Deferred<APIResponse<ContactDTO>>

    /**
     * Delete contact from terminal
     */
    @DELETE("children/{id}/terminal/{terminal}/contacts/{contact}")
    fun deleteSingleContactFromTerminal(
            @Path("id")  kid: String,
            @Path("terminal") terminal: String,
            @Path("contact") contact: String) : Deferred<APIResponse<String>>

    /**
     * Get Contact Detail
     */
    @GET("children/{id}/terminal/{terminal}/contacts/{contact}")
    fun getContactDetailFromTerminal(
            @Path("id")  kid: String,
            @Path("terminal") terminal: String,
            @Path("contact") contact: String
    ) : Deferred<APIResponse<ContactDTO>>

    /**
     * Delete Contacts From Terminal
     */
    @POST("children/{id}/terminal/{terminal}/contacts/delete")
    fun deleteContactsFromTerminal(
            @Path("id")  kid: String,
            @Path("terminal") terminal: String,
            @Body contactsList: List<String>
    ): Deferred<APIResponse<String>>

}