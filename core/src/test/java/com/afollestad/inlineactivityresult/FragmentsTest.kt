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

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.afollestad.inlineactivityresult.internal.Scheduler
import com.afollestad.inlineactivityresult.tests.NoManifestTestRunner
import com.afollestad.inlineactivityresult.tests.TestOnResult
import com.afollestad.inlineactivityresult.tests.singleValue
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(NoManifestTestRunner::class)
class FragmentsTest {
  private val scheduler = mock<Scheduler>()
  private val fragmentManager = mock<FragmentManager>()
  private val activity = mock<FragmentActivity> {
    on { packageName } doReturn "com.afollestad.inlineactivityresult"
    on { supportFragmentManager } doReturn fragmentManager
  }
  private val fragment = mock<Fragment> {
    on { context } doReturn activity
    on { activity } doReturn activity
    on { fragmentManager } doReturn fragmentManager
    on { isAdded } doReturn true
  }

  @Before fun setup() {
    // Force a mocked instance to be returned by instance()
    Scheduler.instanceCreator = { scheduler }
  }

  @After fun cleanup() {
    Scheduler.destroy()
  }

  @Test fun `startActivityForResult - full intent`() {
    val intent = Intent("com.testing.TEST")
    val requestCode = 69
    val onResult = TestOnResult()

    fragment.startActivityForResult(
        intent = intent,
        requestCode = requestCode,
        onResult = onResult.capture()
    )
    onResult.assertNoValues()

    verify(scheduler).schedule(
        fragmentManager,
        intent,
        requestCode,
        onResult.capture()
    )
  }

  @Test fun `startActivityForResult - with extras`() {
    val extras = Bundle().apply { putString("test", "hello") }
    val requestCode = 34
    val onResult = TestOnResult()

    fragment.startActivityForResult<PlaceholderActivity>(
        extras = extras,
        requestCode = requestCode,
        onResult = onResult.capture()
    )

    val intentCaptor = argumentCaptor<Intent>()
    verify(scheduler).schedule(
        eq(fragmentManager),
        intentCaptor.capture(),
        eq(requestCode),
        eq(onResult.capture())
    )

    val intent = intentCaptor.singleValue
    val component = intent.component!!

    assertThat(component.packageName).isEqualTo("com.afollestad.inlineactivityresult")
    assertThat(component.className)
        .isEqualTo("com.afollestad.inlineactivityresult.PlaceholderActivity")
    assertThat(intent.hasExtra("test")).isTrue()
    assertThat(intent.extras!!.getString("test")).isEqualTo("hello")
  }

  @Test fun `startActivityForResult - no extras`() {
    val requestCode = 34
    val onResult = TestOnResult()

    fragment.startActivityForResult<PlaceholderActivity>(
        requestCode = requestCode,
        onResult = onResult.capture()
    )

    val intentCaptor = argumentCaptor<Intent>()
    verify(scheduler).schedule(
        eq(fragmentManager),
        intentCaptor.capture(),
        eq(requestCode),
        eq(onResult.capture())
    )

    val intent = intentCaptor.singleValue
    val component = intent.component!!

    assertThat(component.packageName).isEqualTo("com.afollestad.inlineactivityresult")
    assertThat(component.className)
        .isEqualTo("com.afollestad.inlineactivityresult.PlaceholderActivity")
    assertThat(intent.extras!!.isEmpty).isTrue()
  }
}
