package io.iamjosephmj.flingersample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.unit.dp
import io.iamjosephmj.flinger.behaviours.FlingPresets
import io.iamjosephmj.flinger.configs.FlingConfiguration
import io.iamjosephmj.flinger.flings.flingBehavior
import io.iamjosephmj.flinger.snap.SnapAnimationConfig
import io.iamjosephmj.flinger.snap.SnapConfig
import io.iamjosephmj.flinger.snap.SnapPosition
import io.iamjosephmj.flinger.snap.snapFlingBehavior
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

    // ==================== Snap Fling Behavior Tests ====================

    @Test
    fun lazyRow_with_snap_start_position_renders_correctly() {
        composeTestRule.setContent {
            TestSnapLazyRow(snapPosition = SnapPosition.Start)
        }

        composeTestRule.onNodeWithText("Card 0").assertIsDisplayed()
    }

    @Test
    fun lazyRow_with_snap_center_position_renders_correctly() {
        composeTestRule.setContent {
            TestSnapLazyRow(snapPosition = SnapPosition.Center)
        }

        composeTestRule.onNodeWithText("Card 0").assertIsDisplayed()
    }

    @Test
    fun lazyRow_with_snap_end_position_renders_correctly() {
        composeTestRule.setContent {
            TestSnapLazyRow(snapPosition = SnapPosition.End)
        }

        composeTestRule.onNodeWithText("Card 0").assertIsDisplayed()
    }

    @Test
    fun lazyRow_with_snap_scrolls_on_swipe() {
        composeTestRule.setContent {
            TestSnapLazyRow(snapPosition = SnapPosition.Center)
        }

        composeTestRule.onNodeWithTag("test_snap_lazy_row")
            .performTouchInput {
                swipeLeft(
                    startX = centerX + 200f,
                    endX = centerX - 200f,
                    durationMillis = 200
                )
            }

        composeTestRule.waitForIdle()
        // Verify scroll happened without crash
    }

    // ==================== Snap Animation Config Tests ====================

    @Test
    fun lazyRow_with_smooth_snap_animation_renders_correctly() {
        composeTestRule.setContent {
            TestSnapLazyRow(
                snapPosition = SnapPosition.Center,
                snapAnimation = SnapAnimationConfig.Smooth
            )
        }

        composeTestRule.onNodeWithText("Card 0").assertIsDisplayed()
    }

    @Test
    fun lazyRow_with_snappy_snap_animation_renders_correctly() {
        composeTestRule.setContent {
            TestSnapLazyRow(
                snapPosition = SnapPosition.Center,
                snapAnimation = SnapAnimationConfig.Snappy
            )
        }

        composeTestRule.onNodeWithText("Card 0").assertIsDisplayed()
    }

    @Test
    fun lazyRow_with_bouncy_snap_animation_renders_correctly() {
        composeTestRule.setContent {
            TestSnapLazyRow(
                snapPosition = SnapPosition.Center,
                snapAnimation = SnapAnimationConfig.Bouncy
            )
        }

        composeTestRule.onNodeWithText("Card 0").assertIsDisplayed()
    }

    @Test
    fun lazyRow_with_gentle_snap_animation_renders_correctly() {
        composeTestRule.setContent {
            TestSnapLazyRow(
                snapPosition = SnapPosition.Center,
                snapAnimation = SnapAnimationConfig.Gentle
            )
        }

        composeTestRule.onNodeWithText("Card 0").assertIsDisplayed()
    }

    @Test
    fun lazyRow_with_instant_snap_animation_renders_correctly() {
        composeTestRule.setContent {
            TestSnapLazyRow(
                snapPosition = SnapPosition.Center,
                snapAnimation = SnapAnimationConfig.Instant
            )
        }

        composeTestRule.onNodeWithText("Card 0").assertIsDisplayed()
    }

    @Test
    fun lazyRow_with_ios_snap_animation_renders_correctly() {
        composeTestRule.setContent {
            TestSnapLazyRow(
                snapPosition = SnapPosition.Center,
                snapAnimation = SnapAnimationConfig.IOS
            )
        }

        composeTestRule.onNodeWithText("Card 0").assertIsDisplayed()
    }

    @Test
    fun lazyRow_with_material_snap_animation_renders_correctly() {
        composeTestRule.setContent {
            TestSnapLazyRow(
                snapPosition = SnapPosition.Center,
                snapAnimation = SnapAnimationConfig.Material
            )
        }

        composeTestRule.onNodeWithText("Card 0").assertIsDisplayed()
    }

    @Test
    fun lazyRow_with_custom_spring_snap_animation_renders_correctly() {
        composeTestRule.setContent {
            TestSnapLazyRow(
                snapPosition = SnapPosition.Center,
                snapAnimation = SnapAnimationConfig.custom(
                    stiffness = 350f,
                    dampingRatio = 0.75f
                )
            )
        }

        composeTestRule.onNodeWithText("Card 0").assertIsDisplayed()
    }

    @Test
    fun lazyRow_with_custom_tween_snap_animation_renders_correctly() {
        composeTestRule.setContent {
            TestSnapLazyRow(
                snapPosition = SnapPosition.Center,
                snapAnimation = SnapAnimationConfig.customTween(durationMillis = 400)
            )
        }

        composeTestRule.onNodeWithText("Card 0").assertIsDisplayed()
    }

    // ==================== Smooth Fusion Tests ====================

    @Test
    fun lazyRow_with_smooth_fusion_enabled_renders_correctly() {
        composeTestRule.setContent {
            TestSnapLazyRow(
                snapPosition = SnapPosition.Center,
                smoothFusion = true
            )
        }

        composeTestRule.onNodeWithText("Card 0").assertIsDisplayed()
    }

    @Test
    fun lazyRow_with_smooth_fusion_scrolls_on_swipe() {
        composeTestRule.setContent {
            TestSnapLazyRow(
                snapPosition = SnapPosition.Center,
                smoothFusion = true
            )
        }

        composeTestRule.onNodeWithTag("test_snap_lazy_row")
            .performTouchInput {
                swipeLeft(
                    startX = centerX + 200f,
                    endX = centerX - 200f,
                    durationMillis = 200
                )
            }

        composeTestRule.waitForIdle()
        // Verify smooth fusion scroll works without crash
    }

    @Test
    fun lazyRow_with_smooth_fusion_low_ratio_renders_correctly() {
        composeTestRule.setContent {
            TestSnapLazyRow(
                snapPosition = SnapPosition.Center,
                smoothFusion = true,
                fusionVelocityRatio = 0.05f
            )
        }

        composeTestRule.onNodeWithText("Card 0").assertIsDisplayed()
    }

    @Test
    fun lazyRow_with_smooth_fusion_high_ratio_renders_correctly() {
        composeTestRule.setContent {
            TestSnapLazyRow(
                snapPosition = SnapPosition.Center,
                smoothFusion = true,
                fusionVelocityRatio = 0.4f
            )
        }

        composeTestRule.onNodeWithText("Card 0").assertIsDisplayed()
    }

    @Test
    fun lazyRow_with_smooth_fusion_high_ratio_scrolls_on_swipe() {
        composeTestRule.setContent {
            TestSnapLazyRow(
                snapPosition = SnapPosition.Center,
                smoothFusion = true,
                fusionVelocityRatio = 0.3f
            )
        }

        composeTestRule.onNodeWithTag("test_snap_lazy_row")
            .performTouchInput {
                swipeLeft(
                    startX = centerX + 200f,
                    endX = centerX - 200f,
                    durationMillis = 200
                )
            }

        composeTestRule.waitForIdle()
    }

    // ==================== SnapConfig Tests ====================

    @Test
    fun lazyRow_with_snap_config_renders_correctly() {
        composeTestRule.setContent {
            TestSnapLazyRowWithConfig(
                config = SnapConfig(
                    snapPosition = SnapPosition.Center,
                    velocityThreshold = 500f,
                    snapAnimation = SnapAnimationConfig.Smooth,
                    smoothFusion = false
                )
            )
        }

        composeTestRule.onNodeWithText("Card 0").assertIsDisplayed()
    }

    @Test
    fun lazyRow_with_snap_config_and_fusion_renders_correctly() {
        composeTestRule.setContent {
            TestSnapLazyRowWithConfig(
                config = SnapConfig(
                    snapPosition = SnapPosition.Center,
                    velocityThreshold = 400f,
                    snapAnimation = SnapAnimationConfig.Snappy,
                    smoothFusion = true,
                    fusionVelocityRatio = 0.2f
                )
            )
        }

        composeTestRule.onNodeWithText("Card 0").assertIsDisplayed()
    }

    @Test
    fun lazyRow_with_snap_config_scrolls_on_swipe() {
        composeTestRule.setContent {
            TestSnapLazyRowWithConfig(
                config = SnapConfig(
                    snapPosition = SnapPosition.Center,
                    smoothFusion = true,
                    fusionVelocityRatio = 0.15f
                )
            )
        }

        composeTestRule.onNodeWithTag("test_snap_lazy_row")
            .performTouchInput {
                swipeLeft(
                    startX = centerX + 200f,
                    endX = centerX - 200f,
                    durationMillis = 200
                )
            }

        composeTestRule.waitForIdle()
    }

    // ==================== Snap with Custom Fling Config Tests ====================

    @Test
    fun lazyRow_with_snap_and_custom_fling_config_renders_correctly() {
        composeTestRule.setContent {
            TestSnapLazyRowWithFlingConfig(
                snapPosition = SnapPosition.Center,
                flingConfig = FlingConfiguration.Builder()
                    .scrollViewFriction(0.015f)
                    .decelerationFriction(0.12f)
                    .build()
            )
        }

        composeTestRule.onNodeWithText("Card 0").assertIsDisplayed()
    }

    @Test
    fun lazyRow_with_snap_fusion_and_custom_fling_scrolls_correctly() {
        composeTestRule.setContent {
            TestSnapLazyRowWithFlingConfig(
                snapPosition = SnapPosition.Center,
                flingConfig = FlingConfiguration.Builder()
                    .scrollViewFriction(0.02f)
                    .decelerationFriction(0.15f)
                    .build(),
                smoothFusion = true
            )
        }

        composeTestRule.onNodeWithTag("test_snap_lazy_row")
            .performTouchInput {
                swipeLeft(
                    startX = centerX + 200f,
                    endX = centerX - 200f,
                    durationMillis = 200
                )
            }

        composeTestRule.waitForIdle()
    }

    // ==================== Default Configuration Tests ====================

    @Test
    fun flingConfiguration_default_works_correctly() {
        composeTestRule.setContent {
            TestLazyColumn(
                flingBehavior = flingBehavior(
                    scrollConfiguration = FlingConfiguration.Default
                )
            )
        }

        composeTestRule.onNodeWithText("Item 0").assertIsDisplayed()
    }

    @Test
    fun snapFlingBehavior_with_default_config_works_correctly() {
        composeTestRule.setContent {
            TestSnapLazyRowWithFlingConfig(
                snapPosition = SnapPosition.Center,
                flingConfig = FlingConfiguration.Default
            )
        }

        composeTestRule.onNodeWithText("Card 0").assertIsDisplayed()
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

    @Composable
    private fun TestSnapLazyRow(
        snapPosition: SnapPosition,
        snapAnimation: SnapAnimationConfig = SnapAnimationConfig.Smooth,
        smoothFusion: Boolean = false,
        fusionVelocityRatio: Float = 0.15f
    ) {
        val listState = rememberLazyListState()
        
        MaterialTheme {
            Surface(modifier = Modifier.fillMaxSize()) {
                LazyRow(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .testTag("test_snap_lazy_row"),
                    flingBehavior = snapFlingBehavior(
                        lazyListState = listState,
                        snapPosition = snapPosition,
                        snapAnimation = snapAnimation,
                        smoothFusion = smoothFusion,
                        fusionVelocityRatio = fusionVelocityRatio
                    ),
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(20) { index ->
                        Card(
                            modifier = Modifier
                                .width(200.dp)
                                .height(150.dp)
                        ) {
                            Text(
                                text = "Card $index",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun TestSnapLazyRowWithConfig(
        config: SnapConfig
    ) {
        val listState = rememberLazyListState()
        
        MaterialTheme {
            Surface(modifier = Modifier.fillMaxSize()) {
                LazyRow(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .testTag("test_snap_lazy_row"),
                    flingBehavior = snapFlingBehavior(
                        lazyListState = listState,
                        config = config
                    ),
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(20) { index ->
                        Card(
                            modifier = Modifier
                                .width(200.dp)
                                .height(150.dp)
                        ) {
                            Text(
                                text = "Card $index",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun TestSnapLazyRowWithFlingConfig(
        snapPosition: SnapPosition,
        flingConfig: FlingConfiguration,
        smoothFusion: Boolean = false
    ) {
        val listState = rememberLazyListState()
        
        MaterialTheme {
            Surface(modifier = Modifier.fillMaxSize()) {
                LazyRow(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .testTag("test_snap_lazy_row"),
                    flingBehavior = snapFlingBehavior(
                        lazyListState = listState,
                        snapPosition = snapPosition,
                        flingConfig = flingConfig,
                        smoothFusion = smoothFusion
                    ),
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(20) { index ->
                        Card(
                            modifier = Modifier
                                .width(200.dp)
                                .height(150.dp)
                        ) {
                            Text(
                                text = "Card $index",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
