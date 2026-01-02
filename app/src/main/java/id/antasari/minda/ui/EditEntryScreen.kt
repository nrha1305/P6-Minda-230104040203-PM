package id.antasari.minda.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.antasari.minda.data.DiaryEntry
import id.antasari.minda.data.DiaryRepository
import id.antasari.minda.data.MindaDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val MOODS = listOf("😀", "😢", "😡", "😴", "🤩", "🤯")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEntryScreen(entryId: Int, onBack: () -> Unit, onSaved: (Int) -> Unit) {
    val context = LocalContext.current
    val db = remember { MindaDatabase.getInstance(context) }
    val repo = remember { DiaryRepository(db.diaryDao()) }
    val scope = rememberCoroutineScope()

    var entry by remember { mutableStateOf<DiaryEntry?>(null) }
    var titleText by remember { mutableStateOf("") }
    var contentText by remember { mutableStateOf("") }
    var selectedMood by remember { mutableStateOf("😀") }

    LaunchedEffect(entryId) {
        entry = withContext(Dispatchers.IO) { repo.getById(entryId) }
        entry?.let {
            titleText = it.title
            contentText = it.content
            selectedMood = it.mood.ifBlank { "😀" }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Entry", fontWeight = FontWeight.SemiBold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") } }
            )
        }
    ) { padding ->
        if (entry != null) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // MOOD PICKER
                    Text("Mood", style = MaterialTheme.typography.labelLarge)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MOODS.forEach { mood ->
                            val isSelected = mood == selectedMood
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                                    .clickable { selectedMood = mood },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = mood, fontSize = 24.sp)
                            }
                        }
                    }
                    Divider()

                    OutlinedTextField(
                        value = titleText, onValueChange = { titleText = it },
                        label = { Text("Title") }, modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = contentText, onValueChange = { contentText = it },
                        label = { Text("Content") },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 160.dp),
                        minLines = 5
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) { Text("Cancel") }
                    Button(
                        onClick = {
                            scope.launch {
                                entry?.let { current ->
                                    val updated = current.copy(
                                        title = titleText,
                                        content = contentText,
                                        mood = selectedMood
                                    )
                                    withContext(Dispatchers.IO) { repo.edit(updated) }
                                    onSaved(entryId)
                                }
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) { Text("Update") }
                }
            }
        }
    }
}