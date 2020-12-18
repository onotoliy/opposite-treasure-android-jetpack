package com.github.onotoliy.opposite.treasure.utils

import androidx.work.Data.Builder
import androidx.work.ListenableWorker

fun progress(total: Int, offset: Int) = Builder()
    .putInt("total", total)
    .putInt("offset", offset)
    .build()

fun Builder.setLocalVersion(version: String): Builder =
    putString("local-version", version)

fun Builder.setRemoteVersion(version: String): Builder =
    putString("remote-version", version)

fun Builder.setFinished(success: Boolean): Builder =
    putBoolean("finished", true).putBoolean("success", success)

fun Builder.failure() =
    ListenableWorker.Result.failure(setFinished(false).build())

fun Builder.success() =
    ListenableWorker.Result.success(setFinished(true).build())

