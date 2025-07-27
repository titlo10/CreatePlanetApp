package com.example.createplanetapp.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.createplanetapp.DBHelper
import com.example.createplanetapp.ItemsViewModel
import com.example.createplanetapp.R
import com.example.createplanetapp.csvParser
import com.example.createplanetapp.kazimirBold
import com.example.createplanetapp.kazimirRegular
import com.example.createplanetapp.kazimirSemibold
import com.example.createplanetapp.ui.theme.LocalNavController
import com.example.createplanetapp.ui.theme.blueColor
import com.example.createplanetapp.ui.theme.mainColor

@Composable
fun FavoritesPage(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val dbHelper = remember { DBHelper(context, null) }
    var favoriteItems by remember { mutableStateOf<List<ItemsViewModel>>(emptyList()) }

    fun loadFavorites() {
        val itemsPoGorodu = csvParser(context.resources, R.raw.po_gorodu)
        val itemsZagorodnie = csvParser(context.resources, R.raw.zagorodnie)
        val allItems = itemsPoGorodu + itemsZagorodnie

        favoriteItems = dbHelper.getRecords(allItems, true, false)
    }

    loadFavorites()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp)
    ) {
        Text(
            text = "Избранное",
            fontSize = 30.sp,
            fontFamily = kazimirSemibold,
            color = mainColor,
            modifier = Modifier.padding(top = 30.dp, bottom = 30.dp)
        )

        when {
            favoriteItems.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Нет избранных экскурсий",
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 100.dp)
                ) {
                    items(favoriteItems) { item ->
                        FavoriteItem(
                            item = item,
                            onFavoriteChanged = { loadFavorites() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteItem(
    item: ItemsViewModel,
    onFavoriteChanged: () -> Unit
) {
    val context = LocalContext.current
    val dbHelper = remember { DBHelper(context, null) }
    val navController = LocalNavController.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable {
                navController.navigate("ItemMainPage/${item.title}")
            }
    ) {
        Column {
            Box(contentAlignment = Alignment.TopStart) {
                Image(
                    painter = rememberAsyncImagePainter(item.photo[0]),
                    contentDescription = "image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(10.dp))
                )

                if (item.mark in listOf("HIT", "NEW", "FOR KIDS", "MUST VISIT", "ART", "HISTORY")) {
                    Image(
                        painter = when(item.mark) {
                            "HIT" -> painterResource(R.drawable.ic_mark_hit)
                            "NEW" -> painterResource(R.drawable.ic_mark_new)
                            "FOR KIDS" -> painterResource(R.drawable.ic_mark_for_kids)
                            "HISTORY" -> painterResource(R.drawable.ic_mark_history)
                            "ART" -> painterResource(R.drawable.ic_mark_art)
                            else -> painterResource(R.drawable.ic_mark_must_visit)
                        },
                        contentDescription = "Метка экскурсии",
                        modifier = Modifier
                            .padding(3.dp)
                            .size(60.dp)
                    )
                }
            }

            Text(
                text = item.title,
                fontFamily = kazimirBold,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = item.description,
                fontFamily = kazimirRegular,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = "от ${item.excursions.values.first().values.first().values.first()} р.",
                fontFamily = kazimirRegular,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
            )
        }

        FilledIconToggleButton(
            checked = true,
            colors = IconButtonDefaults.filledIconToggleButtonColors().copy(
                containerColor = Color.White,
                checkedContainerColor = Color.White
            ),
            onCheckedChange = {
                dbHelper.upsertRecord(item.title, false, false)
                onFavoriteChanged()
            },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Outlined.Favorite,
                contentDescription = "Удалить из избранного",
                tint = blueColor
            )
        }
    }
}