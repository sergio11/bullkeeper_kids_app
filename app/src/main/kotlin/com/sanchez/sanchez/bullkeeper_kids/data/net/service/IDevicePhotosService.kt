package com.sanchez.sanchez.bullkeeper_kids.data.net.service

import com.sanchez.sanchez.bullkeeper_kids.data.net.models.request.AddDevicePhotoDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.APIResponse
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.DevicePhotoDTO
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
*    DELETE /api/v1/children/{kid}/terminal/{terminal}/photos DELETE_ALL_DEVICE_PHOTOS
*    GET /api/v1/children/{kid}/terminal/{terminal}/photos GET_DEVICE_PHOTOS
*    POST /api/v1/children/{kid}/terminal/{terminal}/photos SAVE_DEVICE_PHOTO
*    POST /api/v1/children/{kid}/terminal/{terminal}/photos/delete DELETE_DEVICE_PHOTOS
*    GET /api/v1/children/{kid}/terminal/{terminal}/photos/disable GET_LIST_OF_DISABLED_DEVICE_PHOTOS
*    GET /api/v1/children/{kid}/terminal/{terminal}/photos/{photo} GET_DEVICE_PHOTO_DETAIL
*    POST /api/v1/children/{kid}/terminal/{terminal}/photos/{photo}/disable DISABLE_DEVICE_PHOTO
*    POST /api/v1/children/{kid}/terminal/{terminal}/photos/{photo}/disable DISABLE_DEVICE_PHOTO
 **/
interface IDevicePhotosService {

    /**
     * Delete All Device Photos
     */
    @DELETE("children/{kid}/terminal/{terminal}/photos")
    fun deleteAllDevicePhotos(
            @Path("kid")  kid: String,
            @Path("terminal") terminal: String) : Deferred<APIResponse<String>>

    /**
     * Get Device Photos
     */
    @GET("children/{kid}/terminal/{terminal}/contacts")
    fun getDevicePhotos(
            @Path("kid")  kid: String,
            @Path("terminal") terminal: String
    ) : Deferred<APIResponse<DevicePhotoDTO>>


    /**
     * Save Device Photo
     */
    @POST("children/{kid}/terminal/{terminal}/photos")
    fun saveDevicePhoto(
            @Path("kid")  kid: String,
            @Path("terminal") terminal: String,
            @Body devicePhoto: RequestBody
    ): Deferred<APIResponse<DevicePhotoDTO>>

    /**
     * Delete Device Photos
     */
    @POST("children/{kid}/terminal/{terminal}/photos/delete")
    fun deleteDevicePhotos(
            @Path("kid")  kid: String,
            @Path("terminal") terminal: String,
            @Body photoIds: List<String>
    ): Deferred<APIResponse<String>>

    /**
     * Get List Of Disabled Device Photos
     */
    @GET("children/{kid}/terminal/{terminal}/photos/disable")
    fun getListOfDisabledDevicePhotos(
            @Path("kid")  kid: String,
            @Path("terminal") terminal: String
    ) : Deferred<APIResponse<DevicePhotoDTO>>


    /**
     * Get Device Photo Detail
     */
    @GET("children/{kid}/terminal/{terminal}/photos/{photo}")
    fun getDevicePhotoDetail(
            @Path("kid")  kid: String,
            @Path("terminal") terminal: String,
            @Path("photo") photo: String
    ): Deferred<APIResponse<DevicePhotoDTO>>

    /**
     * Disable Device Photo
     */
    @POST("children/{kid}/terminal/{terminal}/photos/{photo}/disable")
    fun disableDevicePhoto(
            @Path("kid")  kid: String,
            @Path("terminal") terminal: String,
            @Path("photo") photo: String
    ): Deferred<APIResponse<String>>

}