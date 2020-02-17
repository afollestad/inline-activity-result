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
package com.afollestad.inlineactivityresultsample

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

/** @author Aidan Follestad (@afollestad) */
class SecondActivity : AppCompatActivity() {
  private val button1 by lazy { findViewById<Button>(R.id.button1) }
  private val button2 by lazy { findViewById<Button>(R.id.button2) }
  private val input by lazy { findViewById<EditText>(R.id.input) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_second)

    supportActionBar?.apply {
      setDisplayHomeAsUpEnabled(true)
      setHomeAsUpIndicator(R.drawable.ic_close)
    }

    button1.setOnClickListener {
      finishWithResult(true)
    }
    button2.setOnClickListener {
      finishWithResult(false)
    }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == android.R.id.home) {
      finish()
      return true
    }
    return super.onOptionsItemSelected(item)
  }

  private fun finishWithResult(success: Boolean) {
    setResult(
        if (success) RESULT_OK else RESULT_CANCELED,
        Intent().putExtra("input", input.text.toString())
    )
    finish()
  }
}
