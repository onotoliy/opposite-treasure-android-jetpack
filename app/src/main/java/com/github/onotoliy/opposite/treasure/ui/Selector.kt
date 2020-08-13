package com.github.onotoliy.opposite.treasure.ui

import androidx.compose.Composable
import androidx.compose.MutableState
import androidx.compose.state
import androidx.ui.core.Modifier
import androidx.ui.core.Popup
import androidx.ui.core.drawOpacity
import androidx.ui.foundation.*
import androidx.ui.graphics.Color
import androidx.ui.graphics.vector.VectorAsset
import androidx.ui.layout.Column
import androidx.ui.layout.Row
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.material.Divider
import androidx.ui.material.RadioGroup
import androidx.ui.text.style.TextAlign
import androidx.ui.unit.dp
import com.github.onotoliy.opposite.data.Option

@Composable
fun Selector(
    selected: MutableState<Option>,
    list: List<Option> = listOf(),
    title: String? = null,
    isOpen: MutableState<Boolean> = state { false },
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
                    .drawBorder(size = 1.dp, color = Color.LightGray)
                    .drawBackground(color = Color.White)
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
                RadioGroup {
                    Column {
                        list.forEach {
                            RadioGroupTextItem(
                                selected = selected.value.uuid == it.uuid,
                                text = it.name,
                                onSelect = {
                                    selected.value = it
                                    isOpen.value = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}