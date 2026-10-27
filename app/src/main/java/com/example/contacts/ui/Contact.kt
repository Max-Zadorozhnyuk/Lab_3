package com.example.contacts

data class Contact(
    val id: Int,
    val name: String,
    val phone: String,
    val email: String = ""
) {
    val initials: String
        get() = name.split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .mapNotNull { it.firstOrNull() }
            .joinToString("")
            .uppercase()
}
