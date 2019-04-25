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
package com.afollestad.inlineactivityresult.util

import android.content.Intent
import androidx.annotation.CheckResult
import com.afollestad.inlineactivityresult.internal.OnResult
import org.junit.Assert.fail

internal class TestOnResult {
  private var success: Boolean? = null
  private var data: Intent? = null
  private val capture: OnResult = { success, data ->
    this.success = success
    this.data = data
  }

  @CheckResult fun capture(): OnResult = capture

  fun assertValues(
    success: Boolean,
    data: Intent
  ) {
    if (this.success == null || this.data == null) {
      fail("No invocations have been received.")
    } else if (this.success != success) {
      fail("Expected success=$success\nActual: ${this.success}")
    } else if (this.data != data) {
      fail("Expected data=$data\nActual: ${this.data}")
    }
    this.data = null
    this.success = null
  }

  fun assertNoValues() {
    if (success != null || data != null) {
      fail("Expected no values but got success=$success, data=$data")
    }
  }
}
