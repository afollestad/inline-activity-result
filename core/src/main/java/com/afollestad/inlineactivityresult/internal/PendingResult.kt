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

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.fragment.app.FragmentManager
import com.afollestad.inlineactivityresult.OnResult
import com.afollestad.inlineactivityresult.internal.Scheduler.Companion.getTag
import com.afollestad.inlineactivityresult.util.transact

/** @author Aidan Follestad (@afollestad) */
internal data class PendingResult(
  private var onResult: OnResult?,
  private var fragmentManager: FragmentManager?
) {
  fun deliverResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent
  ) {
    onResult?.invoke(resultCode == RESULT_OK, data)
    onResult = null
    removeFragment(requestCode)
  }

  private fun removeFragment(requestCode: Int) {
    val tag = getTag(requestCode)
    val fragment = fragmentManager?.findFragmentByTag(tag) ?: return
    fragmentManager?.transact { remove(fragment) }
    fragmentManager = null
  }
}
