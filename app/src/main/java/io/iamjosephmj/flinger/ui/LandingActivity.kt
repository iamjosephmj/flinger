package io.iamjosephmj.flinger.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.iamjosephmj.flinger.ui.utils.CreateNavHost
import io.iamjosephmj.flinger.ui.utils.LightTheme

class LandingActivity : AppCompatActivity() {

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LightTheme {
                Scaffold(modifier = Modifier.background(Color.White)) {
                    InitializeNavComponentsAndView()
                }
            }
        }
    }

    @Composable
    private fun InitializeNavComponentsAndView() {
        navController = rememberNavController()
        CreateNavHost(navController = navController)
    }

    override fun onBackPressed() {
        if (!navController.navigateUp()) {
            super.onBackPressed()
        }
    }


}