package com.clashsing.proxylib.schema.singbox.out

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
object OutTypeBlock : OutType, Parcelable {
    @IgnoredOnParcel
    override val type: String = OutType.BLOCK
    @IgnoredOnParcel
    override val tag: String = OutType.BLOCK
}
