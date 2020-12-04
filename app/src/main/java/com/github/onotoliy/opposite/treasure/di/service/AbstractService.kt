package com.github.onotoliy.opposite.treasure.di.service

import androidx.sqlite.db.SupportSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQueryBuilder
import androidx.work.ListenableWorker
import com.github.onotoliy.opposite.data.page.Page
import retrofit2.Response

abstract class AbstractService<T> {

    abstract fun sync(): ListenableWorker.Result

    abstract fun replace(dto: T)

    protected  fun getAll(
        table: String,
        whereCause: String = "1 = 1",
        whereArgs: Array<String> = arrayOf(),
        limit: String? = null
    ) : SupportSQLiteQuery =
        SupportSQLiteQueryBuilder
            .builder(table)
            .selection(whereCause, whereArgs)
            .limit(limit)
            .create()

    protected fun count(
        table: String,
        whereCause: String = "1 = 1",
        whereArgs: Array<String> = arrayOf()
    ) : SupportSQLiteQuery  =
        SupportSQLiteQueryBuilder
            .builder(table)
            .selection(whereCause, whereArgs)
            .create()

    protected fun syncObject(
        get: () -> Response<T>
    ): ListenableWorker.Result {
        val response = get()

        if (!response.isSuccessful) {
            return ListenableWorker.Result.failure()
        }

        replace(response.body() ?: return ListenableWorker.Result.failure())

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

        page.context.forEach { replace(it)}

        return if (page.meta.total == page.meta.paging.start + page.meta.paging.size) {
            ListenableWorker.Result.success()
        } else {
            syncPage(offset + numberOfRows, numberOfRows, getAll)
        }
    }


}