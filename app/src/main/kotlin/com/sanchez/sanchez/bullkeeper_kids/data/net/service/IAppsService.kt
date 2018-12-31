package com.sanchez.sanchez.bullkeeper_kids.data.net.service

import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.SaveAppInstalledDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.SaveAppStatsDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.AppInstalledDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.AppRuleDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.AppStatsDTO
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
 * POST /api/v1/children/{kid}/terminal/{terminal}/apps/delete DELETE_APPS_INSTALLED
 * GET /api/v1/children/{kid}/terminal/{terminal}/apps/rules GET_APP_RULES_FOR_APPS_IN_THE_TERMINAL
 * POST /api/v1/children/{kid}/terminal/{terminal}/apps/stats SAVE_STATS_FOR_ALL_APPS_INSTALLED_IN_THE_TERMINAL
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


    /**
     * Add App Installed
     */
    @POST("children/{kid}/terminal/{terminal}/apps/add")
    fun addAppInstalled(
            @Path("kid")  kid: String,
            @Path("terminal") terminal: String,
            @Body app: SaveAppInstalledDTO
    ): Deferred<APIResponse<AppInstalledDTO>>

    /**
     * Delete Apps Installed
     */
    @POST("children/{kid}/terminal/{terminal}/apps/delete")
    fun deleteAppsInstalled(
            @Path("kid")  kid: String,
            @Path("terminal") terminal: String,
            @Body appsList: List<String>) : Deferred<APIResponse<String>>

    /**
     * Get App Rules for apps in the terminal
     */
    @GET("children/{kid}/terminal/{terminal}/apps/rules")
    fun getAppRulesForAppsInTheTerminal(
            @Path("kid")  kid: String,
            @Path("terminal") terminal: String
    ) : Deferred<APIResponse<List<AppRuleDTO>>>


    /**
     * Save Stats For All Apps Installed In The Terminal
     */
    @POST("children/{kid}/terminal/{terminal}/apps/stats")
    fun saveStatsForAllAppsInstalledInTheTerminal(
            @Path("kid")  kid: String,
            @Path("terminal") terminal: String,
            @Body appStats: List<SaveAppStatsDTO>
    ): Deferred<APIResponse<List<AppStatsDTO>>>


    /**
     * Delete Apps Stats
     */
    @POST("children/{kid}/terminal/{terminal}/apps/stats/delete")
    fun deleteAppStats(
            @Path("kid")  kid: String,
            @Path("terminal") terminal: String,
            @Body appStats: List<String>) : Deferred<APIResponse<String>>

}