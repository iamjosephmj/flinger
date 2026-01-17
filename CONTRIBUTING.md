# Contributing to Flinger

First off, thank you for considering contributing to Flinger! It's people like you that make Flinger such a great tool for the Android community.

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [How Can I Contribute?](#how-can-i-contribute)
- [Development Setup](#development-setup)
- [Pull Request Process](#pull-request-process)
- [Style Guidelines](#style-guidelines)
- [Testing Guidelines](#testing-guidelines)
- [Adding New Presets](#adding-new-presets)

---

## Code of Conduct

This project and everyone participating in it is governed by our commitment to providing a welcoming and inclusive environment. By participating, you are expected to uphold this commitment:

- **Be respectful** - Treat everyone with respect and kindness
- **Be constructive** - Provide helpful feedback and suggestions
- **Be collaborative** - Work together to improve the project
- **Be inclusive** - Welcome newcomers and help them get started

---

## Getting Started

### Prerequisites

- Android Studio Arctic Fox (2020.3.1) or newer
- JDK 17 or higher
- Kotlin 1.9.0 or higher
- Basic understanding of Jetpack Compose

### Fork and Clone

1. Fork the repository on GitHub
2. Clone your fork locally:
   ```bash
   git clone https://github.com/YOUR_USERNAME/flinger.git
   cd flinger
   ```
3. Add the original repository as upstream:
   ```bash
   git remote add upstream https://github.com/iamjosephmj/flinger.git
   ```

---

## How Can I Contribute?

### Reporting Bugs

Before submitting a bug report:
- Check the [existing issues](https://github.com/iamjosephmj/flinger/issues) to avoid duplicates
- Try to reproduce the bug on the latest version

When submitting a bug report, include:
- **Clear title** - Summarize the issue
- **Steps to reproduce** - List exact steps
- **Expected behavior** - What should happen
- **Actual behavior** - What actually happens
- **Code sample** - Minimal reproducible example
- **Environment** - Android version, Compose version, device/emulator

```markdown
### Bug Report Template

**Description**
A clear description of the bug.

**Steps to Reproduce**
1. Create a LazyColumn with flingBehavior(...)
2. Scroll quickly
3. Observe the behavior

**Expected Behavior**
The list should scroll smoothly.

**Actual Behavior**
The list stutters or stops abruptly.

**Code Sample**
```kotlin
LazyColumn(
    flingBehavior = flingBehavior(
        FlingConfiguration.Builder()
            .scrollViewFriction(0.008f)
            .build()
    )
) { ... }
```

**Environment**
- Flinger version: 1.3.0
- Compose BOM: 2025.04.00
- Android version: 14
- Device: Pixel 8
```

### Suggesting Features

We love new ideas! When suggesting a feature:
- **Search existing issues** first
- **Explain the use case** - Why is this feature needed?
- **Describe the solution** - How should it work?
- **Consider alternatives** - Are there other ways to achieve this?

### Contributing Code

#### Types of Contributions Welcome

- **Bug fixes** - Fix reported issues
- **New presets** - Add useful fling configurations
- **Documentation** - Improve README, KDoc, or examples
- **Tests** - Add or improve test coverage
- **Performance** - Optimize existing code
- **New features** - After discussion in an issue

---

## Development Setup

### Project Structure

```
flinger/
├── app/                    # Sample application
│   └── src/main/java/...   # Sample app source code
├── flinger/                # Library module
│   └── src/
│       ├── main/java/...   # Library source code
│       │   └── io/iamjosephmj/flinger/
│       │       ├── bahaviours/    # Stock fling presets
│       │       ├── configs/       # Configuration classes
│       │       ├── flings/        # Core fling behavior
│       │       └── spline/        # Spline calculations
│       └── test/java/...   # Unit tests
├── gradle/                 # Gradle configuration
└── README.md
```

### Building the Project

```bash
# Build everything
./gradlew build

# Build only the library
./gradlew :flinger:build

# Build and install sample app
./gradlew :app:installDebug
```

### Running Tests

```bash
# Run all unit tests
./gradlew test

# Run library tests only
./gradlew :flinger:test

# Run with coverage report
./gradlew :flinger:testDebugUnitTest
```

---

## Pull Request Process

### Before Submitting

1. **Create an issue first** for significant changes
2. **Branch from `develop`**, not `main`
3. **Keep changes focused** - One feature/fix per PR
4. **Write/update tests** for your changes
5. **Update documentation** if needed
6. **Run lint checks**: `./gradlew lint`
7. **Ensure all tests pass**: `./gradlew test`

### Branch Naming

Use descriptive branch names:
- `feature/add-ios-preset` - New feature
- `fix/scroll-stutter` - Bug fix
- `docs/update-readme` - Documentation
- `refactor/simplify-spline` - Code refactoring
- `test/add-fling-tests` - Test additions

### Commit Messages

Follow conventional commit format:

```
type(scope): brief description

[optional body]

[optional footer]
```

**Types:**
- `feat` - New feature
- `fix` - Bug fix
- `docs` - Documentation
- `style` - Formatting (no code change)
- `refactor` - Code restructuring
- `test` - Adding tests
- `chore` - Maintenance

**Examples:**
```
feat(presets): add iOS-style fling behavior

fix(spline): correct velocity calculation at boundaries

docs(readme): add configuration parameter table

test(calculator): add edge case tests for zero velocity
```

### PR Description Template

```markdown
## Description
Brief description of what this PR does.

## Related Issue
Fixes #123

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Documentation update
- [ ] Code refactoring
- [ ] Test addition

## Checklist
- [ ] Code follows project style guidelines
- [ ] Self-reviewed my code
- [ ] Added/updated tests
- [ ] Updated documentation
- [ ] All tests pass
- [ ] Lint checks pass
```

---

## Style Guidelines

### Kotlin Style

We follow the [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html) with these additions:

```kotlin
// Use explicit types for public APIs
fun flingBehavior(config: FlingConfiguration): FlingBehavior

// Use meaningful parameter names
class FlingConfiguration(
    val scrollFriction: Float,      // Good
    val f: Float                     // Bad
)

// Document public APIs with KDoc
/**
 * Creates a customizable fling behavior for scrollable composables.
 *
 * @param scrollConfiguration The configuration defining fling physics.
 * @return A [FlingBehavior] that can be used with LazyColumn, LazyRow, etc.
 *
 * @sample io.iamjosephmj.flinger.samples.BasicFlingExample
 */
@Composable
fun flingBehavior(
    scrollConfiguration: FlingConfiguration = FlingConfiguration.Builder().build()
): FlingBehavior

// Use named arguments for clarity
FlingConfiguration.Builder()
    .scrollViewFriction(friction = 0.008f)
    .decelerationFriction(friction = 0.09f)
    .build()
```

### Compose Guidelines

```kotlin
// Composable functions should be PascalCase
@Composable
fun FlingPlayground() { }

// State hoisting for reusability
@Composable
fun FlingDemo(
    config: FlingConfiguration,
    onConfigChange: (FlingConfiguration) -> Unit
) { }

// Use remember for expensive calculations
val flingBehavior = remember(config) {
    FlingerFlingBehavior(config)
}
```

### File Organization

```kotlin
// File: StockFlingBehaviours.kt

package io.iamjosephmj.flinger.bahaviours

// 1. Imports (sorted alphabetically)
import androidx.compose.runtime.Composable
import io.iamjosephmj.flinger.configs.FlingConfiguration

// 2. Top-level constants
private const val DEFAULT_FRICTION = 0.008f

// 3. Main class/object
object StockFlingBehaviours {
    // Public functions first
    @Composable
    fun smoothScroll(): FlingBehavior = ...
    
    // Private helpers last
    private fun createConfig(): FlingConfiguration = ...
}
```

---

## Testing Guidelines

### Unit Test Structure

```kotlin
class FlingCalculatorTest {
    
    // Use descriptive test names
    @Test
    fun `flingDistance returns positive value for positive velocity`() {
        // Given
        val calculator = createCalculator()
        
        // When
        val distance = calculator.flingDistance(velocity = 1000f)
        
        // Then
        assertThat(distance).isGreaterThan(0f)
    }
    
    @Test
    fun `flingDistance returns zero for zero velocity`() {
        val calculator = createCalculator()
        
        val distance = calculator.flingDistance(velocity = 0f)
        
        assertThat(distance).isEqualTo(0f)
    }
    
    // Helper methods at the bottom
    private fun createCalculator(
        config: FlingConfiguration = defaultConfig()
    ): FlingCalculator {
        return FlingCalculator(
            density = FlingTestUtils.getDummyDensityComponent(),
            flingConfiguration = config
        )
    }
}
```

### What to Test

- **Configuration building** - Builder creates valid configs
- **Calculations** - Fling distance, duration, velocity
- **Edge cases** - Zero values, negative values, extremes
- **Presets** - Each preset produces expected behavior
- **Composable integration** - Basic rendering without crashes

---

## Adding New Presets

Want to contribute a new fling preset? Here's how:

### 1. Create the Preset

Add to `StockFlingBehaviours.kt`:

```kotlin
/**
 * Creates an iOS-style fling behavior.
 *
 * This preset mimics the scroll physics found in iOS, with
 * smooth deceleration and a longer glide distance.
 *
 * **Best for:**
 * - Cross-platform apps wanting iOS feel
 * - Photo galleries
 * - Content browsing experiences
 *
 * @return A [FlingBehavior] with iOS-like physics
 */
@Composable
fun iOSStyle(): FlingBehavior = flingBehavior(
    scrollConfiguration = FlingConfiguration.Builder()
        .scrollViewFriction(0.02f)
        .decelerationFriction(0.04f)
        .splineInflection(0.15f)
        .build()
)
```

### 2. Add Tests

```kotlin
@Test
fun `iOSStyle produces longer fling than default`() {
    val defaultCalc = createCalculator(defaultConfig())
    val iosCalc = createCalculator(iOSStyleConfig())
    
    val defaultDistance = defaultCalc.flingDistance(1000f)
    val iosDistance = iosCalc.flingDistance(1000f)
    
    assertThat(iosDistance).isGreaterThan(defaultDistance)
}
```

### 3. Update Documentation

- Add to preset table in README
- Include use case description
- Add to sample app preset gallery

### 4. Test in Sample App

Verify the preset feels right in the sample app before submitting.

---

## Questions?

- Open a [Discussion](https://github.com/iamjosephmj/flinger/discussions)
- Reach out on [Twitter](https://twitter.com/iamjosephmj)
- Check existing [Issues](https://github.com/iamjosephmj/flinger/issues)

---

Thank you for contributing to Flinger! 🚀
