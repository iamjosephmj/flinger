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

package io.iamjosephmj.flingersample.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.iamjosephmj.flingersample.ui.theme.FlingerTheme
import io.iamjosephmj.flingersample.ui.utils.CreateNavHost

/**
 * Main activity for the Flinger sample app.
 *
 * This app demonstrates the capabilities of the Flinger library:
 * - Interactive fling parameter playground
 * - Preset gallery with visual previews
 * - Side-by-side comparison with native Android scroll
 * - Snap-to-item gallery demo
 * - Adaptive fling demo
 * - Pager physics demo
 * - Debug overlay visualization
 *
 * Features the Aurora theme - a dark-mode-first design with electric
 * cyan and magenta accents inspired by physics/motion visualization.
 *
 * @author Joseph James
 */
class LandingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable edge-to-edge display
        enableEdgeToEdge()
        
        // Set the decor to fit system windows for proper edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        setContent {
            FlingerTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    FlingerApp()
                }
            }
        }
    }

    @Composable
    private fun FlingerApp() {
        val navController: NavHostController = rememberNavController()
        CreateNavHost(navController = navController)
    }
}
