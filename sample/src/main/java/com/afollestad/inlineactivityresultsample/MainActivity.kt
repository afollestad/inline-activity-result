package com.afollestad.inlineactivityresultsample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.inlineactivityresult.startActivityForResult
import kotlinx.android.synthetic.main.activity_main.button

/** @author Aidan Follestad (@afollestad) */
class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    button.setOnClickListener {
      startActivityForResult<SecondActivity>(20) { success, data ->
        toast("Success? $success\nInput: ${data.getStringExtra("input")}")
      }
    }
  }
}
