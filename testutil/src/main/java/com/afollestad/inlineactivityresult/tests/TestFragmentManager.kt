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
@file:Suppress("unused")

package com.afollestad.inlineactivityresult.tests

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.isA
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.jetbrains.annotations.TestOnly

class TestFragmentManager {
  val fragmentTransaction = mock<FragmentTransaction>()
  val fragmentManager = mock<FragmentManager> {
    on { beginTransaction() } doReturn fragmentTransaction
  }

  init {
    whenever(fragmentTransaction.add(isA<Fragment>(), anyOrNull()))
        .doReturn(fragmentTransaction)
    whenever(fragmentTransaction.remove(isA()))
        .doReturn(fragmentTransaction)
  }

  @TestOnly fun assertFragmentRemoved(fragment: Fragment) {
    verify(fragmentTransaction).remove(fragment)
    verify(fragmentTransaction, atLeastOnce()).commit()
  }
}

@TestOnly
inline fun <reified T : Fragment> TestFragmentManager.assertFragmentAdded(): Pair<Fragment, String> {
  val fragmentCaptor = argumentCaptor<Fragment>()
  val tagCaptor = argumentCaptor<String>()
  verify(fragmentTransaction).add(fragmentCaptor.capture(), tagCaptor.capture())
  verify(fragmentTransaction, atLeastOnce()).commit()

  val fragment = fragmentCaptor.singleValue.assertAs<T>()
  val tag = tagCaptor.singleValue
  whenever(fragmentManager.findFragmentByTag(tag)).doReturn(fragment)

  return Pair(fragment, tag)
}
