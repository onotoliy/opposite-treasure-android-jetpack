package com.github.onotoliy.opposite.treasure.di.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.treasure.di.database.dao.EventDAO
import com.github.onotoliy.opposite.treasure.di.database.data.EventVO
import com.github.onotoliy.opposite.treasure.di.database.data.toDTO
import com.github.onotoliy.opposite.treasure.di.database.data.toVO
import com.github.onotoliy.opposite.treasure.di.database.repositories.EventRepository
import com.github.onotoliy.opposite.treasure.di.resource.EventRetrofit
import com.github.onotoliy.opposite.treasure.utils.setFinished
import javax.inject.Inject
import javax.inject.Provider

class EventWorker @Inject constructor(
    context: Context,
    params: WorkerParameters,
    repository: EventRepository,
    retrofit: EventRetrofit
) : AbstractPageWorker<Event, EventVO, EventDAO>(context, params, repository, retrofit) {
    override fun toVO(dto: Event): EventVO = dto.toVO()

    override fun sendAllLocal(builder: Data.Builder): Boolean {
        repository.getAllLocal().forEach { vo ->
            val response = retrofit.saveOrUpdate(vo.toDTO())

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
        private val retrofit: Provider<EventRetrofit>
    ) : ChildWorkerFactory {
        override fun create(context: Context, params: WorkerParameters): CoroutineWorker =
            EventWorker(context, params, repository.get(), retrofit.get())
    }
}
