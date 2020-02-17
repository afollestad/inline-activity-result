## Inline Activity Result

[![Build Status](https://travis-ci.org/afollestad/inline-activity-result.svg?branch=master)](https://travis-ci.org/afollestad/inline-activity-result)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/4679f36623124f4da988e957e545c8df)](https://www.codacy.com/app/drummeraidan_50/inline-activity-result?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=afollestad/inline-activity-result&amp;utm_campaign=Badge_Grade)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

---

## Table of Contents

1. [Core](#core)
    1. [Gradle Dependency](#gradle-dependency)
    2. [Usage](#usage)
2. [Coroutines](#coroutines)
    1. [Gradle Dependency](#gradle-dependency-1)
    2. [Usage](#usage-1)
3. [RxJava)(#rxjava)
    1. [Gradle Dependency](#gradle-dependency-2)
    2. [Usage](#usage-2)

---

## Core

### Gradle Dependency

[ ![Download](https://api.bintray.com/packages/drummer-aidan/maven/inline-activity-result/images/download.svg) ](https://bintray.com/drummer-aidan/maven/inline-activity-result/_latestVersion)

```gradle
dependencies {
  ...
  implementation 'com.afollestad.inline-activity-result:core:0.2.0'
}
```

### Usage

You call `startActivityForResult`, providing the Activity to launch as the generic type. You
receive the result in a callback *without* having to override `onActivityResult`. And, you don't 
have to worry about `requestCode` or `resultCode`.

```kotlin
class NewActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val extras = Bundle()
        .putString("some_extra", "Hello, World!")

    startActivityForResult<OtherActivity>(extras) { success, data ->
      if (success) {
        toast("Got successful result: $data")
      }
    }
  }
}
```

---

There are multiple variants `startActivityForResult` you can use for different use cases. *All of 
them allow you to pass an optional `requestCode` parameter, but this should generally be unnecessary.*

First, the simplest you can get is just a generic type and the callback.

```kotlin
startActivityForResult<OtherActivity> { success, data ->
    // Do something
}
```

Second, you can provide a `Bundle` of extras to the destination Activity:

```kotlin
val extras = Bundle()
    .putString("some_extra", "Hello, World!")
startActivityForResult<OtherActivity>(extras) { success, data ->
    // Do something
}
    
```

And finally, you can use a full intent. In this variant you do not provide a generic parameter.

```kotlin
val intent = Intent(Intent.ACTION_VIEW)
    .setData("content://some-uri".toUri())
startActivityForResult(intent) { success, data ->
  // Do something
}
```

---

## Coroutines

### Gradle Dependency

[ ![Download](https://api.bintray.com/packages/drummer-aidan/maven/inline-activity-result/images/download.svg) ](https://bintray.com/drummer-aidan/maven/inline-activity-result/_latestVersion)

```gradle
dependencies {
  ...
  implementation 'com.afollestad.inline-activity-result:coroutines:0.2.0'
}
```

### Usage

You can use Kotlin coroutines to get rid of the callback. It of course is a suspend function so it
must be called within a coroutine scope.

Instead of `startActivityForResult`, you can use `startActivityAwaitResult`:

```kotlin
val result: ActivityResult = startActivityAwaitResult<OtherActivity>()
// use result...
```

---

## RxJava

### Gradle Dependency

[ ![Download](https://api.bintray.com/packages/drummer-aidan/maven/inline-activity-result/images/download.svg) ](https://bintray.com/drummer-aidan/maven/inline-activity-result/_latestVersion)

```gradle
dependencies {
  ...
  implementation 'com.afollestad.inline-activity-result:rxjava:0.2.0'
}
```

### Usage

You can use RxJava to integrate the Activity launch and result into your streams.

Instead of `startActivityForResult`, you can use `startActivityEmitResult`:

```kotlin
val disposable = startActivityEmitResult<OtherActivity>()
  .subscribe { result ->
     // use result...
  }

// make sure you dispose of the subscription when your Activity/Fragment goes away
disposable.dispose()
```