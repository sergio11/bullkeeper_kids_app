package com.sanchez.sanchez.bullkeeper_kids.data.net.service

import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.SaveAppInstalledDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.AppInstalledDTO
import kotlinx.coroutines.Deferred
import retrofit2.http.*

/**
 * Apps Service
 * GET /api/v1/children/{kid}/terminal/{terminal} GET_TERMINAL_DETAIL
 * DELETE /api/v1/children/{kid}/terminal/{terminal}/apps DELETE_ALL_APPS_INSTALLED
 * GET /api/v1/children/{kid}/terminal/{terminal}/apps GET_ALL_APPS_INSTALLED_IN_THE_TERMINAL
 * POST /api/v1/children/{kid}/terminal/{terminal}/apps SAVE_APPS_INSTALLED_IN_THE_TERMINAL
 * POST /api/v1/children/{kid}/terminal/{terminal}/apps/add ADD_APP_INSTALLED
 * DELETE /api/v1/children/{kid}/terminal/{terminal}/apps/{app} DELETE_APP_INSTALLED_BY_ID
 */
interface IAppsService {

    /**
     * Delete All Apps Installed
     */
    @DELETE("children/{kid}/terminal/{terminal}/apps")
    fun deleteAllAppInstalled(
            @Path("kid")  kid: String,
            @Path("terminal") terminal: String) : Deferred<APIResponse<String>>

    /**
     * Get All Apps Installed In The terminal
     */
    @GET("children/{kid}/terminal/{terminal}/apps")
    fun getAllAppsInstalledInTheTerminal(
            @Path("kid")  kid: String,
            @Path("terminal") terminal: String
    ) : Deferred<APIResponse<AppInstalledDTO>>

    /**
     * Delete App Installed by id
     */
    @DELETE("children/{kid}/terminal/{terminal}/apps/{app}")
    fun deleteAppInstalledById(
            @Path("kid")  kid: String,
            @Path("terminal") terminal: String,
            @Path("app") app: String) : Deferred<APIResponse<String>>

    /**
     * Save Apps Installed In The Terminal
     */
    @POST("children/{kid}/terminal/{terminal}/apps")
    fun saveAppsInstalledInTheTerminal(
            @Path("kid")  kid: String,
            @Path("terminal") terminal: String,
            @Body apps: List<SaveAppInstalledDTO>
    ): Deferred<APIResponse<List<AppInstalledDTO>>>
}