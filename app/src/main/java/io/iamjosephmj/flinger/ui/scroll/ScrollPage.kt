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

package io.iamjosephmj.flinger.ui.scroll

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.iamjosephmj.flinger.bahaviours.StockFlingBehaviours
import io.iamjosephmj.flinger.flings.flingBehavior
import io.iamjosephmj.flinger.ui.state.ScrollState

/**
 * The below set of methods are used to render the scroll page.
 *
 * @author Joseph James.
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
        RenderButton(navController)
        Spacer(modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 10.dp))
        RenderList()
    }
}

@Composable
private fun RenderButton(navController: NavController) {
    // Settings button.
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                navController.navigate("Settings")
            },
            modifier = Modifier.size(200.dp, 60.dp)
        ) {
            Text(text = "Settings")
        }
    }
}

@Composable
fun RenderList() {
    LazyColumn(
        modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 0.dp),
        /*
         * Important, this is the main functionality that we are looking into.
         */
        flingBehavior = decideFlingBehaviour(),
    ) {
        items(100) { item ->
            Button(
                onClick = { }, shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text(
                    text = "no $item",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h6
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
            // Native scroll.
            StockFlingBehaviours.getAndroidNativeScroll()
        }
        1 -> {
            // Smooth scroll.
            StockFlingBehaviours.smoothScroll()
        }
        2 -> {
            /*
             * custom scroll, this is how you should build the scroll behaviour.
             * you can build the scroll behaviour with the below mentioned builder pattern.
             */
            flingBehavior(scrollConfiguration = ScrollState.buildScrollBehaviour())
        }
        else -> {
            // Smooth scroll.
            StockFlingBehaviours.smoothScroll()
        }
    }
}

