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

package io.iamjosephmj.flinger.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import androidx.navigation.compose.popUpTo
import io.iamjosephmj.flinger.ui.state.ScrollState
import io.iamjosephmj.flinger.ui.utils.toFloatNum

/**
 * The below set of methods are used to render the settings page page.
 *
 * @author Joseph James.
 */

/**
 * Entry point.
 */
@Composable
fun RenderSettingsPage(navController: NavController) {
    BuildPage(navController)
}

@Composable
fun BuildPage(navController: NavController) {
    // Heading.
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        Text(
            text = "Scroll Settings",
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            textAlign = TextAlign.Center
        )
        val radioSelection = remember {
            // default selection is smooth-scroll behaviour.
            mutableStateOf("smooth")
        }
        // Radio buttons used for selecting native, smooth or custom scroll behaviours.
        BuildRadioButton(radioSelection)

        // Edit texts for setting the custom scroll behaviour.
        BuildRadioSelectionPage(radioSelection, navController)
    }
}

@Composable
fun BuildRadioSelectionPage(radioSelection: MutableState<String>, navController: NavController) {
    if (radioSelection.value == "custom" ||
        ScrollState.type == 2
    ) {
        BuildEditTexts(navController)
    } else {
        DrawApplyButton(navController)
    }
}

@Composable
fun DrawApplyButton(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                navController.navigate("scrollPage")
            },
            modifier = Modifier.size(200.dp, 60.dp)
        ) {
            Text(text = "Apply")
        }
    }
}

@Composable
fun BuildRadioButton(radioSelection: MutableState<String>) {
    val options = listOf(
        "native", "smooth", "custom"
    )
    radioGroup(
        radioOptions = options,
        onItemSelection = { selection ->
            ScrollState.type = options.indexOf(selection)
            radioSelection.value = selection

        }
    )
}

@Composable
fun BuildEditTexts(navController: NavController) {

    val scrollFriction = remember { mutableStateOf(ScrollState.scrollFriction.toString()) }
    val absVelocityThreshold =
        remember { mutableStateOf(ScrollState.absVelocityThreshold.toString()) }
    val gravitationalForce = remember { mutableStateOf(ScrollState.gravitationalForce.toString()) }
    val inchesPerMeter = remember { mutableStateOf(ScrollState.inchesPerMeter.toString()) }
    val decelerationFriction =
        remember { mutableStateOf(ScrollState.decelerationFriction.toString()) }
    val decelerationRate = remember { mutableStateOf(ScrollState.decelerationRate.toString()) }
    val splineInflection = remember { mutableStateOf(ScrollState.splineInflection.toString()) }
    val splineStartTension = remember { mutableStateOf(ScrollState.splineStartTension.toString()) }
    val splineEndTension = remember { mutableStateOf(ScrollState.splineEndTension.toString()) }
    val numberOfSplinePoints =
        remember { mutableStateOf(ScrollState.numberOfSplinePoints.toString()) }

    val focusManager = LocalFocusManager.current

    Spacer(modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 10.dp))


    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(100.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(10.dp, 0.dp, 0.dp, 10.dp)
                .width(100.dp)
        ) {
            Text(
                text = "Scroll Friction",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            TextField(
                value = scrollFriction.value,
                maxLines = 1,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = {
                    when {
                        it.filter { data ->
                            data.toString() == "."
                        }.count() > 1 -> {
                            scrollFriction.value = ScrollState.scrollFriction.toString()
                        }
                        it.isNotEmpty() -> {
                            scrollFriction.value = it
                            ScrollState.scrollFriction = it.toFloatNum()
                        }
                        it.isEmpty() -> {
                            scrollFriction.value = ""
                            ScrollState.scrollFriction = 0f
                        }

                    }
                },
                modifier = Modifier.width(100.dp)
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(10.dp, 0.dp, 0.dp, 10.dp)
                .width(100.dp)
        ) {
            Text(
                text = "absVelocityThreshold",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            TextField(
                value = absVelocityThreshold.value,
                maxLines = 1,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = {
                    when {
                        it.filter { data ->
                            data.toString() == "."
                        }.count() > 1 -> {
                            absVelocityThreshold.value = ScrollState.absVelocityThreshold.toString()
                        }
                        it.isNotEmpty() -> {
                            absVelocityThreshold.value = it
                            ScrollState.absVelocityThreshold = it.toFloatNum()
                        }
                        it.isEmpty() -> {
                            absVelocityThreshold.value = ""
                            ScrollState.absVelocityThreshold = 0f
                        }

                    }
                },
                modifier = Modifier.width(100.dp),
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(10.dp, 0.dp, 0.dp, 10.dp)
                .width(100.dp)
        ) {
            Text(
                text = "gravitationalForce",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            TextField(
                value = gravitationalForce.value,
                maxLines = 1,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = {
                    when {
                        it.filter { data ->
                            data.toString() == "."
                        }.count() > 1 -> {
                            gravitationalForce.value = ScrollState.gravitationalForce.toString()
                        }
                        it.isNotEmpty() -> {
                            gravitationalForce.value = it
                            ScrollState.gravitationalForce = it.toFloatNum()
                        }
                        it.isEmpty() -> {
                            gravitationalForce.value = ""
                            ScrollState.gravitationalForce = 0f
                        }

                    }
                },
                modifier = Modifier.width(100.dp)
            )
        }

    }

    Row(horizontalArrangement = Arrangement.Center) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(10.dp, 0.dp, 0.dp, 10.dp)
                .width(100.dp)
        ) {
            Text(
                text = "inchesPerMeter",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            TextField(
                value = inchesPerMeter.value,
                maxLines = 1,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = {
                    when {
                        it.filter { data ->
                            data.toString() == "."
                        }.count() > 1 -> {
                            inchesPerMeter.value = ScrollState.inchesPerMeter.toString()
                        }
                        it.isNotEmpty() -> {
                            inchesPerMeter.value = it
                            ScrollState.inchesPerMeter = it.toFloatNum()
                        }
                        it.isEmpty() -> {
                            inchesPerMeter.value = ""
                            ScrollState.inchesPerMeter = 0f
                        }

                    }
                },
                modifier = Modifier.width(100.dp)
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(10.dp, 0.dp, 0.dp, 10.dp)
                .width(100.dp)
        ) {
            Text(
                text = "decelerationFriction",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            TextField(
                value = decelerationFriction.value,
                maxLines = 1,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = {
                    when {
                        it.filter { data ->
                            data.toString() == "."
                        }.count() > 1 -> {
                            decelerationFriction.value = ScrollState.decelerationFriction.toString()
                        }
                        it.isNotEmpty() -> {
                            decelerationFriction.value = it
                            ScrollState.decelerationFriction = it.toFloatNum()
                        }
                        it.isEmpty() -> {
                            decelerationFriction.value = ""
                            ScrollState.decelerationFriction = 0f
                        }

                    }
                },
                modifier = Modifier.width(100.dp),
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(10.dp, 0.dp, 0.dp, 10.dp)
                .width(100.dp)
        ) {
            Text(
                text = "decelerationRate",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            TextField(
                value = decelerationRate.value,
                maxLines = 1,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = {
                    when {
                        it.filter { data ->
                            data.toString() == "."
                        }.count() > 1 -> {
                            decelerationRate.value = ScrollState.decelerationRate.toString()
                        }
                        it.isNotEmpty() -> {
                            decelerationRate.value = it
                            ScrollState.decelerationRate = it.toFloatNum()
                        }
                        it.isEmpty() -> {
                            decelerationRate.value = ""
                            ScrollState.decelerationRate = 0f
                        }

                    }
                },
                modifier = Modifier.width(100.dp)
            )
        }

    }

    Row(horizontalArrangement = Arrangement.Center) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(10.dp, 0.dp, 0.dp, 10.dp)
                .width(100.dp)
        ) {
            Text(
                text = "splineInflection",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            TextField(
                value = splineInflection.value,
                maxLines = 1,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = {
                    when {
                        it.filter { data ->
                            data.toString() == "."
                        }.count() > 1 -> {
                            splineInflection.value = ScrollState.splineInflection.toString()
                        }
                        it.isNotEmpty() -> {
                            splineInflection.value = it
                            ScrollState.splineInflection = it.toFloatNum()
                        }
                        it.isEmpty() -> {
                            splineInflection.value = ""
                            ScrollState.splineInflection = 0f
                        }

                    }
                },
                modifier = Modifier.width(100.dp)
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(10.dp, 0.dp, 0.dp, 10.dp)
                .width(100.dp)
        ) {
            Text(
                text = "splineStartTension",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            TextField(
                value = splineStartTension.value,
                maxLines = 1,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = {
                    when {
                        it.filter { data ->
                            data.toString() == "."
                        }.count() > 1 -> {
                            splineStartTension.value = ScrollState.splineStartTension.toString()
                        }
                        it.isNotEmpty() -> {
                            splineStartTension.value = it
                            ScrollState.splineStartTension = it.toFloatNum()
                        }
                        it.isEmpty() -> {
                            splineStartTension.value = ""
                            ScrollState.splineStartTension = 0f
                        }

                    }
                },
                modifier = Modifier.width(100.dp),
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(10.dp, 0.dp, 0.dp, 10.dp)
                .width(100.dp)
        ) {
            Text(
                text = "splineEndTension",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            TextField(
                value = splineEndTension.value,
                maxLines = 1,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = {
                    when {
                        it.filter { data ->
                            data.toString() == "."
                        }.count() > 1 -> {
                            splineEndTension.value = ScrollState.splineEndTension.toString()
                        }
                        it.isNotEmpty() -> {
                            splineEndTension.value = it
                            ScrollState.splineEndTension = it.toFloatNum()
                        }
                        it.isEmpty() -> {
                            splineEndTension.value = ""
                            ScrollState.splineEndTension = 0f
                        }

                    }
                },
                modifier = Modifier.width(100.dp)
            )
        }

    }

    Row(horizontalArrangement = Arrangement.Center) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(10.dp, 0.dp, 0.dp, 10.dp)
                .width(100.dp)
        ) {
            Text(
                text = "numberOfSplinePoints",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            TextField(
                value = numberOfSplinePoints.value,
                maxLines = 1,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                onValueChange = {
                    when {
                        it.filter { data ->
                            data.toString() == "."
                        }.count() > 1 -> {
                            numberOfSplinePoints.value = ScrollState.numberOfSplinePoints.toString()
                        }
                        it.isNotEmpty() -> {
                            numberOfSplinePoints.value = it
                            ScrollState.numberOfSplinePoints = it.toInt()
                        }
                        it.isEmpty() -> {
                            numberOfSplinePoints.value = ""
                            ScrollState.numberOfSplinePoints = 0
                        }

                    }
                },
                modifier = Modifier.width(100.dp)
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                navController.navigate("scrollPage") {
                    popUpTo("scrollPage") {
                        inclusive = true
                    }
                }
            },
            modifier = Modifier.size(200.dp, 60.dp)
        ) {
            Text(text = "apply")
        }
    }
}


@Composable
fun radioGroup(
    radioOptions: List<String> = listOf(),
    title: String = "",
    cardBackgroundColor: Color = Color(0xFFFEFEFA),
    onItemSelection: (selection: String) -> Unit
): String {
    if (radioOptions.isNotEmpty()) {
        val selectedOption = remember {
            mutableStateOf(radioOptions[ScrollState.type])
        }

        val onOptionSelected: (selection: String) -> Unit = { selection ->
            selectedOption.value = selection
            onItemSelection(selection)
        }

        Card(
            backgroundColor = cardBackgroundColor,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            elevation = 8.dp,
            shape = RoundedCornerShape(8.dp),
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
            ) {
                Text(
                    text = title,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(5.dp),
                )

                radioOptions.forEach { item ->
                    Row(
                        Modifier
                            .padding(5.dp)
                            .weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (item == selectedOption.value),
                            onClick = { onOptionSelected(item) }
                        )

                        val annotatedString = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(fontWeight = FontWeight.Bold)
                            ) { append("  $item  ") }
                        }

                        ClickableText(
                            text = annotatedString,
                            onClick = {
                                onOptionSelected(item)
                            }
                        )
                    }
                }
            }
        }
        return selectedOption.value
    } else {
        return ""
    }
}