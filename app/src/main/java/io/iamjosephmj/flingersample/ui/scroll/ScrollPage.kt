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

package io.iamjosephmj.flingersample.ui.scroll

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.iamjosephmj.flinger.behaviours.FlingPresets
import io.iamjosephmj.flinger.flings.flingBehavior
import io.iamjosephmj.flingersample.ui.state.ScrollState

/**
 * Legacy scroll page for demonstrating fling behaviors.
 *
 * This page is kept for backward compatibility with the original sample app.
 * For a more comprehensive demo, see the new HomeScreen and its navigation options.
 *
 * @author Joseph James
 */

/**
 * @param navController used for navigating to the settings page.
 *
 * Entry point, this does 2 functions:
 * 1. Render settings button.
 * 2. Renders the list that is used for showcasing the scroll behaviour.
 */
@Composable
fun RenderScrollPage(navController: NavController) {
    Column(modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)) {
        RenderButtons(navController)
        Box(modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 10.dp))
        RenderList()
    }
}

@Composable
private fun RenderButtons(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back to home button
        Button(
            onClick = { navController.navigate("home") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Back to Home")
        }
        
        // Settings button
        Button(
            onClick = { navController.navigate("Settings") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Custom Settings")
        }
    }
}

@Composable
fun RenderList() {
    LazyColumn(
        modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 0.dp),
        flingBehavior = decideFlingBehaviour(),
    ) {
        items(100) { item ->
            Button(
                onClick = { },
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text(
                    text = "Item $item",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Box(modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 2.5.dp))
        }
    }
}

@Composable
fun decideFlingBehaviour(): FlingBehavior {
    return when (ScrollState.type) {
        0 -> {
            // Native scroll
            FlingPresets.androidNative()
        }
        1 -> {
            // Smooth scroll (new API)
            FlingPresets.smooth()
        }
        2 -> {
            // Custom scroll configuration
            flingBehavior(scrollConfiguration = ScrollState.buildScrollBehaviour())
        }
        else -> {
            // Default to smooth scroll
            FlingPresets.smooth()
        }
    }
}
