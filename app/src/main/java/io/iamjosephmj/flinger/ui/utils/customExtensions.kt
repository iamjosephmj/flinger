package io.iamjosephmj.flinger.ui.utils

import android.text.TextUtils

fun String.toFloatNum(): Float {
    return if (isNotEmpty() && length == 1 &&
        TextUtils.equals(get(0).toString(), ".")
    ) {
        "0.".toFloat()
    } else if (isEmpty()) {
        "0".toFloat()
    } else {
        toFloat()
    }
}