package com.github.onotoliy.opposite.treasure.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.di.database.dao.TransactionDAO
import com.github.onotoliy.opposite.treasure.ui.IconEdit
import com.github.onotoliy.opposite.treasure.ui.Menu
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.ui.views.TransactionView
import com.github.onotoliy.opposite.treasure.utils.defaultTransaction
import com.github.onotoliy.opposite.treasure.utils.inject
import com.github.onotoliy.opposite.treasure.utils.mutableStateOf
import com.github.onotoliy.opposite.treasure.utils.navigateTo
import com.github.onotoliy.opposite.treasure.utils.pk
import javax.inject.Inject

class TransactionActivity : AppCompatActivity() {

    @Inject
    lateinit var dao: TransactionDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pk = intent.pk ?: throw IllegalArgumentException("Primary key can not be null")

        inject()

        setContent {
            val context = mutableStateOf(defaultTransaction) { dao.get(pk) }

            TreasureTheme {
                Menu(
                    screen = Screen.TransactionScreen(pk),
                    floatingActionButton = {
                        FloatingActionButton(
                            modifier = Modifier.border(1.dp, Color.LightGray, CircleShape),
                            backgroundColor = Color.White,
                            content = { IconEdit() },
                            onClick = { navigateTo(Screen.TransactionEditScreen(pk)) }
                        )
                    },
                    bodyContent = {
                        Column {
                            TransactionView(dto = context.value, navigateTo = ::navigateTo)
                        }
                    },
                    navigateTo = ::navigateTo
                )
            }
        }
    }
}
