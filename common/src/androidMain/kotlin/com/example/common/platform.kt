package com.example.common

import android.content.res.Resources

actual fun getPlatformName(): String {
    return "Android"
}
actual fun dp2px(dpValue: Float): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}
actual fun px2dp(pxValue: Float): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

actual fun getBrickWidth(): Float {
    return 32f
}