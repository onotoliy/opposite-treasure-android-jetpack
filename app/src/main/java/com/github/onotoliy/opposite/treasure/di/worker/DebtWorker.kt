package com.github.onotoliy.opposite.treasure.di.worker

import android.accounts.AccountManager
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.github.onotoliy.opposite.data.Debt
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.di.database.*
import com.github.onotoliy.opposite.treasure.di.resource.*
import com.github.onotoliy.opposite.treasure.di.service.toVO
import com.github.onotoliy.opposite.treasure.utils.getAuthToken
import retrofit2.Call
import retrofit2.Response
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Provider

class DebtWorker @Inject constructor(
    context: Context,
    params: WorkerParameters,
    dao: DebtDAO,
    version: VersionDAO,
    private val retrofit: DebtResource,
    private val account: AccountManager
) : AbstractPageWorker<Debt, DebtVO>(context, params, "Долги", dao, version) {
    override fun toVO(dto: Debt): DebtVO = dto.toVO()

    override fun getVersion(): Int = 1

    override fun sync(version: Int, offset: Int, numberOfRows: Int): Call<Page<Debt>> =
        retrofit.sync("Bearer "+ account.getAuthToken(), version, offset, numberOfRows)

    class Factory @Inject constructor(
        private val dao: Provider<DebtDAO>,
        private val version: Provider<VersionDAO>,
        private val retrofit: Provider<DebtResource>,
        private val account: Provider<AccountManager>
    ) : ChildWorkerFactory {
        override fun create(context: Context, params: WorkerParameters): CoroutineWorker {
            return DebtWorker(
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

class EventWorker @Inject constructor(
    context: Context,
    params: WorkerParameters,
    dao: EventDAO,
    version: VersionDAO,
    private val retrofit: EventResource,
    private val account: AccountManager
) : AbstractPageWorker<Event, EventVO>(context, params, "События", dao, version) {
    override fun toVO(dto: Event): EventVO = dto.toVO()

    override fun getVersion(): Int = 1

    override fun sync(version: Int, offset: Int, numberOfRows: Int): Call<Page<Event>> =
        retrofit.sync("Bearer "+ account.getAuthToken(), version, offset, numberOfRows)

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

class TransactionWorker @Inject constructor(
    context: Context,
    params: WorkerParameters,
    dao: TransactionDAO,
    version: VersionDAO,
    private val retrofit: TransactionResource,
    private val account: AccountManager
) : AbstractPageWorker<Transaction, TransactionVO>(context, params, "Операции", dao, version) {
    override fun toVO(dto: Transaction): TransactionVO = dto.toVO()

    override fun getVersion(): Int = 1

    override fun sync(version: Int, offset: Int, numberOfRows: Int): Call<Page<Transaction>> =
        retrofit.sync("Bearer "+ account.getAuthToken(), version, offset, numberOfRows)

    class Factory @Inject constructor(
        private val dao: Provider<TransactionDAO>,
        private val version: Provider<VersionDAO>,
        private val retrofit: Provider<TransactionResource>,
        private val account: Provider<AccountManager>
    ) : ChildWorkerFactory {
        override fun create(context: Context, params: WorkerParameters): CoroutineWorker {
            return TransactionWorker(
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

class DepositWorker @Inject constructor(
    context: Context,
    params: WorkerParameters,
    dao: DepositDAO,
    version: VersionDAO,
    private val retrofit: DepositResource,
    private val account: AccountManager
) : AbstractPageWorker<Deposit, DepositVO>(context, params, "Депозиты", dao, version) {
    override fun toVO(dto: Deposit): DepositVO = dto.toVO()

    override fun getVersion(): Int = 1

    override fun sync(version: Int, offset: Int, numberOfRows: Int): Call<Page<Deposit>> =
        retrofit.sync("Bearer "+ account.getAuthToken(), version, offset, numberOfRows)

    class Factory @Inject constructor(
        private val dao: Provider<DepositDAO>,
        private val version: Provider<VersionDAO>,
        private val retrofit: Provider<DepositResource>,
        private val account: Provider<AccountManager>
    ) : ChildWorkerFactory {
        override fun create(context: Context, params: WorkerParameters): CoroutineWorker {
            return DepositWorker(
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

abstract class AbstractPageWorker<D, E> constructor(
    context: Context,
    params: WorkerParameters,
    private val name: String,
    private val dao: Replace<E>,
    private val version: VersionDAO
) : CoroutineWorker(context, params) {

    protected abstract fun toVO(dto: D): E
    protected abstract fun getVersion(): Int
    protected abstract fun sync(version: Int, offset: Int, numberOfRows: Int): Call<Page<D>>

    override suspend fun doWork(): Result {
        val localVersion = VersionVO("0", "0").version
        val remoteVersion = getVersion().toString()

        val builder: Data.Builder = Data.Builder()
            .putString("local-version", localVersion)
            .putString("local-remote", remoteVersion)

        if (localVersion == remoteVersion) {
            return Result.success(
                builder
                    .putBoolean("success", true)
                    .putString("message", "Обновлений по долгам нет.")
                    .build()
            )
        }

        return if (syncPage(builder, localVersion.toInt())) {
            Result.success(builder.putBoolean("success", true).putBoolean("finished" , true).build())
        } else {
            Result.failure(builder.putBoolean("success", false).putBoolean("finished" , true).build())
        }
    }

    private suspend fun syncPage(
        builder: Data.Builder,
        version: Int,
        offset: Int = 0,
        numberOfRows: Int = 20
    ): Boolean {
        val response: Response<Page<D>>

        try {
            response = sync(version, offset, numberOfRows).execute()
        } catch (exc: SocketTimeoutException) {
            return syncPage(builder, version, offset, numberOfRows)
        }

        val page = response.body()

        if (!response.isSuccessful || page == null) {
            if (response.code() == 504) {
                return syncPage(builder, version, offset, numberOfRows)
            }

            builder.putString(
                "message",
                "Ошибка получения обновления ${response.code()}:${String(response.errorBody()?.bytes() ?: ByteArray(0))}"
            )

            return false
        }

        Log.i("AbstractPageWorker", "Response is ok $offset, $numberOfRows ${page.meta.total}. ${this.javaClass.simpleName}")

        setProgress(Data.Builder()
            .putInt("total", page.meta.total)
            .putInt("offset", offset + numberOfRows)
            .putString("name", name)
            .build()
        )

        page.context.forEach {
            dao.replace(toVO(it))
        }

        return if (offset > page.meta.total) {
            builder.putString("message", "Данные успешно загружены (${page.meta.total})")

            dao.getAll().forEach {
                Log.i("AbstractPageWorker", it.toString())
            }

            return true
        } else {
            syncPage(builder, version, offset + numberOfRows, numberOfRows)
        }
    }
}