package com.github.onotoliy.opposite.treasure.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.FloatingActionButton
import androidx.compose.ui.platform.setContent
import com.github.onotoliy.opposite.treasure.di.database.repositories.TransactionRepository
import com.github.onotoliy.opposite.treasure.ui.IconEdit
import com.github.onotoliy.opposite.treasure.ui.Menu
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.ui.views.TransactionView
import com.github.onotoliy.opposite.treasure.utils.defaultTransaction
import com.github.onotoliy.opposite.treasure.utils.inject
import com.github.onotoliy.opposite.treasure.utils.loading
import com.github.onotoliy.opposite.treasure.utils.mutableStateOf
import com.github.onotoliy.opposite.treasure.utils.navigateTo
import com.github.onotoliy.opposite.treasure.utils.pk
import javax.inject.Inject

class TransactionActivity : AppCompatActivity() {

    @Inject
    lateinit var dao: TransactionRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pk = intent.pk ?: throw IllegalArgumentException("Primary key can not be null")

        inject()

        setContent {
            val context = mutableStateOf(defaultTransaction) { dao.get(pk) }

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
