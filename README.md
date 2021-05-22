<p align="center">
  <img src="https://github.com/iamjosephmj/flinger/blob/develop/repo-media/flinger.jpeg" />
</p>

# Flinger

[![License MIT](https://img.shields.io/badge/License-MIT-blue.svg?style=flat)]()
[![Public Yes](https://img.shields.io/badge/Public-yes-green.svg?style=flat)]()

## What is Flinger?

<p>

Flinger is a plugin that is made on top
of <a style = "color: white" href ="https://developer.android.com/jetpack/compose">`jetpack compose`</a>
that will help the developer to tweak the LazyColumn's fling behaviour. This Library will help the
developers to change the fling behaviours much easier without digging deep. Here is a prototype of
Flinger:
</p>

<p align="center">
  <img src="https://github.com/iamjosephmj/flinger/blob/develop/repo-media/flinger-demo.gif" 
     width="250" 
     height="500" 
    />
</p>

## Gradle

Add the following to your project's root build.gradle file

```groovy
repositories {
    maven {
        url "tbd"
    }
}
```

Add the following to your project's build.gradle file

```groovy
dependencies {
    implementation 'tbd'
}
```

## Basics

<p>
By the launch of Jetpack compose at least some of you might have thought if did they port the old 
scroll behaviour to it. The answer to that is YES, they had ported the old 
<a style = "color: white" href ="https://developer.android.com/reference/android/widget/OverScroller">`Overscroller`</a> behaviour 
to the latest compose. The Overscroller is the component that is responsible for flings in 
ScrollView/RecyclerViews in Android. If we dig deeper into the implementation of the Overscroller, 
you can see that Android uses a fixed set of values so that the flings will look almost similar 
throughout different devices. The whole idea behind the creation of this library is that the 
developers will have full access to all the internal parameters that governs the fling behaviour.
</p>



