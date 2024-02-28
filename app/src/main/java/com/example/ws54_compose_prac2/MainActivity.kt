package com.example.ws54_compose_prac2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ws54_compose_prac2.page.HomePage
import com.example.ws54_compose_prac2.page.RegionPage
import com.example.ws54_compose_prac2.service.JsonService
import com.example.ws54_compose_prac2.service.PreferenceManager
import com.example.ws54_compose_prac2.ui.theme.WS54_Compose_prac2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val jsonService = JsonService
        val jsonString = jsonService.readJsonFromAssets(this,"weatherData.json")
        val data = JsonService.parseJson(jsonString) // get a weatherResponse
        super.onCreate(savedInstanceState)
        setContent {
            WS54_Compose_prac2Theme {
                val navController = rememberNavController()
                val preferenceManager = PreferenceManager(this)
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    NavHost(navController = navController, startDestination = "Home"){
                        if (data != null) {
                            composable("Home"){
                                HomePage.build(data, navController)
                            }
                            composable("Region"){
                                RegionPage.build(data, navController, preferencesManager = preferenceManager)
                            }
                        }
                    }
                }
            }
        }
    }
}