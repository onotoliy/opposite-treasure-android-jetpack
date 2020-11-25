package com.github.onotoliy.opposite.treasure.activity

import android.accounts.AccountManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import com.github.onotoliy.opposite.treasure.*
import com.github.onotoliy.opposite.treasure.activity.model.TransactionPageActivityModel
import com.github.onotoliy.opposite.treasure.activity.model.TransactionService
import com.github.onotoliy.opposite.treasure.ui.Menu
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.ui.screens.views.TransactionPageView
import javax.inject.Inject

class TransactionPageActivity : AppCompatActivity()  {

    @Inject
    lateinit var transactionService: TransactionService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as App).appComponent.inject(this)

        val navigateTo: (Screen) -> Unit = { goto(it) }
        val manager: AccountManager = AccountManager.get(applicationContext)
        val screen = TransactionPageActivityModel(transactionService = transactionService)

        screen.loading()

        setContent {
            TreasureTheme {
                Menu(
                    floatingActionButton = {
                        FloatingActionButton(
                            icon = { IconAdd() },
                            onClick = { }
                        )
                    },
                    bodyContent = { TransactionPageScreen(screen, navigateTo) },
                    navigateTo = navigateTo
                )
            }
        }
    }

}


@Composable
fun TransactionPageScreen(
    model: TransactionPageActivityModel,
    navigateTo: (Screen) -> Unit
) {
    model.pending.observe()?.let { pending ->
        Column {
            if (pending) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            model.page.observe()?.let { view ->
                TransactionPageView(
                    view = PageView(view.offset, view.numberOfRows, view.context),
                    navigateTo = navigateTo,
                    navigateToNextPageScreen = { offset, numberOfRows, _ ->
                        model.nextTransactionPageLoading(offset, numberOfRows)
                    }
                )
            }
        }
    }
}
