package com.github.onotoliy.opposite.treasure.di.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.onotoliy.opposite.data.Debt
import com.github.onotoliy.opposite.treasure.di.database.dao.DebtDAO
import com.github.onotoliy.opposite.treasure.di.database.data.DebtVO
import com.github.onotoliy.opposite.treasure.di.database.data.toVO
import com.github.onotoliy.opposite.treasure.di.database.repositories.DebtRepository
import com.github.onotoliy.opposite.treasure.di.restful.resource.DebtResource
import com.github.onotoliy.opposite.treasure.utils.progress
import javax.inject.Inject
import javax.inject.Provider

class DebtWorker @Inject constructor(
    context: Context,
    params: WorkerParameters,
    repository: DebtRepository,
    retrofit: DebtResource
) : AbstractPageWorker<Debt, DebtVO, DebtDAO>(context, params, repository, retrofit) {

    override fun toVO(dto: Debt): DebtVO = dto.toVO()

    override suspend fun doWork(): Result {
        setProgress(progress(this.javaClass.simpleName))

        return super.doWork()
    }

    class Factory @Inject constructor(
        private val repository: Provider<DebtRepository>,
        private val retrofit: Provider<DebtResource>
    ) : ChildWorkerFactory {
        override fun create(context: Context, params: WorkerParameters): CoroutineWorker =
            DebtWorker(context, params, repository.get(), retrofit.get())
    }
}
