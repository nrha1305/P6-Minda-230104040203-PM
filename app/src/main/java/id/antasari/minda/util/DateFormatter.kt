package id.antasari.minda.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatTimestamp(ts: Long): String {
    val sdf = SimpleDateFormat("dd MMM, yyyy, hh:mm a", Locale.getDefault())
    return sdf.format(Date(ts))
}