package com.github.onotoliy.opposite.treasure.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import com.github.onotoliy.opposite.treasure.*
import com.github.onotoliy.opposite.treasure.di.model.DepositPageActivityModel
import com.github.onotoliy.opposite.treasure.di.service.DepositService
import com.github.onotoliy.opposite.treasure.ui.Menu
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.ui.screens.views.DepositPageView
import javax.inject.Inject

class DepositPageActivity : AppCompatActivity()  {

    @Inject
    lateinit var depositService: DepositService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as App).appComponent.inject(this)

        val navigateTo: (Screen) -> Unit = { goto(it) }
        val screen = DepositPageActivityModel(depositService)

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