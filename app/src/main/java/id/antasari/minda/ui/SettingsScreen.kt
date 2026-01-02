package id.antasari.minda.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    userName: String?,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    onResetOnboarding: () -> Unit
) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var showResetDialog by remember { mutableStateOf(false) }

    // Dialog Konfirmasi Reset
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset Application") },
            text = { Text("Are you sure you want to reset the onboarding status? This will take you back to the Welcome screen.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showResetDialog = false
                        onResetOnboarding()
                    }
                ) {
                    Text("Yes, Reset", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding).fillMaxSize()
        ) {
            // --- SECTION 1: PROFILE HEADER ---
            item {
                ProfileHeaderSection(name = userName ?: "Guest")
                Divider(thickness = 8.dp, color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            }

            // --- SECTION 2: PREFERENCES ---
            item {
                SettingsSectionTitle("Preferences")

                // Dark Mode Toggle (Visual Only for now)
                SettingsSwitchItem(
                    icon = Icons.Outlined.Palette,
                    title = "Dark Mode",
                    subtitle = "Adjust appearance",
                    checked = isDarkMode,
                    onCheckedChange = { onToggleDarkMode(it) }
                )

                // Notifications Toggle (Visual Only)
                SettingsSwitchItem(
                    icon = Icons.Outlined.Notifications,
                    title = "Daily Reminder",
                    subtitle = "Remind me to write at 8 PM",
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it }
                )
            }

            item { Divider() }

            // --- SECTION 3: DATA & PRIVACY ---
            item {
                SettingsSectionTitle("Data & Privacy")

                SettingsClickableItem(
                    icon = Icons.Outlined.RestartAlt,
                    title = "Reset Onboarding",
                    subtitle = "Show welcome screens again",
                    onClick = { showResetDialog = true },
                    textColor = MaterialTheme.colorScheme.primary
                )
            }

            item { Divider() }

            // --- SECTION 4: ABOUT ---
            item {
                SettingsSectionTitle("About")

                SettingsClickableItem(
                    icon = Icons.Outlined.Info,
                    title = "Version",
                    subtitle = "1.0.0 (Build 1)",
                    onClick = { /* No action */ }
                )

                Box(modifier = Modifier.padding(24.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Made with ❤️ for Mobile Programming",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// --- COMPONENTS HELPER ---

@Composable
fun ProfileHeaderSection(name: String) {
    val initial = name.firstOrNull()?.toString()?.uppercase() ?: "M"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar Lingkaran dengan Inisial
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initial,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Keep writing your story",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
    )
}

@Composable
fun SettingsSwitchItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    ListItem(
        headlineContent = { Text(title, fontWeight = FontWeight.Medium) },
        supportingContent = { Text(subtitle) },
        leadingContent = {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        },
        trailingContent = {
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    )
}

@Composable
fun SettingsClickableItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit,
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    ListItem(
        modifier = Modifier.clickable { onClick() },
        headlineContent = { Text(title, fontWeight = FontWeight.Medium, color = textColor) },
        supportingContent = if (subtitle != null) { { Text(subtitle) } } else null,
        leadingContent = {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    )
}