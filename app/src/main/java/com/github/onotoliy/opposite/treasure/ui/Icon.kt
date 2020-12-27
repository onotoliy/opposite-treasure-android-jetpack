package com.github.onotoliy.opposite.treasure.ui

import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import com.github.onotoliy.opposite.treasure.R

@Composable
fun IconSmartphone(modifier: Modifier = Modifier, tint: Color = Color.White) =
    Icon(
        imageVector = vectorResource(id = R.drawable.ic_baseline_smartphone_24),
        modifier = modifier,
        tint = tint
    )

@Composable
fun IconCached(modifier: Modifier = Modifier, tint: Color = Color.White) =
    Icon(
        imageVector = vectorResource(id = R.drawable.ic_baseline_cached_24),
        modifier = modifier,
        tint = tint
    )

@Composable
fun IconCheck(modifier: Modifier = Modifier, tint: Color = Color.White) =
    Icon(
        imageVector = vectorResource(id = R.drawable.ic_baseline_check_24),
        modifier = modifier,
        tint = tint
    )

@Composable
fun IconLeftArrow(modifier: Modifier = Modifier) =
    Icon(imageVector = vectorResource(id = R.drawable.ic_left_arrow), modifier = modifier)

@Composable
fun IconRightArrow(modifier: Modifier = Modifier) =
    Icon(imageVector = vectorResource(id = R.drawable.ic_right_arrow), modifier = modifier)

@Composable
fun IconAccountCircle(modifier: Modifier = Modifier, tint: Color = Color.Black) =
    Icon(imageVector = vectorResource(id = R.drawable.ic_account_circle), tint = tint, modifier = modifier)

@Composable
fun IconAdd(modifier: Modifier = Modifier, tint: Color = Color.Black) =
    Icon(imageVector = vectorResource(id = R.drawable.ic_add), tint = tint, modifier = modifier)

@Composable
fun IconHome(modifier: Modifier = Modifier, tint: Color = Color.Black) =
    Icon(imageVector = vectorResource(id = R.drawable.ic_home), tint = tint, modifier = modifier)

@Composable
fun IconDeposits(modifier: Modifier = Modifier, tint: Color = Color.Black) =
    Icon(imageVector = vectorResource(id = R.drawable.ic_people), tint = tint, modifier = modifier)

@Composable
fun IconTransactions(modifier: Modifier = Modifier, tint: Color = Color.Black) =
    Icon(imageVector = vectorResource(id = R.drawable.ic_payments), tint = tint, modifier = modifier)

@Composable
fun IconEvents(modifier: Modifier = Modifier, tint: Color = Color.Black) =
    Icon(imageVector = vectorResource(id = R.drawable.ic_event), tint = tint, modifier = modifier)

@Composable
fun IconRefresh(modifier: Modifier = Modifier, tint: Color = Color.Black) =
    Icon(imageVector = vectorResource(id = R.drawable.ic_refresh), tint = tint, modifier = modifier)

@Composable
fun IconSave(modifier: Modifier = Modifier, tint: Color = Color.Black) =
    Icon(imageVector = vectorResource(id = R.drawable.ic_save), tint = tint, modifier = modifier)

@Composable
fun IconEdit(modifier: Modifier = Modifier, tint: Color = Color.Black) =
    Icon(imageVector = vectorResource(id = R.drawable.ic_edit), tint = tint, modifier = modifier)

@Composable
fun IconRemove(modifier: Modifier = Modifier, tint: Color = Color.Black) =
    Icon(imageVector = vectorResource(id = R.drawable.ic_delete), tint = tint, modifier = modifier)

@Composable
fun IconUp(modifier: Modifier = Modifier, tint: Color = Color.Black) =
    Icon(imageVector = vectorResource(id = R.drawable.ic_trending_up), tint = tint, modifier = modifier)

@Composable
fun IconDown(modifier: Modifier = Modifier, tint: Color = Color.Black) =
    Icon(imageVector = vectorResource(id = R.drawable.ic_trending_down), tint = tint, modifier = modifier)
