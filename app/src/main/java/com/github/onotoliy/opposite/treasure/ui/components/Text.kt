package com.github.onotoliy.opposite.treasure.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSizeConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em

@Composable
fun LabeledText(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    labelColor: Color = MaterialTheme.colors.primary,
    labelFontSize: Int = 4,
    valueColor: Color = Color.Black,
    valueFontSize: Int = 5,
    background: Color = Color.White,
    textAlign: TextAlign = TextAlign.Left,
    leadingIcon: @Composable() (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .defaultMinSizeConstraints(280.dp)
            .preferredWidth(280.dp)
            .background(
                color = background,
                shape = RoundedCornerShape(topLeft = 4.dp, topRight = 4.dp)
            )
            .clickable(
                enabled = onClick != null,
                onClick = { onClick?.let { it() } }
            )
    ) {
        if (leadingIcon == null) {
            Column(
                modifier = Modifier.padding(18.dp, if (value.isBlank()) 18.dp else 5.dp)
            ) {
                androidx.compose.material.Text(
                    text = label,
                    fontSize = if (value.isBlank()) valueFontSize.em else labelFontSize.em,
                    color = labelColor
                )

                if (value.isNotBlank()) {
                    androidx.compose.material.Text(
                        text = value,
                        modifier = Modifier.fillMaxWidth(),
                        color = valueColor,
                        textAlign = textAlign,
                        fontSize = valueFontSize.em
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .padding(horizontal = 14.dp, vertical = if (value.isBlank()) 14.dp else 5.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(35.dp)
                        .width(40.dp)
                        .alpha(0.54f)
                        .padding(top = if (value.isBlank()) 1.dp else labelFontSize.dp)
                ) {
                    leadingIcon()
                }
                Column {
                    androidx.compose.material.Text(
                        modifier = Modifier.defaultMinSizeConstraints(212.dp),
                        text = label,
                        fontSize = if (value.isBlank()) valueFontSize.em else labelFontSize.em,
                        color = labelColor
                    )

                    if (value.isNotBlank()) {
                        androidx.compose.material.Text(
                            modifier = Modifier.defaultMinSizeConstraints(212.dp),
                            text = value,
                            fontSize = valueFontSize.em,
                            color = valueColor,
                            textAlign = textAlign
                        )
                    }
                }
            }
        }
    }
}
