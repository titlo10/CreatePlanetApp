package com.example.createplanetapp.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.createplanetapp.R
import com.example.createplanetapp.csvParser
import com.example.createplanetapp.ui.theme.LocalNavController
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import com.example.createplanetapp.kazimirBold
import com.example.createplanetapp.kazimirRegular
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.createplanetapp.DBHelper
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import com.example.createplanetapp.ui.theme.blueColor
import com.example.createplanetapp.ui.theme.mainColor
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.ui.BiasAbsoluteAlignment

@Composable
fun HomePage() {
    val context = LocalContext.current
    val navController = LocalNavController.current
    val itemsPoGorodu = remember { csvParser(context.resources, R.raw.po_gorodu) }
    val itemsZagorodnie = remember { csvParser(context.resources, R.raw.zagorodnie) }
    var searchQuery by remember { mutableStateOf("") }

    val featuredItems = remember(itemsPoGorodu, itemsZagorodnie) {
        (itemsPoGorodu + itemsZagorodnie).filter { it.mark.isNotEmpty() }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 80.dp
            )
        ) {
            item {
                Column {
                    SearchField(searchQuery) { searchQuery = it }

                    Text(
                        text = "Не упустите возможность",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            if (featuredItems.isEmpty()) {
                item {
                    Text("Скоро появятся новые экскурсии")
                }
            } else {
                items(featuredItems) { item ->
                    CatalogItem(
                        modifier = Modifier.padding(bottom = 12.dp),
                        title = item.title,
                        description = item.description,
                        price = item.excursions.values.first().values.first().values.first().toString(),
                        photo = item.photo.firstOrNull() ?: "",
                        mark = item.mark,
                        displayMark = true,
                        onItemClick = {
                            navController.navigate("ItemMainPage/${item.title}")
                        }
                    )
                }
            }

            item {
                ContactInfoSection()
            }
        }
    }
}

@Composable
fun SearchField(searchQuery: String, onSearchQueryChanged: (String) -> Unit) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChanged,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        placeholder = { Text("Поиск экскурсий...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Поиск",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        )
    )
}

@Composable
fun ContactInfoSection() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp, bottom = 32.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "Контактная информация",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            ContactInfoItem(
                icon = Icons.Default.Phone,
                title = "Телефон",
                value = "+7 (953) 173 30-51"
            )

            Spacer(modifier = Modifier.height(16.dp))

            ContactInfoItem(
                icon = Icons.Default.Email,
                title = "Email",
                value = "leonid@createplanet.org"
            )

            Spacer(modifier = Modifier.height(16.dp))

            ContactInfoItem(
                icon = Icons.Default.LocationOn,
                title = "Адрес",
                value = "г. Санкт-Петербург"
            )
        }
    }
}

@Composable
fun ContactInfoItem(icon: ImageVector, title: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(28.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun CatalogItem(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    price: String,
    photo: String,
    mark: String,
    displayMark: Boolean = true,
    isForOrdersPage: Boolean = false,
    onItemClick: () -> Unit
) {
    val context = LocalContext.current
    val dbHelper = remember { DBHelper(context, null) }
    var isFavorite by rememberSaveable(title) { mutableStateOf(dbHelper.isFavorite(title)) }

    Card(
        modifier = modifier,
        onClick = onItemClick
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(contentAlignment = Alignment.TopStart) {
                Image(
                    painter = rememberAsyncImagePainter(photo),
                    contentDescription = "image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                if (displayMark && mark in listOf("HIT", "NEW", "FOR KIDS", "MUST VISIT", "ART", "HISTORY")) {
                    Image(
                        painter = when (mark) {
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

                if (isForOrdersPage) {
                    Text(
                        text = "Не оплачено",
                        fontFamily = kazimirRegular,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(6.dp)
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        color = Color(0xFFE33232)
                    )
                } else {
                    FilledIconToggleButton(
                        checked = isFavorite,
                        colors = IconButtonDefaults.filledIconToggleButtonColors().copy(
                            containerColor = Color.White,
                            checkedContainerColor = Color.White
                        ),
                        modifier = Modifier.padding(8.dp),
                        onCheckedChange = {
                            dbHelper.upsertRecord(title, (!isFavorite).toString(), "false")
                            isFavorite = !isFavorite
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Favorite,
                            contentDescription = "Избранное",
                            tint = if (isFavorite) blueColor else mainColor
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                Text(
                    text = title,
                    fontFamily = kazimirBold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = description,
                    fontFamily = kazimirRegular,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = "от $price р.",
                    fontFamily = kazimirRegular,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}