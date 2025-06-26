package com.example.createplanetapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.createplanetapp.pages.HomePage
import com.example.createplanetapp.pages.CatalogPage
import com.example.createplanetapp.pages.ProfilePage
import com.example.createplanetapp.pages.OrdersPage
import com.example.createplanetapp.pages.FavoritesPage

@Composable
fun MainScreen() {
    val navItemList = listOf(
        NavItem("Главная", R.drawable.home_menu_icon),
        NavItem("Каталог", R.drawable.catalog_menu_icon),
        NavItem("Избранное", R.drawable.favorite_menu_icon),
        NavItem("Заказы", R.drawable.orders_menu_icon),
        NavItem("Профиль", R.drawable.profile_menu_icon),
    )

    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

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
                            Icon(painter = painterResource(navItem.icon), contentDescription = null)
                        },
                        label = {
                            Text(text = navItem.label)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        ContentScreen(modifier = Modifier.padding(innerPadding), selectedIndex)
    }
}


@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex: Int) {
    when (selectedIndex) {
        0 -> HomePage()
        1 -> CatalogPage()
        2 -> FavoritesPage()
        3 -> OrdersPage()
        4 -> ProfilePage()
    }
}