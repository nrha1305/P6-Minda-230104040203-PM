package id.antasari.minda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import id.antasari.minda.data.UserPrefsRepository
import id.antasari.minda.ui.BottomNavBar
import id.antasari.minda.ui.navigation.AppNavHost
import id.antasari.minda.ui.navigation.Routes
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = this
            val userPrefs = remember { UserPrefsRepository(context) }
            val scope = rememberCoroutineScope()

            // 1. AMBIL DATA DARI DATASTORE
            val userName by userPrefs.userNameFlow.collectAsState(initial = null)
            val onboardingCompleted by userPrefs.onboardingCompletedFlow.collectAsState(initial = null)

            // Ambil preferensi Dark Mode (Default ikut sistem HP jika null)
            val systemDark = isSystemInDarkTheme()
            val storedDarkMode by userPrefs.isDarkModeFlow.collectAsState(initial = null)
            val isDarkTheme = storedDarkMode ?: systemDark

            // 2. BUNGKUS APLIKASI DENGAN TEMA YANG DIPILIH
            // Jika Anda punya custom theme (MindaTheme), gunakan itu.
            // Jika pakai default MaterialTheme, atur colorScheme-nya:
            MaterialTheme(
                colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
            ) {
                // Tunggu data loading
                if (onboardingCompleted == null) {
                    Surface { Text("Loading...") }
                } else {
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    val startDest = if (onboardingCompleted == true) Routes.HOME else Routes.ONBOARD_WELCOME
                    val showBottomBar = currentRoute in listOf(Routes.HOME, Routes.CALENDAR, Routes.INSIGHTS, Routes.SETTINGS)

                    Scaffold(
                        bottomBar = {
                            if (showBottomBar) BottomNavBar(navController)
                        },
                        floatingActionButton = {
                            if (showBottomBar) {
                                FloatingActionButton(onClick = { navController.navigate(Routes.NEW_ENTRY) }) {
                                    Icon(Icons.Default.Add, contentDescription = "Add")
                                }
                            }
                        }
                    ) { innerPadding ->
                        AppNavHost(
                            navController = navController,
                            startDestination = startDest,
                            onSaveUserName = { name -> scope.launch { userPrefs.saveUserName(name) } },
                            onOnboardingComplete = { completed ->
                                scope.launch { userPrefs.setOnboardingCompleted(completed) }
                            },

                            // 3. MASUKKAN LOGIKANYA KE SINI
                            isDarkMode = isDarkTheme,
                            onToggleDarkMode = { enable ->
                                scope.launch { userPrefs.setDarkMode(enable) }
                            },

                            modifier = Modifier.padding(innerPadding),
                            userName = userName
                        )
                    }
                }
            }
        }
    }
}