package com.github.onotoliy.opposite.treasure.di.service

import androidx.work.ListenableWorker
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.di.database.AbstractRepository
import retrofit2.Response
import java.lang.Exception

abstract class AbstractService<T>(
    private val repository: AbstractRepository<T>
) {

    abstract fun sync(): ListenableWorker.Result

    protected fun syncTransactional(apply: () -> Unit): ListenableWorker.Result =
        try {
            repository.beginTransaction()
            apply()
            repository.setTransactionSuccessful()
            ListenableWorker.Result.success()
        } catch (exc: Exception) {
            ListenableWorker.Result.failure()
        } finally {
            repository.endTransaction()
        }

    protected fun syncObject(
        get: () -> Response<T>
    ): ListenableWorker.Result {
        val response = get()

        if (!response.isSuccessful) {
            return ListenableWorker.Result.failure()
        }

        repository.merge(response.body() ?: return ListenableWorker.Result.failure())

        return ListenableWorker.Result.success()
    }

    protected fun syncPage(
        offset: Int = 0,
        numberOfRows: Int = 20,
        getAll: (offset: Int, numberOfRows: Int) -> Response<Page<T>>
    ): ListenableWorker.Result {
        val response = getAll(offset, numberOfRows)

        if (!response.isSuccessful) {
            return ListenableWorker.Result.failure()
        }

        val page = response.body() ?: return ListenableWorker.Result.failure()

        page.context.forEach(repository::merge)

        return if (page.meta.total == page.meta.paging.start + page.meta.paging.size) {
            ListenableWorker.Result.success()
        } else {
            syncPage(offset + numberOfRows, numberOfRows, getAll)
        }
    }

}