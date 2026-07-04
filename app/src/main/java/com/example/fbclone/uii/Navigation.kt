package com.example.fbclone.uii


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.example.fbclone.viewmodel.AuthViewModel
import com.example.fbclone.viewmodel.PostViewModel
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AppNavGraph(authVM: AuthViewModel, postVM: PostViewModel) {
    val navController = rememberNavController()
    val startDest = if (authVM.currentUser.value != null) "main" else "login"

    NavHost(navController = navController, startDestination = startDest) {
        composable("login") { LoginScreen(authVM, onAuthSuccess = { navController.navigate("main") { popUpTo("login") { inclusive = true } } }, onGoToRegister = { navController.navigate("register") }) }
        composable("register") { RegisterScreen(authVM, onAuthSuccess = { navController.navigate("main") { popUpTo("register") { inclusive = true } } }) }
        composable("main") { MainScreen(authVM, postVM) { navController.navigate("login") { popUpTo("main") { inclusive = true } } } }
    }
}

@Composable
fun MainScreen(authVM: AuthViewModel, postVM: PostViewModel, onLogout: () -> Unit) {
    val innerNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by innerNavController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                NavigationBarItem(
                    selected = currentRoute == "feed",
                    onClick = { innerNavController.navigate("feed") },
                    icon = { Icon(Icons.Default.Home, "Feed") },
                    label = { Text("Feed") }
                )
                NavigationBarItem(
                    selected = currentRoute == "create",
                    onClick = { innerNavController.navigate("create") },
                    icon = { Icon(Icons.Default.Add, "Post") },
                    label = { Text("Post") }
                )
                NavigationBarItem(
                    selected = currentRoute == "profile",
                    onClick = { innerNavController.navigate("profile") },
                    icon = { Icon(Icons.Default.Person, "Profile") },
                    label = { Text("Profile") }
                )
            }
        }
    ) { padding ->
        NavHost(innerNavController, startDestination = "feed", modifier = Modifier.padding(padding)) {
            composable("feed") { FeedScreen(postVM, authVM.currentUser.value?.email ?: "") }
            composable("create") { CreatePostScreen(postVM, authVM.currentUser.value?.email ?: "") { innerNavController.navigate("feed") } }
            composable("profile") { ProfileScreen(authVM, onLogout) }
        }
    }
}


@Composable
fun LoginScreen(vm: AuthViewModel, onAuthSuccess: () -> Unit, onGoToRegister: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().padding(32.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Facebook Local", style = MaterialTheme.typography.headlineLarge, color = Color(0xFF1877F2))
        Spacer(Modifier.height(16.dp))
        TextField(email, { email = it }, placeholder = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        TextField(pass, { pass = it }, placeholder = { Text("Password") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(16.dp))
        Button({ vm.login(email, pass, onAuthSuccess) }, Modifier.fillMaxWidth()) { Text("Log In") }
        TextButton(onGoToRegister) { Text("Don't have an account? Register") }
        if (vm.authError.value.isNotEmpty()) Text(vm.authError.value, color = Color.Red)
    }
}

@Composable
fun RegisterScreen(vm: AuthViewModel, onAuthSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().padding(32.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Create Account", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(16.dp))
        TextField(email, { email = it }, placeholder = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        TextField(pass, { pass = it }, placeholder = { Text("Password") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(16.dp))
        Button({ vm.register(email, pass, onAuthSuccess) }, Modifier.fillMaxWidth()) { Text("Register") }
        if (vm.authError.value.isNotEmpty()) Text(vm.authError.value, color = Color.Red)
    }
}

@Composable
fun FeedScreen(vm: PostViewModel, currentUserEmail: String) {
    LazyColumn(Modifier.fillMaxSize().padding(8.dp)) {
        items(vm.posts) { post ->
            Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Text(post.authorEmail, style = MaterialTheme.typography.labelLarge, color = Color.Gray)
                    Spacer(Modifier.height(4.dp))
                    Text(post.content, style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton({ vm.toggleLike(post, currentUserEmail) }) {
                            val isLiked = post.likedBy.contains(currentUserEmail)
                            Icon(Icons.Default.ThumbUp, "Like", tint = if (isLiked) Color(0xFF1877F2) else Color.Gray)
                        }
                        Text("${post.likedBy.size} Likes")
                    }
                }
            }
        }
    }
}

@Composable
fun CreatePostScreen(vm: PostViewModel, currentUserEmail: String, onPostSuccess: () -> Unit) {
    var text by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        TextField(text, { text = it }, placeholder = { Text("What's on your mind?") }, modifier = Modifier.fillMaxWidth().weight(1f))
        Spacer(Modifier.height(16.dp))
        Button({ if (text.isNotEmpty()) vm.createPost(text, currentUserEmail, onPostSuccess) }, Modifier.fillMaxWidth()) { Text("Post") }
    }
}

@Composable
fun ProfileScreen(vm: AuthViewModel, onLogout: () -> Unit) {
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Logged in as: ${vm.currentUser.value?.email}")
        Spacer(Modifier.height(16.dp))
        Button({ vm.logout(onLogout) }) { Text("Log Out") }
    }
}






@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FullAppFeedPreview() {
    val previewAuthVM = AuthViewModel().apply {
        currentUser.value = com.example.fbclone.model.User("lazare@example.com", "password123")
    }
    val previewPostVM = PostViewModel()

    MaterialTheme {
        MainScreen(
            authVM = previewAuthVM,
            postVM = previewPostVM,
            onLogout = {}
        )
    }
}

@Preview(name = "Login Page", showBackground = true, showSystemUi = true)
@Composable
fun PreviewLogin() {
    MaterialTheme {
        LoginScreen(
            vm = AuthViewModel(),
            onAuthSuccess = {},
            onGoToRegister = {}
        )
    }
}

@Preview(name = "Register Page", showBackground = true, showSystemUi = true)
@Composable
fun PreviewRegister() {
    MaterialTheme {
        RegisterScreen(
            vm = AuthViewModel(),
            onAuthSuccess = {}
        )
    }
}


@Preview(name = "Create Post Page", showBackground = true, showSystemUi = true)
@Composable
fun PreviewCreatePost() {
    MaterialTheme {
        CreatePostScreen(
            vm = PostViewModel(),
            currentUserEmail = "lazare@example.com",
            onPostSuccess = {}
        )
    }
}

@Preview(name = "Profile Page", showBackground = true, showSystemUi = true)
@Composable
fun PreviewProfile() {
    val mockAuthVM = AuthViewModel().apply {
        currentUser.value = com.example.fbclone.model.User("lazare@example.com", "1234")
    }

    MaterialTheme {
        ProfileScreen(
            vm = mockAuthVM,
            onLogout = {}
        )
    }
}