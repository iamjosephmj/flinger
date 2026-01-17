<p align="center">
  <img src="https://github.com/iamjosephmj/flinger/blob/main/repo-media/flinger.jpeg" width="600" alt="Flinger Banner"/>
</p>

<h1 align="center">Flinger</h1>

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
</p>

<p align="center">
  <img src="https://github.com/iamjosephmj/flinger/blob/main/repo-media/flinger-demo.gif" width="280" alt="Flinger Demo"/>
</p>

---

## Table of Contents

- [Why Flinger?](#why-flinger)
- [Quick Start](#quick-start)
- [Installation](#installation)
- [Usage Examples](#usage-examples)
- [Configuration Parameters](#configuration-parameters)
- [Preset Behaviors](#preset-behaviors)
- [Sample App](#sample-app)
- [Compatibility](#compatibility)
- [Migration Guide](#migration-guide)
- [Contributing](#contributing)
- [Roadmap](#roadmap)
- [Community & Support](#community--support)
- [License](#license)
- [Acknowledgements](#acknowledgements)

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

## Quick Start

Get up and running in under 5 minutes:

```kotlin
// 1. Add the dependency (see Installation section)

// 2. Import the library
import io.iamjosephmj.flinger.flings.flingBehavior

// 3. Use in any scrollable Composable
LazyColumn(
    flingBehavior = flingBehavior()  // Smooth, customizable fling
) {
    items(100) { index ->
        Text("Item $index")
    }
}
```

That's it! Your `LazyColumn` now uses Flinger's smooth scroll physics.

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
    implementation("com.github.iamjosephmj:flinger:1.3.0")
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
    implementation 'com.github.iamjosephmj:flinger:1.3.0'
}
```

### Version Catalog

Add to your `libs.versions.toml`:

```toml
[versions]
flinger = "1.3.0"

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
import io.iamjosephmj.flinger.bahaviours.StockFlingBehaviours

// Smooth, balanced scrolling (default)
LazyColumn(flingBehavior = StockFlingBehaviours.smoothScroll())

// Higher friction for more controlled scrolling
LazyColumn(flingBehavior = StockFlingBehaviours.presetOne())

// Modified spline for different feel
LazyColumn(flingBehavior = StockFlingBehaviours.presetTwo())

// Quick deceleration
LazyColumn(flingBehavior = StockFlingBehaviours.presetThree())

// Bouncy, playful feel
LazyColumn(flingBehavior = StockFlingBehaviours.presetFour())

// Long, floaty scrolls
LazyColumn(flingBehavior = StockFlingBehaviours.presetFive())

// Native Android behavior (for comparison)
LazyColumn(flingBehavior = StockFlingBehaviours.getAndroidNativeScroll())
```

### Custom Configuration

Fine-tune every aspect of the fling physics:

```kotlin
import io.iamjosephmj.flinger.configs.FlingConfiguration
import io.iamjosephmj.flinger.flings.flingBehavior

LazyColumn(
    flingBehavior = flingBehavior(
        scrollConfiguration = FlingConfiguration.Builder()
            // Friction applied during scrolling (lower = longer scroll distance)
            .scrollViewFriction(0.008f)
            
            // Minimum velocity to trigger a fling (0 = any velocity triggers fling)
            .absVelocityThreshold(0f)
            
            // Simulated gravitational force affecting scroll
            .gravitationalForce(9.80665f)
            
            // Physical constant for density calculations
            .inchesPerMeter(39.37f)
            
            // Rate at which scroll decelerates
            .decelerationRate(2.358201f)
            
            // Friction applied during deceleration phase
            .decelerationFriction(0.09f)
            
            // Point where acceleration transitions to deceleration
            .splineInflection(0.1f)
            
            // Controls initial momentum curve
            .splineStartTension(0.1f)
            
            // Controls final momentum curve
            .splineEndTension(1.0f)
            
            // Resolution of the spline curve (higher = smoother)
            .numberOfSplinePoints(100)
            
            .build()
    )
) {
    items(100) { index ->
        ListItem(text = "Item $index")
    }
}
```

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

| Parameter | Default | Range | Description |
|:----------|:-------:|:-----:|:------------|
| `scrollViewFriction` | `0.008` | 0.001 - 0.1 | Resistance during active scrolling. **Lower values** = longer scroll distances |
| `absVelocityThreshold` | `0` | 0 - 100 | Minimum velocity required to trigger a fling. `0` means any velocity works |
| `gravitationalForce` | `9.80665` | 1 - 20 | Simulated gravity affecting momentum. Earth's gravity is ~9.8 |
| `inchesPerMeter` | `39.37` | - | Physical constant, rarely needs changing |
| `decelerationRate` | `2.358201` | 1 - 5 | How quickly the scroll decelerates. **Higher values** = faster stopping |
| `decelerationFriction` | `0.09` | 0.01 - 1.0 | Friction during deceleration phase. **Higher values** = quicker stop |
| `splineInflection` | `0.1` | 0.01 - 0.5 | Where the scroll curve transitions. Affects mid-scroll feel |
| `splineStartTension` | `0.1` | 0.01 - 1.0 | Initial momentum curve. Affects launch feel |
| `splineEndTension` | `1.0` | 0.1 - 2.0 | Final momentum curve. Affects landing feel |
| `numberOfSplinePoints` | `100` | 50 - 500 | Spline resolution. Higher = smoother but more computation |

### Visual Guide: How Parameters Affect Scrolling

```
Scroll Distance vs. Friction
                                    
 Distance │                         
    ↑     │ ╲                        Low friction (0.008)
          │  ╲ ╲                     
          │   ╲  ╲                   
          │    ╲   ╲                 Medium friction (0.04)
          │     ╲    ╲               
          │      ╲     ╲ ╲           High friction (0.1)
          │       ╲      ╲ ╲         
          └────────────────────→ Time


Deceleration Curves
                                    
 Velocity │                         
    ↑     │╲                         
          │ ╲                        Fast decel (high friction)
          │  ╲                       
          │   ╲ ╲                    
          │    ╲  ╲                  Medium decel
          │     ╲   ╲ ╲              
          │      ╲    ╲  ╲           Slow decel (low friction)
          │       ╲     ╲   ╲        
          └────────────────────→ Time
```

---

## Preset Behaviors

Flinger includes pre-configured presets for common scenarios:

```
┌─────────────────────────────────────────────────────────────────┐
│  PRESET SCROLL DISTANCE COMPARISON                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Native Android  ████████████░░░░░░░░░░░░  Standard distance    │
│  smoothScroll()  ████████████████░░░░░░░░  Balanced, smooth     │
│  presetOne()     ██████████░░░░░░░░░░░░░░  Higher friction      │
│  presetTwo()     ████████████████████░░░░  Modified spline      │
│  presetThree()   ██████░░░░░░░░░░░░░░░░░░  Quick stop           │
│  presetFour()    ████████████████████████  Long, bouncy         │
│  presetFive()    ████████████████████████  Floaty, maximum      │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

| Preset | Best For | Key Settings | Feel |
|:-------|:---------|:-------------|:-----|
| `smoothScroll()` | General purpose | Default values | Balanced, smooth |
| `presetOne()` | Controlled lists | `friction: 0.04` | More resistance |
| `presetTwo()` | Visual content | `splineInflection: 0.16` | Unique curve |
| `presetThree()` | Text/reading | `decelFriction: 0.5` | Quick stop |
| `presetFour()` | Playful UIs | `decelFriction: 0.6, inflection: 0.4` | Bouncy |
| `presetFive()` | Galleries | `friction: 0.09, decelFriction: 0.015` | Long glide |

### Creating Your Own Presets

```kotlin
object MyFlingPresets {
    @Composable
    fun iOSStyle() = flingBehavior(
        scrollConfiguration = FlingConfiguration.Builder()
            .scrollViewFriction(0.02f)
            .decelerationFriction(0.04f)
            .splineInflection(0.15f)
            .build()
    )
    
    @Composable
    fun snappyList() = flingBehavior(
        scrollConfiguration = FlingConfiguration.Builder()
            .scrollViewFriction(0.05f)
            .decelerationFriction(0.3f)
            .absVelocityThreshold(50f)
            .build()
    )
}
```

---

## Sample App

Experience Flinger's capabilities firsthand with our sample app:

**[Download Sample APK](https://github.com/iamjosephmj/flinger/tree/main/apk)**

### Features

- **Interactive Playground** - Adjust all parameters in real-time
- **Preset Gallery** - Compare all built-in presets side-by-side
- **Native vs Flinger** - See the difference between default and customized scrolling
- **Code Export** - Generate configuration code for your settings

<p align="center">
  <img src="https://github.com/iamjosephmj/flinger/blob/main/repo-media/flinger-demo.gif" width="250" alt="Sample App Demo"/>
</p>

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

### From 1.2.x to 1.3.x

No breaking changes. Update your dependency version:

```kotlin
// Old
implementation("com.github.iamjosephmj:flinger:1.2.0")

// New
implementation("com.github.iamjosephmj:flinger:1.3.0")
```

### Common Import

```kotlin
// Core fling behavior
import io.iamjosephmj.flinger.flings.flingBehavior

// Configuration builder
import io.iamjosephmj.flinger.configs.FlingConfiguration

// Stock presets
import io.iamjosephmj.flinger.bahaviours.StockFlingBehaviours
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

Have a great fling configuration? Add it to `StockFlingBehaviours.kt`! Include:
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

### In Progress
- [ ] Kotlin DSL builder syntax
- [ ] Enhanced documentation site

### Planned
- [ ] Snap-to-item fling behavior
- [ ] Compose Multiplatform support
- [ ] Fling curve visualization composable
- [ ] More real-world presets (iOS, Material You)
- [ ] Integration with Pager components

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
