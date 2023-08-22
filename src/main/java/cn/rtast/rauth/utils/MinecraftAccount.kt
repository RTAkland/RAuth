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

import com.google.gson.Gson
import okhttp3.FormBody

class MinecraftAccount {
    companion object {
        const val MC_BEARER_TOKEN_URL = "https://api.minecraftservices.com/authentication/login_with_xbox"

    }

    private val gson = Gson()

    private val header = mapOf("Content-Type" to "application/json")

    fun getBearerToken(xstsData: XBoxLiveAuth.XBoxLiveAuthResponse) {
        val uhs = xstsData.DisplayClaims.xui.first().uhs
        val token = xstsData.Token
        val requestBody = FormBody.Builder()
            .add("identityToken", "XBL3.0%20x=$uhs;$token")
            .add("ensureLegacyEnabled", "true")
            .build()
        val response = Http.post(MC_BEARER_TOKEN_URL, requestBody, this.header)
    }

    data class Profile(
        val username: String,
        val roles: List<Any>,
        val access_token: String,
        val token_type: String,
        val expires_in: Int
    )
}