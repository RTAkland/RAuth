/*
 * Copyright 2023 RTAkland
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package cn.rtast.rauth.utils

import java.io.File

object PlayerManager {
    private val pmFile = File("./pm.json")
    private val pmContent = this.read().fromJson<PlayerManager>()

    const val MOJANG_UUID_API = "https://api.mojang.com/users/profiles/minecraft/"

    private fun write(content: String) {
        this.pmFile.writeText(content)
    }

    private fun read(): String {
        return this.pmFile.readText()
    }

    fun registerPlayer(profile: PlayerProfile) {
        this.pmContent.players.add(profile)
        this.write(this.pmContent.toJsonString())
    }

    fun removePlayer(profile: PlayerProfile) {
        this.pmContent.players.remove(profile)
    }

    fun registered(username: String): Boolean {
        for (i in this.pmContent.players) {
            if (username == i.username) {
                return true
            }
            break
        }
        return false
    }

    fun onlineUUID(username: String): String? {
        val response = Http.get(MOJANG_UUID_API + username, null, null)
        if (response.code != 200) {
            return null
        }
        val uuid = response.toJsonString().fromJson<APIProfile>().id

        val parts = mutableListOf(
            uuid.substring(0, 8),
            uuid.substring(8, 12),
            uuid.substring(12, 16),
            uuid.substring(16, 20),
            uuid.substring(20, 32)
        )

        return parts.joinToString("-")
    }

    fun onlineAccount(username: String): Boolean {
        for (i in this.pmContent.players) {
            if (username == i.username) {
                if (i.onlineMode) {
                    return true
                }
                break
            }
            break
        }
        return false
    }

    data class APIProfile(
        val id: String,
        val name: String
    )

    data class PlayerManager(
        val players: MutableList<PlayerProfile>,
        val version: Int
    )

    data class PlayerProfile(
        val username: String,
        val uuid: String,
        val onlineMode: Boolean,
        val password: String?
    )
}