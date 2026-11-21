package com.borgesnath1.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.borgesnath1.MainViewModel
import com.borgesnath1.ui.CriaEvento
import com.borgesnath1.ui.DetalhesEventoPage
import com.borgesnath1.ui.HomePage
import com.borgesnath1.ui.ListPage
import com.borgesnath1.ui.ProfilePage

@Composable
fun MainNavHost (navController: NavHostController,
                 modifier: Modifier = Modifier,
                 viewModel: MainViewModel
) {
    NavHost(navController, startDestination = Route.Home) {
        composable<Route.Home> {
            HomePage(
                modifier = modifier,
                viewModel = viewModel,
                onNavigateToDetails = { navController.navigate(Route.DetalhesEvento) },
                onNavigateToProfile = { navController.navigate(Route.Perfil) }
            )
        }
        composable<Route.List> {
            ListPage(
                modifier = modifier,
                viewModel = viewModel,
                onNavigateToDetails = { navController.navigate(Route.DetalhesEvento) },
                onNavigateToCreate = { navController.navigate(Route.CriaEvento) }
            )
        }
        composable<Route.CriaEvento> {
            CriaEvento(
                modifier = modifier,
                viewModel = viewModel,
                onEventoCriado = {
                    // Tira a tela de criação da pilha; senão a barra de baixo restaura
                    // "CriaEvento -> List" e o botão Criar Evento cai de novo na lista
                    navController.navigate(Route.List) {
                        popUpTo(Route.Home)
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<Route.Perfil> {
            ProfilePage(modifier = modifier, viewModel = viewModel)
        }
        composable<Route.DetalhesEvento> {
            DetalhesEventoPage(
                modifier = modifier,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
