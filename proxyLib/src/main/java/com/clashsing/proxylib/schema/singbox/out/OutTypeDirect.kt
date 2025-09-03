package com.clashsing.proxylib.schema.singbox.out

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
object OutTypeDirect : OutType, Parcelable {
    @IgnoredOnParcel
    override val type: String = OutType.Type.DIRECT
    @IgnoredOnParcel
    override val tag: String = OutType.Type.DIRECT
}
