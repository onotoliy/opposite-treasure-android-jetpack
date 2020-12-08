package com.github.onotoliy.opposite.treasure.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.ui.tooling.preview.Preview
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.di.worker.DebtWorker
import com.github.onotoliy.opposite.treasure.di.worker.DepositWorker
import com.github.onotoliy.opposite.treasure.di.worker.EventWorker
import com.github.onotoliy.opposite.treasure.di.worker.TransactionWorker
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.utils.inject
import com.github.onotoliy.opposite.treasure.utils.observe
import javax.inject.Inject

class LoadingActivity : AppCompatActivity() {

    @Inject
    lateinit var worker: WorkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()

        val debt: WorkRequest = OneTimeWorkRequestBuilder<DebtWorker>().build()
        val event: WorkRequest = OneTimeWorkRequestBuilder<EventWorker>().build()
        val transaction: WorkRequest = OneTimeWorkRequestBuilder<TransactionWorker>().build()
        val deposit: WorkRequest = OneTimeWorkRequestBuilder<DepositWorker>().build()

        worker.enqueue(listOf(debt, event, transaction, deposit))

        setContent {
            TreasureTheme {
                LoadingScreen(
                    debt = worker.getWorkInfoByIdLiveData(debt.id),
                    event = worker.getWorkInfoByIdLiveData(event.id),
                    deposit = worker.getWorkInfoByIdLiveData(deposit.id),
                    transaction = worker.getWorkInfoByIdLiveData(transaction.id)
                )
            }
        }
    }
}

@Preview
@Composable
fun LoadingScreenPreview() {
    LoadingScreen(
        debt = MutableLiveData(),
        event = MutableLiveData(),
        deposit = MutableLiveData(),
        transaction = MutableLiveData()
    )
}

@Composable
fun LoadingScreen(
    debt: LiveData<WorkInfo>,
    event: LiveData<WorkInfo>,
    deposit: LiveData<WorkInfo>,
    transaction: LiveData<WorkInfo>
) {
    Column {
        Image(asset = vectorResource(id = R.drawable.ic_launcher_foreground))
        Text(text = "Выполняется загрузка данных... Пожалуйста, подождите.")

        debt.observe()?.let {
            if (it.outputData.getBoolean("finished", false)) {
                if (it.outputData.getBoolean("success", false)) {
                    Text(text = "Долги успешно загружены")
                } else {
                    Text(text = "При загрузке долгов произощла ошибка")
                }
            } else {
                val total = it.progress.getInt("total", 0)
                val offset = it.progress.getInt("offset", 0)
                if (offset > total) {
                    Text(text = "Загружаются долги $total/$total")
                } else {
                    Text(text = "Загружаются долги $total/$offset")
                }

            }
        }

        event.observe()?.let {
            Text(text = "Загружаются события " + it.progress.keyValueMap.toString())
        }

        deposit.observe()?.let {
            Text(text = "Загружаются депозиты " + it.outputData.keyValueMap.toString())
        }

        transaction.observe()?.let {
            Text(text = "Загружаются операции " + it.outputData.keyValueMap.toString())
        }
    }
}
