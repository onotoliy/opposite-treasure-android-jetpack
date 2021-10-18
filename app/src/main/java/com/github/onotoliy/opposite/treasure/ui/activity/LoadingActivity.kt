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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.github.onotoliy.opposite.treasure.ui.IconClose
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.ui.views.EventItemView
import com.github.onotoliy.opposite.treasure.ui.views.TransactionItemView
import com.github.onotoliy.opposite.treasure.utils.complete
import com.github.onotoliy.opposite.treasure.utils.defaultWorkInfo
import com.github.onotoliy.opposite.treasure.utils.event
import com.github.onotoliy.opposite.treasure.utils.indicator
import com.github.onotoliy.opposite.treasure.utils.inject
import com.github.onotoliy.opposite.treasure.utils.mutableStateOf
import com.github.onotoliy.opposite.treasure.utils.navigateTo
import com.github.onotoliy.opposite.treasure.utils.setDefaultUncaughtExceptionHandler
import com.github.onotoliy.opposite.treasure.utils.transaction
import com.github.onotoliy.opposite.treasure.utils.worker
import javax.inject.Inject

class LoadingActivity : AppCompatActivity() {

    @Inject
    lateinit var worker: WorkManager

    @Inject
    lateinit var account: AccountManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()

        setDefaultUncaughtExceptionHandler()

        val debt: OneTimeWorkRequest = OneTimeWorkRequestBuilder<DebtWorker>().build()
        val event: OneTimeWorkRequest = OneTimeWorkRequestBuilder<EventWorker>().build()
        val deposit: OneTimeWorkRequest = OneTimeWorkRequestBuilder<DepositWorker>().build()
        val cashbox: OneTimeWorkRequest = OneTimeWorkRequestBuilder<CashboxWorker>().build()
        val transaction: OneTimeWorkRequest = OneTimeWorkRequestBuilder<TransactionWorker>().build()

        worker
            .beginWith(event)
            .then(transaction)
            .then(cashbox)
            .then(deposit)
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
            val cancel = remember(true) { mutableStateOf(true) }

            Image(
                imageVector = vectorResource(id = R.drawable.ic_launcher_foreground),
                contentScale = ContentScale.None
            )

            Spacer(Modifier.padding(10.dp))

            if (!works.all { it.complete }) {
                LinearProgressIndicators(works)
            }

            if (works.all { it.state == WorkInfo.State.SUCCEEDED }) {
                IconButton(
                    modifier = Modifier.border(1.dp, Color.LightGray, CircleShape),
                    onClick = { navigateTo(Screen.DepositScreen()) }
                ) {
                    IconCheck(tint = Color.Green)
                }
            }

            for (work in works) {
                if (work.state == WorkInfo.State.FAILED) {
                    if (work.event.uuid.isNotEmpty()) {
                        EventItemView(work.event, navigateTo)
                        cancel.value = false
                    }
                    if (work.transaction.uuid.isNotEmpty()) {
                        TransactionItemView(work.transaction, navigateTo)
                        cancel.value = false
                    }
                }
            }

            if (works.any { it.state == WorkInfo.State.FAILED } && cancel.value) {
                IconButton(
                    modifier = Modifier.border(1.dp, Color.LightGray, CircleShape),
                    onClick = { navigateTo(Screen.DepositScreen()) }
                ) {
                    IconClose(tint = Color.Red)
                }
            }
        }
    }
}

@Composable
fun LinearProgressIndicators(works: List<WorkInfo>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val info = works.findLast { it.state == WorkInfo.State.RUNNING }

        Text(
            text = stringResource(
                when (info?.worker) {
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
            progress = (works.count(WorkInfo::complete) * 0.2).toFloat()
        )

        Spacer(Modifier.padding(5.dp))

        info?.let {
            LinearProgressIndicator(progress = it.indicator)
        }
    }
}
