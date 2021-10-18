package com.github.onotoliy.opposite.treasure.utils

import android.content.Intent

val Intent?.pk: String?
    get() = this?.getStringExtra("pk")

val Intent?.previousScreen: String?
    get() = this?.getStringExtra("previousScreen")

val Intent?.message: String?
    get() = this?.getStringExtra("message")

val Intent?.localizedMessage: String?
    get() = this?.getStringExtra("localizedMessage")

val Intent?.stackTrace: String?
    get() = this?.getStringExtra("stackTrace")
