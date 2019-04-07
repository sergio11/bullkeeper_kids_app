package com.sanchez.sanchez.bullkeeper_kids.services.impl

import android.content.Context
import android.database.Cursor
import android.net.Uri
import com.sanchez.sanchez.bullkeeper_kids.services.IDeviceGalleryService
import javax.inject.Inject
import android.provider.MediaStore
import android.provider.MediaStore.MediaColumns
import com.sanchez.sanchez.bullkeeper_kids.data.entity.GalleryImageEntity
import timber.log.Timber
import android.provider.MediaStore.Images
import java.io.File


/**
 * Device Gallery Service
 */
class DeviceGalleryServiceImpl
    @Inject constructor(private val context: Context): IDeviceGalleryService {


    private val TAG = "DEVICE_GALLERY_SERVICE"

    /**
     * URI
     */
    private val uri: Uri =
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    /**
     * Get Images Path
     */
    override fun getImagesPath(): List<String> {
        val projection = arrayOf(MediaColumns.DATA)
        val listOfAllImages = ArrayList<String>()
        val cursor: Cursor =
                context.contentResolver.query(uri, projection, null, null, null)
        val columnIndexData: Int =
                cursor.getColumnIndexOrThrow(MediaColumns.DATA)
        while (cursor.moveToNext()) {
            listOfAllImages.add(cursor.getString(columnIndexData))
        }
        return listOfAllImages
    }

    /**
     * Get Images
     */
    override fun getImages(): List<GalleryImageEntity> {

        val projection = arrayOf(
                MediaColumns.DATA,
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_ADDED,
                MediaStore.Images.ImageColumns.DATE_MODIFIED,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.HEIGHT,
                MediaStore.Images.ImageColumns.WIDTH,
                MediaStore.Images.ImageColumns.ORIENTATION,
                MediaStore.Images.ImageColumns.SIZE)

        val listOfAllImages = ArrayList<GalleryImageEntity>()
        val cursor: Cursor =
                context.contentResolver.query(uri, projection, null, null, null)

        val columnIndexData: Int =
                cursor.getColumnIndexOrThrow(MediaColumns.DATA)
        val columnIndexId: Int =
                cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID)
        val columnIndexDisplayName: Int =
                cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME)
        val columnIndexDateAdded: Int =
                cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_ADDED)
        val columnIndexDateModified: Int =
                cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_MODIFIED)
        val columnIndexDateTaken: Int =
                cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_TAKEN)
        val columnIndexHeight: Int =
                cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.HEIGHT)
        val columnIndexWidth: Int =
                cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.WIDTH)
        val columnIndexOrientation: Int =
                cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION)
        val columnIndexSize: Int =
                cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.SIZE)

        while (cursor.moveToNext()) {
            listOfAllImages.add(GalleryImageEntity(
                    id = cursor.getString(columnIndexId),
                    path = cursor.getString(columnIndexData),
                    displayName = cursor.getString(columnIndexDisplayName),
                    dateAdded = cursor.getLong(columnIndexDateAdded),
                    dateModified = cursor.getLong(columnIndexDateModified),
                    dateTaken = cursor.getLong(columnIndexDateTaken),
                    height = cursor.getInt(columnIndexHeight),
                    width = cursor.getInt(columnIndexWidth),
                    orientation = cursor.getInt(columnIndexOrientation),
                    size = cursor.getInt(columnIndexSize)
            ))
        }
        return listOfAllImages
    }

    /**
     * Delete Image
     * @param imageFile
     */
    override fun deleteImage(imageFile: String) {
        Timber.d("%s Delete Image -> %s", TAG, imageFile)
        // images
        var count = 0
        var imageCursor: Cursor? = null
        try {
            val select = Images.Media.DATA + "=?"
            val selectArgs = arrayOf(imageFile)

            val projection = arrayOf(Images.ImageColumns._ID)
            imageCursor = context.contentResolver
                    .query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            projection, select, selectArgs, null)
            if (imageCursor!!.count > 0) {
                imageCursor.moveToFirst()
                val imagesToDelete = ArrayList<Uri>()
                do {
                    val id = imageCursor.getString(imageCursor
                            .getColumnIndex(Images.ImageColumns._ID))

                    imagesToDelete
                            .add(Uri.withAppendedPath(
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    id))
                } while (imageCursor.moveToNext())

                for (uri in imagesToDelete) {
                    Timber.d("%s attempting to delete: %s", TAG, uri.toString())
                    count += context.contentResolver
                            .delete(uri, null, null)
                }
            }
        } catch (e: Exception) {
            Timber.d(e, "%s Unable to delete image file from media provider", TAG)
        } finally {
            imageCursor?.close()
        }
        val f = File(imageFile)
        if (f.exists()) {
            Timber.d("%s File Exists Deleted", TAG)
            f.delete()
        }
        Timber.d("%s %d Files deleted for %s", TAG, count, imageFile)

    }

    /**
     * Delete Image
     * @param imageFileList
     */
    override fun deleteImage(imageFileList: List<String>) {
        imageFileList.forEach { deleteImage(it) }
    }

}