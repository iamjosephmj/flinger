# Changelog

All notable changes to Flinger will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- Comprehensive README with detailed documentation
- CONTRIBUTING.md with contribution guidelines
- CHANGELOG.md for version tracking
- Expanded unit test coverage
- UI instrumented tests for sample app
- Interactive Fling Playground in sample app
- Preset Gallery with visual previews
- Side-by-side comparison screen (Native vs Flinger)
- Real-world example screens

### Changed
- Renamed package `bahaviours` to `behaviours` (with backward compatibility)
- Renamed presets to descriptive names (e.g., `presetOne` → `iOSStyle`)
- Updated compileSdk to 35
- Added Material3 support

### Fixed
- Fixed documentation showing incorrect class name (`ScrollViewConfiguration` → `FlingConfiguration`)

---

## [1.3.0] - 2024-01-15

### Added
- Support for Compose BOM 2024+
- Kotlin 2.0 compatibility
- New preset: `presetFive()` for floaty, long-distance scrolling

### Changed
- Updated to Android Gradle Plugin 8.x
- Migrated to Kotlin DSL for Gradle files
- Updated minimum Kotlin version to 1.9.0

### Fixed
- Fixed spline calculation edge cases
- Improved fling behavior stability at high velocities

---

## [1.2.0] - 2023-06-20

### Added
- New configuration options:
  - `inchesPerMeter` for physical density calculations
  - `numberOfSplinePoints` for curve resolution control
- Additional presets for common use cases

### Changed
- Improved default values for smoother scrolling
- Better handling of zero velocity edge cases

### Fixed
- Fixed memory leak in fling calculator
- Corrected velocity calculations at animation boundaries

---

## [1.1.0] - 2022-11-10

### Added
- `StockFlingBehaviours` object with preset configurations:
  - `smoothScroll()` - Balanced, smooth scrolling
  - `presetOne()` - Higher friction
  - `presetTwo()` - Modified spline
  - `presetThree()` - Quick deceleration
  - `presetFour()` - Bouncy feel
- `getAndroidNativeScroll()` for native comparison
- Sample application with settings UI

### Changed
- Refactored configuration builder for better usability
- Improved documentation with KDoc comments

---

## [1.0.0] - 2021-09-15

### Added
- Initial release of Flinger
- `FlingConfiguration` builder for custom fling physics
- `flingBehavior()` composable function
- Support for `LazyColumn`, `LazyRow`, and other lazy layouts
- Core fling parameters:
  - `scrollViewFriction` - Scroll resistance
  - `absVelocityThreshold` - Minimum fling velocity
  - `gravitationalForce` - Simulated gravity
  - `decelerationRate` - Deceleration curve
  - `decelerationFriction` - Deceleration resistance
  - `splineInflection` - Curve transition point
  - `splineStartTension` - Initial momentum
  - `splineEndTension` - Final momentum
- `AndroidFlingSpline` for spline-based animations
- `FlingCalculator` for physics calculations
- Unit tests for core functionality
- MIT License

---

## Version History Summary

| Version | Release Date | Highlights |
|---------|--------------|------------|
| 1.3.0 | 2024-01-15 | Compose BOM 2024+, Kotlin 2.0 |
| 1.2.0 | 2023-06-20 | New config options, improved defaults |
| 1.1.0 | 2022-11-10 | Stock presets, sample app |
| 1.0.0 | 2021-09-15 | Initial release |

---

## Upgrade Guide

### Upgrading to 1.4.0 (Upcoming)

**Package Rename:**
```kotlin
// Old import (deprecated, still works)
import io.iamjosephmj.flinger.bahaviours.StockFlingBehaviours

// New import (recommended)
import io.iamjosephmj.flinger.behaviours.FlingPresets
```

**Preset Renames:**
```kotlin
// Old (deprecated)
StockFlingBehaviours.presetOne()
StockFlingBehaviours.presetTwo()

// New (recommended)
FlingPresets.iOSStyle()
FlingPresets.precisionScroll()
```

### Upgrading to 1.3.0

No breaking changes. Simply update the dependency version:

```kotlin
implementation("com.github.iamjosephmj:flinger:1.3.0")
```

### Upgrading to 1.2.0

No breaking changes. New configuration options are optional with sensible defaults.

---

[Unreleased]: https://github.com/iamjosephmj/flinger/compare/v1.3.0...HEAD
[1.3.0]: https://github.com/iamjosephmj/flinger/compare/v1.2.0...v1.3.0
[1.2.0]: https://github.com/iamjosephmj/flinger/compare/v1.1.0...v1.2.0
[1.1.0]: https://github.com/iamjosephmj/flinger/compare/v1.0.0...v1.1.0
[1.0.0]: https://github.com/iamjosephmj/flinger/releases/tag/v1.0.0
