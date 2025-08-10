package com.example.createplanetapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.createplanetapp.pages.HomePage
import com.example.createplanetapp.pages.CatalogPage
import com.example.createplanetapp.pages.ProfilePage
import com.example.createplanetapp.pages.OrdersPage
import com.example.createplanetapp.pages.FavoritesPage
import com.example.createplanetapp.ui.theme.blueColor
import com.example.createplanetapp.ui.theme.lightBlueColor

val kazimirRegular = FontFamily(Font(R.font.kazimir_text_regular))
val kazimirSemibold = FontFamily(Font(R.font.kazimir_text_semibold))
val kazimirBold = FontFamily(Font(R.font.kazimir_text_bold))

@Composable
fun MainScreen(authViewModel: AuthViewModel) {
    val navItemList = listOf(
        NavItem("Главная", R.drawable.home_menu_icon),
        NavItem("Каталог", R.drawable.catalog_menu_icon),
        NavItem("Избранное", R.drawable.favorite_menu_icon),
        NavItem("Заказы", R.drawable.orders_menu_icon),
        NavItem("Профиль", R.drawable.profile_menu_icon),
    )

    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    val catalogState: CatalogState = viewModel()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                navItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index
                        },
                        icon = {
                            Icon(painter = painterResource(navItem.icon), contentDescription = null, modifier = Modifier.size(30.dp))
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = lightBlueColor,
                            unselectedIconColor = blueColor,
                            indicatorColor = Color.Transparent,
                        ),
                    )
                }
            }
        }
    ) { innerPadding ->
        ContentScreen(
            modifier = Modifier.padding(innerPadding),
            selectedIndex = selectedIndex,
            catalogState = catalogState,
            authViewModel = authViewModel
        )
    }
}


@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex: Int, catalogState: CatalogState, authViewModel: AuthViewModel) {
    when (selectedIndex) {
        0 -> HomePage()
        1 -> CatalogPage(catalogState = catalogState)
        2 -> FavoritesPage(authViewModel = authViewModel)
        3 -> OrdersPage(authViewModel = authViewModel)
        4 -> ProfilePage(authViewModel = authViewModel)
    }
}