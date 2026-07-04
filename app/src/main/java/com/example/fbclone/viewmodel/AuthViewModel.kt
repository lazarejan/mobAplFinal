package com.example.fbclone.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.fbclone.model.MockDatabase
import com.example.fbclone.model.User

class AuthViewModel : ViewModel() {
    var currentUser = mutableStateOf<User?>(null)
    var authError = mutableStateOf("")

    fun login(email: String, pass: String, onSuccess: () -> Unit) {
        val user = MockDatabase.users.find { it.email == email && it.pass == pass }
        if (user != null) {
            currentUser.value = user
            authError.value = ""
            onSuccess()
        } else {
            authError.value = "Invalid email or password"
        }
    }

    fun register(email: String, pass: String, onSuccess: () -> Unit) {
        if (email.isEmpty() || pass.isEmpty()) {
            authError.value = "Fields cannot be empty"
            return
        }
        if (MockDatabase.users.any { it.email == email }) {
            authError.value = "User already exists"
            return
        }
        val newUser = User(email, pass)
        MockDatabase.users.add(newUser)
        currentUser.value = newUser
        authError.value = ""
        onSuccess()
    }

    fun logout(onSuccess: () -> Unit) {
        currentUser.value = null
        onSuccess()
    }
}