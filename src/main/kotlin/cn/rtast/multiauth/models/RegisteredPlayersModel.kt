package cn.rtast.multiauth.models

data class RegisteredPlayersModel(
    val username: String,
    var password: String,
    val uuid: String,
    var isOnlineMode: Boolean,
    var isLoggedIn: Boolean,
)
