@file:Suppress("unused")

package com.afollestad.inlineactivityresult

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity

/**
 * Starts Activity [T] with the given [requestCode]. The result is delivered to [onResult].
 */
inline fun <reified T : Activity> FragmentActivity.startActivityForResult(
  requestCode: Int,
  noinline onResult: OnResult
) = startActivityForResult<T>(Bundle(), requestCode, onResult)

/**
 * Starts Activity [T] with extras in [data], and the given [requestCode]. The result
 * is delivered to [onResult].
 */
inline fun <reified T : Activity> FragmentActivity.startActivityForResult(
  data: Bundle,
  requestCode: Int,
  noinline onResult: OnResult
) {
  val intent = Intent(this, T::class.java).putExtras(data)
  startActivityForResult(intent, requestCode, onResult)
}

/**
 * Starts an Activity that [intent] resolves to, with the given [requestCode]. The result
 * is delivered to [onResult].
 */
fun FragmentActivity.startActivityForResult(
  intent: Intent,
  requestCode: Int,
  onResult: OnResult
) {
  InlineActivityResult.instance()
      .schedule(
          fragmentManager = supportFragmentManager,
          intent = intent,
          requestCode = requestCode,
          onResult = onResult
      )
}
