package com.github.onotoliy.opposite.treasure.di.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

@Module
internal abstract class WorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(DebtWorker::class)
    internal abstract fun bindDebtWorker(factory: DebtWorker.Factory): ChildWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(EventWorker::class)
    internal abstract fun bindEventWorker(factory: EventWorker.Factory): ChildWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(TransactionWorker::class)
    internal abstract fun bindTransactionWorker(factory: TransactionWorker.Factory): ChildWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(DepositWorker::class)
    internal abstract fun bindDepositWorker(factory: DepositWorker.Factory): ChildWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(CashboxWorker::class)
    internal abstract fun bindCashboxWorker(factory: CashboxWorker.Factory): ChildWorkerFactory

    @Binds
    internal abstract fun bindWorkerFactory(factory: DaggerWorkerFactory): WorkerFactory
}

@MapKey
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class WorkerKey(val value: KClass<out CoroutineWorker>)

interface ChildWorkerFactory {
    fun create(context: Context, params: WorkerParameters): CoroutineWorker
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
