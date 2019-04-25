package com.afollestad.inlineactivityresultsample

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_second.button1
import kotlinx.android.synthetic.main.activity_second.button2
import kotlinx.android.synthetic.main.activity_second.input

/** @author Aidan Follestad (@afollestad) */
class SecondActivity : AppCompatActivity() {

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