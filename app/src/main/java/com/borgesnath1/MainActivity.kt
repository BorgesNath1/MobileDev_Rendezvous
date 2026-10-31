package com.borgesnath1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.borgesnath1.ui.nav.BottomNavBar
import com.borgesnath1.ui.nav.BottomNavItem
import com.borgesnath1.ui.nav.MainNavHost
import com.borgesnath1.ui.theme.RendezvousTheme
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val currentRout = navController.currentBackStackEntryAsState()
            val viewModel: MainViewModel = viewModel()

            RendezvousTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        val items = listOf(
                            BottomNavItem.HomeButton,
                            BottomNavItem.ListaEventoButton,
                            BottomNavItem.CriarEventoButton,
                            BottomNavItem.Perfil
                        )
                        BottomNavBar(navController = navController, items = items)
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        MainNavHost(navController = navController, viewModel = viewModel)
                    }
                }
            }
        }
    }
}
