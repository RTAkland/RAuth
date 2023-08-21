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
        const val MINECRAFT_SERVICE_URL = "https://api.minecraftservices.com/authentication/login_with_xbox"
        const val MINECRAFT_ACCOUNT_CHECK_URL = "https://api.minecraftservices.com/entitlements/mcstore"
    }

    private val gson = Gson()

    fun getAccessToken(xstsAuthToken: XBoxLiveAuth.XBoxLiveAuthModel): Profile {
        val requestBody = FormBody.Builder()
            .add(
                "identityToken",
                "XBL3.0 x=${xstsAuthToken.DisplayClaims.xui.first().uhs};${xstsAuthToken.Token}"
            )
            .add("ensureLegacyEnabled", "true")
            .build()
        val response = Http.post(MINECRAFT_SERVICE_URL, requestBody, null)
        return gson.fromJson(response, Profile::class.java)
    }

    fun hasMinecraftProfile(profile: Profile): Boolean {
        return false
    }

    fun login(code: String) {
        try {
            val oauthToken = OAuth2Flow().getMicrosoftAccessToken(code)
            val response = XBoxLiveAuth().getXBoxLiveAuthResponse(oauthToken)
            val xstsAccessToken = XBoxLiveAuth().getXSTSAuthAccessToken(response)
            val accessToken = getAccessToken(xstsAccessToken)
            println(accessToken)
        } catch (error: Exception) {
            error.printStackTrace()
        }

    }

    data class Profile(
        val username: String,
        val roles: List<Any>,
        val access_token: String,
        val token_type: String,
        val expires_in: Int
    )
}