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

import org.junit.Assert.fail
import kotlin.reflect.KClass

internal fun expectThrows(
  type: KClass<*>,
  message: String? = null,
  block: () -> Unit
) {
  try {
    block()
    fail("Block did not throw exception ${type.java.simpleName} as expected.")
  } catch (ex: Exception) {
    if (ex::class.java != type.java) {
      fail("Expected exception of type ${type.java.name} but was ${ex::class.java.name}")
    } else if (message != null && message != ex.message) {
      fail("Expected exception message: $message\nBut was: ${ex.message}")
    }
  }
}

internal inline fun <reified T : Any> Any.assertAs(): T {
  if (this is T) {
    return this
  }
  throw AssertionError("$this is not a ${T::class.java.name}")
}
