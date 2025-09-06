package com.clashsing.proxylib

import android.app.Application
import com.tencent.mmkv.MMKV

object ProxyComponent {
    private const val ERROR_MSG = "ProxyComponent not initialized. Call ProxyComponent.init(application) first."

    @Volatile
    private var _application: Application? = null
    val application: Application
        get() = _application ?: throw IllegalStateException(ERROR_MSG)

    fun init(application: Application) {
        val checkResult = _application
        if (checkResult != null) {
            return
        }
        synchronized(this) {
            val doubleCheckResult = _application
            if (doubleCheckResult != null) {
                return
            }
            _application = application
            MMKV.initialize(application)
        }
    }
}
