package com.example.replyapp.data

data class Email(
    val id: Long,
    val sender: String,
    val subject: String,
    val body: String
)

object EmailsRepository {
    fun getEmails(): List<Email> {
        return listOf(
            Email(1, "Google", "New Security Alert", "A new device signed in..."),
            Email(2, "JetBrains", "License Certificate", "Your license for IntelliJ is..."),
            Email(3, "Amazon", "Your order has shipped!", "Your package will be delivered..."),
            Email(4, "LinkedIn", "New connection request", "Someone wants to connect..."),
            Email(5, "GitHub", "You have a new comment", "A user has commented on your PR...")
        )
    }
}