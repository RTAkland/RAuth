package cn.rtast.multiauth.utils

import cn.rtast.multiauth.models.RegisteredPlayersModel
import com.google.gson.Gson
import com.google.gson.JsonParser
import java.io.File


open class PlayerStatusUtil(
    private val username: String
) {
    private val file = File("./registered.json")
    private val json = file.readText()
    private val gson = Gson()
    private val parser = JsonParser()
    private val allUsers = getJson()

    init {
        val file = File("./registered.json")
        if (!file.exists()) {
            file.createNewFile()
            file.writeText("[]")
        }
    }

    private fun save() {
        file.writeText(gson.toJson(allUsers))
    }

    private fun getJson(): MutableList<RegisteredPlayersModel> {
        val jsonArray = parser.parse(json).asJsonArray
        val userList = mutableListOf<RegisteredPlayersModel>()
        for (user in jsonArray) {
            val userBean = gson.fromJson(user, RegisteredPlayersModel::class.java)
            userList.add(userBean)
        }
        return userList
    }

    private fun getIndexOfData(): RegisteredPlayersModel? {
        if (allUsers.isNotEmpty()) {
            return if (isExists()) {
                var index = 0
                for (i in allUsers) {
                    if (i.username == username) {
                        break
                    } else {
                        index++
                    }
                }
                allUsers[index]
            } else {
                null
            }
        } else {
            return null
        }
    }

    fun default() {
        if (allUsers.isNotEmpty()) {
            for (i in allUsers) {
                i.isLoggedIn = false
            }
            save()
        }
    }


    fun isExists(): Boolean {
        val allPlayersName = mutableListOf<String>()
        return if (allUsers.isNotEmpty()) {
            for (i in allUsers) {
                allPlayersName.add(i.username)
            }
            allPlayersName.contains(username)
        } else {
            false
        }
    }

    fun register(
        username: String,
        password: String,
        uuid: String,
        isOnlineMode: Boolean,
        isLoggedIn: Boolean
    ) {
        allUsers.add(RegisteredPlayersModel(username, password, uuid, isOnlineMode, isLoggedIn))
        save()
    }

    fun setLogged(state: Boolean) {
        for (i in allUsers) {
            if (i.username == username) {
                i.isLoggedIn = state
                break
            }
        }
        save()
    }

    fun isLoggedIn(): Boolean {
        val data = getIndexOfData()
        return data?.isLoggedIn ?: false
    }

    fun isOnlineMode(): Boolean {
        val data = getIndexOfData()
        return data?.isOnlineMode ?: false
    }

    fun isCorrectPassword(password: String): Boolean {
        val data = getIndexOfData()
        if (data !== null) {
            if (data.username == username && data.password == password) {
                return true
            }
            return false
        } else {
            return false
        }
    }
}