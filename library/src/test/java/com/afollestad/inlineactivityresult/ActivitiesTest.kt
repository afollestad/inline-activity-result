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

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.afollestad.inlineactivityresult.internal.InlineActivityResult
import com.afollestad.inlineactivityresult.util.NoManifestTestRunner
import com.afollestad.inlineactivityresult.util.TestOnResult
import com.afollestad.inlineactivityresult.util.singleValue
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
class ActivitiesTest {
  private val instance = mock<InlineActivityResult>()
  private val fragmentManager = mock<FragmentManager>()
  private val activity = mock<FragmentActivity> {
    on { packageName } doReturn "com.afollestad.inlineactivityresult"
    on { supportFragmentManager } doReturn fragmentManager
  }

  @Before fun setup() {
    // Force a mocked instance to be returned by instance()
    InlineActivityResult.instanceCreator = { instance }
  }

  @After fun cleanup() {
    InlineActivityResult.destroy()
  }

  @Test fun `startActivityForResult - full intent`() {
    val intent = Intent("com.testing.TEST")
    val requestCode = 69
    val onResult = TestOnResult()

    activity.startActivityForResult(
        intent = intent,
        requestCode = requestCode,
        onResult = onResult.capture()
    )
    onResult.assertNoValues()

    verify(instance).schedule(
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

    activity.startActivityForResult<PlaceholderActivity>(
        extras = extras,
        requestCode = requestCode,
        onResult = onResult.capture()
    )

    val intentCaptor = argumentCaptor<Intent>()
    verify(instance).schedule(
        eq(fragmentManager),
        intentCaptor.capture(),
        eq(requestCode),
        eq(onResult.capture())
    )

    val intent = intentCaptor.singleValue
    val component = intent.component!!

    assertThat(component.packageName).isEqualTo("com.afollestad.inlineactivityresult")
    assertThat(component.className).isEqualTo(
        "com.afollestad.inlineactivityresult.PlaceholderActivity"
    )
    assertThat(intent.hasExtra("test")).isTrue()
    assertThat(intent.extras!!.getString("test")).isEqualTo("hello")
  }

  @Test fun `startActivityForResult - no extras`() {
    val requestCode = 34
    val onResult = TestOnResult()

    activity.startActivityForResult<PlaceholderActivity>(
        requestCode = requestCode,
        onResult = onResult.capture()
    )

    val intentCaptor = argumentCaptor<Intent>()
    verify(instance).schedule(
        eq(fragmentManager),
        intentCaptor.capture(),
        eq(requestCode),
        eq(onResult.capture())
    )

    val intent = intentCaptor.singleValue
    val component = intent.component!!

    assertThat(component.packageName).isEqualTo("com.afollestad.inlineactivityresult")
    assertThat(component.className).isEqualTo(
        "com.afollestad.inlineactivityresult.PlaceholderActivity"
    )
    assertThat(intent.extras!!.isEmpty).isTrue()
  }
}

internal class PlaceholderActivity : Activity()
