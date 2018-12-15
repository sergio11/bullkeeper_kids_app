package com.sanchez.sanchez.bullkeeper_kids.data.repository.impl

import com.sanchez.sanchez.bullkeeper_kids.data.entity.CallDetailEntity
import com.sanchez.sanchez.bullkeeper_kids.data.repository.ICallDetailRepository

/**
 * Call Repository
 */
class CallDetailRepositoryImpl: SupportRepositoryImpl<CallDetailEntity>(), ICallDetailRepository {
    override fun delete(model: CallDetailEntity) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(modelList: List<CallDetailEntity>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun list(): List<CallDetailEntity> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}