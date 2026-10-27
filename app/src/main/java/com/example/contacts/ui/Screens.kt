package com.example.contacts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Orange = Color(0xFFFF9800)
private val Background = Color(0xFFF5F5F5)
private val Border = Color(0xFFE0E0E0)
private val ButtonGray = Color(0xFFF4F4F4)
private val Green = Color(0xFF4CAF50)

// ---------- Спільні компоненти ----------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OrangeTopBar(title: String, onBack: (() -> Unit)? = null) {
    CenterAlignedTopAppBar(
        title = { Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Orange,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        )
    )
}

@Composable
private fun GrayButton(text: String, onClick: () -> Unit, enabled: Boolean = true) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Border),
        colors = ButtonDefaults.buttonColors(
            containerColor = ButtonGray,
            contentColor = Color.Black,
            disabledContainerColor = ButtonGray,
            disabledContentColor = Color.Gray
        )
    ) {
        Text(text, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

@Composable
private fun Avatar(initials: String, size: Int = 62) {
    Box(
        modifier = Modifier.size(size.dp).clip(CircleShape).background(Orange),
        contentAlignment = Alignment.Center
    ) {
        Text(initials, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
    }
}

// ---------- Екран 1: Список контактів ----------

@Composable
fun ContactsListScreen(
    contacts: List<Contact>,
    onContactClick: (Int) -> Unit,
    onAddClick: () -> Unit
) {
    Scaffold(
        topBar = { OrangeTopBar("Мої контакти") },
        containerColor = Background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = ButtonGray,
                contentColor = Color.Black,
                shape = RoundedCornerShape(12.dp)
            ) { Text("+", fontSize = 24.sp) }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(contacts, key = { it.id }) { contact ->
                Card(
                    onClick = { onContactClick(contact.id) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Border)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Avatar(contact.initials)
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(contact.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text(contact.phone, color = Color.Gray, fontSize = 16.sp)
                        }
                        Spacer(Modifier.width(62.dp)) // для візуального центрування тексту
                    }
                }
            }
        }
    }
}

// ---------- Екран 2: Додавання / редагування ----------

@Composable
fun AddContactScreen(
    existing: Contact?,
    onSave: (name: String, phone: String, email: String) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf(existing?.name ?: "") }
    var phone by remember { mutableStateOf(existing?.phone ?: "") }
    var email by remember { mutableStateOf(existing?.email ?: "") }

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        unfocusedBorderColor = Border,
        focusedBorderColor = Orange
    )

    Scaffold(
        topBar = { OrangeTopBar(if (existing == null) "Новий контакт" else "Редагування") },
        containerColor = Background
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name, onValueChange = { name = it },
                placeholder = { Text("Ім'я") },
                singleLine = true, shape = RoundedCornerShape(10.dp),
                colors = fieldColors, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = phone, onValueChange = { phone = it },
                placeholder = { Text("Телефон") },
                singleLine = true, shape = RoundedCornerShape(10.dp),
                colors = fieldColors, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = email, onValueChange = { email = it },
                placeholder = { Text("Email") },
                singleLine = true, shape = RoundedCornerShape(10.dp),
                colors = fieldColors, modifier = Modifier.fillMaxWidth()
            )
            GrayButton(
                text = "Зберегти",
                enabled = name.isNotBlank() && phone.isNotBlank(),
                onClick = { onSave(name, phone, email) }
            )
            GrayButton(text = "Скасувати", onClick = onCancel)
        }
    }
}

// ---------- Екран 3: Деталі контакту ----------

@Composable
fun DetailsContactScreen(
    contact: Contact,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = { OrangeTopBar("Деталі контакту", onBack = onBack) },
        containerColor = Background
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Border)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(contact.initials, fontSize = 48.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(contact.name, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                    Text("● Доступна", color = Green, fontSize = 16.sp)
                }
            }

            InfoCard(label = "Телефон", value = contact.phone)
            if (contact.email.isNotBlank()) {
                InfoCard(label = "Email", value = contact.email)
            }

            GrayButton(text = "Редагувати", onClick = onEdit)
            GrayButton(text = "Видалити", onClick = onDelete)
        }
    }
}

@Composable
private fun InfoCard(label: String, value: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
    ) {
        Box(
            Modifier.width(4.dp).matchParentSize().background(Orange)
        )
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, color = Color.Gray, fontSize = 14.sp)
            Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp, textAlign = TextAlign.Center)
        }
    }
}
