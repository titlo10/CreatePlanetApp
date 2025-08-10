package com.example.createplanetapp

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val dbHelper : DBHelper = GlobalData.db

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    private val _emailVerified = MutableLiveData<Boolean>()
    val emailVerified: LiveData<Boolean> = _emailVerified

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            _authState.value = AuthState.Unauthenticated
            _emailVerified.value = false
        } else {
            _authState.value = AuthState.Authenticated
            _emailVerified.value = currentUser.isEmailVerified
        }
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Поля не могут быть пустыми")
            return
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    _authState.value = AuthState.Authenticated
                    migrateData()
                    _emailVerified.value = user?.isEmailVerified ?: false
                } else {
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "Что-то пошло не так")
                }
            }
    }

    fun register(name: String, surname: String, email: String, password: String) {
        if (email.isBlank() || password.isBlank() || name.isBlank() || surname.isBlank()) {
            _authState.value = AuthState.Error("Поля не могут быть пустыми")
            return
        }

        _authState.value = AuthState.Loading

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid

                    val userMap = mapOf(
                        "name" to name,
                        "surname" to surname,
                        "email" to email
                    )

                    FirebaseDatabase.getInstance().getReference("users")
                        .child(userId ?: "")
                        .setValue(userMap)
                        .addOnCompleteListener {
                            auth.currentUser?.sendEmailVerification()
                            _authState.value = AuthState.Authenticated
                            migrateData()
                            _emailVerified.value = false
                        }
                        .addOnFailureListener {
                            _authState.value =
                                AuthState.Error("Ошибка при сохранении данных: ${it.message}")
                        }
                } else {
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "Что-то пошло не так")
                }
            }
    }

    private fun migrateData() {
        val favoriteItems = dbHelper.getFavoriteRecords(GlobalData.items)
        val orderedItems = dbHelper.getOrderedRecords(GlobalData.items)
        val userId = auth.currentUser?.uid

        if (userId != null) {
            val favOrderedRef = FirebaseDatabase.getInstance()
                .getReference("users/$userId")
                .child("FAVORITES_AND_ORDERED")

            favoriteItems.forEach { excursion ->
                val updates = HashMap<String, Any>()
                updates["favorite"] = true
                if (orderedItems.contains(excursion)) {
                    updates["ordered"] = true
                } else {
                    updates["ordered"] = false
                }
                favOrderedRef.child(excursion.title).updateChildren(updates)
            }

            orderedItems.forEach { excursion ->
                if (!favoriteItems.contains(excursion)) {
                    val updates = HashMap<String, Any>()
                    updates["favorite"] = false
                    updates["ordered"] = true
                    favOrderedRef.child(excursion.title).updateChildren(updates)
                }
            }
        }
    }

    fun sendEmailVerification() {
        auth.currentUser?.sendEmailVerification()
    }

    fun refreshEmailVerificationStatus() {
        auth.currentUser?.reload()?.addOnCompleteListener {
            _emailVerified.value = auth.currentUser?.isEmailVerified ?: false
        }
    }

    fun logout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
        _emailVerified.value = false
    }
}

sealed class AuthState {
    data object Authenticated : AuthState()
    data object Unauthenticated : AuthState()
    data object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}
