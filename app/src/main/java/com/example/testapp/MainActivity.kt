package com.example.testapp

import WeatherScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigationevent.NavigationEvent
import com.example.testapp.ui.theme.TestAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherScreen()
//            TestAppTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    val navController = rememberNavController()
//                    NavHost(navController, startDestination = "home") {
//                        composable("home") {
//                            Button(
//                                onClick = {
//                                navController.navigate("detail/12")
//                                },
//                                modifier = Modifier.padding(innerPadding)
//                            ) {
//                                Text("Go to Detail")
//                            }
//                        }
//                        composable(
//                            route = "detail/{id}",
//                            arguments = listOf(
//                                navArgument("id") { type = NavType.IntType }
//                            )
//                            ) {
//                            val id = it.arguments?.getInt("id") ?: 0
//                            HomeScreen(navController, innerPadding, id)
//                        }
//                    }
//                }
//            }
        }
    }
}



@Composable
fun HomeScreen(navController: NavHostController, padding: PaddingValues, id: Int = 0){
    Column (
        modifier = Modifier.fillMaxWidth()
            .padding(padding)
    ) {
        Text(
            text = "Home screen ${id}"
        )
        Button(
            onClick = {
                navController.popBackStack()
            }
        ) {
            Text("CLICK TO HOME")
        }
    }
}
