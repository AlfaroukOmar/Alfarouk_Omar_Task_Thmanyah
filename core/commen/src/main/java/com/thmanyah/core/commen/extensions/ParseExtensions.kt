package com.thmanyah.core.commen.extensions

import kotlinx.datetime.LocalDate


fun String?.orEmptyTrimmed(): String = this?.trim().orEmpty()

fun String?.toIntSafe(): Int? {
    val s = this?.trim()?.takeIf { it.isNotEmpty() } ?: return null
    return s.toIntOrNull()
}

fun String?.toLongSafe(): Long? {
    val s = this?.trim()?.takeIf { it.isNotEmpty() } ?: return null
    return s.toLongOrNull()
}

/**
 * Parses duration from API: seconds as number or string; caps sanity at 48h.
 */
fun Any?.toDurationSecondsSafe(): Long? {
    return when (this) {
        null -> null
        is Long -> this.takeIf { it in 0..172800L }
        is Int -> this.toLong().takeIf { it in 0..172800L }
        is Double -> this.toLong().takeIf { it in 0..172800L }
        is Float -> this.toLong().takeIf { it in 0..172800L }
        is String -> this.toLongSafe()?.takeIf { it in 0..172800L }
        else -> null
    }
}

fun String?.parseReleaseDate(): LocalDate? {
    val raw = this?.trim()?.takeIf { it.isNotEmpty() } ?: return null
    return runCatching {
        when {
            raw.contains('T') -> LocalDate.parse(raw.substringBefore('T'))
            else -> LocalDate.parse(raw.take(10))
        }
    }.getOrNull()
}

fun String?.isValidHttpUrl(): Boolean {
    val s = this ?: return false
    return s.startsWith("http://", ignoreCase = true) || s.startsWith("https://", ignoreCase = true)
}
