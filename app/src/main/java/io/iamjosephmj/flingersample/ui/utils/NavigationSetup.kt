/*
* MIT License
*
* Copyright (c) 2021 Joseph James
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*
*/

package io.iamjosephmj.flingersample.ui.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.iamjosephmj.flingersample.ui.comparison.ComparisonScreen
import io.iamjosephmj.flingersample.ui.home.HomeScreen
import io.iamjosephmj.flingersample.ui.playground.PlaygroundScreen
import io.iamjosephmj.flingersample.ui.presets.PresetsGalleryScreen
import io.iamjosephmj.flingersample.ui.scroll.RenderScrollPage
import io.iamjosephmj.flingersample.ui.settings.RenderSettingsPage

/**
 * Navigation setup for the Flinger sample app.
 *
 * Routes:
 * - "home" - Main dashboard with navigation cards
 * - "playground" - Interactive parameter playground
 * - "presets" - Preset gallery with visual previews
 * - "comparison" - Side-by-side comparison screen
 * - "scrollPage" - Legacy scroll demo page
 * - "Settings" - Legacy settings page
 */
@Composable
fun CreateNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home",
    ) {
        // New screens
        composable("home") { HomeScreen(navController) }
        composable("playground") { PlaygroundScreen(navController) }
        composable("presets") { PresetsGalleryScreen(navController) }
        composable("comparison") { ComparisonScreen(navController) }
        
        // Legacy screens (kept for backward compatibility)
        composable("scrollPage") { RenderScrollPage(navController) }
        composable("Settings") { RenderSettingsPage(navController) }
    }
}
