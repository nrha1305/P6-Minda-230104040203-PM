package id.antasari.minda.ui.calendar

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import id.antasari.minda.data.DiaryEntry
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen(onEdit: (Int) -> Unit) {
    val context = LocalContext.current
    val vm: CalendarViewModel = viewModel(factory = CalendarViewModel.provideFactory(context.applicationContext as Application))
    val diaryByDate by vm.diaryByDate.collectAsStateWithLifecycle()

    val today = remember { LocalDate.now() }
    var visibleMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf(today) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Header Bulan & Navigasi
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${visibleMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${visibleMonth.year}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Row {
                IconButton(onClick = { visibleMonth = visibleMonth.minusMonths(1) }) {
                    Text("<", fontSize = 24.sp)
                }
                IconButton(onClick = { visibleMonth = visibleMonth.plusMonths(1) }) {
                    Text(">", fontSize = 24.sp)
                }
            }
        }

        // Grid Kalender
        val daysInMonth = remember(visibleMonth) {
            val start = visibleMonth.atDay(1)
            val end = visibleMonth.atEndOfMonth()
            val days = mutableListOf<LocalDate?>()

            // Padding hari sebelum tanggal 1
            repeat(start.dayOfWeek.value % 7) { days.add(null) }

            var curr = start
            while (!curr.isAfter(end)) {
                days.add(curr)
                curr = curr.plusDays(1)
            }
            days
        }

        LazyVerticalGrid(columns = GridCells.Fixed(7)) {
            items(daysInMonth) { date ->
                if (date != null) {
                    val isSelected = date == selectedDate
                    val hasDiary = diaryByDate[date]?.isNotEmpty() == true

                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { selectedDate = date },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = date.dayOfMonth.toString(),
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                            )
                            if (hasDiary) {
                                Spacer(modifier = Modifier.height(2.dp))
                                Box(
                                    modifier = Modifier
                                        .size(4.dp)
                                        .background(if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary, CircleShape)
                                )
                            }
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.padding(4.dp).aspectRatio(1f))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))

        // List Diary pada tanggal terpilih
        Text("Entries for ${selectedDate.format(DateTimeFormatter.ofPattern("d MMM yyyy"))}", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        val entries = diaryByDate[selectedDate] ?: emptyList()
        if (entries.isEmpty()) {
            Text("No entries yet.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
            LazyColumn {
                items(entries) { entry ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { onEdit(entry.id) },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("${entry.mood} ${entry.title}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(entry.content, maxLines = 2, overflow = TextOverflow.Ellipsis)
                        }
                    }
                }
            }
        }
    }
}