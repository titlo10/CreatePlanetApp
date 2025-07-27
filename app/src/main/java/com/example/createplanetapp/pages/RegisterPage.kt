package com.example.createplanetapp.pages

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.createplanetapp.AuthState
import com.example.createplanetapp.AuthViewModel
import com.example.createplanetapp.kazimirBold
import com.example.createplanetapp.kazimirRegular
import com.example.createplanetapp.ui.theme.*

@Composable
fun RegisterPage(navController: NavController, authViewModel: AuthViewModel) {
    var name by remember { mutableStateOf("")}
    var surname by remember {mutableStateOf("")}
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val context = LocalContext.current

    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Authenticated -> navController.navigate("userProfile")
            is AuthState.Error -> Toast.makeText(context, (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Регистрация",
                fontSize = 36.sp,
                fontFamily = kazimirBold
            )

            Spacer(modifier = Modifier.height(24.dp))

            LabeledTextField(
                value = name,
                onValueChange = { name = it },
                labelText = "Введите ваше имя"
            )

            Spacer(modifier = Modifier.height(12.dp))

            LabeledTextField(
                value = surname,
                onValueChange = { surname = it },
                labelText = "Введите вашу фамилию"
            )

            Spacer(modifier = Modifier.height(12.dp))

            LabeledTextField(
                value = email,
                onValueChange = { email = it },
                labelText = "Введите адрес вашей почты"
            )

            Spacer(modifier = Modifier.height(12.dp))

            LabeledTextField(
                value = password,
                onValueChange = { password = it },
                labelText = "Введите пароль",
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(12.dp))

            LabeledTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                labelText = "Повторите пароль",
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (password != confirmPassword) {
                        Toast.makeText(context, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    authViewModel.register(name, surname, email, password)
                },
                enabled = authState.value != AuthState.Loading,
                colors = ButtonDefaults.buttonColors(containerColor = blueColor)
            ) {
                Text(text = "Регистрация", fontFamily = kazimirBold, fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Уже есть профиль?",
                    fontSize = 15.sp,
                    fontFamily = kazimirBold
                )
                Row {
                    Text(
                        text = "Тогда нажмите ",
                        fontSize = 15.sp,
                        fontFamily = kazimirBold
                    )
                    Text(
                        text = "Войти",
                        color = blueColor,
                        fontSize = 15.sp,
                        fontFamily = kazimirBold,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { navController.navigate("login") }
                    )
                }
            }
        }
    }
}


@Composable
fun LabeledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                labelText,
                fontFamily = kazimirRegular,
                fontSize = 20.sp
            )
        },
        singleLine = true,
        visualTransformation = visualTransformation,
        shape = RoundedCornerShape(25.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = blueColor,
            focusedLabelColor = blueColor,
            cursorColor = blueColor,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        )
    )
}