/**
 * Designed and developed by Aidan Follestad (@afollestad)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
