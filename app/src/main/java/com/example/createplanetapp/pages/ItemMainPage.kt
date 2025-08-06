package com.example.createplanetapp.pages

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.createplanetapp.DBHelper
import com.example.createplanetapp.GlobalData
import com.example.createplanetapp.ItemsViewModel
import com.example.createplanetapp.kazimirBold
import com.example.createplanetapp.kazimirRegular
import com.example.createplanetapp.kazimirSemibold
import com.example.createplanetapp.ui.theme.blueColor
import com.example.createplanetapp.ui.theme.mainColor

@Composable
fun ItemMainPage(title: String, onClick: () -> Unit) {
    val item: ItemsViewModel = GlobalData.items
        .flatten()
        .first { it.title == title }

    val context = LocalContext.current
    val dbHelper = remember { DBHelper(context, null) }

    var isTextExpanded by remember { mutableStateOf(false) }
    var isWayExpanded by remember { mutableStateOf(false) }
    var isTicketsExpanded by remember { mutableStateOf(false) }
    var isInfoExpanded by remember { mutableStateOf(false) }

    // Инициализируем с первыми доступными значениями
    val firstOptions = item.excursions.keys.toList()
    var selectedComposition by remember { mutableStateOf(firstOptions.firstOrNull() ?: "") }

    val secondOptions = selectedComposition.let {
        item.excursions[it]?.keys?.toList()
    } ?: emptyList()
    var selectedDuration by remember { mutableStateOf(secondOptions.firstOrNull() ?: "") }

    val thirdOptions = if (selectedComposition.isNotEmpty() && selectedDuration.isNotEmpty()) {
        item.excursions[selectedComposition]?.get(selectedDuration)?.keys?.toList() ?: emptyList()
    } else {
        emptyList()
    }
    var selectedCarClass by remember { mutableStateOf(thirdOptions.firstOrNull() ?: "") }

    // Автоматически обновляем дочерние выборы при изменении родительских
    LaunchedEffect(selectedComposition) {
        selectedDuration = item.excursions[selectedComposition]?.keys?.firstOrNull() ?: ""
    }

    LaunchedEffect(selectedDuration) {
        selectedCarClass = item.excursions[selectedComposition]?.get(selectedDuration)?.keys?.firstOrNull() ?: ""
    }

    /*ЗАНОВО ПЕРЕДЕЛАТЬ ДИНАМИЧЕСКОЕ ИЗМЕНЕНИЕ ЦЕНЫ*/

    Box(
        modifier = Modifier
            .padding(6.dp),
        contentAlignment = Alignment.TopEnd
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            val state = rememberLazyListState()

            //Images
            LazyRow(
                state = state,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                flingBehavior = rememberSnapFlingBehavior(lazyListState = state),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(item.photo) { photoUrl ->
                    Image(
                        painter = rememberAsyncImagePainter(photoUrl),
                        contentDescription = "Фото",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(300.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
            }

            //Excursion title
            Text(
                text = item.title,
                fontSize = 22.sp,
                fontFamily = kazimirBold,
                color = mainColor,
                modifier = Modifier.padding(start = 16.dp, top = 14.dp)
            )

            //Excursion description & brand
            Text(
                text = "${item.description} (${item.brand})",
                fontSize = 18.sp,
                fontFamily = kazimirRegular,
                color = mainColor,
                modifier = Modifier.padding(start = 16.dp, top = 14.dp)
            )

            //Excursion text
            Text(
                modifier = Modifier
                    .clickable { isTextExpanded = !isTextExpanded }
                    .padding(start = 16.dp, top = 14.dp),
                maxLines = if (isTextExpanded) Int.MAX_VALUE else 1,
                text = "Об экскурсии...\n${item.text}",
                fontSize = 18.sp,
                fontFamily = kazimirRegular,
                color = mainColor
            )

            //Excursion param controller: Composition
            Text(
                text = "Состав",
                fontSize = 18.sp,
                fontFamily = kazimirRegular,
                color = mainColor,
                modifier = Modifier.padding(start = 16.dp, top = 25.dp)
            )
            DropdownSelector(
                options = firstOptions,
                initialValue = selectedComposition,
                onSelectionChange = { selectedComposition = it }
            )

            //Excursion param controller: Duration
            if (secondOptions.isNotEmpty()) {
                Text(
                    text = "Продолжительность",
                    fontSize = 18.sp,
                    fontFamily = kazimirRegular,
                    color = mainColor,
                    modifier = Modifier.padding(start = 16.dp, top = 15.dp)
                )
                DropdownSelector(
                    options = secondOptions,
                    initialValue = selectedDuration,
                    onSelectionChange = { selectedDuration = it }
                )
            }

            //Excursion param controller: CarClass
            if (thirdOptions.isNotEmpty()) {
                Text(
                    text = "Класс Авто",
                    fontSize = 18.sp,
                    fontFamily = kazimirRegular,
                    color = mainColor,
                    modifier = Modifier.padding(start = 16.dp, top = 15.dp)
                )
                DropdownSelector(
                    options = thirdOptions,
                    initialValue = selectedCarClass,
                    onSelectionChange = { selectedCarClass = it }
                )
            }

            //Excursion price
            if (selectedComposition.isNotEmpty() &&
                selectedDuration.isNotEmpty() &&
                selectedCarClass.isNotEmpty()
            ) {

                val price = getItemPrice(
                    item.excursions,
                    selectedComposition,
                    selectedDuration,
                    selectedCarClass
                ) ?: 0

                Text(
                    text = "Цена: $price р.",
                    fontSize = 18.sp,
                    fontFamily = kazimirRegular,
                    color = mainColor,
                    modifier = Modifier.padding(start = 16.dp, top = 14.dp)
                )
            }

            //Excursion way
            Text(
                modifier = Modifier
                    .clickable { isWayExpanded = !isWayExpanded }
                    .padding(start = 16.dp, top = 14.dp),
                maxLines = if (isWayExpanded) Int.MAX_VALUE else 1,
                text = "Маршрут экскурсии...\n",
                fontSize = 18.sp,
                fontFamily = kazimirRegular,
                color = mainColor
            )

            //Excursion entrance tickets
            Text(
                modifier = Modifier
                    .clickable { isTicketsExpanded = !isTicketsExpanded }
                    .padding(start = 16.dp, top = 14.dp),
                maxLines = if (isTicketsExpanded) Int.MAX_VALUE else 1,
                text = "Входные билеты...\n",
                fontSize = 18.sp,
                fontFamily = kazimirRegular,
                color = mainColor
            )

            //Excursion important info
            Text(
                modifier = Modifier
                    .clickable { isInfoExpanded = !isInfoExpanded }
                    .padding(start = 16.dp, top = 14.dp),
                maxLines = if (isInfoExpanded) Int.MAX_VALUE else 1,
                text = "Важная информация...\n",
                fontSize = 18.sp,
                fontFamily = kazimirRegular,
                color = mainColor
            )


            //Booking button
            Box(modifier = Modifier.fillMaxSize()) {
                Button(
                    onClick = {
                        dbHelper.setOrderedStatus(title, "true")
                        Toast.makeText(context, "Бронь оформлена!", Toast.LENGTH_LONG).show()
                              },
                    modifier = Modifier
                        .padding(start = 16.dp, top = 20.dp, bottom = 30.dp)
                        .align(Alignment.BottomCenter),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = blueColor,
                        disabledContentColor = mainColor
                    )
                ) {
                    Text(
                        text = "Забронировать",
                        fontSize = 18.sp,
                        fontFamily = kazimirSemibold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelector(
    options: List<String>,
    initialValue: String = options.first(),
    onSelectionChange: (selectedValue: String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(initialValue) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 50.dp, bottom = 10.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = !isExpanded },
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = mainColor,
                    shape = RoundedCornerShape(16.dp)
                )
                .height(40.dp)
        ) {
            Box(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = selectedText,
                    fontFamily = kazimirRegular,
                    color = mainColor,
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = isExpanded,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }

            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                options.forEach { text ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = text,
                                fontFamily = kazimirRegular,
                                color = mainColor,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        },
                        onClick = {
                            selectedText = text
                            isExpanded = false
                            onSelectionChange(text) // Передаем выбранное значение
                        },
                        contentPadding = PaddingValues(vertical = 12.dp)
                    )
                }
            }
        }
    }
}

fun getItemPrice(
    excursions: MutableMap<String, MutableMap<String, MutableMap<String, Int>>>,
    composition: String,
    duration: String,
    carClass: String
): Int? {
    return excursions[composition]
        ?.get(duration)
        ?.get(carClass)
        ?.takeIf { it > 0 }
}