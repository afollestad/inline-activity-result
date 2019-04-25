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
package com.afollestad.inlineactivityresult.internal

import android.content.Intent
import androidx.annotation.CheckResult
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.FragmentManager
import com.afollestad.inlineactivityresult.util.transact

/** @author Aidan Follestad (@afollestad) */
internal open class InlineActivityResult {
  @VisibleForTesting var pending: MutableMap<Int, PendingResult> = mutableMapOf()

  open fun schedule(
    fragmentManager: FragmentManager,
    intent: Intent,
    requestCode: Int,
    onResult: OnResult
  ) {
    check(!pending.containsKey(requestCode)) {
      "There is already a pending request for requestCode $requestCode."
    }
    pending[requestCode] = PendingResult(
        onResult = onResult,
        fragmentManager = fragmentManager
    )

    val fragment = InlineFragment.newInstance(
        launchIntent = intent,
        requestCode = requestCode
    )
    fragmentManager.transact {
      add(fragment, getTag(requestCode))
    }
  }

  fun deliverResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent
  ) {
    val pendingRequest = pending[requestCode]
        ?: throw IllegalStateException("There's no pending request for requestCode $requestCode.")
    pendingRequest.deliverResult(
        requestCode = requestCode,
        resultCode = resultCode,
        data = data
    )
    pending.remove(requestCode)
  }

  fun destroy() {
    pending.clear()
  }

  companion object {
    @VisibleForTesting var instanceCreator: (() -> InlineActivityResult)? = null

    @CheckResult fun instance(): InlineActivityResult {
      val defaultCreator = {
        instance ?: InlineActivityResult().also { instance = it }
      }
      return instanceCreator?.invoke() ?: defaultCreator()
    }

    @CheckResult fun getTag(requestCode: Int): String = "${FRAGMENT_TAG_PREFIX}_$requestCode"

    fun destroy() {
      instance?.destroy()
      instance = null
      instanceCreator = null
    }

    @VisibleForTesting const val FRAGMENT_TAG_PREFIX = "inline_activity_result_"
    private var instance: InlineActivityResult? = null
  }
}
