package id.antasari.minda.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import id.antasari.minda.data.DiaryEntry
import id.antasari.minda.data.DiaryRepository
import id.antasari.minda.data.MindaDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * INSIGHTS SCREEN (Screen 7)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreen() {
    val context = LocalContext.current
    val db = remember { MindaDatabase.getInstance(context) }
    val repo = remember { DiaryRepository(db.diaryDao()) }
    var entries by remember { mutableStateOf<List<DiaryEntry>>(emptyList()) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            entries = repo.allEntries()
        }
    }

    val totalEntries = entries.size
    val nowMillis = System.currentTimeMillis()
    val weekAgoMillis = nowMillis - (7L * 24 * 60 * 60 * 1000)
    val last7Count = entries.count { it.timestamp > weekAgoMillis }

    // Hitung Mood -> Jumlah
    val moodCounts = remember(entries) {
        entries.groupingBy { it.mood }.eachCount()
    }
    val maxCount = moodCounts.values.maxOrNull() ?: 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Insights",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "This journal",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "$totalEntries entries total",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "$last7Count in the last 7 days",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Mood Overview
            Text(
                text = "Mood overview",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            if (moodCounts.isEmpty()) {
                Text(
                    text = "No mood data yet. Start journaling to see your patterns.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    moodCounts.entries
                        .sortedByDescending { it.value }
                        .forEach { (mood, count) ->
                            val fraction = if (maxCount > 0) count.toFloat() / maxCount.toFloat() else 0f

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Emoji / Label Mood
                                Text(
                                    text = if(mood.isBlank()) "❓" else mood,
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.widthIn(min = 32.dp)
                                )
                                Spacer(Modifier.width(8.dp))

                                // Bar Chart
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(10.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            shape = RoundedCornerShape(50)
                                        )
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .fillMaxWidth(fraction)
                                            .background(
                                                color = MaterialTheme.colorScheme.primary,
                                                shape = RoundedCornerShape(50)
                                            )
                                    )
                                }

                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = count.toString(),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                }
            }
        }
    }
}