package id.antasari.minda.data

import kotlinx.coroutines.flow.Flow

class DiaryRepository(private val dao: DiaryDao) {
    suspend fun add(entry: DiaryEntry): Long = dao.insert(entry)

    suspend fun edit(entry: DiaryEntry) = dao.update(entry)

    suspend fun remove(entry: DiaryEntry) = dao.delete(entry)

    suspend fun allEntries(): List<DiaryEntry> = dao.getAll()

    suspend fun getById(id: Int): DiaryEntry? = dao.getById(id)

    fun observeAll(): Flow<List<DiaryEntry>> = dao.observeAll()
}