package id.antasari.minda.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import id.antasari.minda.ui.*
import id.antasari.minda.ui.calendar.CalendarScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    onSaveUserName: (String) -> Unit,
    onOnboardingComplete: (Boolean) -> Unit,

    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,

    modifier: Modifier = Modifier,
    userName: String?
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Onboarding Flow
        composable(Routes.ONBOARD_WELCOME) {
            WelcomeScreen(onNext = { navController.navigate(Routes.ONBOARD_ASKNAME) })
        }
        composable(Routes.ONBOARD_ASKNAME) {
            AskNameScreen(onNameSubmit = { name ->
                onSaveUserName(name)
                navController.navigate(Routes.ONBOARD_HELLO)
            })
        }
        composable(Routes.ONBOARD_HELLO) {
            HelloScreen(name = userName ?: "", onNext = { navController.navigate(Routes.ONBOARD_CTA) })
        }
        composable(Routes.ONBOARD_CTA) {
            StartJournalingScreen(onStart = {
                onOnboardingComplete(true)
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.ONBOARD_WELCOME) { inclusive = true }
                }
            })
        }

        // Main Tabs
        composable(Routes.HOME) {
            HomeScreen(userName = userName, onOpenEntry = { id -> navController.navigate("detail/$id") })
        }
        composable(Routes.CALENDAR) {
            CalendarScreen(onEdit = { id -> navController.navigate("edit/$id") })
        }
        composable(Routes.INSIGHTS) { InsightsScreen() }
        composable(Routes.SETTINGS) {
            SettingsScreen(
                userName = userName,
                isDarkMode = isDarkMode, // Teruskan nilai
                onToggleDarkMode = onToggleDarkMode, // Teruskan fungsi
                onResetOnboarding = {
                    onOnboardingComplete(false)
                    navController.navigate(Routes.ONBOARD_WELCOME) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // CRUD Flow
        composable(Routes.NEW_ENTRY) {
            NewEntryScreen(
                onBack = { navController.popBackStack() },
                onSaved = { id ->
                    navController.popBackStack()
                    navController.navigate("detail/$id")
                }
            )
        }
        composable(
            Routes.DETAIL,
            arguments = listOf(navArgument("entryId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("entryId") ?: -1
            NoteDetailScreen(
                entryId = id,
                onBack = { navController.popBackStack() },
                onEdit = { eid -> navController.navigate("edit/$eid") },
                onDeleted = { navController.popBackStack() }
            )
        }
        composable(
            Routes.EDIT,
            arguments = listOf(navArgument("entryId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("entryId") ?: -1
            EditEntryScreen(
                entryId = id,
                onBack = { navController.popBackStack() },
                onSaved = { savedId ->
                    navController.popBackStack()
                    navController.navigate("detail/$savedId") {
                        popUpTo("detail/$savedId") { inclusive = true }
                    }
                }
            )
        }
    }
}