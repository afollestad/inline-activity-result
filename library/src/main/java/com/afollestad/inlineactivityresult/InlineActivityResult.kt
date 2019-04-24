package com.afollestad.inlineactivityresult

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.fragment.app.FragmentManager

typealias OnResult = (success: Boolean, data: Intent) -> Unit

/** @author Aidan Follestad (@afollestad) */
internal class InlineActivityResult {
  private val pending: MutableMap<Int, OnResult> = mutableMapOf()

  fun schedule(
    fragmentManager: FragmentManager,
    intent: Intent,
    requestCode: Int,
    onResult: OnResult
  ) {
    check(!pending.containsKey(requestCode)) {
      "There is already a pending request for request code $requestCode"
    }
    pending[requestCode] = onResult

    val fragment = InlineFragment.newInstance(
        launchIntent = intent,
        requestCode = requestCode
    )
    fragmentManager
        .beginTransaction()
        .add(fragment, FRAGMENT_TAG)
        .commit()
  }

  fun takeResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent
  ) {
    val pendingResult = pending[requestCode]
    requireNotNull(pendingResult) {
      "Couldn't find any pending result for request code $requestCode"
    }
    pendingResult(resultCode == RESULT_OK, data)
  }

  companion object {
    fun instance(): InlineActivityResult {
      return instance ?: InlineActivityResult().also { instance = it }
    }

    private var instance: InlineActivityResult? = null
    private const val FRAGMENT_TAG = "inline_activity_result"
  }
}
