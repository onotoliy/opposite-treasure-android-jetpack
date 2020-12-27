package com.github.onotoliy.opposite.treasure.di.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.treasure.di.database.dao.EventDAO
import com.github.onotoliy.opposite.treasure.di.database.data.EventVO
import com.github.onotoliy.opposite.treasure.di.database.data.toDTO
import com.github.onotoliy.opposite.treasure.di.database.data.toVO
import com.github.onotoliy.opposite.treasure.di.database.repositories.EventRepository
import com.github.onotoliy.opposite.treasure.di.restful.resource.EventResource
import com.github.onotoliy.opposite.treasure.utils.setFinished
import javax.inject.Inject
import javax.inject.Provider

class EventWorker @Inject constructor(
    context: Context,
    params: WorkerParameters,
    repository: EventRepository,
    retrofit: EventResource
) : AbstractPageWorker<Event, EventVO, EventDAO>(context, params, repository, retrofit) {
    override fun toVO(dto: Event): EventVO = dto.toVO()

    override fun sendAllLocal(builder: Data.Builder): Boolean {
        repository.getAllLocal().forEach { vo ->
            val response = resource.saveOrUpdate(vo.toDTO())

            Log.i("EventWorker", "VO $vo")
            Log.i("EventWorker", "Code ${response.code()}")
            Log.i("EventWorker", "Message ${response.message()}")
            Log.i("EventWorker", "Error \"${String(response.errorBody()?.bytes() ?: ByteArray(0))}\"")

            if (!response.isSuccessful || response.body()?.uuid != vo.uuid) {
                builder.putString("uuid", vo.uuid)
                       .putString("message", response.message())
                       .setFinished(false)

                return false
            }
        }

        return true
    }

    class Factory @Inject constructor(
        private val repository: Provider<EventRepository>,
        private val retrofit: Provider<EventResource>
    ) : ChildWorkerFactory {
        override fun create(context: Context, params: WorkerParameters): CoroutineWorker =
            EventWorker(context, params, repository.get(), retrofit.get())
    }
}
