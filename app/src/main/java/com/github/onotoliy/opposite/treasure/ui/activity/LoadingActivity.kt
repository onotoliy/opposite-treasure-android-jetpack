package com.github.onotoliy.opposite.treasure.ui.activity

import android.accounts.AccountManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.di.worker.DebtWorker
import com.github.onotoliy.opposite.treasure.di.worker.DepositWorker
import com.github.onotoliy.opposite.treasure.di.worker.EventWorker
import com.github.onotoliy.opposite.treasure.di.worker.TransactionWorker
import com.github.onotoliy.opposite.treasure.ui.IconSave
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.utils.getUUID
import com.github.onotoliy.opposite.treasure.utils.inject
import com.github.onotoliy.opposite.treasure.utils.navigateTo
import com.github.onotoliy.opposite.treasure.utils.observe
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
        val transaction: OneTimeWorkRequest = OneTimeWorkRequestBuilder<TransactionWorker>().build()

        worker
            .beginWith(event)
//            .then(debt)
//            .then(deposit)
//            .then(transaction)
            .enqueue()

        setContent {
            TreasureTheme {
                LoadingScreen(
                    debt = worker.getWorkInfoByIdLiveData(debt.id),
                    event = worker.getWorkInfoByIdLiveData(event.id),
                    deposit = worker.getWorkInfoByIdLiveData(deposit.id),
                    transaction = worker.getWorkInfoByIdLiveData(transaction.id),
                    onClick = { navigateTo(Screen.DepositScreen(account.getUUID())) }
                )
            }
        }
    }
}

@Composable
fun LoadingScreen(
    debt: LiveData<WorkInfo>,
    event: LiveData<WorkInfo>,
    deposit: LiveData<WorkInfo>,
    transaction: LiveData<WorkInfo>,
    onClick: () -> Unit
) {
    val enabled = remember(mutableMapOf<String, Boolean>()) {
        mutableStateOf(mutableMapOf(
            "debt" to true,
            "event" to false,
            "deposit" to true,
            "transaction" to true
        ))
    }
    val f = remember(false) {
        mutableStateOf(enabled.value.all { it.value })
    }
//    debt.observeForever {
//        Log.i("LoadingActivity", "Debt ${it.finished} : ${enabled.value} : ${it.outputData.keyValueMap}")
//        enabled.value["debt"] = it.finished
//    }
    event.observeForever {
        Log.i("LoadingActivity", "Event ${it.finished} : ${enabled.value} : ${it.outputData.keyValueMap}")
        enabled.value["event"] = it.finished
        f.value = enabled.value.all { k -> k.value }
    }
//    deposit.observeForever {
//        Log.i("LoadingActivity", "Deposit ${it.finished} : ${enabled.value} : ${it.outputData.keyValueMap}")
//        enabled.value["deposit"] = it.finished
//    }
//    transaction.observeForever {
//        Log.i("LoadingActivity", "Transaction ${it.finished} : ${enabled.value} : ${it.outputData.keyValueMap}")
//        enabled.value["transaction"] = it.finished
//    }

    Box(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        alignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.8f).fillMaxHeight(0.8f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = enabled.value.toString() + " : " + f.value)
            Image(asset = vectorResource(id = R.drawable.ic_launcher_foreground))
            if (enabled.value.all { it.value }) {
                Text(text = "Загрузка завершена успешно!")
            } else {
                Text(text = "Выполняется загрузка данных...")
                Text(text = "Пожалуйста, подождите.")

                WorkInfoProgress(title = "Долги", value = debt)
                WorkInfoProgress(title = "События", value = event)
                WorkInfoProgress(title = "Депозиты", value = deposit)
                WorkInfoProgress(title = "Операции", value = transaction)
            }

            IconButton(
                modifier = Modifier.clip(CircleShape).background(
                    if (f.value) MaterialTheme.colors.primary else Color.Gray,
                    CircleShape
                ),
                enabled = f.value,
                onClick = onClick
            ) {
                IconSave()
            }
        }
    }
}

@Composable
fun WorkInfoProgress(title: String, value: LiveData<WorkInfo>) {
    value.observe()?.let {
        val total = it.progress.getInt("total", 0)
        val offset = it.progress.getInt("offset", 0)
        val progress =
            if (it.outputData.getBoolean("finished", false)) {
                1f
            } else {
                if (offset == 0 || total == 0) 0f else offset.toFloat() / total.toFloat()
            }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title)
            LinearProgressIndicator(progress = progress)
        }
    }
}

private val WorkInfo?.finished: Boolean
    get() = this?.outputData?.getBoolean("finished", false) ?: false

private val State<WorkInfo?>.finished: Boolean
    get() = this.value?.outputData?.getBoolean("finished", false) ?: false