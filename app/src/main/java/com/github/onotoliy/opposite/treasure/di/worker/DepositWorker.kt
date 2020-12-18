package com.github.onotoliy.opposite.treasure.di.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.treasure.di.database.dao.DepositDAO
import com.github.onotoliy.opposite.treasure.di.database.data.DepositVO
import com.github.onotoliy.opposite.treasure.di.database.data.toVO
import com.github.onotoliy.opposite.treasure.di.database.repositories.DepositRepository
import com.github.onotoliy.opposite.treasure.di.resource.DepositRetrofit
import javax.inject.Inject
import javax.inject.Provider

class DepositWorker @Inject constructor(
    context: Context,
    params: WorkerParameters,
    repository: DepositRepository,
    retrofit: DepositRetrofit
) : AbstractPageWorker<Deposit, DepositVO, DepositDAO>(context, params, repository, retrofit) {

    override fun toVO(dto: Deposit): DepositVO = dto.toVO()

    class Factory @Inject constructor(
        private val repository: Provider<DepositRepository>,
        private val retrofit: Provider<DepositRetrofit>
    ) : ChildWorkerFactory {
        override fun create(context: Context, params: WorkerParameters): CoroutineWorker =
            DepositWorker(context, params, repository.get(), retrofit.get())
    }
}
