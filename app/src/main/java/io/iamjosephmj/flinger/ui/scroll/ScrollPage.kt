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
import androidx.navigation.compose.navigate
import io.iamjosephmj.flinger.bahaviours.ScrollBehaviourBank
import io.iamjosephmj.flinger.flings.flingBehavior
import io.iamjosephmj.flinger.ui.state.ScrollState


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
        flingBehavior = DecideFlingBehaviour(),
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
fun DecideFlingBehaviour(): FlingBehavior {
    return when (ScrollState.type) {
        0 -> {
            ScrollBehaviourBank.getAndroidNativeScroll()
        }
        1 -> {
            ScrollBehaviourBank.lowInflectionLowFrictionLowDecelerationHighSampleScroll()
        }
        2 -> {
            flingBehavior(scrollConfiguration = ScrollState.buildScrollBehaviour())
        }
        else -> {
            ScrollBehaviourBank.lowInflectionLowFrictionLowDecelerationHighSampleScroll()
        }
    }
}

