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
import androidx.compose.runtime.emptyContent
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
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
import com.github.onotoliy.opposite.treasure.ui.views.EventItemView
import com.github.onotoliy.opposite.treasure.ui.views.TransactionItemView
import com.github.onotoliy.opposite.treasure.utils.defaultWorkInfo
import com.github.onotoliy.opposite.treasure.utils.event
import com.github.onotoliy.opposite.treasure.utils.failed
import com.github.onotoliy.opposite.treasure.utils.finished
import com.github.onotoliy.opposite.treasure.utils.getUUID
import com.github.onotoliy.opposite.treasure.utils.indicator
import com.github.onotoliy.opposite.treasure.utils.inject
import com.github.onotoliy.opposite.treasure.utils.mutableStateOf
import com.github.onotoliy.opposite.treasure.utils.worker
import com.github.onotoliy.opposite.treasure.utils.navigateTo
import com.github.onotoliy.opposite.treasure.utils.transaction
import javax.inject.Inject

class LoadingActivity : AppCompatActivity() {

    @Inject
    lateinit var worker: WorkManager
    @Inject
    lateinit var account: AccountManager

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
                    works = listOf(sDebt.value, sEvent.value, sDeposit.value, sCashbox.value, sTransaction.value),
                    navigateTo = ::navigateTo
                )
            }
        }
    }
}

@Composable
fun LoadingScreen(
    works: List<WorkInfo>,
    navigateTo: (Screen) -> Unit
) {
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

            Spacer(Modifier.padding(10.dp))

            LinearProgressIndicators(works)

            if (works.all { it.state == WorkInfo.State.SUCCEEDED }) {
                IconButton(
                    modifier = Modifier.border(1.dp, Color.LightGray, CircleShape),
                    onClick = { navigateTo(Screen.DepositScreen()) }
                ) {
                    IconCheck(tint = Color.Green)
                }
            }

            works
                .filter { it.state == WorkInfo.State.FAILED }
                .firstOrNull { it.event.uuid.isNotEmpty() || it.transaction.uuid.isNotEmpty() }
                ?.let {
                    if (it.event.uuid.isNotEmpty()) {
                        EventItemView(it.event, navigateTo)
                    }
                    if (it.transaction.uuid.isNotEmpty()) {
                        TransactionItemView(it.transaction, navigateTo)
                    }
                }
        }
    }
}

@Composable
fun LinearProgressIndicators(works: List<WorkInfo>) {
    works.findLast { it.state == WorkInfo.State.RUNNING }?.let { info ->
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(
                    when (info.worker) {
                        "DebtWorker" -> R.string.loading_debts
                        "CashboxWorker" -> R.string.loading_cashbox
                        "EventWorker" -> R.string.loading_events
                        "DepositWorker" -> R.string.loading_deposits
                        "TransactionWorker" -> R.string.loading_transactions
                        else -> R.string.loading_loading
                    }
                )
            )

            LinearProgressIndicator(
                progress = (works.count { it.finished || it.failed } * 0.2).toFloat()
            )

            Spacer(Modifier.padding(5.dp))

            LinearProgressIndicator(progress = info.indicator)
        }
    }
}