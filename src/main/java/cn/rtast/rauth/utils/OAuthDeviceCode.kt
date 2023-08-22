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

import cn.rtast.rauth.exceptions.OAuthException
import com.google.gson.Gson
import okhttp3.FormBody

class OAuthDeviceCode {
    companion object {
        const val CLIENT_ID = "0effb02d-8381-48c5-8f45-e7eea4d85424"
        const val DEVICE_CODE_URL = "https://login.microsoftonline.com/consumers/oauth2/v2.0/devicecode"
        const val TOKEN_URL = "https://login.microsoftonline.com/consumers/oauth2/v2.0/token"
    }

    private val gson = Gson()

    private val header = mapOf("Content-Type" to "application/x-www-form-urlencoded")

    fun getDeviceCode(): DeviceCode {
        val requestBody = FormBody.Builder()
            .add("client_id", CLIENT_ID)
            .add("scope", "XBoxLive.signin%20offline_access")
            .build()

        val response = Http.post(DEVICE_CODE_URL, requestBody, this.header)
        return gson.fromJson(response.body.string(), DeviceCode::class.java)
    }

    fun confirmVerification(deviceCode: DeviceCode): DeviceAccessToken {
        val requestBody = FormBody.Builder()
            .add("grant_type", "urn:ietf:params:oauth:grant-type:device_code")
            .add("device_code", deviceCode.device_code)
            .add("client_id", CLIENT_ID)
            .add("scope", "XBoxLive.signin%20offline_access")
            .build()

        val response = Http.post(TOKEN_URL, requestBody, this.header)

        if (response.code != 200) {
            val exceptionMessage = gson.fromJson(response.body.string(), DeviceAccessTokenException::class.java)
            throw OAuthException("OAuth Exception: ${exceptionMessage.error_description}")
        }
        return gson.fromJson(response.body.string(), DeviceAccessToken::class.java)

    }

    data class DeviceCode(
        val user_code: String,
        val device_code: String,
        val verification_uri: String,
        val expires_in: Int,
        val interval: Int,
        val message: String
    )

    data class DeviceAccessToken(
        val token_type: String,
        val scope: String,
        val expires_in: Int,
        val ext_expires_in: Int,
        val access_token: String,
        val refresh_token: String
    )

    data class DeviceAccessTokenException(
        val error: String,
        val error_description: String,
        val error_codes: List<Int>,
        val timestamp: String,
        val trace_id: String,
        val correlation_id: String,
        val error_uri: String
    )
}