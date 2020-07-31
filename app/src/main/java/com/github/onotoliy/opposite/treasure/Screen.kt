package com.github.onotoliy.opposite.treasure

import android.accounts.AccountManager
import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.Cashbox
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.treasure.auth.*
import com.github.onotoliy.opposite.treasure.resources.*
import com.github.onotoliy.opposite.treasure.ui.screens.DepositTab
import com.github.onotoliy.opposite.treasure.ui.screens.EventTab

sealed class Screen {
    abstract fun loading(manager: AccountManager)

    val pending: MutableLiveData<Boolean> = MutableLiveData(true)

    object LoginScreen : Screen() {
        override fun loading(manager: AccountManager) {}
    }

    data class DepositPageScreen(
        private val default: PageView<Deposit> = PageView()
    ) : Screen() {

        val page: MutableLiveData<PageView<Deposit>> = MutableLiveData(default)

        override fun loading(manager: AccountManager) {
            manager
                .deposits
                .getAll(default.offset, default.numberOfRows)
                .enqueue(DepositPageCallback(default.default) {
                    page.postValue(default.copy(context = it))
                    pending.postValue(false)
                })
        }
    }

    data class EventPageScreen(
        private val default: PageView<Event> = PageView()
    ) : Screen() {

        val page: MutableLiveData<PageView<Event>> = MutableLiveData(default)

        override fun loading(manager: AccountManager) {
            manager
                .events
                .getAll(offset = default.offset, numberOfRows = default.numberOfRows)
                .enqueue(
                    EventPageCallback(default.default) {
                        page.postValue(default.copy(context = it))
                        pending.postValue(false)
                    }
                )
        }
    }

    data class TransactionPageScreen(
        private val default: PageView<Transaction> = PageView()
    ) : Screen() {

        val page: MutableLiveData<PageView<Transaction>> = MutableLiveData(default)

        override fun loading(manager: AccountManager) {
            manager
                .transactions
                .getAll(offset = default.offset, numberOfRows = default.numberOfRows)
                .enqueue(TransactionPageCallback(default.default) {
                    page.postValue(default.copy(context = it))
                    pending.postValue(false)
                })
        }
    }

    data class EventScreen(
        private val pk: String,
        private val dp: PageView<Deposit> = PageView(),
        private val tp: PageView<Transaction> = PageView(),
        val tab: EventTab = EventTab.GENERAL
    ) : Screen() {

        val event: MutableLiveData<Event> = MutableLiveData()
        val debtors: MutableLiveData<PageView<Deposit>> = MutableLiveData(dp)
        val transactions: MutableLiveData<PageView<Transaction>> = MutableLiveData(tp)

        override fun loading(manager: AccountManager) {
            val loading = mutableMapOf(
                "event" to true,
                "debtors" to true,
                "transactions" to true
            )

            manager.events.get(pk).enqueue(EventCallback {
                event.postValue(it)
                loading["event"] = false
                pending.postValue(loading.any(Map.Entry<String, Boolean>::value))
            })
            manager.transactions
                .getAll(event = pk, offset = tp.offset, numberOfRows = tp.numberOfRows)
                .enqueue(TransactionPageCallback(tp.default) {
                    transactions.postValue(
                        PageView(
                            offset = tp.offset,
                            numberOfRows = tp.numberOfRows,
                            context = it,
                            default = tp.default
                        )
                    )
                    loading["debtors"] = false
                    loading["transactions"] = false
                    pending.postValue(loading.any(Map.Entry<String, Boolean>::value))
                })
        }
    }

    data class TransactionScreen(private val pk: String) : Screen() {

        val transaction: MutableLiveData<Transaction> = MutableLiveData()

        override fun loading(manager: AccountManager) {
            manager.transactions.get(pk).enqueue(TransactionCallback {
                transaction.postValue(it)
                pending.postValue(false)
            })
        }
    }

    data class DepositScreen(
        private val pk: String? = null,
        private val ep: PageView<Event> = PageView(),
        private val tp: PageView<Transaction> = PageView(),
        var tab: DepositTab = DepositTab.GENERAL
    ) : Screen() {
        val cashbox: MutableLiveData<Cashbox> = MutableLiveData()
        val deposit: MutableLiveData<Deposit> = MutableLiveData()
        val events: MutableLiveData<PageView<Event>> = MutableLiveData(ep)
        val transactions: MutableLiveData<PageView<Transaction>> = MutableLiveData(tp)

        override fun loading(manager: AccountManager) {
            val loading = mutableMapOf(
                "cashbox" to true,
                "deposit" to true,
                "events" to true,
                "transactions" to true
            )

            manager.cashbox.get().enqueue(CashboxCallback {
                cashbox.postValue(it)
                loading["cashbox"] = false
                pending.postValue(loading.any(Map.Entry<String, Boolean>::value))
            })
            manager.deposits.get(pk ?: manager.getUUID()).enqueue(DepositCallback {
                deposit.postValue(it)
                loading["deposit"] = false
                pending.postValue(loading.any(Map.Entry<String, Boolean>::value))
            })
            manager.debts.getAll(pk ?: manager.getUUID()).enqueue(EventPageCallback(ep.default) {
                events.postValue(
                    PageView(
                        offset = ep.offset,
                        numberOfRows = ep.numberOfRows,
                        context = it,
                        default = ep.default
                    )
                )
                loading["events"] = false
                pending.postValue(loading.any(Map.Entry<String, Boolean>::value))
            })
            manager
                .transactions
                .getAll(user = pk ?: manager.getUUID(), offset = tp.offset, numberOfRows = tp.numberOfRows)
                .enqueue(TransactionPageCallback(tp.default) {
                    transactions.postValue(
                        PageView(
                            offset = tp.offset,
                            numberOfRows = tp.numberOfRows,
                            context = it,
                            default = tp.default
                        )
                    )
                    loading["transactions"] = false
                    pending.postValue(loading.any(Map.Entry<String, Boolean>::value))
                })
        }
    }
}
