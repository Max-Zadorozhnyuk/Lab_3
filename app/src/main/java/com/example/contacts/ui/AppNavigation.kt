package com.example.contacts

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

object Routes {
    const val LIST = "contacts"
    const val ADD = "add?contactId={contactId}"      // contactId = -1 -> новий контакт
    const val DETAILS = "details/{contactId}"

    fun add() = "add"
    fun edit(id: Int) = "add?contactId=$id"
    fun details(id: Int) = "details/$id"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    // Один ViewModel на весь NavHost (спільні дані для всіх екранів)
    val viewModel: ContactsViewModel = viewModel()

    NavHost(navController = navController, startDestination = Routes.LIST) {

        composable(Routes.LIST) {
            ContactsListScreen(
                contacts = viewModel.contacts,
                onContactClick = { id -> navController.navigate(Routes.details(id)) },
                onAddClick = { navController.navigate(Routes.add()) }
            )
        }

        composable(
            route = Routes.ADD,
            arguments = listOf(navArgument("contactId") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) { entry ->
            val contactId = entry.arguments?.getInt("contactId") ?: -1
            AddContactScreen(
                existing = viewModel.getContact(contactId),
                onSave = { name, phone, email ->
                    if (contactId == -1) viewModel.addContact(name, phone, email)
                    else viewModel.updateContact(contactId, name, phone, email)
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.DETAILS,
            arguments = listOf(navArgument("contactId") { type = NavType.IntType })
        ) { entry ->
            val contactId = entry.arguments?.getInt("contactId") ?: return@composable
            val contact = viewModel.getContact(contactId) ?: return@composable
            DetailsContactScreen(
                contact = contact,
                onEdit = { navController.navigate(Routes.edit(contactId)) },
                onDelete = {
                    navController.popBackStack()
                    viewModel.deleteContact(contactId)
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
