package com.example.myfintech.utils

import android.util.Log

object ReceiptParser {

    fun extractTotalAmount(text: String): Int? {
        Log.d("ReceiptParser", "Raw OCR text:\n$text")

        val lines = text.split("\n").map { it.trim() }

        val totalKeywords = listOf(
            "grand total",
            "total pembayaran",
            "total bayar",
            "total harga",
            "total belanja",
            "total amount",
            "total price",
            "grand ttl",
            "jumlah total",
            "total",
            "ttl",
            "amount due",
            "balance due",
            "subtotal",
            "nilai bayar",
            "nilai total"
        )

        for (keyword in totalKeywords) {
            for (line in lines) {
                val lowerLine = line.lowercase()
                if (lowerLine.contains(keyword)) {
                    val amount = extractAmountFromLine(line)
                    if (amount != null && amount > 0) {
                        Log.d("ReceiptParser", "Found total with keyword '$keyword': $amount from line: $line")
                        return amount
                    }
                }
            }
        }

        val totalPatterns = listOf(
            Regex("""(?:total|ttl|grand\s*total|subtotal)\s*:?\s*(?:rp\.?\s*)?([0-9]{1,3}(?:[.,][0-9]{3})*(?:[.,][0-9]{2})?)""", RegexOption.IGNORE_CASE),
            Regex("""(?:rp\.?\s*)?([0-9]{1,3}(?:[.,][0-9]{3})*(?:[.,][0-9]{2})?)\s*(?:total|ttl)""", RegexOption.IGNORE_CASE)
        )

        for (pattern in totalPatterns) {
            val match = pattern.find(text)
            if (match != null) {
                val amountStr = match.groupValues[1]
                val amount = parseAmount(amountStr)
                if (amount != null && amount > 0) {
                    Log.d("ReceiptParser", "Found total with pattern: $amount from match: ${match.value}")
                    return amount
                }
            }
        }

        val allAmounts = mutableListOf<Pair<Int, Int>>()
        lines.forEachIndexed { index, line ->
            val amount = extractAmountFromLine(line)
            if (amount != null && amount > 0) {
                allAmounts.add(Pair(amount, index))
            }
        }

        if (allAmounts.isNotEmpty()) {
            val bottomHalfThreshold = lines.size / 2
            val bottomAmounts = allAmounts.filter { it.second >= bottomHalfThreshold }

            val result = if (bottomAmounts.isNotEmpty()) {
                bottomAmounts.maxByOrNull { it.first }?.first
            } else {
                allAmounts.maxByOrNull { it.first }?.first
            }

            Log.d("ReceiptParser", "No keyword found, using largest amount from bottom: $result")
            return result
        }

        Log.d("ReceiptParser", "No amount detected")
        return null
    }

    private fun extractAmountFromLine(line: String): Int? {
        var cleanLine = line.replace(Regex("""[Rp$€£¥]""", RegexOption.IGNORE_CASE), "")
        cleanLine = cleanLine.replace(Regex("""\b(?:rupiah|idr)\b""", RegexOption.IGNORE_CASE), "")

        val patterns = listOf(
            // Indonesian format: 1.234.567 or 1.234.567,00
            Regex("""([0-9]{1,3}(?:\.[0-9]{3})+)(?:,[0-9]{2})?"""),
            // US format: 1,234,567 or 1,234,567.00
            Regex("""([0-9]{1,3}(?:,[0-9]{3})+)(?:\.[0-9]{2})?"""),
            // Plain number: 123456
            Regex("""([0-9]{4,})""")
        )

        for (pattern in patterns) {
            val match = pattern.find(cleanLine)
            if (match != null) {
                val amountStr = match.groupValues[1]
                val amount = parseAmount(amountStr)
                if (amount != null) {
                    return amount
                }
            }
        }

        return null
    }

    private fun parseAmount(amountStr: String): Int? {
        return try {
            val cleaned = when {
                amountStr.contains('.') && amountStr.indexOf('.') < amountStr.length - 3 -> {
                    amountStr.replace(".", "").replace(",", ".")
                }
                amountStr.contains(',') && amountStr.indexOf(',') < amountStr.length - 3 -> {
                    amountStr.replace(",", "")
                }
                else -> amountStr.replace(Regex("""[,.]"""), "")
            }

            val parsed = cleaned.toDoubleOrNull()?.toInt()
            parsed
        } catch (e: Exception) {
            Log.e("ReceiptParser", "Error parsing amount: $amountStr", e)
            null
        }
    }
}

