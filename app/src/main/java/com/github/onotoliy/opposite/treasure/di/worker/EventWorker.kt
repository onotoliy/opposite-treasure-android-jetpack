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
import com.github.onotoliy.opposite.treasure.di.restful.resource.EventResource
import com.github.onotoliy.opposite.treasure.utils.progress
import com.github.onotoliy.opposite.treasure.utils.setEvent
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

    override suspend fun doWork(): Result {
        setProgress(progress(worker = this.javaClass.simpleName))

        return super.doWork()
    }

    override fun sendAllLocal(builder: Data.Builder): Boolean {
        repository.getAllLocal().forEach { vo ->
            val response = resource.saveOrUpdate(vo.toDTO())

            if (response.isSuccessful) {
                if (response.body()?.status != 200) {
                    builder.putString("uuid", vo.uuid)
                        .putString("message", response.body()?.exception ?: "")
                        .setEvent(vo)
                        .setFinished(false)

                    vo.exceptions = response.body()?.exception ?: ""

                    repository.replace(vo)

                    return false
                }
            } else {
                 builder.putString("uuid", vo.uuid)
                     .putString("message", "При выполнении синхронизации произошла ошибка")
                     .setEvent(vo)
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
