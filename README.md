<p align="center">
  <img src="https://github.com/iamjosephmj/flinger/blob/develop/repo-media/flinger.jpeg"
      width=25% 
       height=25%
       />
</p>

# Flinger (Only compatible with compose)

[![CircleCI Build Status](https://circleci.com/gh/willowtreeapps/spruce-android.svg?style=shield)](https://app.circleci.com/projects/project-setup/github/iamjosephmj/flinger/)
[![License MIT](https://img.shields.io/badge/License-MIT-blue.svg?style=flat)](https://github.com/iamjosephmj/flinger/blob/main/LICENSE)
[![Public Yes](https://img.shields.io/badge/Public-yes-green.svg?style=flat)]()
[![](https://jitpack.io/v/iamjosephmj/flinger.svg)](https://jitpack.io/#iamjosephmj/flinger)


## What is Flinger?

<p>

Flinger is a plugin that is made on top
of <a style = "color: white" href ="https://developer.android.com/jetpack/compose">`jetpack compose`</a>
that will help the developer to tweak the LazyList's fling behaviour. This Library will help the
developers to change the fling behaviours much easier without digging deep. Here is a prototype of
Flinger:
</p>

<p align="center">
  <img src="https://github.com/iamjosephmj/flinger/blob/develop/repo-media/flinger-demo.gif" 
     width="250" 
     height="600" 
    />
</p>

## Gradle

Add the following to your project's root build.gradle file

```groovy
repositories {
    maven {
        url "https://jitpack.io"
    }
}
```

Add the following to your project's build.gradle file

```groovy
dependencies {
    implementation 'com.github.iamjosephmj:flinger:1.0.0'
}
```

## Basics

<p>

By the launch of Jetpack compose at least some of you might have thought if did they port the old
scroll behaviour to it. The answer to that is YES, they had ported the old
<a style = "color: white" href ="https://developer.android.com/reference/android/widget/OverScroller">`Overscroller`</a>
behaviour to the latest compose. The Overscroller is the component that is responsible for flings in
ScrollView/RecyclerViews in Android. If we dig deeper into the implementation of the Overscroller,
you can see that Android uses a fixed set of values so that the flings will look almost similar
throughout different devices. The whole idea behind the creation of this library is that the
developers will have full access to all the internal parameters that governs the fling behaviour.
This library can be easily integrated with
the <a style = "color: white" href ="https://developer.android.com/jetpack/compose/lists">`LazyColumns`</a>
,
<a style = "color: white" href ="https://developer.android.com/jetpack/compose/lists">`LazyRows`</a>
,
<a style = "color: white" href ="https://developer.android.com/jetpack/compose/lists">`LazyLists`</a>
that is provided by compose.
</p>

## Usage

Refer to the Sample project under to get more insights about the implementation.

```kotlin

LazyColumn(
    flingBehavior = flingBehavior(

        ScrollViewConfiguration.Builder()
            /*
             * This variable manages the friction to the scrolls in the LazyColumn
             */
            .scrollViewFriction(0.008f)

            /*
             * This is the absolute value of a velocity threshold, below which the
             * animation is considered finished.
             */
            .absVelocityThreshold(0f)

            /*
             * Gravitational obstruction to the scroll.
             */
            .gravitationalForce(9.80665f)

            /*
             * Scroll Inches per meter
             */
            .inchesPerMeter(39.37f)

            /*
             * Rate of deceleration of the scrollView.
             */
            .decelerationRate((ln(0.78) / ln(0.9)).toFloat())

            /*
             * Friction at the time of deceleration.
             */
            .decelerationFriction(0.09f)

            /*
             * Inflection is the place where the start and end tension lines cross each other.
             */
            .splineInflection(0.1f)

            /*
             * Spline's start tension.
             */
            .splineStartTension(0.1f)

            /*
             * Spline's end tension.
             */
            .splineEndTension(1.0f)

            /*
             * number of sampling points in the spline
             */
            .numberOfSplinePoints(100)

            // builder pattern.
            .build(),
    )
)
{
    // Columns/Rows
}

```

## Stock Behaviours

If you are not comfortable with tweaking values, we provide you some pre-defined methods that can be
used to bring the behaviour to your project, you can refer to
<a style = "color: white" href ="https://github.com/iamjosephmj/flinger/blob/main/flinger/src/main/java/io/iamjosephmj/flinger/bahaviours/StockFlingBehaviours.kt">`StockFlingBehaviours.kt`</a>

## Custom Behaviours

If you are interested in designing your own behaviours for the Flings, you can tryout different
possibilities in
the <a style = "color: white" href ="https://github.com/iamjosephmj/flinger/tree/develop/apk/">`Flinger app`</a>

## Contribution, Issues or Future Ideas

If part of Flinger is not working correctly be sure to file a Github issue. In the issue provide as
many details as possible. This could include example code or the exact steps that you did so that
everyone can reproduce the issue. Sample projects are always the best way :). This makes it easy for
our developers or someone from the open-source community to start working!

If you have a feature idea submit an issue with a feature request or submit a pull request and we
will work with you to merge it in!

## Contribution guidelines

Contributions are more than welcome!
- You should make sure that all the test are working properly.
- You should raise a PR to `develop` branch
- Anyone can contribute fling behaviours
to <a style = "color: white" href ="https://github.com/iamjosephmj/flinger/blob/main/flinger/src/main/java/io/iamjosephmj/flinger/bahaviours/StockFlingBehaviours.kt">`StockFlingBehaviours.kt`</a>
. 
- Before you raise a PR please make sure your code had no issue from Android studio lint analyzer.  

