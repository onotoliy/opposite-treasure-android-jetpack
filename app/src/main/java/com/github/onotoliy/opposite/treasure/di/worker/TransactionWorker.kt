package com.github.onotoliy.opposite.treasure.di.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.TransactionType.*
import com.github.onotoliy.opposite.treasure.di.database.dao.TransactionDAO
import com.github.onotoliy.opposite.treasure.di.database.data.TransactionVO
import com.github.onotoliy.opposite.treasure.di.database.data.toDTO
import com.github.onotoliy.opposite.treasure.di.database.data.toVO
import com.github.onotoliy.opposite.treasure.di.database.repositories.TransactionRepository
import com.github.onotoliy.opposite.treasure.di.resource.TransactionRetrofit
import com.github.onotoliy.opposite.treasure.utils.setFinished
import javax.inject.Inject
import javax.inject.Provider

class TransactionWorker @Inject constructor(
    context: Context,
    params: WorkerParameters,
    repository: TransactionRepository,
    retrofit: TransactionRetrofit
) : AbstractPageWorker<Transaction, TransactionVO, TransactionDAO>(context, params, repository, retrofit) {

    override fun toVO(dto: Transaction): TransactionVO = dto.toVO()

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
                val response = retrofit.saveOrUpdate(vo.toDTO())

                if (response.isSuccessful && response.body()?.uuid == vo.uuid) {
                    Log.i("TransactionWorker", "Success upload transaction: $vo")
                } else {
                    builder.putString("uuid", vo.uuid)
                        .putString("message", response.message())
                        .setFinished(false)

                    return false
                }
            }

        return true
    }

    class Factory @Inject constructor(
        private val repository: Provider<TransactionRepository>,
        private val retrofit: Provider<TransactionRetrofit>
    ) : ChildWorkerFactory {
        override fun create(context: Context, params: WorkerParameters): CoroutineWorker =
            TransactionWorker(context, params, repository.get(), retrofit.get())
    }
}
