<p align="center">
  <img src="https://github.com/iamjosephmj/flinger/blob/main/repo-media/flinger.jpeg" width="600" alt="Flinger Banner"/>
</p>

<p align="center">
  <b>Customizable Fling Physics for Jetpack Compose</b><br/>
  Take full control of scroll momentum in LazyColumn, LazyRow, Pagers, and more.
</p>

<p align="center">
  <a href="https://android-arsenal.com/api?level=21"><img src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat" alt="API 21+"/></a>
  <a href="https://kotlinlang.org"><img src="https://img.shields.io/badge/Kotlin-2.0%2B-purple.svg?style=flat" alt="Kotlin 2.0+"/></a>
  <a href="https://developer.android.com/jetpack/compose"><img src="https://img.shields.io/badge/Jetpack%20Compose-BOM%202025-blue.svg?style=flat" alt="Compose BOM 2025"/></a>
  <a href="https://jitpack.io/#iamjosephmj/flinger"><img src="https://jitpack.io/v/iamjosephmj/flinger.svg" alt="JitPack"/></a>
  <a href="https://github.com/iamjosephmj/flinger/blob/main/LICENSE"><img src="https://img.shields.io/badge/License-MIT-blue.svg?style=flat" alt="License MIT"/></a>
</p>

<p align="center">
  <a href="https://github.com/iamjosephmj/flinger/stargazers"><img src="https://img.shields.io/github/stars/iamjosephmj/flinger?style=social" alt="GitHub Stars"/></a>
  <a href="https://android-arsenal.com/details/1/8249"><img src="https://img.shields.io/badge/Android%20Arsenal-Flinger-green.svg?style=flat" alt="Android Arsenal"/></a>
  <a href="https://jetc.dev/issues/067.html"><img src="https://img.shields.io/badge/As_Seen_In-jetc.dev_Newsletter_%2367-blue?logo=Jetpack+Compose&logoColor=white" alt="jetc.dev Newsletter"/></a>
  <a href="#privacy"><img src="https://img.shields.io/badge/Privacy-GDPR_Compliant-brightgreen.svg?style=flat" alt="GDPR Compliant"/></a>
</p>

---

## ✨ Key Features

| Feature | Description |
|:--------|:------------|
| **9+ Built-in Presets** | iOS-style, bouncy, floaty, snappy, ultra-smooth, and more |
| **Full Physics Control** | Customize friction, gravity, tension, deceleration curves |
| **Snap-to-Item** | Carousel/gallery snap behavior with smooth fusion |
| **Adaptive Fling** | Velocity-aware physics that adapts to swipe intensity |
| **Accessibility** | System-aware presets respecting "Reduce Motion" |
| **Debug Overlay** | Real-time fling visualization for development |
| **All Lazy Layouts** | Works with LazyColumn, LazyRow, Grids, and Pagers |

---

## 🎬 See It In Action

https://github.com/user-attachments/assets/f57c2071-ff15-4416-9bc3-fd5c85c4d956

---

## 🚀 Quick Start

```kotlin
// Just add flingBehavior to any scrollable Composable
LazyColumn(
    flingBehavior = flingBehavior()  // That's it!
) {
    items(100) { Text("Item $it") }
}
```

**Or use a preset:**

```kotlin
LazyColumn(flingBehavior = FlingPresets.iOSStyle())  // iOS-like scroll
LazyColumn(flingBehavior = FlingPresets.bouncy())    // Playful bounce
LazyColumn(flingBehavior = FlingPresets.floaty())    // Long glide
```

---

## Table of Contents

- [Installation](#installation)
- [Why Flinger?](#why-flinger)
- [Usage Examples](#usage-examples)
- [Configuration Parameters](#configuration-parameters)
- [Preset Behaviors](#preset-behaviors)
- [Advanced Features](#advanced-features)
- [Sample App & Demos](#sample-app--demos)
- [Compatibility](#compatibility)
- [Migration Guide](#migration-guide)
- [Contributing](#contributing)
- [Roadmap](#roadmap)
- [Community & Support](#community--support)
- [License](#license)
- [Privacy](#privacy)

### Additional Documentation

| Document | Description |
|:---------|:------------|
| [Physics Tuning Guide](docs/PHYSICS_TUNING.md) | Deep dive into all physics parameters with visual diagrams and recipes |
| [Migration Guide](MIGRATION.md) | Upgrade from 1.x to 2.0 |
| [Contributing](CONTRIBUTING.md) | How to contribute to Flinger |

---

## Installation

### Gradle (Kotlin DSL)

**Step 1:** Add JitPack repository to your `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

**Step 2:** Add the dependency to your module's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.iamjosephmj:flinger:2.0.2")
}
```

### Gradle (Groovy)

**Step 1:** Add JitPack repository to your root `build.gradle`:

```groovy
allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

**Step 2:** Add the dependency:

```groovy
dependencies {
    implementation 'com.github.iamjosephmj:flinger:2.0.2'
}
```

### Version Catalog

Add to your `libs.versions.toml`:

```toml
[versions]
flinger = "2.0.2"

[libraries]
flinger = { module = "com.github.iamjosephmj:flinger", version.ref = "flinger" }
```

Then in your `build.gradle.kts`:

```kotlin
dependencies {
    implementation(libs.flinger)
}
```

---

## Why Flinger?

Android's default fling behavior uses fixed physics that can't be customized. Flinger gives you complete control over scroll momentum, letting you create unique, polished scroll experiences.

| Default Compose Fling | With Flinger |
|:---------------------:|:------------:|
| Fixed scroll physics | **Fully customizable** |
| One-size-fits-all | **Platform-specific feel** (iOS, custom) |
| No control over deceleration | **Fine-tune friction, gravity, tension** |
| Hard to create unique UX | **Create signature scroll experiences** |

### Use Cases

- **Cross-platform apps** - Match iOS scroll physics for consistency
- **Photo galleries** - Create buttery-smooth, long-glide scrolling
- **Text-heavy lists** - Build snappy, precise interactions with quick stops
- **Branded experiences** - Design unique scroll behaviors that match your app's personality
- **Games & creative apps** - Implement physics-based scrolling effects

---

## Usage Examples

### Basic Usage - Default Smooth Scroll

```kotlin
import io.iamjosephmj.flinger.flings.flingBehavior

LazyColumn(
    flingBehavior = flingBehavior()
) {
    items(100) { index ->
        ListItem(text = "Item $index")
    }
}
```

### Using Preset Behaviors

Flinger includes several pre-configured behaviors for common use cases:

```kotlin
import io.iamjosephmj.flinger.behaviours.FlingPresets

// Smooth, balanced scrolling (default)
LazyColumn(flingBehavior = FlingPresets.smooth())

// iOS-style scrolling with higher friction
LazyColumn(flingBehavior = FlingPresets.iOSStyle())

// Modified spline for unique feel
LazyColumn(flingBehavior = FlingPresets.smoothCurve())

// Quick deceleration for precision
LazyColumn(flingBehavior = FlingPresets.quickStop())

// Bouncy, playful feel
LazyColumn(flingBehavior = FlingPresets.bouncy())

// Long, floaty scrolls for galleries
LazyColumn(flingBehavior = FlingPresets.floaty())

// Snappy, responsive feel
LazyColumn(flingBehavior = FlingPresets.snappy())

// Ultra-smooth for premium apps
LazyColumn(flingBehavior = FlingPresets.ultraSmooth())

// Native Android behavior (for comparison)
LazyColumn(flingBehavior = FlingPresets.androidNative())
```

### Custom Configuration

Fine-tune fling physics with the configuration builder:

```kotlin
import io.iamjosephmj.flinger.configs.FlingConfiguration
import io.iamjosephmj.flinger.flings.flingBehavior

LazyColumn(
    flingBehavior = flingBehavior(
        scrollConfiguration = FlingConfiguration.Builder()
            .scrollViewFriction(0.008f)      // Lower = longer scroll
            .decelerationFriction(0.09f)     // Higher = quicker stop
            .splineInflection(0.1f)          // Higher = bouncier
            .build()
    )
) {
    items(100) { index ->
        ListItem(text = "Item $index")
    }
}
```

> 💡 **Tip:** See the **[Physics Tuning Guide](docs/PHYSICS_TUNING.md)** for detailed explanations of all parameters and ready-to-use recipes for common scenarios like iOS-style, photo galleries, and snappy UIs.

### Works With All Lazy Layouts

Flinger works seamlessly with all Compose scrollable components:

```kotlin
// LazyRow
LazyRow(flingBehavior = flingBehavior()) {
    items(50) { HorizontalCard(it) }
}

// LazyVerticalGrid
LazyVerticalGrid(
    columns = GridCells.Fixed(2),
    flingBehavior = flingBehavior()
) {
    items(100) { GridItem(it) }
}

// LazyHorizontalGrid
LazyHorizontalGrid(
    rows = GridCells.Fixed(3),
    flingBehavior = flingBehavior()
) {
    items(100) { GridItem(it) }
}

// LazyVerticalStaggeredGrid
LazyVerticalStaggeredGrid(
    columns = StaggeredGridCells.Fixed(2),
    flingBehavior = flingBehavior()
) {
    items(100) { StaggeredItem(it) }
}
```

### Dynamic Configuration

Change fling behavior based on state:

```kotlin
@Composable
fun DynamicFlingList(isPrecisionMode: Boolean) {
    val flingConfig = remember(isPrecisionMode) {
        if (isPrecisionMode) {
            // Quick stop for precise selection
            FlingConfiguration.Builder()
                .decelerationFriction(0.5f)
                .scrollViewFriction(0.04f)
                .build()
        } else {
            // Smooth, long scrolls
            FlingConfiguration.Builder()
                .decelerationFriction(0.015f)
                .scrollViewFriction(0.008f)
                .build()
        }
    }
    
    LazyColumn(
        flingBehavior = flingBehavior(scrollConfiguration = flingConfig)
    ) {
        items(100) { ListItem(it) }
    }
}
```

---

## Configuration Parameters

The most commonly adjusted parameters:

| Parameter | Default | What It Does |
|:----------|:-------:|:-------------|
| `scrollViewFriction` | `0.008` | **Primary friction control.** Lower = longer scroll distance |
| `decelerationFriction` | `0.09` | **Stopping behavior.** Higher = quicker stop |
| `splineInflection` | `0.1` | **Curve shape.** Higher = bouncier feel |

All parameters at a glance:

| Parameter | Default | Quick Description |
|:----------|:-------:|:------------------|
| `scrollViewFriction` | `0.008` | Resistance during scrolling |
| `decelerationFriction` | `0.09` | Friction during deceleration |
| `splineInflection` | `0.1` | Momentum curve transition point |
| `splineStartTension` | `0.1` | Launch feel (initial momentum) |
| `splineEndTension` | `1.0` | Landing feel (final momentum) |
| `decelerationRate` | `2.358` | Overall deceleration speed |
| `gravitationalForce` | `9.8` | Simulated gravity |
| `absVelocityThreshold` | `0` | Minimum velocity to trigger fling |
| `numberOfSplinePoints` | `100` | Animation smoothness |

> 📖 **Want to master these parameters?** See the **[Physics Tuning Guide](docs/PHYSICS_TUNING.md)** for detailed explanations, visual diagrams, and tuning recipes.

---

## Preset Behaviors

Flinger includes pre-configured presets for common scenarios:

| Preset | Best For | Feel |
|:-------|:---------|:-----|
| `smooth()` | General purpose | Balanced, natural |
| `iOSStyle()` | Cross-platform apps | iOS-like, controlled |
| `quickStop()` | Text & reading | Precise, quick stop |
| `floaty()` | Photo galleries | Long, gliding scrolls |
| `bouncy()` | Games & playful UIs | Bouncy, responsive |
| `snappy()` | E-commerce | Quick, responsive |
| `ultraSmooth()` | Premium apps | Buttery smooth |
| `smoothCurve()` | Creative apps | Unique curve feel |
| `androidNative()` | Comparison | Stock Android |

### Creating Your Own Presets

```kotlin
object MyFlingPresets {
    @Composable
    fun galleryScroll() = flingBehavior(
        scrollConfiguration = FlingConfiguration.Builder()
            .scrollViewFriction(0.006f)    // Long, floaty scrolls
            .decelerationFriction(0.02f)   // Smooth deceleration
            .build()
    )
    
    @Composable
    fun precisionList() = flingBehavior(
        scrollConfiguration = FlingConfiguration.Builder()
            .scrollViewFriction(0.05f)     // Shorter scrolls
            .decelerationFriction(0.3f)    // Quick stop
            .absVelocityThreshold(30f)     // Ignore tiny swipes
            .build()
    )
}
```

> 📖 **More recipes** in the **[Physics Tuning Guide](docs/PHYSICS_TUNING.md#tuning-recipes)** — includes iOS-style, bouncy, ultra-premium, and accessibility configurations.

---

## Advanced Features

### Snap-to-Item Behavior

Create carousel-like experiences where items snap into place:

```kotlin
import io.iamjosephmj.flinger.snap.snapFlingBehavior
import io.iamjosephmj.flinger.snap.SnapPosition

val listState = rememberLazyListState()

LazyRow(
    state = listState,
    flingBehavior = snapFlingBehavior(
        lazyListState = listState,
        snapPosition = SnapPosition.Center,  // Snap to center, start, or end
        smoothFusion = true,  // Smooth transition into snap
        fusionVelocityRatio = 0.15f  // When to start blending
    )
) {
    items(20) { PhotoCard(it) }
}
```

### Adaptive Fling Behavior

Automatically adjust physics based on fling velocity:

```kotlin
import io.iamjosephmj.flinger.adaptive.adaptiveFlingBehavior
import io.iamjosephmj.flinger.adaptive.AdaptiveMode

LazyColumn(
    flingBehavior = adaptiveFlingBehavior(
        mode = AdaptiveMode.Balanced  // or Precise, Momentum
    )
) {
    items(100) { ListItem(it) }
}
```

### Accessibility-Aware Presets

Respect system accessibility settings:

```kotlin
import io.iamjosephmj.flinger.behaviours.FlingPresets

// Automatically adapts to system "Reduce Motion" setting
LazyColumn(flingBehavior = FlingPresets.accessibilityAware())

// Explicitly reduced motion
LazyColumn(flingBehavior = FlingPresets.reducedMotion())
```

### Fling Callbacks

Monitor fling lifecycle events:

```kotlin
import io.iamjosephmj.flinger.callbacks.FlingCallbacks

val callbacks = FlingCallbacks(
    onFlingStart = { velocity -> println("Fling started: $velocity") },
    onFlingEnd = { println("Fling ended") }
)

LazyColumn(
    flingBehavior = flingBehavior(callbacks = callbacks)
)
```

---

## Sample App & Demos

Experience Flinger's capabilities firsthand with our sample app:

**[Download Sample APK](https://github.com/iamjosephmj/flinger/tree/main/apk)**

### App Features

- **Interactive Playground** - Adjust all parameters in real-time
- **Preset Gallery** - Compare all built-in presets side-by-side
- **Native vs Flinger** - See the difference between default and customized scrolling
- **Snap Gallery Demo** - Experience snap-to-item behavior
- **Pager Demo** - Custom pager physics
- **Debug Overlay** - Visualize fling physics in real-time

### Demo Videos

<details>
<summary><b>🎯 Snap Behavior</b></summary>
<br/>

https://github.com/user-attachments/assets/b0ced30a-f6df-4407-a640-c85c57a9ba8c

</details>

<details>
<summary><b>⚡ Side-by-Side Comparison</b></summary>
<br/>

https://github.com/user-attachments/assets/30f43308-0acd-4234-a996-8eaf585dcfe2

</details>

<details>
<summary><b>🍎 Custom Physics</b></summary>
<br/>

https://github.com/user-attachments/assets/2817852c-99b3-4c68-b3ae-12b8fedfa754

</details>

<details>
<summary><b>🎨 Pager Demo</b></summary>
<br/>

https://github.com/user-attachments/assets/ca5b7666-8681-4590-b478-f5f29f79ae0d

</details>

<details>
<summary><b>✨ Debug Overlay</b></summary>
<br/>

https://github.com/user-attachments/assets/0b2752d3-cc17-4885-bc05-0383b8cd1c1b

</details>

---

## Compatibility

| Component | Minimum Version | Recommended |
|:----------|:---------------:|:-----------:|
| Android API | 21 (Lollipop) | 26+ (Oreo) |
| Kotlin | 1.9.0 | 2.0+ |
| Compose BOM | 2024.01.00 | 2025.04.00 |
| Compose Compiler | 1.5.0 | 2.0+ |
| Android Gradle Plugin | 8.0.0 | 8.13.0+ |

### Tested With

- Jetpack Compose Material & Material3
- Accompanist libraries
- Compose Navigation
- All standard Lazy layouts

---

## Migration Guide

### From 1.x to 2.0

Version 2.0 includes breaking changes to improve API consistency and performance. See [MIGRATION.md](MIGRATION.md) for detailed instructions.

**Quick Summary:**

```kotlin
// OLD (1.x) - Deprecated and removed
import io.iamjosephmj.flinger.bahaviours.StockFlingBehaviours
StockFlingBehaviours.smoothScroll()
StockFlingBehaviours.presetOne()
StockFlingBehaviours.presetTwo()

// NEW (2.0) - Use FlingPresets
import io.iamjosephmj.flinger.behaviours.FlingPresets
FlingPresets.smooth()
FlingPresets.iOSStyle()
FlingPresets.smoothCurve()
```

### Common Imports

```kotlin
// Core fling behavior
import io.iamjosephmj.flinger.flings.flingBehavior

// Configuration builder
import io.iamjosephmj.flinger.configs.FlingConfiguration

// Preset behaviors (NEW in 2.0)
import io.iamjosephmj.flinger.behaviours.FlingPresets

// Snap behavior
import io.iamjosephmj.flinger.snap.snapFlingBehavior
import io.iamjosephmj.flinger.snap.SnapPosition

// Adaptive behavior
import io.iamjosephmj.flinger.adaptive.adaptiveFlingBehavior
```

---

## Contributing

We love contributions! Whether it's bug fixes, new features, or documentation improvements, your help is welcome.

### Quick Start

1. **Fork** the repository
2. **Clone** your fork: `git clone https://github.com/YOUR_USERNAME/flinger.git`
3. **Create** a branch: `git checkout -b feature/amazing-feature`
4. **Make** your changes
5. **Test** your changes: `./gradlew test`
6. **Commit**: `git commit -m 'Add amazing feature'`
7. **Push**: `git push origin feature/amazing-feature`
8. **Open** a Pull Request to `develop` branch

### Guidelines

- Ensure all tests pass before submitting PR
- Follow existing code style and conventions
- Add tests for new functionality
- Update documentation as needed
- Run Android Studio lint analyzer

### Contributing Presets

Have a great fling configuration? Add it to `FlingPresets.kt`! Include:
- Descriptive function name
- KDoc explaining the use case
- Recommended scenarios

See [CONTRIBUTING.md](CONTRIBUTING.md) for detailed guidelines.

---

## Roadmap

### Completed

- [x] Custom fling configuration
- [x] Multiple preset behaviors
- [x] Support for all Lazy layouts
- [x] Sample application
- [x] Snap-to-item fling behavior
- [x] Velocity-aware adaptive fling
- [x] Fling event callbacks
- [x] Accessibility presets
- [x] Pager integration
- [x] Debug visualization overlay
- [x] Physics tuning documentation

### In Progress

- [ ] Kotlin DSL builder syntax
- [ ] Enhanced documentation site

### Planned

- [ ] Compose Multiplatform support
- [ ] More real-world presets (Material You)
- [ ] Performance profiling tools

---

## Community & Support

### Get Help

- **Bug Reports**: [GitHub Issues](https://github.com/iamjosephmj/flinger/issues)
- **Feature Requests**: [GitHub Issues](https://github.com/iamjosephmj/flinger/issues)
- **Discussions**: [GitHub Discussions](https://github.com/iamjosephmj/flinger/discussions)

### Connect

- **Twitter**: [@iamjosephmj](https://twitter.com/iamjosephmj)
- **GitHub**: [@iamjosephmj](https://github.com/iamjosephmj)

### Star History

<a href="https://star-history.com/#iamjosephmj/flinger&Timeline">
  <picture>
    <source media="(prefers-color-scheme: dark)" srcset="https://api.star-history.com/svg?repos=iamjosephmj/flinger&type=Timeline&theme=dark" />
    <source media="(prefers-color-scheme: light)" srcset="https://api.star-history.com/svg?repos=iamjosephmj/flinger&type=Timeline" />
    <img alt="Star History Chart" src="https://api.star-history.com/svg?repos=iamjosephmj/flinger&type=Timeline" />
  </picture>
</a>

---

## License

```
MIT License

Copyright (c) 2021 Joseph James

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

## Privacy

Flinger is privacy-friendly and GDPR compliant:
- ✅ No data collection
- ✅ No analytics or tracking  
- ✅ No internet permissions required
- ✅ 100% offline - all processing happens locally on device
- ✅ No personal data processed

This library only handles scroll physics calculations in-memory and does not collect, store, or transmit any user information.

---

## Acknowledgements

- Inspired by Android's native [`OverScroller`](https://developer.android.com/reference/android/widget/OverScroller)
- Built for the [Jetpack Compose](https://developer.android.com/jetpack/compose) community
- Thanks to all [contributors](https://github.com/iamjosephmj/flinger/graphs/contributors)

---

<p align="center">
  <b>If Flinger helps your project, please star the repo!</b>
</p>

<p align="center">
  <a href="https://github.com/iamjosephmj/flinger/stargazers">
    <img src="https://img.shields.io/github/stars/iamjosephmj/flinger?style=for-the-badge&logo=github" alt="GitHub Stars"/>
  </a>
</p>

<p align="center">
  Made with ❤️ by <a href="https://github.com/iamjosephmj">Joseph James</a>
</p>
