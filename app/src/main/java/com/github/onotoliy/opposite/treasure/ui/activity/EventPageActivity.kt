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
import com.github.onotoliy.opposite.treasure.App
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.di.model.EventPageActivityModel
import com.github.onotoliy.opposite.treasure.goto
import com.github.onotoliy.opposite.treasure.observe
import com.github.onotoliy.opposite.treasure.ui.IconAdd
import com.github.onotoliy.opposite.treasure.ui.Menu
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.ui.views.EventPageView
import javax.inject.Inject

class EventPageActivity : AppCompatActivity()  {

    @Inject
    lateinit var model: EventPageActivityModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as App).appComponent.inject(this)

        val navigateTo: (Screen) -> Unit = { goto(it) }

        model.loading()

        setContent {
            TreasureTheme {
                Menu(
                    floatingActionButton = {
                        FloatingActionButton(
                            icon = { IconAdd() },
                            onClick = { navigateTo(Screen.EventEditScreen()) }
                        )
                    },
                    bodyContent = { EventPageScreen(model, navigateTo) },
                    navigateTo = navigateTo
                )
            }
        }
    }

}


@Composable
fun EventPageScreen(
    model: EventPageActivityModel,
    navigateTo: (Screen) -> Unit
) {
    model.pending.observe()?.let { pending ->
        Column {
            if (pending) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            model.page.observe()?.let { view ->
                EventPageView(
                    view =  view,
                    navigateTo = navigateTo,
                    navigateToNextPageScreen = { offset, numberOfRows, _ ->
                        model.nextEventPageLoading(offset, numberOfRows)
                    }
                )
            }
        }
    }
}