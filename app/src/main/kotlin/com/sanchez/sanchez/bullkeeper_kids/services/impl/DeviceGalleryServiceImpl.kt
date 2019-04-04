package com.sanchez.sanchez.bullkeeper_kids.services.impl

import android.content.Context
import android.database.Cursor
import android.net.Uri
import com.sanchez.sanchez.bullkeeper_kids.services.IDeviceGalleryService
import javax.inject.Inject
import android.provider.MediaStore
import android.provider.MediaStore.MediaColumns
import timber.log.Timber
import java.io.File
import android.media.MediaScannerConnection
import android.os.Environment
import com.sanchez.sanchez.bullkeeper_kids.data.entity.GalleryImageEntity


/**
 * Device Gallery Service
 */
class DeviceGalleryServiceImpl
    @Inject constructor(private val context: Context): IDeviceGalleryService {

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
                    id = cursor.getLong(columnIndexId),
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
     */
    override fun deleteImage(imagePath: String) {
        val fdelete = File(imagePath)
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                Timber.d("-->", "file Deleted :$imagePath")
                refreshGallery()
            } else {
                Timber.d("-->", "file not Deleted :$imagePath")
            }
        }
    }

    /**
     * Refresh Gallery
     */
    private fun refreshGallery() {
        Timber.d("-->", " >= 14")
        MediaScannerConnection.scanFile(context, arrayOf(Environment.getExternalStorageDirectory().toString()), null, { path, uri ->
            Timber.d("ExternalStorage", "Scanned $path:")
            Timber.e("ExternalStorage", "-> uri=$uri")
        })
    }
}