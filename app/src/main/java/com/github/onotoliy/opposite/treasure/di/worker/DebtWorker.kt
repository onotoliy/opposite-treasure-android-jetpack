package com.github.onotoliy.opposite.treasure.di.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.onotoliy.opposite.data.Debt
import com.github.onotoliy.opposite.treasure.di.database.dao.DebtDAO
import com.github.onotoliy.opposite.treasure.di.database.data.DebtVO
import com.github.onotoliy.opposite.treasure.di.database.data.toVO
import com.github.onotoliy.opposite.treasure.di.database.repositories.DebtRepository
import com.github.onotoliy.opposite.treasure.di.resource.DebtRetrofit
import javax.inject.Inject
import javax.inject.Provider

class DebtWorker @Inject constructor(
    context: Context,
    params: WorkerParameters,
    repository: DebtRepository,
    retrofit: DebtRetrofit
) : AbstractPageWorker<Debt, DebtVO, DebtDAO>(context, params, repository, retrofit) {

    override fun toVO(dto: Debt): DebtVO = dto.toVO()

    class Factory @Inject constructor(
        private val repository: Provider<DebtRepository>,
        private val retrofit: Provider<DebtRetrofit>
    ) : ChildWorkerFactory {
        override fun create(context: Context, params: WorkerParameters): CoroutineWorker =
            DebtWorker(context, params, repository.get(), retrofit.get())
    }
}
