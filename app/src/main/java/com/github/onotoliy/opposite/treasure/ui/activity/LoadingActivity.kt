package com.github.onotoliy.opposite.treasure.ui.activity

import android.accounts.AccountManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
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
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.utils.getUUID
import com.github.onotoliy.opposite.treasure.utils.inject
import com.github.onotoliy.opposite.treasure.utils.navigateTo
import com.github.onotoliy.opposite.treasure.utils.observeAs
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
            TreasureTheme {
                LoadingScreen(
                    cashbox = worker.getWorkInfoByIdLiveData(cashbox.id).observeAs(),
                    debt = worker.getWorkInfoByIdLiveData(debt.id).observeAs(),
                    event = worker.getWorkInfoByIdLiveData(event.id).observeAs(),
                    deposit = worker.getWorkInfoByIdLiveData(deposit.id).observeAs(),
                    transaction = worker.getWorkInfoByIdLiveData(transaction.id).observeAs(),
                    onClick = { navigateTo(Screen.DepositScreen(account.getUUID())) }
                )
            }
        }
    }
}

@Composable
fun LoadingScreen(
    cashbox: MutableState<WorkInfo?>,
    debt: MutableState<WorkInfo?>,
    event: MutableState<WorkInfo?>,
    deposit: MutableState<WorkInfo?>,
    transaction: MutableState<WorkInfo?>,
    onClick: () -> Unit
) {
    val finished = remember(false) { mutableStateOf(false) }

    finished.value = cashbox.finished && debt.finished && event.finished && deposit.finished && transaction.finished

    Box(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.8f).fillMaxHeight(0.8f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(imageVector = vectorResource(id = R.drawable.ic_launcher_foreground))
            if (finished.value) {
                Text(text = stringResource(id = R.string.loading_finished))
            } else {
                Text(text = stringResource(id = R.string.loading_loading))
                Text(text = stringResource(id = R.string.loading_please_wait))

                WorkInfoProgress(title = stringResource(id = R.string.loading_cashbox), value = cashbox)
                WorkInfoProgress(title = stringResource(id = R.string.loading_debts), value = debt)
                WorkInfoProgress(title = stringResource(id = R.string.loading_events), value = event)
                WorkInfoProgress(title = stringResource(id = R.string.loading_deposits), value = deposit)
                WorkInfoProgress(title = stringResource(id = R.string.loading_transactions), value = transaction)
            }

            IconButton(
                modifier = Modifier.padding(vertical = 5.dp).clip(CircleShape).background(
                    if (finished.value) MaterialTheme.colors.primary else Color.Gray,
                    CircleShape
                ),
                enabled = finished.value,
                onClick = onClick
            ) {
                IconCheck()
            }
        }
    }
}

@Composable
fun WorkInfoProgress(title: String, value: MutableState<WorkInfo?>) {
    value.value?.let {
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

private val MutableState<WorkInfo?>.finished: Boolean
    get() = this.value.finished

private val WorkInfo.total: Int
    get() = this.progress.getInt("total", 0)

private val WorkInfo.offset: Int
    get() = this.progress.getInt("offset", 0)

private val WorkInfo?.finished: Boolean
    get() = this?.outputData?.getBoolean("finished", false) ?: false
