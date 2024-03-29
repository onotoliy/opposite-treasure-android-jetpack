package com.github.onotoliy.opposite.treasure.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.setContent
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.di.database.data.DepositVO
import com.github.onotoliy.opposite.treasure.di.database.repositories.DepositRepository
import com.github.onotoliy.opposite.treasure.ui.Menu
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.ui.views.DepositPageView
import com.github.onotoliy.opposite.treasure.utils.defaultDeposits
import com.github.onotoliy.opposite.treasure.utils.inject
import com.github.onotoliy.opposite.treasure.utils.loading
import com.github.onotoliy.opposite.treasure.utils.mutableStateOf
import com.github.onotoliy.opposite.treasure.utils.navigateTo
import com.github.onotoliy.opposite.treasure.utils.setDefaultUncaughtExceptionHandler
import javax.inject.Inject

class DepositPageActivity : AppCompatActivity() {

    @Inject
    lateinit var dao: DepositRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()

        setDefaultUncaughtExceptionHandler()

        setContent {
            val total = mutableStateOf(0L, dao::count)
            val context = mutableStateOf(defaultDeposits) { o, n -> dao.getAll(o, n) }

            TreasureTheme {
                Menu(
                    screen = Screen.DepositPageScreen,
                    bodyContent = {
                        DepositPageScreen(
                            context = context,
                            total = total,
                            navigateTo = ::navigateTo,
                            navigateToNextPageScreen = { loading(context) { o, n -> dao.getAll(o, n) } }
                        )
                    },
                    navigateTo = ::navigateTo
                )
            }
        }
    }
}

@Composable
fun DepositPageScreen(
    context: MutableState<List<DepositVO>> = mutableStateOf(defaultDeposits),
    total: MutableState<Long> = mutableStateOf(0L),
    navigateTo: (Screen) -> Unit,
    navigateToNextPageScreen: () -> Unit
) {
    Column {
        DepositPageView(
            list = context.value,
            total = total.value,
            navigateTo = navigateTo,
            navigateToNextPageScreen = navigateToNextPageScreen
        )
    }
}
