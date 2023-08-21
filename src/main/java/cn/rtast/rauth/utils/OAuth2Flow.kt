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

import cn.rtast.rauth.entities.MicrosoftAccessToken
import com.google.gson.Gson
import okhttp3.FormBody
import java.awt.Desktop
import java.net.URI

class OAuth2Flow {
    companion object {
        const val OAUTH_URL =
            "https://login.live.com/oauth20_authorize.srf?" +
                    "client_id=288ec5dd-6736-4d4b-9b96-30e083a8cad2" +
                    "&redirect_uri=http://localhost:29116/authentication-response" +
                    "&scope=XBoxLive.signin%20offline_access" +
                    "&response_type=code"

        const val OAUTH_TOKEN_URL = "https://login.live.com/oauth20_token.srf"
    }

    private val gson = Gson()

    fun openBrowser() {
        Desktop.getDesktop().browse(URI(OAUTH_URL))
    }

    fun getMicrosoftAccessToken(code: String): MicrosoftAccessToken {
        // Step 1
        val body = FormBody.Builder()
            .add("client_id", "288ec5dd-6736-4d4b-9b96-30e083a8cad2")
            .add("code", code)
            .add("grant_type", "authorization_code")
            .add("redirect_uri", "http://localhost:29116/authentication-response")
            .add("scope", "XBoxLive.signin")
            .build()

        val header = mapOf("Content-Type" to "application/x-www-form-urlencoded")
        val response = Http.post(OAUTH_TOKEN_URL, body, header)
        return gson.fromJson(response, MicrosoftAccessToken::class.java)
    }
}