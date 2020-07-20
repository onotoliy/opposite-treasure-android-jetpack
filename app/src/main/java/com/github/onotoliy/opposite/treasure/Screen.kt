package com.github.onotoliy.opposite.treasure

import android.accounts.AccountManager
import androidx.compose.Composable
import androidx.lifecycle.MutableLiveData
import androidx.ui.foundation.ScrollerPosition
import com.github.onotoliy.opposite.data.Cashbox
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.auth.*
import com.github.onotoliy.opposite.treasure.observe
import com.github.onotoliy.opposite.treasure.scrollerPosition
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

sealed class Screen {
    abstract fun loading(manager: AccountManager)

    object LoginScreen : Screen() {
        override fun loading(manager: AccountManager) { }
    }

    object HomeScreen : Screen() {
        private val _cashbox: MutableLiveData<Cashbox> = MutableLiveData()
        private val _deposit: MutableLiveData<Deposit> = MutableLiveData()

        @Composable
        val deposit: Deposit?
            get() = observe(data = _deposit)

        @Composable
        val cashbox: Cashbox?
            get() = observe(data = _cashbox)

        override fun loading(manager: AccountManager) {
            manager.cashbox.get().enqueue(object : Callback<Cashbox> {
                override fun onFailure(call: Call<Cashbox>, t: Throwable) { }

                override fun onResponse(call: Call<Cashbox>, response: Response<Cashbox>) =
                    _cashbox.postValue(response.body())
            })
            manager.deposits.get(manager.getUUID()).enqueue(object : Callback<Deposit> {
                override fun onFailure(call: Call<Deposit>, t: Throwable) { }
                override fun onResponse(call: Call<Deposit>, response: Response<Deposit>) =
                    _deposit.postValue(response.body())
            })
        }
    }

    data class DepositPageScreen(
        private val offset: Int = 0,
        private val numberOfRows: Int = 5,
        private val default: Page<Deposit>? = null
    ) : Screen() {
        private val _page: MutableLiveData<Page<Deposit>> =
            MutableLiveData(default ?: Page())

        @Composable
        val page: Page<Deposit>?
            get() = observe(data = _page)

        @Composable
        val scrollerPosition: ScrollerPosition
            get() = default.scrollerPosition

        override fun loading(manager: AccountManager) {
            manager.deposits.getAll(offset, numberOfRows).enqueue(object : Callback<Page<Deposit>> {
                override fun onFailure(call: Call<Page<Deposit>>, t: Throwable) {
                }

                override fun onResponse(
                    call: Call<Page<Deposit>>,
                    response: Response<Page<Deposit>>
                ) {
                    response.body()?.let {
                        val list = mutableListOf<Deposit>()
                        list.addAll(default?.context ?: listOf())
                        list.addAll(it.context)

                        _page.postValue(Page(it.meta, list))
                    }
                }
            })
        }
    }

    data class EventPageScreen(
        private val offset: Int = 0,
        private val numberOfRows: Int = 5,
        private val default: Page<Event>? = null
    ) : Screen() {
        private val _page: MutableLiveData<Page<Event>> =
            MutableLiveData(default ?: Page())

        @Composable
        val page: Page<Event>?
            get() = observe(data = _page)

        @Composable
        val scrollerPosition: ScrollerPosition
            get() = default.scrollerPosition

        override fun loading(manager: AccountManager) {
            manager.events.getAll(offset, numberOfRows).enqueue(object : Callback<Page<Event>> {
                override fun onFailure(call: Call<Page<Event>>, t: Throwable) {
                }

                override fun onResponse(
                    call: Call<Page<Event>>,
                    response: Response<Page<Event>>
                ) {
                    response.body()?.let {
                        val list = mutableListOf<Event>()
                        list.addAll(default?.context ?: listOf())
                        list.addAll(it.context)

                        _page.postValue(Page(it.meta, list))
                    }
                }
            })
        }
    }

    data class TransactionPageScreen(
        private val offset: Int = 0,
        private val numberOfRows: Int = 5,
        private val default: Page<Transaction>? = null
    ) : Screen() {
        private val _page: MutableLiveData<Page<Transaction>> =
            MutableLiveData(default ?: Page())

        @Composable
        val page: Page<Transaction>?
            get() = observe(data = _page)

        @Composable
        val scrollerPosition: ScrollerPosition
            get() = default.scrollerPosition

        override fun loading(manager: AccountManager) {
            manager.transactions.getAll(offset, numberOfRows).enqueue(object : Callback<Page<Transaction>> {
                override fun onFailure(call: Call<Page<Transaction>>, t: Throwable) {
                }

                override fun onResponse(
                    call: Call<Page<Transaction>>,
                    response: Response<Page<Transaction>>
                ) {
                    response.body()?.let {
                        val list = mutableListOf<Transaction>()
                        list.addAll(default?.context ?: listOf())
                        list.addAll(it.context)

                        _page.postValue(Page(it.meta, list))
                    }
                }
            })
        }
    }

    data class EventScreen(private val pk: String) : Screen() {
        private val _event: MutableLiveData<Event> = MutableLiveData()

        @Composable
        val event: Event?
            get() = observe(data = _event)

        override fun loading(manager: AccountManager) {
            manager.events.get(pk).enqueue(object : Callback<Event> {
                override fun onFailure(call: Call<Event>, t: Throwable) { }

                override fun onResponse(call: Call<Event>, response: Response<Event>) =
                    _event.postValue(response.body())
            })
        }
    }

    data class DepositScreen(private val pk: String) : Screen() {
        private val _cashbox: MutableLiveData<Cashbox> = MutableLiveData()
        private val _deposit: MutableLiveData<Deposit> = MutableLiveData()

        @Composable
        val deposit: Deposit?
            get() = observe(data = _deposit)

        @Composable
        val cashbox: Cashbox?
            get() = observe(data = _cashbox)

        override fun loading(manager: AccountManager) {
            manager.cashbox.get().enqueue(object : Callback<Cashbox> {
                override fun onFailure(call: Call<Cashbox>, t: Throwable) { }

                override fun onResponse(call: Call<Cashbox>, response: Response<Cashbox>) =
                    _cashbox.postValue(response.body())
            })
            manager.deposits.get(pk).enqueue(object : Callback<Deposit> {
                override fun onFailure(call: Call<Deposit>, t: Throwable) { }
                override fun onResponse(call: Call<Deposit>, response: Response<Deposit>) =
                    _deposit.postValue(response.body())
            })
        }
    }

    data class TransactionScreen(private val pk: String) : Screen() {
        private val _transaction: MutableLiveData<Transaction> = MutableLiveData()

        @Composable
        val transaction: Transaction?
            get() = observe(data = _transaction)

        override fun loading(manager: AccountManager) {
            manager.transactions.get(pk).enqueue(object : Callback<Transaction> {
                override fun onFailure(call: Call<Transaction>, t: Throwable) { }

                override fun onResponse(call: Call<Transaction>, response: Response<Transaction>) =
                    _transaction.postValue(response.body())
            })
        }
    }
}
