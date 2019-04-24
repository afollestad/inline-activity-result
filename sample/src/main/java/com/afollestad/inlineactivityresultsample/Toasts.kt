package com.afollestad.inlineactivityresultsample

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

private var toast: Toast? = null

fun Context.toast(message: String) {
  toast?.cancel()
  toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
      .apply { show() }
}

fun Fragment.toast(message: String) {
  toast?.cancel()
  toast = Toast.makeText(activity, message, Toast.LENGTH_LONG)
      .apply { show() }
}
