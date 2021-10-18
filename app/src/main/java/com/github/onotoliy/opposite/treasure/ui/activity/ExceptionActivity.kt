package com.github.onotoliy.opposite.treasure.ui.activity

import android.accounts.AccountManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.di.restful.resource.ExceptionDeviceResource
import com.github.onotoliy.opposite.treasure.ui.IconClose
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.ui.components.LabeledText
import com.github.onotoliy.opposite.treasure.utils.hasAccount
import com.github.onotoliy.opposite.treasure.utils.inject
import com.github.onotoliy.opposite.treasure.utils.localizedMessage
import com.github.onotoliy.opposite.treasure.utils.message
import com.github.onotoliy.opposite.treasure.utils.navigateTo
import com.github.onotoliy.opposite.treasure.utils.previousScreen
import com.github.onotoliy.opposite.treasure.utils.stackTrace
import java.util.*
import javax.inject.Inject

class ExceptionActivity : AppCompatActivity() {

    @Inject
    lateinit var account: AccountManager

    @Inject
    lateinit var resource: ExceptionDeviceResource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val previousScreen = intent?.previousScreen ?: throw IllegalArgumentException("Previous screen can not be null")
        val message = intent.message ?: throw IllegalArgumentException("Message can not be null")
        val localizedMessage = intent.localizedMessage ?: throw IllegalArgumentException("Localized message can not be null")
        val stackTrace = intent.stackTrace ?: throw IllegalArgumentException("Stack trace can not be null")

        inject()

        resource.register(
            device = getDeviceName(),
            agent = getOSVersion(),
            message = "Previous screen is $previousScreen. $message",
            localizedMessage = localizedMessage,
            stackTrace = stackTrace)

        setContent {
            TreasureTheme {
                ExceptionScreen(previousScreen, message, localizedMessage) {
                    if (account.hasAccount()) {
                        navigateTo(Screen.LoadingScreen)
                    } else {
                        navigateTo(Screen.LoginScreen)
                    }
                }
            }
        }

    }

    private fun getOSVersion(): String = Build.VERSION.SDK_INT.toString()

    private fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL

        return if (model.toLowerCase(Locale.ROOT).startsWith(manufacturer.toLowerCase(Locale.ROOT))) {
            model ?: ""
        } else {
            "$manufacturer $model"
        }
    }
}

@Composable
fun ExceptionScreen(
    previousScreen: String,
    message: String,
    localizedMessage: String,
    navigateTo: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.8f).fillMaxHeight(0.8f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LabeledText(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(id = R.string.exception_screen),
                value = previousScreen
            )

            LabeledText(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(id = R.string.exception_message),
                value = message
            )

            LabeledText(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(id = R.string.exception_localized_message),
                value = localizedMessage
            )

            IconButton(
                modifier = Modifier.border(1.dp, Color.LightGray, CircleShape),
                onClick = { navigateTo() }
            ) {
                IconClose(tint = Color.Red)
            }
        }
    }
}
