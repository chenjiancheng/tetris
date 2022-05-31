package com.example.common

import androidx.compose.ui.unit.dp

actual fun getPlatformName(): String {
    return "Desktop"
}

actual fun dp2px(dpValue: Float): Int {
    return dpValue.toInt()
}

actual fun px2dp(pxValue: Float): Int {
    return pxValue.toInt()
}

actual fun getBrickWidth(): Float {
    return 22f
}