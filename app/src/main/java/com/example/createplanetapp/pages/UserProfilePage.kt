package com.example.createplanetapp.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.createplanetapp.AuthState
import com.example.createplanetapp.AuthViewModel
import com.example.createplanetapp.R
import com.example.createplanetapp.kazimirBold
import com.example.createplanetapp.kazimirRegular
import com.example.createplanetapp.ui.theme.blueColor
import com.example.createplanetapp.ui.theme.lightBlueColor
import com.example.createplanetapp.ui.theme.transparentBlack
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.database.FirebaseDatabase

@Composable
fun UserProfilePage(navController: NavController, authViewModel: AuthViewModel) {
    val authState = authViewModel.authState.observeAsState()
    val isEmailVerified by authViewModel.emailVerified.observeAsState()

    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var newEmail by remember { mutableStateOf("") }
    var newName by remember { mutableStateOf("") }
    var newSurname by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    FirebaseDatabase.getInstance().getReference("users")
                        .child(userId)
                        .get()
                        .addOnSuccessListener { snapshot ->
                            name = snapshot.child("name").value?.toString() ?: ""
                            surname = snapshot.child("surname").value?.toString() ?: ""
                            email = snapshot.child("email").value?.toString() ?: ""
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            is AuthState.Unauthenticated -> {
                navController.navigate("login")
            }
            else -> Unit
        }
    }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 0.dp,
                bottomStart = 24.dp,
                bottomEnd = 24.dp,
            ),
            colors = CardDefaults.cardColors(containerColor = blueColor),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(R.drawable.profile_menu_icon),
                        contentDescription = "User",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        tint = lightBlueColor
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (name.isNotEmpty() && surname.isNotEmpty()) {
                        Text(
                            text = "$name $surname",
                            fontSize = 20.sp,
                            color = Color.Black,
                            fontFamily = kazimirRegular
                        )
                    }
                    else {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
            }
        }

        if (authState.value is AuthState.Authenticated && isEmailVerified == false) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = transparentBlack)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Подтвердите email ($email)",
                        fontFamily = kazimirBold
                    )
                    Text(
                        text = "Мы отправили письмо на ваш email. Перейдите по ссылке, чтобы подтвердить аккаунт.",
                        fontFamily = kazimirRegular,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Button(
                        onClick = {
                            authViewModel.sendEmailVerification()
                            Toast.makeText(context, "Письмо отправлено", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.padding(top = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = blueColor)
                    ) {
                        Text(text = "Отправить повторно", fontFamily = kazimirBold, fontSize = 15.sp)
                    }
                    TextButton(
                        onClick = { authViewModel.refreshEmailVerificationStatus() }
                    ) {
                        Text(text = "Проверить подтверждение", fontFamily = kazimirRegular, fontSize = 15.sp, color = Color.Black, textDecoration = TextDecoration.Underline)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text("Настройки профиля", fontSize = 23.sp, fontFamily = kazimirBold)

            Spacer(modifier = Modifier.height(16.dp))

            Text("Ваше имя и фамилия", fontSize = 20.sp, fontFamily = kazimirBold)
            Text("Здесь вы можете поменять эти данные", fontSize = 18.sp, fontFamily = kazimirRegular)

            LabeledTextField(
                value = newName,
                onValueChange = { newName = it },
                labelText = "Введите новое имя",
            )
            if (newName.isNotBlank() && newName != name) {
                Button(
                    onClick = {
                        val userId = FirebaseAuth.getInstance().currentUser?.uid
                        if (userId != null) {
                            FirebaseDatabase.getInstance().getReference("users")
                                .child(userId)
                                .child("name")
                                .setValue(newName)
                                .addOnSuccessListener {
                                    name = newName
                                    newName = ""
                                    Toast.makeText(context, "Имя обновлено", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Ошибка при обновлении имени", Toast.LENGTH_SHORT).show()
                                }
                        }
                    },
                    modifier = Modifier.padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = blueColor)
                ) {
                    Text("Сменить", fontFamily = kazimirBold, fontSize = 15.sp)
                }
            }

            LabeledTextField(
                value = newSurname,
                onValueChange = { newSurname = it },
                labelText = "Введите новую фамилию",
            )
            if (newSurname.isNotBlank() && newSurname != surname) {
                Button(
                    onClick = {
                        val userId = FirebaseAuth.getInstance().currentUser?.uid
                        if (userId != null) {
                            FirebaseDatabase.getInstance().getReference("users")
                                .child(userId)
                                .child("surname")
                                .setValue(newSurname)
                                .addOnSuccessListener {
                                    surname = newSurname
                                    newSurname = ""
                                    Toast.makeText(context, "Фамилия обновлена", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Ошибка при обновлении фамилии", Toast.LENGTH_SHORT).show()
                                }
                        }
                    },
                    modifier = Modifier.padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = blueColor)
                ) {
                    Text("Сменить", fontFamily = kazimirBold, fontSize = 15.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Ваша почта", fontSize = 20.sp, fontFamily = kazimirBold)
            Text("Здесь вы можете сменить адрес почты", fontSize = 18.sp, fontFamily = kazimirRegular)
            LabeledTextField(
                value = newEmail,
                onValueChange = { newEmail = it },
                labelText = "Введите новую почту",
            )

            if (newEmail.isNotBlank() && newEmail != email) {
                Button(
                    onClick = {
                        val user = FirebaseAuth.getInstance().currentUser
                        val userId = user?.uid

                        if (user != null && userId != null) {
                            user.verifyBeforeUpdateEmail(newEmail)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Письмо отправлено на новый email. Подтвердите, чтобы изменить.", Toast.LENGTH_LONG).show()
                                }
                                .addOnFailureListener { e ->
                                    if (e is FirebaseAuthRecentLoginRequiredException) {
                                        Toast.makeText(context, "Требуется повторная авторизация для смены email", Toast.LENGTH_LONG).show()
                                    } else {
                                        Toast.makeText(context, "Ошибка при обновлении email: ${e.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                        }

                    },
                    modifier = Modifier.padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = blueColor)
                ) {
                    Text("Сменить", fontFamily = kazimirBold, fontSize = 15.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Ваш пароль", fontSize = 20.sp, fontFamily = kazimirBold)
            Text("Для смены пароля необходимо подтверждение по почте", fontSize = 18.sp, fontFamily = kazimirRegular)
            LabeledTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                labelText = "Введите новый пароль",
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        authViewModel.logout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = blueColor)
                ) {
                    Text(text = "Выйти", fontFamily = kazimirBold, fontSize = 20.sp)
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Text("Контакты", fontWeight = FontWeight.Bold, fontSize = 25.sp, fontFamily = kazimirBold)

            Spacer(modifier = Modifier.height(4.dp))

            HorizontalDivider(modifier = Modifier.width(100.dp), thickness = 1.dp, color = Color.Black)

            Spacer(modifier = Modifier.height(20.dp))

            Text("Санкт-Петербург", fontFamily = kazimirRegular, fontSize = 16.sp)
            Text("Телефон: +7 (953) 173 30-51", fontFamily = kazimirRegular, fontSize = 16.sp)
            Text("Email: leonid@createplanet.org", fontFamily = kazimirRegular, fontSize = 16.sp)

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_phone),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Icon(
                    modifier = Modifier
                        .size(30.dp),
                    painter = painterResource(R.drawable.ic_wa),
                    contentDescription = null,
                )
                Icon(
                    modifier = Modifier
                        .size(30.dp),
                    painter = painterResource(R.drawable.ic_telegram),
                    contentDescription = null,
                )
                Icon(
                    modifier = Modifier
                        .size(30.dp),
                    painter = painterResource(R.drawable.ic_email),
                    contentDescription = null,
                )
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}