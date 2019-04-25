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

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.CheckResult
import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY
import androidx.fragment.app.Fragment

/** @author Aidan Follestad (@afollestad) */
@RestrictTo(LIBRARY)
class InlineFragment : Fragment() {

  override fun onAttach(context: Context) {
    super.onAttach(context)
    if (!didStart()) {
      startActivityForResult(
          launchIntent(),
          requestCode()
      )
      didStart(true)
    }
  }

  override fun onActivityResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent?
  ) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == requestCode()) {
      InlineActivityResult.instance()
          .deliverResult(
              requestCode = requestCode,
              resultCode = resultCode,
              data = data ?: Intent()
          )
    }
  }

  @CheckResult private fun didStart(): Boolean {
    return arguments?.getBoolean(
        KEY_DID_START
    ) ?: throw IllegalStateException(
        "No arguments provided"
    )
  }

  private fun didStart(didStart: Boolean) {
    arguments?.putBoolean(
        KEY_DID_START, didStart
    ) ?: throw IllegalStateException(
        "No launch intent provided"
    )
  }

  @CheckResult private fun launchIntent(): Intent {
    return arguments?.getParcelable(
        KEY_LAUNCH_INTENT
    ) ?: throw IllegalStateException(
        "No launch intent provided"
    )
  }

  @CheckResult private fun requestCode(): Int {
    return arguments?.getInt(
        KEY_REQUEST_CODE, 0
    )?.takeIf { it != 0 }
        ?: throw IllegalStateException("No non-zero request code provided")
  }

  companion object {
    @CheckResult fun newInstance(
      launchIntent: Intent,
      requestCode: Int
    ): InlineFragment {
      return InlineFragment()
          .apply {
            arguments = Bundle().apply {
              putParcelable(
                  KEY_LAUNCH_INTENT, launchIntent
              )
              putInt(
                  KEY_REQUEST_CODE, requestCode
              )
            }
          }
    }

    private const val KEY_LAUNCH_INTENT = "launch_intent"
    private const val KEY_REQUEST_CODE = "request_code"
    private const val KEY_DID_START = "did_start"
  }
}
