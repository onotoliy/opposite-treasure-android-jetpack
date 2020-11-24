package com.github.onotoliy.opposite.treasure.ui

import android.widget.RadioGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.github.onotoliy.opposite.data.Option

@Composable
fun Selector(
    selected: MutableState<Option>,
    list: List<Option> = listOf(),
    title: String? = null,
    isOpen: MutableState<Boolean> = remember { mutableStateOf(false) },
    asset: VectorAsset? = null,
    close: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxWidth().clickable(onClick = {
        close()
        isOpen.value = true
    })) {
        title?.let {
            Text(text = it, style = H6_BOLD)
        }

        Row {
            asset?.let { Icon(asset = it) }
            Text(text = selected.value.name, style = BODY)
        }
    }

    if (isOpen.value) {
        Popup {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = Color.LightGray)
                    .background(color = Color.White)
                    .drawOpacity(opacity = 0.8f)
            ) {
                title?.let {
                    Text(
                        modifier = Modifier.padding(5.dp).fillMaxWidth(),
                        text = it,
                        style = H6_BOLD,
                        textAlign = TextAlign.Center
                    )
                    Divider()
                }
//                RadioGroup {
//                    Column {
//                        list.forEach {
//                            RadioGroup(
//                                selected = selected.value.uuid == it.uuid,
//                                text = it.name,
//                                onSelect = {
//                                    selected.value = it
//                                    isOpen.value = false
//                                }
//                            )
//                        }
//                    }
//                }
            }
        }
    }
}