package com.github.onotoliy.opposite.treasure.ui

import androidx.compose.foundation.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import com.github.onotoliy.opposite.treasure.R

@Composable
fun IconSmartphone(modifier: Modifier = Modifier, tint: Color = Color.White) =
    Icon(
        asset = vectorResource(id = R.drawable.ic_baseline_smartphone_24),
        modifier = modifier,
        tint = tint
    )

@Composable
fun IconCached(modifier: Modifier = Modifier, tint: Color = Color.White) =
    Icon(
        asset = vectorResource(id = R.drawable.ic_baseline_cached_24),
        modifier = modifier,
        tint = tint
    )

@Composable
fun IconCheck(modifier: Modifier = Modifier) =
    Icon(asset = vectorResource(id = R.drawable.ic_baseline_check_24), modifier = modifier)

@Composable
fun IconLeftArrow(modifier: Modifier = Modifier) =
    Icon(asset = vectorResource(id = R.drawable.ic_left_arrow), modifier = modifier)

@Composable
fun IconRightArrow(modifier: Modifier = Modifier) =
    Icon(asset = vectorResource(id = R.drawable.ic_right_arrow), modifier = modifier)

@Composable
fun IconAccountCircle(modifier: Modifier = Modifier) =
    Icon(asset = vectorResource(id = R.drawable.ic_account_circle), modifier = modifier)

@Composable
fun IconAdd(modifier: Modifier = Modifier) =
    Icon(asset = vectorResource(id = R.drawable.ic_add), modifier = modifier)

@Composable
fun IconHome(modifier: Modifier = Modifier) =
    Icon(asset = vectorResource(id = R.drawable.ic_home), modifier = modifier)

@Composable
fun IconDeposits(modifier: Modifier = Modifier) =
    Icon(asset = vectorResource(id = R.drawable.ic_people), modifier = modifier)

@Composable
fun IconTransactions(modifier: Modifier = Modifier) =
    Icon(asset = vectorResource(id = R.drawable.ic_payments), modifier = modifier)

@Composable
fun IconEvents(modifier: Modifier = Modifier) =
    Icon(asset = vectorResource(id = R.drawable.ic_event), modifier = modifier)

@Composable
fun IconRefresh(modifier: Modifier = Modifier) =
    Icon(asset = vectorResource(id = R.drawable.ic_refresh), modifier = modifier)

@Composable
fun IconSave(modifier: Modifier = Modifier) =
    Icon(asset = vectorResource(id = R.drawable.ic_save), modifier = modifier)

@Composable
fun IconEdit(modifier: Modifier = Modifier) =
    Icon(asset = vectorResource(id = R.drawable.ic_edit), modifier = modifier)

@Composable
fun IconRemove(modifier: Modifier = Modifier) =
    Icon(asset = vectorResource(id = R.drawable.ic_delete), modifier = modifier)

@Composable
fun IconUp(modifier: Modifier = Modifier) =
    Icon(
        asset = vectorResource(id = R.drawable.ic_trending_up),
        tint = Color.Green,
        modifier = modifier
    )

@Composable
fun IconDown(modifier: Modifier = Modifier) =
    Icon(
        asset = vectorResource(id = R.drawable.ic_trending_down),
        tint = Color.Red,
        modifier = modifier
    )
