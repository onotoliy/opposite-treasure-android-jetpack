package com.github.onotoliy.opposite.treasure.di.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.TransactionType.CONTRIBUTION
import com.github.onotoliy.opposite.data.TransactionType.COST
import com.github.onotoliy.opposite.data.TransactionType.EARNED
import com.github.onotoliy.opposite.data.TransactionType.NONE
import com.github.onotoliy.opposite.data.TransactionType.PAID
import com.github.onotoliy.opposite.data.TransactionType.WRITE_OFF
import com.github.onotoliy.opposite.treasure.di.database.dao.TransactionDAO
import com.github.onotoliy.opposite.treasure.di.database.data.TransactionVO
import com.github.onotoliy.opposite.treasure.di.database.data.toDTO
import com.github.onotoliy.opposite.treasure.di.database.data.toVO
import com.github.onotoliy.opposite.treasure.di.database.repositories.TransactionRepository
import com.github.onotoliy.opposite.treasure.di.restful.resource.TransactionResource
import com.github.onotoliy.opposite.treasure.utils.GLOBAL
import com.github.onotoliy.opposite.treasure.utils.progress
import com.github.onotoliy.opposite.treasure.utils.setTransaction
import javax.inject.Inject
import javax.inject.Provider

class TransactionWorker @Inject constructor(
    context: Context,
    params: WorkerParameters,
    repository: TransactionRepository,
    retrofit: TransactionResource
) : AbstractPageWorker<Transaction, TransactionVO, TransactionDAO>(context, params, repository, retrofit) {

    override fun toVO(dto: Transaction): TransactionVO = dto.toVO()

    override suspend fun doWork(): Result {
        setProgress(progress(this.javaClass.simpleName))

        return super.doWork()
    }

    override fun sendAllLocal(builder: Data.Builder): Boolean {
        repository
            .getAllLocal()
            .sortedByDescending {
                when (it.type) {
                    CONTRIBUTION -> 100
                    EARNED -> 90
                    PAID -> 80
                    WRITE_OFF -> 70
                    COST -> 60
                    NONE -> 50
                }
            }
            .forEach { vo ->
                val response = resource.saveOrUpdate(vo.toDTO())

                if (response.isSuccessful) {
                    if (response.body()?.status != 200) {
                        builder.setTransaction(vo)

                        vo.exceptions = response.body()?.exception ?: ""

                        repository.replace(vo)

                        return false
                    }
                } else {
                    builder.setTransaction(vo)

                    vo.exceptions = "При выполнении синхронизации произошла ошибка"

                    repository.replace(vo)

                    return false
                }

                vo.local = GLOBAL

                repository.replace(vo)
            }

        return true
    }

    class Factory @Inject constructor(
        private val repository: Provider<TransactionRepository>,
        private val retrofit: Provider<TransactionResource>
    ) : ChildWorkerFactory {
        override fun create(context: Context, params: WorkerParameters): CoroutineWorker =
            TransactionWorker(context, params, repository.get(), retrofit.get())
    }
}
