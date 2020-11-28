package com.github.onotoliy.opposite.treasure.utils

import android.content.Intent

val Intent?.pk: String?
    get() = this?.getStringExtra("pk")