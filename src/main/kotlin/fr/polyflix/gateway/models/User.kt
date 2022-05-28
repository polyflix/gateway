package fr.polyflix.gateway.models

data class User(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val roles: List<String> = listOf()
)
