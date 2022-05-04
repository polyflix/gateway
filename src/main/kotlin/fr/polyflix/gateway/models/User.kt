package fr.polyflix.gateway.models

data class User(val id: String = "", val displayName: String = "", val roles: List<String> = listOf()) {
    override fun toString(): String {
        return "User { id: \"$id\", displayName: \"$displayName\", roles: \"$roles\" }"
    }
}
