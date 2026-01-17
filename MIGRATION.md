# Migration Guide

This guide helps you migrate from Flinger 1.x to 2.0.

## Overview

Version 2.0 includes breaking changes to improve API consistency, fix a package name typo, and enhance performance. Most migrations are simple find-and-replace operations.

---

## Breaking Changes Summary

| Change | Impact | Effort |
|:-------|:------:|:------:|
| Package rename (`bahaviours` → `behaviours`) | High | Low |
| Class rename (`StockFlingBehaviours` → `FlingPresets`) | High | Low |
| Method renames (presets) | Medium | Low |
| Removed `flingPosition()` | Low | Medium |
| Removed `FlingResult` | Low | Medium |

---

## Step-by-Step Migration

### 1. Update Package Imports

The package name typo `bahaviours` has been corrected to `behaviours`:

```kotlin
// OLD (1.x)
import io.iamjosephmj.flinger.bahaviours.StockFlingBehaviours

// NEW (2.0)
import io.iamjosephmj.flinger.behaviours.FlingPresets
```

**IDE Quick Fix:** In Android Studio, use `Ctrl+Shift+R` (Windows/Linux) or `Cmd+Shift+R` (Mac) to find and replace:
- Find: `io.iamjosephmj.flinger.bahaviours`
- Replace: `io.iamjosephmj.flinger.behaviours`

### 2. Update Class References

The preset class has been renamed for clarity:

```kotlin
// OLD (1.x)
StockFlingBehaviours.smoothScroll()

// NEW (2.0)
FlingPresets.smooth()
```

### 3. Update Method Names

All preset method names have been updated for better clarity:

| Old Method (1.x) | New Method (2.0) | Description |
|:-----------------|:-----------------|:------------|
| `getAndroidNativeScroll()` | `androidNative()` | Native Android fling |
| `smoothScroll()` | `smooth()` | Balanced, smooth fling |
| `presetOne()` | `iOSStyle()` | iOS-like higher friction |
| `presetTwo()` | `smoothCurve()` | Modified spline curve |
| `presetThree()` | `quickStop()` | Quick deceleration |
| `presetFour()` | `bouncy()` | Bouncy, playful feel |
| `presetFive()` | `floaty()` | Long, floaty scrolls |

**Example migration:**

```kotlin
// OLD (1.x)
LazyColumn(flingBehavior = StockFlingBehaviours.smoothScroll()) { ... }
LazyColumn(flingBehavior = StockFlingBehaviours.presetOne()) { ... }
LazyColumn(flingBehavior = StockFlingBehaviours.presetTwo()) { ... }
LazyColumn(flingBehavior = StockFlingBehaviours.presetThree()) { ... }
LazyColumn(flingBehavior = StockFlingBehaviours.presetFour()) { ... }
LazyColumn(flingBehavior = StockFlingBehaviours.presetFive()) { ... }
LazyColumn(flingBehavior = StockFlingBehaviours.getAndroidNativeScroll()) { ... }

// NEW (2.0)
LazyColumn(flingBehavior = FlingPresets.smooth()) { ... }
LazyColumn(flingBehavior = FlingPresets.iOSStyle()) { ... }
LazyColumn(flingBehavior = FlingPresets.smoothCurve()) { ... }
LazyColumn(flingBehavior = FlingPresets.quickStop()) { ... }
LazyColumn(flingBehavior = FlingPresets.bouncy()) { ... }
LazyColumn(flingBehavior = FlingPresets.floaty()) { ... }
LazyColumn(flingBehavior = FlingPresets.androidNative()) { ... }
```

### 4. New Presets Available

Version 2.0 adds new presets you might want to try:

```kotlin
// NEW in 2.0
FlingPresets.snappy()        // Quick, responsive feel
FlingPresets.ultraSmooth()   // Premium, buttery smooth
FlingPresets.accessibilityAware()  // Respects system accessibility settings
FlingPresets.reducedMotion() // Minimal motion for accessibility
```

---

## Internal API Changes (Advanced)

These changes only affect you if you were using internal APIs directly.

### Removed: `flingPosition()` and `FlingResult`

For performance reasons, the `flingPosition()` method and `FlingResult` data class have been removed from `AndroidFlingSpline`. Use the allocation-free alternatives:

```kotlin
// OLD (1.x) - Created object allocations
val result = spline.flingPosition(time)
val distance = result.distanceCoefficient
val velocity = result.velocityCoefficient

// NEW (2.0) - Allocation-free
val distance = spline.flingDistanceCoefficient(time)
val velocity = spline.flingVelocityCoefficient(time)
```

**Note:** These were internal APIs. If you were using them directly, please use the new methods instead.

---

## Complete Migration Example

Here's a complete before/after example:

### Before (1.x)

```kotlin
import io.iamjosephmj.flinger.bahaviours.StockFlingBehaviours
import io.iamjosephmj.flinger.configs.FlingConfiguration
import io.iamjosephmj.flinger.flings.flingBehavior

@Composable
fun MyScreen() {
    // Using preset
    LazyColumn(flingBehavior = StockFlingBehaviours.smoothScroll()) {
        items(100) { Text("Item $it") }
    }
    
    // Using iOS-style
    LazyRow(flingBehavior = StockFlingBehaviours.presetOne()) {
        items(50) { Card(it) }
    }
    
    // Custom configuration (unchanged)
    LazyColumn(
        flingBehavior = flingBehavior(
            scrollConfiguration = FlingConfiguration.Builder()
                .scrollViewFriction(0.02f)
                .build()
        )
    ) {
        items(100) { Text("Item $it") }
    }
}
```

### After (2.0)

```kotlin
import io.iamjosephmj.flinger.behaviours.FlingPresets
import io.iamjosephmj.flinger.configs.FlingConfiguration
import io.iamjosephmj.flinger.flings.flingBehavior

@Composable
fun MyScreen() {
    // Using preset
    LazyColumn(flingBehavior = FlingPresets.smooth()) {
        items(100) { Text("Item $it") }
    }
    
    // Using iOS-style
    LazyRow(flingBehavior = FlingPresets.iOSStyle()) {
        items(50) { Card(it) }
    }
    
    // Custom configuration (unchanged)
    LazyColumn(
        flingBehavior = flingBehavior(
            scrollConfiguration = FlingConfiguration.Builder()
                .scrollViewFriction(0.02f)
                .build()
        )
    ) {
        items(100) { Text("Item $it") }
    }
}
```

---

## Automated Migration Script

For larger codebases, you can use this sed script to automate most of the migration:

```bash
#!/bin/bash
# migrate-flinger-2.0.sh

# Run from project root
find . -name "*.kt" -type f -exec sed -i '' \
    -e 's/io\.iamjosephmj\.flinger\.bahaviours/io.iamjosephmj.flinger.behaviours/g' \
    -e 's/StockFlingBehaviours/FlingPresets/g' \
    -e 's/\.smoothScroll()/.smooth()/g' \
    -e 's/\.getAndroidNativeScroll()/.androidNative()/g' \
    -e 's/\.presetOne()/.iOSStyle()/g' \
    -e 's/\.presetTwo()/.smoothCurve()/g' \
    -e 's/\.presetThree()/.quickStop()/g' \
    -e 's/\.presetFour()/.bouncy()/g' \
    -e 's/\.presetFive()/.floaty()/g' \
    {} \;

echo "Migration complete! Please review changes and run tests."
```

---

## Troubleshooting

### "Unresolved reference: StockFlingBehaviours"

Update your import and class reference:
```kotlin
// Change this
import io.iamjosephmj.flinger.bahaviours.StockFlingBehaviours

// To this
import io.iamjosephmj.flinger.behaviours.FlingPresets
```

### "Unresolved reference: flingPosition"

Use the new allocation-free methods:
```kotlin
// Change this
spline.flingPosition(time).distanceCoefficient

// To this
spline.flingDistanceCoefficient(time)
```

### "Unresolved reference: FlingResult"

`FlingResult` has been removed. Use individual coefficient methods instead.

---

## Getting Help

If you encounter issues during migration:

1. Check this guide for common solutions
2. Search [existing issues](https://github.com/iamjosephmj/flinger/issues)
3. Open a [new issue](https://github.com/iamjosephmj/flinger/issues/new) with details

---

## Version History

| Version | Release Date | Notes |
|:--------|:------------:|:------|
| 2.0.0 | 2026 | Breaking changes, new features |
| 1.3.0 | 2025 | Last 1.x release |
| 1.2.0 | 2024 | Added callbacks |
| 1.1.0 | 2023 | Initial stable release |
