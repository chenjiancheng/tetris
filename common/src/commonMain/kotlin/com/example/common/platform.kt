package com.example.common

expect fun getPlatformName(): String
expect fun dp2px(dpValue: Float): Int
expect fun px2dp(pxValue: Float): Int
expect fun getBrickWidth(): Float