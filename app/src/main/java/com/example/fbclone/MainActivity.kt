package com.example.fbclone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.fbclone.uii.AppNavGraph
import com.example.fbclone.viewmodel.AuthViewModel
import com.example.fbclone.viewmodel.PostViewModel

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val postViewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavGraph(authVM = authViewModel, postVM = postViewModel)
        }
    }
}