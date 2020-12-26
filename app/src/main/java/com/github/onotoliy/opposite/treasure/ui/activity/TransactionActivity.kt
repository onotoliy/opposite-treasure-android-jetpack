package com.github.onotoliy.opposite.treasure.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.FloatingActionButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.setContent
import com.github.onotoliy.opposite.treasure.di.database.repositories.TransactionRepository
import com.github.onotoliy.opposite.treasure.ui.IconEdit
import com.github.onotoliy.opposite.treasure.ui.Menu
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.ui.views.TransactionView
import com.github.onotoliy.opposite.treasure.utils.defaultTransaction
import com.github.onotoliy.opposite.treasure.utils.inject
import com.github.onotoliy.opposite.treasure.utils.navigateTo
import com.github.onotoliy.opposite.treasure.utils.pk
import com.github.onotoliy.opposite.treasure.utils.v3Loading
import javax.inject.Inject

class TransactionActivity : AppCompatActivity() {

    @Inject
    lateinit var dao: TransactionRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()

        setContent {
            val context = remember(defaultTransaction) {
                mutableStateOf(defaultTransaction).apply {
                    v3Loading {
                        dao.get(intent.pk ?: throw IllegalArgumentException("Primary key can not be null"))
                    }
                }
            }

            TreasureTheme {
                Menu(
                    floatingActionButton = {
                        FloatingActionButton(
                            content = { IconEdit() },
                            onClick = { }
                        )
                    },
                    bodyContent = {
                        Column {
                            TransactionView(data = context.value, navigateTo = ::navigateTo)
                        }
                    },
                    navigateTo = ::navigateTo
                )
            }
        }
    }
}
