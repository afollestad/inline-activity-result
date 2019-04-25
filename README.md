## Inline Activity Result

[![Build Status](https://travis-ci.org/afollestad/inline-activity-result.svg?branch=master)](https://travis-ci.org/afollestad/inline-activity-result)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/4679f36623124f4da988e957e545c8df)](https://www.codacy.com/app/drummeraidan_50/inline-activity-result?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=afollestad/inline-activity-result&amp;utm_campaign=Badge_Grade)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## What does it do?

### WITH this library

You call `startActivityForResult`, providing an Activity to launch as the generic type. You 
receive the result in a callback *without* having to override `onActivityResult`. And, you don't 
have to worry about requestCode or resultCode.

```kotlin
class NewActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val extras = Bundle().apply {
      putString("some_extra", "Hello, World!")
    }
    startActivityForResult<OtherActivity>(extras) { success, data ->
      if (success) {
        toast("Got successful result!")
      }
    }
  }
}
```

### WithOUT this library

Well, the code speaks for itself.

```kotlin
class OldActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val intent = Intent(this, OtherActivity::class.java)
        .putExtra("some_extra", "Hello, World!")
    startActivityForResult(intent, REQUEST_CODE)
  }

  override fun onActivityResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent?
  ) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
      toast("Got successful result!")
    }
  }

  companion object {
    private const val REQUEST_CODE = 69
  }
}
```

### What's the big deal?

You do not have to override `onActivityResult` at all. All of your results are received inline. 
This may not seem like a big deal with the sample above, but it gets more valuable as you start to 
have more than one possible result. This can all be used from within a `Fragment` as well. 

---

## Variants of startActivityForResult

There are multiple variants `startActivityForResult` you can use for different use cases. *All of 
them allow you to pass an optional `requestCode` parameter, but this should generally be unnecessary.*

### Simple

The simplest you can get is just a generic type and the callback.

```kotlin
startActivityForResult<OtherActivity> { success, data ->
    // Do something
}
```

### With Extras

You can provide a `Bundle` of extras to the destination Activity:

```kotlin
val extras = Bundle().apply {
    putString("some_extra", "Hello, World!")
}
startActivityForResult<OtherActivity>(extras) { success, data ->
    // Do something
}
    
```

### Full Intent

And finally, you can use a full intent. In this variant you do not provide a generic parameter.

```kotlin
val intent = Intent(Intent.ACTION_VIEW).apply {
    setData("content://some-uri".toUri())
}
startActivityForResult(intent) { success, data ->
  // Do something
}
```