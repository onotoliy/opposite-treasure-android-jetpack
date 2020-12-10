package com.github.onotoliy.opposite.treasure.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.di.model.TransactionPageActivityModel
import com.github.onotoliy.opposite.treasure.utils.observe
import com.github.onotoliy.opposite.treasure.ui.IconAdd
import com.github.onotoliy.opposite.treasure.ui.Menu
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.ui.views.TransactionPageViewVO
import com.github.onotoliy.opposite.treasure.utils.inject
import com.github.onotoliy.opposite.treasure.utils.navigateTo
import javax.inject.Inject

class TransactionPageActivity : AppCompatActivity()  {

    @Inject lateinit var model: TransactionPageActivityModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()

        model.loading()

        setContent {
            TreasureTheme {
                Menu(
                    floatingActionButton = {
                        FloatingActionButton(
                            icon = { IconAdd() },
                            onClick = { navigateTo(Screen.TransactionEditScreen()) }
                        )
                    },
                    bodyContent = { TransactionPageScreen(model, ::navigateTo) },
                    navigateTo = ::navigateTo
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
                TransactionPageViewVO(
                    view = model.page,
                    navigateTo = navigateTo,
                    navigateToNextPageScreen = { offset, numberOfRows, _ ->
                        model.nextTransactionPageLoading(offset, numberOfRows)
                    }
                )
        }
    }
}
