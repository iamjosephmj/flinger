package io.iamjosephmj.flinger.ui.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.iamjosephmj.flinger.ui.scroll.RenderScrollPage
import io.iamjosephmj.flinger.ui.settings.RenderSettingsPage

@Composable
fun CreateNavHost(navController: NavController) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = "scrollPage",
    ) {
        composable("scrollPage") { RenderScrollPage(navController) }
        composable("Settings") { RenderSettingsPage(navController) }
    }
}