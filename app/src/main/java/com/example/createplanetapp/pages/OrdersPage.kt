package com.example.createplanetapp.pages

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.createplanetapp.AuthState
import com.example.createplanetapp.AuthViewModel
import com.example.createplanetapp.DBHelper
import com.example.createplanetapp.GlobalData
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
fun OrdersPage(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    val dbHelper = remember { DBHelper(context, null) }
    val authState = authViewModel.authState.observeAsState()

    var orderedItems by remember { mutableStateOf<List<ItemsViewModel>>(emptyList()) }

//    LaunchedEffect(authState.value) {
//        when(authState.value){
//            is AuthState.Authenticated -> get data from remote DB
//            is AuthState.Unauthenticated -> get data from build-in DB
//            else -> Unit
//        }
//    }

    orderedItems = dbHelper.getOrderedRecords(GlobalData.items)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp)
    ) {
        Text(
            text = "Забронированное",
            fontSize = 30.sp,
            fontFamily = kazimirSemibold,
            color = mainColor,
            modifier = Modifier.padding(top = 30.dp, bottom = 30.dp)
        )


        when {
            orderedItems.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Список заказов пока пуст",
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
                    items(orderedItems) { item ->
                        OrdersItem(
                            item = item,
                            onOrderedChanged = { orderedItems = dbHelper.getOrderedRecords(GlobalData.items) }
                        )
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersItem(
    item: ItemsViewModel,
    onOrderedChanged: () -> Unit
) {
    val navController = LocalNavController.current
    val context = LocalContext.current
    val dbHelper = remember { DBHelper(context, null) }

    val bottomSheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    //Шторка для отображения деталей заказа
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = bottomSheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Детали заказа",
                    fontFamily = kazimirBold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text("Здесь будет список деталей заказа")

                Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            dbHelper.setOrderedStatus(item.title, "false")
                            onOrderedChanged()
                            Toast.makeText(context, "Бронь снята!", Toast.LENGTH_LONG).show()
                        },
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .height(33.dp)
                            .width(150.dp)
                            .align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = blueColor,
                            disabledContentColor = mainColor
                        )
                    ) {
                        Text(
                            text = "Снять бронь",
                            fontSize = 14.sp,
                            fontFamily = kazimirRegular,
                            color = Color.White
                        )
                    }

            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Column (
            modifier = Modifier.clickable {
                navController.navigate("ItemMainPage/${item.title}")
            }
        ) {
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
                            .size(60.dp))
                }
            }
            Text(
                text = item.title,
                fontFamily = kazimirBold,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp))
            Text(
                text = item.description,
                fontFamily = kazimirRegular,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp))

            Row {
                Text(
                    text = "от ${item.excursions.values.first().values.first().values.first()} р.",
                    fontFamily = kazimirRegular,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 20.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Button(
                        onClick = { showBottomSheet = true }, // Показываем шторку
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .height(33.dp)
                            .width(150.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = blueColor,
                            disabledContentColor = mainColor
                        )
                    ) {
                        Text(
                            text = "Детали заказа",
                            fontSize = 14.sp,
                            fontFamily = kazimirRegular,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}