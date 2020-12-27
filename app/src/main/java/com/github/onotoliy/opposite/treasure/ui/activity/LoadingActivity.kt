package com.github.onotoliy.opposite.treasure.ui.activity

import android.accounts.AccountManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.di.worker.CashboxWorker
import com.github.onotoliy.opposite.treasure.di.worker.DebtWorker
import com.github.onotoliy.opposite.treasure.di.worker.DepositWorker
import com.github.onotoliy.opposite.treasure.di.worker.EventWorker
import com.github.onotoliy.opposite.treasure.di.worker.TransactionWorker
import com.github.onotoliy.opposite.treasure.ui.IconCheck
import com.github.onotoliy.opposite.treasure.ui.IconClose
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.utils.defaultWorkInfo
import com.github.onotoliy.opposite.treasure.utils.getUUID
import com.github.onotoliy.opposite.treasure.utils.inject
import com.github.onotoliy.opposite.treasure.utils.mutableStateOf
import com.github.onotoliy.opposite.treasure.utils.navigateTo
import javax.inject.Inject

class LoadingActivity : AppCompatActivity() {

    @Inject lateinit var worker: WorkManager
    @Inject lateinit var account: AccountManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()

        val debt: OneTimeWorkRequest = OneTimeWorkRequestBuilder<DebtWorker>().build()
        val event: OneTimeWorkRequest = OneTimeWorkRequestBuilder<EventWorker>().build()
        val deposit: OneTimeWorkRequest = OneTimeWorkRequestBuilder<DepositWorker>().build()
        val cashbox: OneTimeWorkRequest = OneTimeWorkRequestBuilder<CashboxWorker>().build()
        val transaction: OneTimeWorkRequest = OneTimeWorkRequestBuilder<TransactionWorker>().build()

        worker
            .beginWith(cashbox)
            .then(event)
            .then(deposit)
            .then(transaction)
            .then(debt)
            .enqueue()

        setContent {
            val sDebt = mutableStateOf(defaultWorkInfo) {
                worker.getWorkInfoByIdLiveData(cashbox.id)
            }
            val sEvent = mutableStateOf(defaultWorkInfo) {
                worker.getWorkInfoByIdLiveData(event.id)
            }
            val sDeposit = mutableStateOf(defaultWorkInfo) {
                worker.getWorkInfoByIdLiveData(deposit.id)
            }
            val sCashbox = mutableStateOf(defaultWorkInfo) {
                worker.getWorkInfoByIdLiveData(transaction.id)
            }
            val sTransaction = mutableStateOf(defaultWorkInfo) {
                worker.getWorkInfoByIdLiveData(debt.id)
            }

            TreasureTheme {
                LoadingScreen(
                    cashbox = sCashbox,
                    debt = sDebt,
                    event = sEvent,
                    deposit = sDeposit,
                    transaction = sTransaction,
                    onClick = { navigateTo(Screen.DepositScreen(account.getUUID())) }
                )
            }
        }
    }
}

@Composable
fun LoadingScreen(
    cashbox: MutableState<WorkInfo>,
    debt: MutableState<WorkInfo>,
    event: MutableState<WorkInfo>,
    deposit: MutableState<WorkInfo>,
    transaction: MutableState<WorkInfo>,
    onClick: () -> Unit
) {
    val failed = remember(false) { mutableStateOf(false) }
    val success = remember(false) { mutableStateOf(false) }
    failed.value = cashbox.failed || debt.failed || event.failed || deposit.failed || transaction.failed
    success.value = cashbox.finished && debt.finished && event.finished && deposit.finished && transaction.finished

    Box(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.8f).fillMaxHeight(0.8f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                bitmap = imageResource(id = R.mipmap.ic_launcher),
                contentScale = ContentScale.None
            )

            if (success.value || failed.value) {
                if (success.value) {
                    Text(text = stringResource(id = R.string.loading_finished))
                } else {
                    Text(text = "Ошибка")
                }
            } else {
                Text(text = stringResource(id = R.string.loading_loading))
                Text(text = stringResource(id = R.string.loading_please_wait))

                WorkInfoProgress(title = stringResource(id = R.string.loading_cashbox), value = cashbox)
                WorkInfoProgress(title = stringResource(id = R.string.loading_debts), value = debt)
                WorkInfoProgress(title = stringResource(id = R.string.loading_events), value = event)
                WorkInfoProgress(title = stringResource(id = R.string.loading_deposits), value = deposit)
                WorkInfoProgress(title = stringResource(id = R.string.loading_transactions), value = transaction)
            }

            Spacer(Modifier.padding(5.dp))

            if (success.value || failed.value) {
                IconButton(
                    modifier = Modifier.border(1.dp, Color.LightGray, CircleShape),
                    onClick = onClick
                ) {
                    if (success.value) {
                        IconCheck(tint = Color.Green)
                    } else {
                        IconClose(tint = Color.Red)
                    }
                }
            }
        }
    }
}

@Composable
fun WorkInfoProgress(title: String, value: MutableState<WorkInfo>) {
    value.value.let {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title)
            LinearProgressIndicator(progress = it.indicator)
        }
    }
}

private val WorkInfo.indicator: Float
    get() =
        if (finished) {
            1f
        } else {
            if (offset == 0 || total == 0) 0f else offset.toFloat() / total.toFloat()
        }

private val MutableState<WorkInfo>.finished: Boolean
    get() = this.value.finished


private val MutableState<WorkInfo>.failed: Boolean
    get() = this.value.failed

private val WorkInfo.total: Int
    get() = this.progress.getInt("total", 0)

private val WorkInfo.offset: Int
    get() = this.progress.getInt("offset", 0)

private val WorkInfo?.failed: Boolean
    get() = this?.state == WorkInfo.State.FAILED

private val WorkInfo?.finished: Boolean
    get() = this?.outputData?.getBoolean("finished", false) ?: false
