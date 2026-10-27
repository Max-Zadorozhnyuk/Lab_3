package com.example.contacts

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class ContactsViewModel : ViewModel() {

    private var nextId = 4

    private val _contacts = mutableStateListOf(
        Contact(1, "Марія Сидоренко", "+380 50 123 45 67", "maria@example.com"),
        Contact(2, "Іван Петренко", "+380 66 987 65 43", "ivan@example.com"),
        Contact(3, "Олена Коваленко", "+380 95 555 11 22", "olena@example.com")
    )

    val contacts: List<Contact> get() = _contacts

    fun getContact(id: Int): Contact? = _contacts.find { it.id == id }

    fun addContact(name: String, phone: String, email: String) {
        _contacts.add(Contact(nextId++, name.trim(), phone.trim(), email.trim()))
    }

    fun updateContact(id: Int, name: String, phone: String, email: String) {
        val index = _contacts.indexOfFirst { it.id == id }
        if (index != -1) {
            _contacts[index] = Contact(id, name.trim(), phone.trim(), email.trim())
        }
    }

    fun deleteContact(id: Int) {
        _contacts.removeAll { it.id == id }
    }
}
