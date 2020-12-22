package com.github.onotoliy.opposite.treasure.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.di.model.DepositPageActivityModel
import com.github.onotoliy.opposite.treasure.ui.Menu
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.ui.views.DepositPageViewVO
import com.github.onotoliy.opposite.treasure.utils.inject
import com.github.onotoliy.opposite.treasure.utils.navigateTo
import com.github.onotoliy.opposite.treasure.utils.observe
import javax.inject.Inject

class DepositPageActivity : AppCompatActivity()  {

    @Inject lateinit var model: DepositPageActivityModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()

        model.loading()

        setContent {
            TreasureTheme {
                Menu(
                    bodyContent = { DepositPageScreen(model, ::navigateTo) },
                    navigateTo = ::navigateTo
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
            DepositPageViewVO(
                view = model.page,
                navigateTo = navigateTo,
                navigateToNextPageScreen = { offset, numberOfRows, _ ->
                    model.nextDepositPageLoading(offset, numberOfRows)
                }
            )
        }
    }
}
