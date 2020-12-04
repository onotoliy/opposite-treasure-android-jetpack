package com.github.onotoliy.opposite.treasure.di.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.github.onotoliy.opposite.data.Debt
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.di.resource.DebtResource
import com.github.onotoliy.opposite.treasure.di.service.*
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

class SyncWorker @Inject constructor(
    context: Context,
    params: WorkerParameters,
    private val debt: DebtService,
    private val event: EventService,
    private val deposit: DepositService,
    private val cashbox: CashboxService,
    private val transaction: TransactionService,

    private val retrofit: DebtResource
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        Log.i("SyncWorker", "Version 0")

        Log.i("SyncWorker", "Delete all")

        syncPage { offset, numberOfRows ->
            Log.i("SyncWorker", "Start SyncPage. Offset: $offset. NumberOfRows: $numberOfRows")

            val p = retrofit
                .sync(version = 0, offset = offset, numberOfRows = numberOfRows)
                .execute()

            Log.i("SyncWorker", "End SyncPage. Offset: $offset. NumberOfRows: $numberOfRows")

            p
        }


        return Result.success()
    }

    protected fun syncPage(
        offset: Int = 0,
        numberOfRows: Int = 20,
        getAll: (offset: Int, numberOfRows: Int) -> Response<Page<Debt>>
    ): ListenableWorker.Result {
        val response = getAll(offset, numberOfRows)

        if (!response.isSuccessful) {
            return ListenableWorker.Result.failure()
        }

        Log.i("SyncWorker", "Response Body ${response.body()}")

        val page = response.body() ?: return ListenableWorker.Result.failure()

        page.context.forEach {
            Log.i("SyncWorker", "Insert $it")
            debt.replace(it)
        }

        Log.i("SyncWorker", "Meta. Total ${page.meta.total}. Start: ${page.meta.paging.start}. Size ${page.meta.paging.size}/${page.context.size}")

        return if (numberOfRows > page.context.size) {
            Log.i("SyncWorker", "Success. Meta ${page.meta}")

            ListenableWorker.Result.success()
        } else {
            Log.i("SyncWorker", "Next page. Offset: ${offset + numberOfRows}. NumberOfRows: $numberOfRows")

            syncPage(offset + numberOfRows, numberOfRows, getAll)
        }
    }

    class Factory @Inject constructor(
        private val debt: Provider<DebtService>,
        private val event: Provider<EventService>,
        private val deposit: Provider<DepositService>,
        private val cashbox: Provider<CashboxService>,
        private val transaction: Provider<TransactionService>,
        private val retrofit: Provider<DebtResource>
    ) : ChildWorkerFactory {
        override fun create(context: Context, params: WorkerParameters): CoroutineWorker {
            return SyncWorker(
                context,
                params,
                debt.get(),
                event.get(),
                deposit.get(),
                cashbox.get(),
                transaction.get(),
                retrofit.get()
            )
        }
    }
}

@MapKey
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class WorkerKey(val value: KClass<out CoroutineWorker>)

interface ChildWorkerFactory {
    fun create(context: Context, params: WorkerParameters): CoroutineWorker
}

@Module
internal abstract class WorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(SyncWorker::class)
    internal abstract fun bindSyncWorker(factory: SyncWorker.Factory): ChildWorkerFactory

    @Binds
    internal abstract fun bindWorkerFactory(factory: DaggerWorkerFactory): WorkerFactory
}

class DaggerWorkerFactory @Inject constructor(
    private val workerFactories: Map<Class<out CoroutineWorker>, @JvmSuppressWildcards Provider<ChildWorkerFactory>>
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): CoroutineWorker? {

        val foundEntry =
            workerFactories.entries.find { Class.forName(workerClassName).isAssignableFrom(it.key) }
        val factoryProvider = foundEntry?.value
            ?: throw IllegalArgumentException("unknown worker class name: $workerClassName")

        return factoryProvider.get().create(appContext, workerParameters)
    }
}
