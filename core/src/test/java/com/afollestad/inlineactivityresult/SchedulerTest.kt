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

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import com.afollestad.inlineactivityresult.internal.InlineFragment
import com.afollestad.inlineactivityresult.internal.Scheduler
import com.afollestad.inlineactivityresult.internal.Scheduler.Companion.FRAGMENT_TAG_PREFIX
import com.afollestad.inlineactivityresult.tests.NoManifestTestRunner
import com.afollestad.inlineactivityresult.tests.TestFragmentManager
import com.afollestad.inlineactivityresult.tests.TestOnResult
import com.afollestad.inlineactivityresult.tests.assertFragmentAdded
import com.afollestad.inlineactivityresult.tests.expectThrows
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(NoManifestTestRunner::class)
class SchedulerTest {
  private val testFragmentManager = TestFragmentManager()
  private val intent = Intent("com.action.TEST")

  @After fun cleanup() {
    Scheduler.destroy()
  }

  @Test fun `schedule and deliver result - success`() {
    val requestCode = 69
    val onResult = TestOnResult()

    val instance = Scheduler.get()
        .apply {
          schedule(
              fragmentManager = testFragmentManager.fragmentManager,
              intent = intent,
              requestCode = requestCode,
              onResult = onResult.capture()
          )
        }
    assertThat(instance.pending).containsKey(requestCode)

    val (fragment, tag) = testFragmentManager.assertFragmentAdded<InlineFragment>()
    assertThat(tag).isEqualTo("${FRAGMENT_TAG_PREFIX}_$requestCode")

    // Try to deliver result with mismatched requestCodex
    expectThrows<IllegalStateException>("There's no pending request for requestCode 12313.") {
      instance.deliverResult(12313, RESULT_OK, intent)
    }

    // Now successfully deliver a result
    instance.deliverResult(requestCode, RESULT_OK, intent)
    testFragmentManager.assertFragmentRemoved(fragment)
    assertThat(instance.pending).doesNotContainKey(requestCode)

    // Assert the callback invocation
    onResult.assertValues(true, intent)
  }

  @Test fun `schedule and deliver result - failure`() {
    val requestCode = 70
    val onResult = TestOnResult()

    val instance = Scheduler.get()
        .apply {
          schedule(
              fragmentManager = testFragmentManager.fragmentManager,
              intent = intent,
              requestCode = requestCode,
              onResult = onResult.capture()
          )
        }
    assertThat(instance.pending).containsKey(requestCode)

    val (fragment, tag) = testFragmentManager.assertFragmentAdded<InlineFragment>()
    assertThat(tag).isEqualTo("${FRAGMENT_TAG_PREFIX}_$requestCode")

    // Now successfully deliver a result
    instance.deliverResult(requestCode, RESULT_CANCELED, intent)
    testFragmentManager.assertFragmentRemoved(fragment)
    assertThat(instance.pending).doesNotContainKey(requestCode)

    // Assert the callback invocation
    onResult.assertValues(false, intent)
  }
}
