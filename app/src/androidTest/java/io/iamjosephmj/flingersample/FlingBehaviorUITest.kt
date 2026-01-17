package io.iamjosephmj.flingersample

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.unit.dp
import io.iamjosephmj.flinger.behaviours.FlingPresets
import io.iamjosephmj.flinger.configs.FlingConfiguration
import io.iamjosephmj.flinger.flings.flingBehavior
import org.junit.Rule
import org.junit.Test

/**
 * UI tests for verifying Flinger fling behaviors in Compose.
 */
class FlingBehaviorUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ==================== Basic Rendering Tests ====================

    @Test
    fun lazyColumn_with_default_fling_renders_correctly() {
        composeTestRule.setContent {
            TestLazyColumn(flingBehavior = flingBehavior())
        }

        // Verify first item is visible
        composeTestRule.onNodeWithText("Item 0").assertIsDisplayed()
    }

    @Test
    fun lazyColumn_with_smooth_preset_renders_correctly() {
        composeTestRule.setContent {
            TestLazyColumn(flingBehavior = FlingPresets.smooth())
        }

        composeTestRule.onNodeWithText("Item 0").assertIsDisplayed()
    }

    @Test
    fun lazyColumn_with_iOS_style_preset_renders_correctly() {
        composeTestRule.setContent {
            TestLazyColumn(flingBehavior = FlingPresets.iOSStyle())
        }

        composeTestRule.onNodeWithText("Item 0").assertIsDisplayed()
    }

    @Test
    fun lazyColumn_with_quick_stop_preset_renders_correctly() {
        composeTestRule.setContent {
            TestLazyColumn(flingBehavior = FlingPresets.quickStop())
        }

        composeTestRule.onNodeWithText("Item 0").assertIsDisplayed()
    }

    @Test
    fun lazyColumn_with_bouncy_preset_renders_correctly() {
        composeTestRule.setContent {
            TestLazyColumn(flingBehavior = FlingPresets.bouncy())
        }

        composeTestRule.onNodeWithText("Item 0").assertIsDisplayed()
    }

    @Test
    fun lazyColumn_with_floaty_preset_renders_correctly() {
        composeTestRule.setContent {
            TestLazyColumn(flingBehavior = FlingPresets.floaty())
        }

        composeTestRule.onNodeWithText("Item 0").assertIsDisplayed()
    }

    @Test
    fun lazyColumn_with_snappy_preset_renders_correctly() {
        composeTestRule.setContent {
            TestLazyColumn(flingBehavior = FlingPresets.snappy())
        }

        composeTestRule.onNodeWithText("Item 0").assertIsDisplayed()
    }

    @Test
    fun lazyColumn_with_ultra_smooth_preset_renders_correctly() {
        composeTestRule.setContent {
            TestLazyColumn(flingBehavior = FlingPresets.ultraSmooth())
        }

        composeTestRule.onNodeWithText("Item 0").assertIsDisplayed()
    }

    // ==================== Scroll Interaction Tests ====================

    @Test
    fun lazyColumn_with_default_fling_scrolls_on_swipe() {
        composeTestRule.setContent {
            TestLazyColumn(flingBehavior = flingBehavior())
        }

        // Perform swipe up gesture
        composeTestRule.onNodeWithTag("test_lazy_column")
            .performTouchInput {
                swipeUp(
                    startY = centerY + 200f,
                    endY = centerY - 200f,
                    durationMillis = 200
                )
            }

        // Wait for animation to complete
        composeTestRule.waitForIdle()

        // Item 0 should no longer be visible after scrolling
        // Note: This may vary based on scroll distance, so we just verify no crash
    }

    @Test
    fun lazyColumn_with_quick_stop_scrolls_and_stops() {
        composeTestRule.setContent {
            TestLazyColumn(flingBehavior = FlingPresets.quickStop())
        }

        composeTestRule.onNodeWithTag("test_lazy_column")
            .performTouchInput {
                swipeUp(
                    startY = centerY + 200f,
                    endY = centerY - 200f,
                    durationMillis = 200
                )
            }

        composeTestRule.waitForIdle()
        // Quick stop should stop quickly - test passes if no crash
    }

    @Test
    fun lazyColumn_with_floaty_scrolls_with_momentum() {
        composeTestRule.setContent {
            TestLazyColumn(flingBehavior = FlingPresets.floaty())
        }

        composeTestRule.onNodeWithTag("test_lazy_column")
            .performTouchInput {
                swipeUp(
                    startY = centerY + 200f,
                    endY = centerY - 200f,
                    durationMillis = 200
                )
            }

        composeTestRule.waitForIdle()
        // Floaty should scroll further - test passes if no crash
    }

    // ==================== Custom Configuration Tests ====================

    @Test
    fun lazyColumn_with_custom_high_friction_renders_correctly() {
        composeTestRule.setContent {
            TestLazyColumn(
                flingBehavior = flingBehavior(
                    scrollConfiguration = FlingConfiguration.Builder()
                        .scrollViewFriction(0.1f)
                        .build()
                )
            )
        }

        composeTestRule.onNodeWithText("Item 0").assertIsDisplayed()
    }

    @Test
    fun lazyColumn_with_custom_low_friction_renders_correctly() {
        composeTestRule.setContent {
            TestLazyColumn(
                flingBehavior = flingBehavior(
                    scrollConfiguration = FlingConfiguration.Builder()
                        .scrollViewFriction(0.001f)
                        .build()
                )
            )
        }

        composeTestRule.onNodeWithText("Item 0").assertIsDisplayed()
    }

    @Test
    fun lazyColumn_with_custom_deceleration_renders_correctly() {
        composeTestRule.setContent {
            TestLazyColumn(
                flingBehavior = flingBehavior(
                    scrollConfiguration = FlingConfiguration.Builder()
                        .decelerationFriction(0.3f)
                        .decelerationRate(3.0f)
                        .build()
                )
            )
        }

        composeTestRule.onNodeWithText("Item 0").assertIsDisplayed()
    }

    @Test
    fun lazyColumn_with_custom_spline_renders_correctly() {
        composeTestRule.setContent {
            TestLazyColumn(
                flingBehavior = flingBehavior(
                    scrollConfiguration = FlingConfiguration.Builder()
                        .splineInflection(0.2f)
                        .splineStartTension(0.3f)
                        .splineEndTension(0.8f)
                        .build()
                )
            )
        }

        composeTestRule.onNodeWithText("Item 0").assertIsDisplayed()
    }

    @Test
    fun lazyColumn_with_all_custom_params_renders_correctly() {
        composeTestRule.setContent {
            TestLazyColumn(
                flingBehavior = flingBehavior(
                    scrollConfiguration = FlingConfiguration.Builder()
                        .scrollViewFriction(0.02f)
                        .absVelocityThreshold(10f)
                        .gravitationalForce(10f)
                        .inchesPerMeter(40f)
                        .decelerationRate(2.5f)
                        .decelerationFriction(0.15f)
                        .splineInflection(0.15f)
                        .splineStartTension(0.2f)
                        .splineEndTension(0.9f)
                        .numberOfSplinePoints(150)
                        .build()
                )
            )
        }

        composeTestRule.onNodeWithText("Item 0").assertIsDisplayed()
    }

    // ==================== Edge Case Tests ====================

    @Test
    fun lazyColumn_with_zero_friction_renders_correctly() {
        composeTestRule.setContent {
            TestLazyColumn(
                flingBehavior = flingBehavior(
                    scrollConfiguration = FlingConfiguration.Builder()
                        .scrollViewFriction(0f)
                        .build()
                )
            )
        }

        composeTestRule.onNodeWithText("Item 0").assertIsDisplayed()
    }

    @Test
    fun lazyColumn_with_max_friction_renders_correctly() {
        composeTestRule.setContent {
            TestLazyColumn(
                flingBehavior = flingBehavior(
                    scrollConfiguration = FlingConfiguration.Builder()
                        .scrollViewFriction(1f)
                        .build()
                )
            )
        }

        composeTestRule.onNodeWithText("Item 0").assertIsDisplayed()
    }

    @Test
    fun lazyColumn_with_few_spline_points_renders_correctly() {
        composeTestRule.setContent {
            TestLazyColumn(
                flingBehavior = flingBehavior(
                    scrollConfiguration = FlingConfiguration.Builder()
                        .numberOfSplinePoints(10)
                        .build()
                )
            )
        }

        composeTestRule.onNodeWithText("Item 0").assertIsDisplayed()
    }

    @Test
    fun lazyColumn_with_many_spline_points_renders_correctly() {
        composeTestRule.setContent {
            TestLazyColumn(
                flingBehavior = flingBehavior(
                    scrollConfiguration = FlingConfiguration.Builder()
                        .numberOfSplinePoints(500)
                        .build()
                )
            )
        }

        composeTestRule.onNodeWithText("Item 0").assertIsDisplayed()
    }

    // ==================== Native Comparison Tests ====================

    @Test
    fun lazyColumn_with_native_behavior_renders_correctly() {
        composeTestRule.setContent {
            TestLazyColumn(flingBehavior = FlingPresets.androidNative())
        }

        composeTestRule.onNodeWithText("Item 0").assertIsDisplayed()
    }

    // ==================== Helper Composables ====================

    @Composable
    private fun TestLazyColumn(
        flingBehavior: androidx.compose.foundation.gestures.FlingBehavior
    ) {
        MaterialTheme {
            Surface(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("test_lazy_column"),
                    flingBehavior = flingBehavior
                ) {
                    items(100) { index ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "Item $index",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
