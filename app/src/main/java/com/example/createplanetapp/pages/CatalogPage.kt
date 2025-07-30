package com.example.createplanetapp.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.createplanetapp.CatalogState
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

val sortDropDownColor = Color(0xFF2F2E2E)

val buttonsContentList = listOf("Туры", "Экскурсии", "Проживание", "Услуги")

val toursList = listOf("Школьникам", "Семьям")
val excursionsList = listOf("По городу", "Пешеходные", "Загородные", "В музеях")
val residenceList = listOf("Отели", "Апарт-отели", "Апартаменты", "Хостелы")
val servicesList = listOf("Подбор отеля", "Трансфер", "Аренда авто", "Катера и теплоходы", "Фотопрогулки", "Туристическое меню", "Корпоративный отдых")

var listInDropDownMenu = toursList

val listInDropDownSortMenu = listOf("По умолчанию", "По возрастанию цены", "По убыванию цены", "От А-Я", "От Я-А", "Сначала новые", "Сначала старые")

@Composable
fun CatalogPage(
    modifier: Modifier = Modifier,
    catalogState: CatalogState
) {
    Column(
        modifier = modifier
            .systemBarsPadding()
            .fillMaxSize()
    ) {
        CatalogMainButtons(
            selectedButton = catalogState.selectedButton,
            onSelectedButtonChange = { newSelectedButton ->
                catalogState.updateState(button = newSelectedButton, option = "Выберите раздел")
            }
        )

        CatalogDropdownMenu(
            selectedOption = catalogState.selectedOption,
            selectedSortType = catalogState.selectedSortType,
            onSelectedSortTypeChange = { newSelectedSortType ->
                catalogState.updateState(sortType = newSelectedSortType)
            },
            onSelectedOptionChange = {newSelectedOption ->
                catalogState.updateState(option = newSelectedOption)
            }
        )

        CatalogGoodsList(catalogState = catalogState)
    }
}

@Composable
private fun CatalogMainButtons(
    selectedButton: String,
    onSelectedButtonChange: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .horizontalScroll(scrollState)
    ) {
        for(element in buttonsContentList) {
            CatalogButton(
                onClick = {
                    listInDropDownMenu = when (element) {
                        "Туры" -> toursList
                        "Экскурсии" -> excursionsList
                        "Проживание" -> residenceList
                        else -> servicesList
                    }
                    onSelectedButtonChange(element)
                },
                text = element,
                isSelected = (selectedButton == element),
                modifier = if(element == "Туры") Modifier.padding(start = 12.dp, end = 6.dp)
                else Modifier.padding(end = 6.dp)
            )
        }
    }
}

/**
 * This is a default button, but with changed shape, colors, border, content padding.
 *   I.e. it is a SHABLON
 *
 * @param text represents the text inside the button
 * @param fontSize font size of the text
 * @param fontFamily font family of the text
 * @param textColor color of the text inside the button
 * @param isSelected marking a button as selected. By default - false, if true - borders
 *   becomes stronger, without "alpha parameter".
 *   If in your realization you don't need border change - don't touch this param.
 */
@Composable
fun CatalogButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,                                               //added
    fontSize: TextUnit = 24.sp,                                 //added
    fontFamily: FontFamily = kazimirRegular,                    //added
    textColor: Color = mainColor,                               //added
    enabled: Boolean = true,
    isSelected: Boolean = false,                                //added
    shape: Shape = RoundedCornerShape(16.dp),                   //changed
    colors: ButtonColors = ButtonDefaults.buttonColors(         //changed
        containerColor = Color.Transparent,
        disabledContentColor = mainColor,
        contentColor = Color.Transparent
    ),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    border: BorderStroke? =
        if (isSelected) BorderStroke(1.dp, mainColor)
        else BorderStroke(1.dp, mainColor.copy(alpha = 0.1f)),  //changed
    contentPadding: PaddingValues = PaddingValues(16.dp),       //changed
    interactionSource: MutableInteractionSource? = null
) {
    Button(onClick, modifier, enabled, shape, colors, elevation, border, contentPadding, interactionSource) {
        Text(
            text = text,
            fontSize = fontSize,
            fontFamily = fontFamily,
            color = textColor
        )
    }
}


@Composable
private fun CatalogDropdownMenu(
    selectedOption: String,
    selectedSortType: String,
    onSelectedSortTypeChange: (String) -> Unit,
    onSelectedOptionChange: (String) -> Unit
) {
    var isExpandedMainDropDown by rememberSaveable { mutableStateOf(false) }
    var isExpandedSortDropDown by remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, top = 16.dp, end = 26.dp),
            horizontalArrangement = Arrangement.spacedBy(26.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CatalogDropDownButton(
                onClick = { isExpandedMainDropDown = true },
                modifier = Modifier.weight(1f),
                text = selectedOption,
                isExpanded = isExpandedMainDropDown
            )

            IconButton(
                onClick = { isExpandedSortDropDown = true },
                modifier = Modifier.size(26.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_catalog_sort),
                    contentDescription = "Сортировка",
                    modifier = Modifier.size(24.dp),
                    tint = if(isExpandedSortDropDown || selectedSortType != "По умолчанию") mainColor
                    else Color(0xFFA3A3A3)
                )
            }
        }

        //Выпадающий список основной кнопки
        DropdownMenu(
            expanded = isExpandedMainDropDown,
            onDismissRequest = { isExpandedMainDropDown = false },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .background(Color.Transparent),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, mainColor),
            offset = DpOffset(x = 12.dp, y = 0.dp)
        ) {
            for(element in listInDropDownMenu) {
                DropdownMenuItem(
                    onClick = {
                        onSelectedOptionChange(element)
                        isExpandedMainDropDown = false
                    },
                    modifier = Modifier.height(40.dp),
                    text = {
                        Text(
                            text = element,
                            fontSize = 16.sp,
                            fontFamily = kazimirSemibold,
                            color = mainColor
                        )
                    }
                )
            }
        }

        //Выпадающий список сортировки
        DropdownMenu(
            // TODO: внешний вид списка сортировки?
            expanded = isExpandedSortDropDown,
            onDismissRequest = { isExpandedSortDropDown = false },
            modifier = Modifier
                .wrapContentSize()
                .background(Color.Transparent),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, mainColor),
            offset = DpOffset(x = 170.dp, y = 5.dp)
        ) {
            for(element in listInDropDownSortMenu) {
                DropdownMenuItem(
                    onClick = {
                        onSelectedSortTypeChange(element)
                        isExpandedSortDropDown = false
                    },
                    modifier = Modifier.height(30.dp),
                    text = {
                        Text(
                            text = element,
                            fontSize = 14.sp,
                            fontFamily = if (element == selectedSortType) kazimirSemibold else kazimirRegular,
                            textDecoration = TextDecoration.Underline,
                            color = sortDropDownColor
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun CatalogDropDownButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    isExpanded: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(16.dp),
        border = BorderStroke(1.dp, mainColor),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                fontFamily = kazimirSemibold,
                fontSize = 18.sp,
                color = mainColor.copy(alpha = 0.8f)
            )
            Icon(
                imageVector = if (isExpanded)
                    Icons.Filled.KeyboardArrowUp
                else Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = if (isExpanded) "Свернуть" else "Развернуть",
                tint = mainColor
            )
        }
    }
}


@Composable
private fun CatalogGoodsList(
    catalogState: CatalogState
) {
    val context = LocalContext.current

    val itemsPoGorodu = remember { csvParser(context.resources, R.raw.po_gorodu) }
    val itemsZagorodnie = remember { csvParser(context.resources, R.raw.zagorodnie) }

    var previousOption by remember { mutableStateOf(catalogState.selectedOption) }
    var shouldRestoreScroll by remember { mutableStateOf(true) }

    val listToDisplay = remember (catalogState.selectedButton, catalogState.selectedOption, catalogState.selectedSortType) {
        when (catalogState.selectedButton) {
            "Экскурсии" -> when (catalogState.selectedOption) {
                "По городу" -> itemsPoGorodu
                "Загородные" -> itemsZagorodnie
                else -> emptyList()
            }
            else -> emptyList()
        }.sortedWith(getItemsComparator(catalogState.selectedSortType))
    }

    val scrollState = rememberScrollState()

    LaunchedEffect(catalogState.selectedOption) {
        if (catalogState.selectedOption != previousOption) {
            scrollState.scrollTo(0)
            previousOption = catalogState.selectedOption
            shouldRestoreScroll = false
        } else if (shouldRestoreScroll) {
            scrollState.scrollTo(catalogState.scrollState)
            shouldRestoreScroll = false
        }
    }

    LaunchedEffect(scrollState.value) {
        if (!shouldRestoreScroll) {
            catalogState.updateState(scroll = scrollState.value)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, bottom = 88.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            for(item in listToDisplay) {
                key(item.title) {
                    CatalogItem(
                        modifier = if(item == listToDisplay[0]) Modifier.padding(start = 26.dp, end = 26.dp)
                        else Modifier.padding(top = 16.dp, start = 26.dp, end = 26.dp),
                        title = item.title,
                        description = item.description,
                        price = item.excursions.values.first().values.first().values.first().toString(),
                        photo = item.photo[0],
                        mark = item.mark
                    )
                }
            }
        }
    }
}


/**
 * Composable function which display one particular good on the screen.
 *
 * Use this in a loop through the entire list you want to display
 *
 * @param modifier modifier to be applied
 * @param title good's title. Use `.title` field of this item to get it
 * @param description good's description. Use `.description` field
 * @param price good's lowest price. To parse nested mutable maps and find lowest value
 *        use `.excursions.values.first().values.first().values.first().toString()`
 * @param photo good's photo url to download. Use `.photo[0]` field
 * @param mark string value. Could be `HIT`, `NEW`, `MUST VISIT`, `ART`, `HISTORY`, `FOR KIDS`.
 *        Depends on "what exactly mark it is" displays corresponding image in the topLeft corner
 * @param displayMark do you need to display any mark? By default `true` (displays marks).
 * @param isForOrdersPage `true` if you use it in "Orders" page, otherwise `false` (by default).
 *        If true - changing "like" icon to "Оплачено"/"Не оплачено"
 */
@Composable
fun CatalogItem(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    price: String,
    photo: String,
    mark: String,
    displayMark: Boolean = true,
    isForOrdersPage: Boolean = false
) {
    val context = LocalContext.current
    val dbHelper = remember { DBHelper(context, null) }
    var isFavorite by rememberSaveable(title) { mutableStateOf(dbHelper.isFavorite(title)) }

    // var isPaid by rememberSaveable(title) { mutableStateOf(dbHelper.isPaid(title)) }
    //в бд добавить поле Оплачено/Не оплачено + функцию на проверку этого поля по аналогии с isFavorite


    val navController = LocalNavController.current

    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopEnd
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClickLabel = "Узнать подробности") {
                    navController.navigate("ItemMainPage/$title")
                }
        ) {
            Box(contentAlignment = Alignment.TopStart) {
                Image(
                    painter = rememberAsyncImagePainter(photo),
                    //painter = painterResource(R.drawable.for_test_only),
                    contentDescription = "image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(10.dp))
                )

                if(displayMark &&
                    mark in listOf("HIT", "NEW", "FOR KIDS", "MUST VISIT", "ART", "HISTORY")) {
                    Image(
                        painter = when(mark) {
                            "HIT" -> painterResource(R.drawable.ic_mark_hit)
                            "NEW" -> painterResource(R.drawable.ic_mark_new)
                            "FOR KIDS" -> painterResource(R.drawable.ic_mark_for_kids)
                            "HISTORY" -> painterResource(R.drawable.ic_mark_history)
                            "ART" -> painterResource(R.drawable.ic_mark_art)
                            else -> painterResource(R.drawable.ic_mark_must_visit)
                        },
                        contentDescription = "Метка экскурсии",
                        modifier = Modifier.padding(3.dp)
                            .size(60.dp)
                    )
                }
            }

            Text(
                text = title,
                fontFamily = kazimirBold,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp, start = 5.dp)
            )
            Text(
                text = description,
                fontFamily = kazimirRegular,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp, start = 5.dp)
            )
            Text(
                text = "от $price р.",
                fontFamily = kazimirRegular,
                fontSize = 14.sp,
                modifier = if (description == "") Modifier.padding(start = 5.dp, bottom = 4.dp)
                else Modifier.padding(top = 16.dp, start = 5.dp, bottom = 4.dp)
            )
        }

        if(isForOrdersPage) {
            // TODO: add check for isPaid & add text for this situation
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
                    .padding(start = 12.dp, end = 12.dp, top = 6.dp, bottom = 6.dp),
                color = Color(0xFFE33232)
            )
        }
        else {
            FilledIconToggleButton(
                checked = isFavorite,
                colors = IconButtonDefaults.filledIconToggleButtonColors().copy(
                    containerColor = Color.White,
                    checkedContainerColor = Color.White
                ),
                onCheckedChange = {
                    dbHelper.upsertRecord(title, (!isFavorite).toString(), "false")
                    isFavorite = !isFavorite
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Favorite,
                    contentDescription = "Избранное - добавить/удалить",
                    tint = if(isFavorite) blueColor else mainColor
                )
            }
        }
    }
}

private fun getItemsComparator(sortType: String): Comparator<ItemsViewModel> {
    return when (sortType) {
        "По возрастанию цены" -> compareBy { it.excursions.values.first().values.first().values.first() }
        "По убыванию цены" -> compareByDescending { it.excursions.values.first().values.first().values.first() }
        "От А-Я" -> compareBy { it.title.lowercase() }
        "От Я-А" -> compareByDescending { it.title.lowercase() }
        "Сначала новые" -> Comparator{ a: ItemsViewModel, b: ItemsViewModel ->
            when {
                a.mark == "NEW" && b.mark != "NEW" -> -1
                a.mark != "NEW" && b.mark == "NEW" -> 1
                else -> 0
            }
        }
        "Сначала старые" -> Comparator{ a: ItemsViewModel, b: ItemsViewModel ->
            when {
                a.mark == "NEW" && b.mark != "NEW" -> 1
                a.mark != "NEW" && b.mark == "NEW" -> -1
                else -> 0
            }
        }
        else -> compareBy { 0 }
    }
}