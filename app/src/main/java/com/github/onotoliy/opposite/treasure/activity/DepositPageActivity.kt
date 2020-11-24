package com.github.onotoliy.opposite.treasure.activity

import android.accounts.AccountManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.Cashbox
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.PageView
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.activity.model.DepositPageActivityModel
import com.github.onotoliy.opposite.treasure.goto
import com.github.onotoliy.opposite.treasure.observe
import com.github.onotoliy.opposite.treasure.resources.CashboxCallback
import com.github.onotoliy.opposite.treasure.resources.DepositCallback
import com.github.onotoliy.opposite.treasure.services.*
import com.github.onotoliy.opposite.treasure.ui.Menu
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.ui.screens.views.DepositPageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DepositPageActivity : AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navigateTo: (Screen) -> Unit = { goto(it) }
        val manager: AccountManager = AccountManager.get(applicationContext)
        val screen = DepositPageActivityModel(manager)

        screen.loading()

        setContent {
            TreasureTheme {
                Menu(
                    bodyContent = { DepositPageScreen(screen, navigateTo) },
                    navigateTo = navigateTo
                )
            }
        }
    }
}

@Composable
fun DepositPageScreen(model: DepositPageActivityModel, navigateTo: (Screen) -> Unit) {
    model.pending.observe()?.let { pending ->
        Column {
            if (pending) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            model.page.observe()?.let { view ->
                DepositPageView(
                    view = PageView(view.offset, view.numberOfRows, view.context),
                    navigateTo = navigateTo,
                    navigateToNextPageScreen = { offset, numberOfRows, _ ->
                        model.nextDepositPageLoading(offset, numberOfRows)
                    }
                )
            }
        }
    }
}