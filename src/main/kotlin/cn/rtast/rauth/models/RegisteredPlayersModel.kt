package cn.rtast.rauth.models

data class RegisteredPlayersModel(
    val username: String,
    var password: String,
    val uuid: String,
    var isLoggedIn: Boolean,
)
