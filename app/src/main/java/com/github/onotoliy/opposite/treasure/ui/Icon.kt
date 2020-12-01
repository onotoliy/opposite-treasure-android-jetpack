package com.github.onotoliy.opposite.treasure.ui

import androidx.compose.foundation.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import com.github.onotoliy.opposite.treasure.R

@Composable
fun IconLeftArrow(modifier: Modifier = Modifier) =
    Icon(asset = vectorResource(id = R.drawable.ic_left_arrow), modifier = modifier)

@Composable
fun IconRightArrow(modifier: Modifier = Modifier) =
    Icon(asset = vectorResource(id = R.drawable.ic_right_arrow), modifier = modifier)

@Composable
fun IconAccountCircle() = Icon(asset = vectorResource(id = R.drawable.ic_account_circle))

@Composable
fun IconAdd() = Icon(asset = vectorResource(id = R.drawable.ic_add))

@Composable
fun IconHome() = Icon(asset = vectorResource(id = R.drawable.ic_home))

@Composable
fun IconDeposits() = Icon(asset = vectorResource(id = R.drawable.ic_people))

@Composable
fun IconTransactions() = Icon(asset = vectorResource(id = R.drawable.ic_payments))

@Composable
fun IconEvents() = Icon(asset = vectorResource(id = R.drawable.ic_event))

@Composable
fun IconRefresh() = Icon(asset = vectorResource(id = R.drawable.ic_refresh))

@Composable
fun IconSave() = Icon(asset = vectorResource(id = R.drawable.ic_save))

@Composable
fun IconEdit() = Icon(asset = vectorResource(id = R.drawable.ic_edit))

@Composable
fun IconRemove() = Icon(asset = vectorResource(id = R.drawable.ic_delete))

@Composable
fun IconUp() = Icon(asset = vectorResource(id = R.drawable.ic_trending_up), tint = Color.Green)

@Composable
fun IconDown() = Icon(asset = vectorResource(id = R.drawable.ic_trending_down), tint = Color.Red)
