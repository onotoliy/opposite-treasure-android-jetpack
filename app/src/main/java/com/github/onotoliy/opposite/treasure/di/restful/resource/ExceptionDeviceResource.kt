package com.github.onotoliy.opposite.treasure.di.restful.resource

import android.util.Log
import com.github.onotoliy.opposite.data.core.ExceptionDevice
import com.github.onotoliy.opposite.treasure.di.restful.retrofit.ExceptionDeviceRetrofit
import java.io.PrintWriter
import java.io.StringWriter
import java.util.concurrent.Executors
import javax.inject.Inject

class ExceptionDeviceResource @Inject constructor(
    private val retrofit: ExceptionDeviceRetrofit
) {
    fun register(
        device: String,
        agent: String,
        message: String = "",
        localizedMessage: String,
        stackTrace: String
    ) {
        Executors.newSingleThreadExecutor().execute {
            val r: retrofit2.Response<Void> = retrofit.register(
                ExceptionDevice(
                    device = device,
                    agent = agent,
                    message = message,
                    localizedMessage = localizedMessage,
                    stackTrace = stackTrace
                )
            ).execute()

            Log.i("ExceptionDeviceResource", "Code: ${r.code()}. Message: ${r.message()}")
        }
    }
}
