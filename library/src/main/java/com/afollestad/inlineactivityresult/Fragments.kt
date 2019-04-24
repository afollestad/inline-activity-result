@file:Suppress("unused")

package com.afollestad.inlineactivityresult

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * Starts Activity [T] with the given [requestCode]. The result is delivered to [onResult].
 */
inline fun <reified T : Activity> Fragment.startActivityForResult(
  requestCode: Int,
  noinline onResult: OnResult
) = startActivityForResult<T>(Bundle(), requestCode, onResult)

/**
 * Starts Activity [T] with extras in [data], and the given [requestCode]. The result
 * is delivered to [onResult].
 */
inline fun <reified T : Activity> Fragment.startActivityForResult(
  data: Bundle = Bundle(),
  requestCode: Int,
  noinline onResult: OnResult
) {
  val intent = Intent(context, T::class.java).putExtras(data)
  startActivityForResult(intent, requestCode, onResult)
}

/**
 * Starts an Activity that [intent] resolves to, with the given [requestCode]. The result
 * is delivered to [onResult].
 */
fun Fragment.startActivityForResult(
  intent: Intent,
  requestCode: Int,
  onResult: OnResult
) {
  check(activity != null) {
    "Fragment must be attached to an Activity."
  }
  InlineActivityResult.instance()
      .schedule(
          fragmentManager = fragmentManager ?: activity!!.supportFragmentManager,
          intent = intent,
          requestCode = requestCode,
          onResult = onResult
      )
}
