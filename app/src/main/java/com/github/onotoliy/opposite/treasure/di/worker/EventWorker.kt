package com.github.onotoliy.opposite.treasure.di.worker

import android.accounts.AccountManager
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.di.database.dao.EventDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.VersionDAO
import com.github.onotoliy.opposite.treasure.di.database.data.EventVO
import com.github.onotoliy.opposite.treasure.di.database.data.toDTO
import com.github.onotoliy.opposite.treasure.di.database.data.toVO
import com.github.onotoliy.opposite.treasure.di.resource.EventResource
import com.github.onotoliy.opposite.treasure.utils.getAuthToken
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Provider

class EventWorker @Inject constructor(
    context: Context,
    params: WorkerParameters,
    dao: EventDAO,
    version: VersionDAO,
    private val retrofit: EventResource,
    account: AccountManager
) : AbstractPageWorker<Event, EventVO>(context, params, "event", version, dao, account) {
    override fun toVO(dto: Event): EventVO = dto.toVO()

    override fun getRemoteVersion(): String = retrofit.version(account.getAuthToken()).name

    override fun getAll(token: String, version: Int, offset: Int, numberOfRows: Int): Call<Page<Event>> =
        retrofit.sync(token, version, offset, numberOfRows)

    override suspend fun doWork(): Result {
        dao.getAllLocal().forEach { vo ->
            val response = if (vo.updated == 1) {
                retrofit.put(account.getAuthToken(), vo.toDTO()).execute()
            } else {
                retrofit.post(account.getAuthToken(), vo.toDTO()).execute()
            }

            if (response.isSuccessful && response.body()?.uuid == vo.uuid) {
                Log.i("EventWorker", "Success upload event")
            } else {
                return Result.failure(
                    Data
                        .Builder()
                        .putString("uuid", vo.uuid)
                        .putBoolean("finished", true)
                        .putBoolean("success", false)
                        .build()
                )
            }
        }

        return super.doWork()
    }

    class Factory @Inject constructor(
        private val dao: Provider<EventDAO>,
        private val version: Provider<VersionDAO>,
        private val retrofit: Provider<EventResource>,
        private val account: Provider<AccountManager>
    ) : ChildWorkerFactory {
        override fun create(context: Context, params: WorkerParameters): CoroutineWorker {
            return EventWorker(
                context,
                params,
                dao.get(),
                version.get(),
                retrofit.get(),
                account.get()
            )
        }
    }
}
