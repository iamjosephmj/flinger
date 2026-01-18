# Physics Tuning Guide

This guide provides detailed explanations of Flinger's physics parameters, helping you understand how each one affects scroll behavior and how to tune them for your specific use case.

---

## Table of Contents

- [Overview: How Fling Physics Work](#overview-how-fling-physics-work)
- [Friction Parameters](#friction-parameters)
  - [scrollViewFriction](#scrollviewfriction)
  - [decelerationFriction](#decelerationfriction)
- [Physics Parameters](#physics-parameters)
  - [gravitationalForce](#gravitationalforce)
  - [inchesPerMeter](#inchespermeter)
  - [decelerationRate](#decelerationrate)
  - [absVelocityThreshold](#absvelocitythreshold)
- [Spline Curve Parameters](#spline-curve-parameters)
  - [splineInflection](#splineinflection)
  - [splineStartTension](#splinestarttension)
  - [splineEndTension](#splineendtension)
  - [numberOfSplinePoints](#numberofsplinepoints)
- [Tuning Recipes](#tuning-recipes)
- [Troubleshooting](#troubleshooting)

---

## Overview: How Fling Physics Work

When a user flings a scrollable list, Flinger calculates the scroll trajectory using a **spline-based deceleration curve**. This is the same approach Android's native `OverScroller` uses, but with full parameter customization.

The fling animation consists of two phases:

```
┌─────────────────────────────────────────────────────────────┐
│                    FLING LIFECYCLE                          │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  PHASE 1: Launch          │  PHASE 2: Deceleration          │
│  ─────────────────────────┼───────────────────────────────  │
│                           │                                 │
│  User releases finger     │  Momentum gradually decreases   │
│  Initial velocity applied │  Friction slows scroll          │
│  splineStartTension       │  splineEndTension controls      │
│  controls this curve      │  this curve                     │
│                           │                                 │
│         ╱╲                │           ╲                     │
│        ╱  ╲               │            ╲                    │
│       ╱    ╲──────────────┼─────────────╲───────────────    │
│                           │                                 │
│     ◀── splineInflection ─┼─▶                               │
│         (transition point)│                                 │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

Each parameter controls a different aspect of this animation.

---

## Friction Parameters

Friction determines how much resistance is applied to the scroll motion.

### scrollViewFriction

Controls resistance during the **active scroll phase** — the primary factor determining how far a fling will travel.

| Property | Value |
|:---------|:------|
| **Default** | `0.008` |
| **Range** | `0.001` - `0.1` |
| **Unit** | Coefficient (dimensionless) |

#### Effect of Changing

| Value | Effect | Use Case |
|:------|:-------|:---------|
| **Lower** (0.001-0.006) | Scroll travels much farther, feels "slippery" | Photo galleries, long content feeds |
| **Default** (0.008) | Balanced feel | General purpose lists |
| **Higher** (0.02-0.1) | Scroll stops quickly, more controlled | Precision selection, text-heavy content |

#### Visual Comparison

```
Scroll Distance vs. scrollViewFriction
                                    
 Distance │                         
    ↑     │ ╲                        
          │  ╲                       friction = 0.004 (low)
          │   ╲  ╲                   
          │    ╲   ╲                 friction = 0.008 (default)
          │     ╲    ╲               
          │      ╲     ╲ ╲           friction = 0.04 (high)
          │       ╲      ╲ ╲         
          │        ╲       ╲ ╲       
          └────────────────────→ Time
```

#### Example

```kotlin
// Long, floaty scrolls for a photo gallery
FlingConfiguration.Builder()
    .scrollViewFriction(0.004f)
    .build()

// Quick-stop for a settings menu
FlingConfiguration.Builder()
    .scrollViewFriction(0.05f)
    .build()
```

---

### decelerationFriction

Controls resistance during the **deceleration phase** — how aggressively the scroll slows down once it starts decelerating.

| Property | Value |
|:---------|:------|
| **Default** | `0.09` |
| **Range** | `0.01` - `1.0` |
| **Unit** | Coefficient (dimensionless) |

#### Effect of Changing

| Value | Effect | Use Case |
|:------|:-------|:---------|
| **Lower** (0.01-0.05) | Gradual, smooth deceleration | Premium feel, galleries |
| **Default** (0.09) | Natural deceleration | General purpose |
| **Higher** (0.2-1.0) | Abrupt stop, bouncy feel | Playful UIs, quick selection |

#### Visual Comparison

```
Velocity Decay During Deceleration
                                    
 Velocity │                         
    ↑     │╲                         
          │ ╲                        decelerationFriction = 0.5 (high)
          │  ╲  ╲                    
          │   ╲   ╲                  decelerationFriction = 0.09 (default)
          │    ╲    ╲  ╲             
          │     ╲     ╲   ╲          decelerationFriction = 0.02 (low)
          │      ╲      ╲    ╲       
          │       ╲       ╲     ╲    
          └────────────────────────→ Time
```

#### Example

```kotlin
// Ultra-smooth premium feel
FlingConfiguration.Builder()
    .decelerationFriction(0.02f)
    .build()

// Bouncy, playful feel
FlingConfiguration.Builder()
    .decelerationFriction(0.6f)
    .build()
```

---

## Physics Parameters

These parameters simulate real-world physics properties.

### gravitationalForce

Simulates gravitational pull affecting the scroll momentum. Based on Earth's gravity (~9.8 m/s²).

| Property | Value |
|:---------|:------|
| **Default** | `9.80665` |
| **Range** | `1.0` - `20.0` |
| **Unit** | m/s² (meters per second squared) |

#### Effect of Changing

| Value | Effect | Use Case |
|:------|:-------|:---------|
| **Lower** (1-5) | "Moon gravity" — scroll feels lighter, floatier | Dreamy/fantasy UIs |
| **Default** (9.8) | Earth-like physics | Standard behavior |
| **Higher** (15-20) | "Heavy gravity" — scroll feels heavier, more grounded | Industrial/serious UIs |

#### Example

```kotlin
// Floaty, low-gravity feel
FlingConfiguration.Builder()
    .gravitationalForce(3.0f)
    .build()
```

---

### inchesPerMeter

Physical constant for converting between metric and imperial units in DPI calculations. **Rarely needs changing.**

| Property | Value |
|:---------|:------|
| **Default** | `39.37` |
| **Range** | Fixed constant |
| **Unit** | inches/meter |

This is the actual physical conversion factor (1 meter = 39.37 inches). Only modify this if you're doing something unusual with screen density calculations.

---

### decelerationRate

The exponential rate at which velocity decreases. Controls the overall "shape" of the deceleration curve.

| Property | Value |
|:---------|:------|
| **Default** | `2.358201` (computed as `ln(0.78) / ln(0.9)`) |
| **Range** | `1.0` - `5.0` |
| **Unit** | Exponential coefficient |

#### Effect of Changing

| Value | Effect | Use Case |
|:------|:-------|:---------|
| **Lower** (1.0-1.5) | Very gradual deceleration, long scrolls | Infinite scroll feeds |
| **Default** (~2.36) | Natural deceleration curve | General purpose |
| **Higher** (3.0-5.0) | Rapid deceleration, shorter scrolls | Precise control |

#### Example

```kotlin
// Extended momentum for content discovery
FlingConfiguration.Builder()
    .decelerationRate(1.5f)
    .build()
```

---

### absVelocityThreshold

The minimum velocity (in pixels/second) required to trigger a fling. Below this threshold, the scroll stops immediately.

| Property | Value |
|:---------|:------|
| **Default** | `0` |
| **Range** | `0` - `100+` |
| **Unit** | pixels/second |

#### Effect of Changing

| Value | Effect | Use Case |
|:------|:-------|:---------|
| **0** (default) | Any velocity triggers a fling | Smooth, responsive |
| **10-50** | Very slow swipes are ignored | Prevent accidental flings |
| **50-100+** | Only deliberate swipes trigger fling | Accessibility, precision |

#### Example

```kotlin
// Require intentional swipes
FlingConfiguration.Builder()
    .absVelocityThreshold(50f)
    .build()
```

---

## Spline Curve Parameters

The spline curve defines the exact shape of the scroll animation. These are **advanced parameters** that control the mathematical curve used to interpolate position over time.

### splineInflection

The point where the scroll transitions from the "launch" phase to the "landing" phase. Think of it as the "peak" of the momentum curve.

| Property | Value |
|:---------|:------|
| **Default** | `0.1` |
| **Range** | `0.01` - `0.5` |
| **Unit** | Normalized position (0-1) |

#### How It Works

```
Momentum Curve with Different Inflection Points
                                                
                    ╱╲  inflection = 0.4 (late transition)
                   ╱  ╲                          
                  ╱    ╲                         
            ╱╲   ╱      ╲                        
           ╱  ╲ ╱        ╲    inflection = 0.16 (mid)
          ╱    ╲          ╲                      
     ╱╲  ╱                 ╲                     
    ╱  ╲╱                   ╲    inflection = 0.1 (default, early)
   ╱                         ╲                   
  ╱                           ╲                  
 ──────────────────────────────────────────────→ Time
```

#### Effect of Changing

| Value | Effect | Use Case |
|:------|:-------|:---------|
| **Lower** (0.01-0.08) | Very quick transition, mostly deceleration | Snappy, responsive |
| **Default** (0.1) | Early transition, balanced | General purpose |
| **Higher** (0.2-0.5) | Extended launch phase, delayed deceleration | Bouncy, playful |

#### Example

```kotlin
// Bouncy feel with extended launch phase
FlingConfiguration.Builder()
    .splineInflection(0.4f)
    .build()
```

---

### splineStartTension

Controls the curvature of the **launch phase** — how the scroll accelerates immediately after the finger lifts.

| Property | Value |
|:---------|:------|
| **Default** | `0.1` |
| **Range** | `0.01` - `1.0` |
| **Unit** | Tension coefficient |

#### How It Works

The start tension affects the initial momentum. Higher values create a more aggressive initial acceleration, while lower values create a gentler start.

```
Launch Phase Curves
                                                
 Position │         ╱                            
          │        ╱   tension = 0.5 (high - aggressive start)
          │       ╱                              
          │      ╱  ╱                            
          │     ╱  ╱   tension = 0.1 (default)
          │    ╱  ╱                              
          │   ╱  ╱  ╱                            
          │  ╱  ╱  ╱   tension = 0.01 (low - gentle start)
          │ ╱  ╱  ╱                              
          │╱  ╱  ╱                               
          └────────────────────────────────────→ Time
```

#### Effect of Changing

| Value | Effect | Use Case |
|:------|:-------|:---------|
| **Lower** (0.01-0.05) | Gentle, gradual launch | Elegant, refined feel |
| **Default** (0.1) | Natural launch momentum | General purpose |
| **Higher** (0.3-1.0) | Aggressive, snappy launch | Responsive, energetic UIs |

#### Example

```kotlin
// Aggressive, snappy launch
FlingConfiguration.Builder()
    .splineStartTension(0.5f)
    .build()
```

---

### splineEndTension

Controls the curvature of the **landing phase** — how the scroll decelerates as it approaches its final position.

| Property | Value |
|:---------|:------|
| **Default** | `1.0` |
| **Range** | `0.1` - `2.0` |
| **Unit** | Tension coefficient |

#### How It Works

The end tension affects how the scroll "lands." Higher values create a more abrupt stop, while lower values create a gradual, smooth landing.

```
Landing Phase Curves
                                                
 Velocity │                                      
          │╲                                     
          │ ╲                                    
          │  ╲  tension = 1.5 (high - abrupt stop)
          │   ╲  ╲                               
          │    ╲   ╲  tension = 1.0 (default)
          │     ╲    ╲                           
          │      ╲     ╲  ╲  tension = 0.5 (low - gradual)
          │       ╲      ╲   ╲                   
          │        ╲       ╲    ╲                
          └────────────────────────────────────→ Time
```

#### Effect of Changing

| Value | Effect | Use Case |
|:------|:-------|:---------|
| **Lower** (0.1-0.5) | Extended, gradual landing | Smooth, premium feel |
| **Default** (1.0) | Natural landing | General purpose |
| **Higher** (1.5-2.0) | Quick, decisive landing | Precise positioning |

#### Example

```kotlin
// Smooth, gradual landing
FlingConfiguration.Builder()
    .splineEndTension(0.5f)
    .build()
```

---

### numberOfSplinePoints

The resolution of the spline curve — how many sample points are used to compute the animation curve.

| Property | Value |
|:---------|:------|
| **Default** | `100` |
| **Range** | `50` - `500` |
| **Unit** | Sample points |

#### Effect of Changing

| Value | Effect | Use Case |
|:------|:-------|:---------|
| **Lower** (50-80) | Slightly less smooth, better performance | Low-end devices |
| **Default** (100) | Good balance of smoothness and performance | General purpose |
| **Higher** (150-500) | Silky smooth curves, more computation | Premium devices, smooth UX |

#### Example

```kotlin
// Ultra-smooth for premium devices
FlingConfiguration.Builder()
    .numberOfSplinePoints(150)
    .build()
```

---

## Tuning Recipes

Here are complete configurations for common use cases:

### iOS-Style Scroll

Higher friction with controlled momentum, similar to iOS scroll physics.

```kotlin
FlingConfiguration.Builder()
    .scrollViewFriction(0.04f)
    .decelerationFriction(0.04f)
    .splineInflection(0.15f)
    .build()
```

### Photo Gallery (Floaty)

Long, gliding scrolls perfect for browsing visual content.

```kotlin
FlingConfiguration.Builder()
    .scrollViewFriction(0.006f)
    .decelerationFriction(0.015f)
    .gravitationalForce(7.0f)
    .numberOfSplinePoints(150)
    .build()
```

### Quick Selection (Snappy)

Fast, responsive scrolling for lists requiring precise selection.

```kotlin
FlingConfiguration.Builder()
    .scrollViewFriction(0.03f)
    .decelerationFriction(0.3f)
    .splineInflection(0.08f)
    .absVelocityThreshold(20f)
    .build()
```

### Bouncy/Playful

Fun, bouncy scrolling for games or playful UIs.

```kotlin
FlingConfiguration.Builder()
    .decelerationFriction(0.6f)
    .splineInflection(0.4f)
    .splineStartTension(0.3f)
    .build()
```

### Ultra-Premium

Buttery smooth scrolling for luxury/premium apps.

```kotlin
FlingConfiguration.Builder()
    .scrollViewFriction(0.006f)
    .decelerationFriction(0.05f)
    .splineEndTension(0.7f)
    .numberOfSplinePoints(150)
    .build()
```

### Accessibility / Reduced Motion

Minimal, controlled scrolling for users sensitive to motion.

```kotlin
FlingConfiguration.Builder()
    .scrollViewFriction(0.08f)
    .decelerationFriction(0.5f)
    .absVelocityThreshold(30f)
    .numberOfSplinePoints(80)
    .build()
```

---

## Troubleshooting

### Scroll feels too slow/heavy

- **Decrease** `scrollViewFriction` (try 0.004-0.006)
- **Decrease** `decelerationFriction` (try 0.02-0.05)
- **Decrease** `gravitationalForce` (try 5.0-7.0)

### Scroll feels too slippery/uncontrolled

- **Increase** `scrollViewFriction` (try 0.03-0.05)
- **Increase** `decelerationFriction` (try 0.15-0.3)
- Set `absVelocityThreshold` to filter out accidental swipes

### Scroll stops too abruptly

- **Decrease** `decelerationFriction` (try 0.02-0.05)
- **Decrease** `splineEndTension` (try 0.5-0.7)
- **Increase** `splineInflection` (try 0.15-0.2)

### Scroll start feels wrong

- Adjust `splineStartTension`:
  - **Lower** for gentler start
  - **Higher** for more aggressive start
- Adjust `splineInflection` to change when deceleration kicks in

### Animation looks choppy

- **Increase** `numberOfSplinePoints` (try 150-200)
- Check device performance — complex animations may need simpler configs on older devices

---

## Further Reading

- [Main README](../README.md) - Quick start and usage examples
- [FlingPresets.kt](../flinger/src/main/java/io/iamjosephmj/flinger/behaviours/FlingPresets.kt) - Pre-built configurations
- [Android OverScroller](https://developer.android.com/reference/android/widget/OverScroller) - The original Android implementation that inspired this library
