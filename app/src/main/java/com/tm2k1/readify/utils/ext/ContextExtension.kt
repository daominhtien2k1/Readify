package com.tm2k1.readify.utils.ext

import android.content.Context
import android.widget.Toast

fun Context.showToast(msg: String, isLong: Boolean = false) {
    Toast.makeText(this, msg, if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
}

