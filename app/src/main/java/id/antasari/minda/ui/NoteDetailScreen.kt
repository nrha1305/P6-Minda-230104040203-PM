package id.antasari.minda.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import id.antasari.minda.data.DiaryEntry
import id.antasari.minda.data.DiaryRepository
import id.antasari.minda.data.MindaDatabase
import id.antasari.minda.util.formatTimestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(entryId: Int, onBack: () -> Unit, onEdit: (Int) -> Unit, onDeleted: () -> Unit) {
    val context = LocalContext.current
    val db = remember { MindaDatabase.getInstance(context) }
    val repo = remember { DiaryRepository(db.diaryDao()) }
    val scope = rememberCoroutineScope()
    var entry by remember { mutableStateOf<DiaryEntry?>(null) }

    LaunchedEffect(entryId) {
        entry = withContext(Dispatchers.IO) { repo.getById(entryId) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") } },
                actions = {
                    IconButton(onClick = { onEdit(entryId) }) { Icon(Icons.Default.Edit, "Edit") }
                    IconButton(onClick = {
                        scope.launch {
                            entry?.let {
                                withContext(Dispatchers.IO) { repo.remove(it) }
                                onDeleted()
                            }
                        }
                    }) { Icon(Icons.Default.Delete, "Delete") }
                }
            )
        }
    ) { padding ->
        entry?.let { e ->
            Column(
                modifier = Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState())
            ) {
                Text(formatTimestamp(e.timestamp), style = MaterialTheme.typography.labelMedium)
                Spacer(Modifier.height(8.dp))
                Text("${e.mood} ${e.title}", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))
                Text(e.content, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}