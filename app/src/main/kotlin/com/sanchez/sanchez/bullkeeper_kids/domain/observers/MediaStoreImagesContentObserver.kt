package com.sanchez.sanchez.bullkeeper_kids.domain.observers

import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.gallery.SynchronizeGalleryInteract
import timber.log.Timber
import javax.inject.Inject

/**
 * Media Store Images Content Observer
 */
class MediaStoreImagesContentObserver
    @Inject constructor(
            handler: Handler,
            private val synchronizeGalleryInteract: SynchronizeGalleryInteract
    ): ContentObserver(handler) {

    /**
     * On Change
     */
    override fun onChange(selfChange: Boolean, uri: Uri?) {
        super.onChange(selfChange, uri)
        if (!selfChange) {
            Timber.d("MediaStoreImagesContentObserver sync gallery images")
            synchronizeGalleryInteract(UseCase.None()){
                it.either(fnL = fun(_: Failure) {
                    Timber.d("Gallery Images Sync failed")
                }, fnR = fun(total: Int){
                    Timber.d("Gallery Images sync total -> %d", total)
                })
            }
        }
    }
}