package com.borgesnath1.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Home : Route

    @Serializable
    data object List : Route

    @Serializable
    data object CriaEvento : Route

    @Serializable
    data object Perfil : Route

    @Serializable
    data object DetalhesEvento : Route
}

sealed class BottomNavItem(val title: String, val icon: ImageVector, val route: Route)  {
    data object HomeButton :  BottomNavItem("Início", Icons.Default.Home, Route.Home)
    data object ListaEventoButton :  BottomNavItem("Meus Eventos", Icons.Default.Favorite, Route.List)
    data object CriarEventoButton  :  BottomNavItem("Criar Evento", Icons.Default.CalendarMonth, Route.CriaEvento)
    data object Perfil : BottomNavItem("Meu Perfil", Icons.Default.Person, Route.Perfil)
}
