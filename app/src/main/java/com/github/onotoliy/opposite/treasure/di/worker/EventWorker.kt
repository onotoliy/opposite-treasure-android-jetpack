package com.github.onotoliy.opposite.treasure.di.worker

import android.accounts.AccountManager
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.di.database.dao.EventDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.VersionDAO
import com.github.onotoliy.opposite.treasure.di.database.data.EventVO
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
    private val account: AccountManager
) : AbstractPageWorker<Event, EventVO>(context, params, dao, version) {
    override fun toVO(dto: Event): EventVO = dto.toVO()

    override fun getVersion(): Int = 1

    override fun sync(version: Int, offset: Int, numberOfRows: Int): Call<Page<Event>> =
        retrofit.sync("Bearer " + account.getAuthToken(), version, offset, numberOfRows)

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
