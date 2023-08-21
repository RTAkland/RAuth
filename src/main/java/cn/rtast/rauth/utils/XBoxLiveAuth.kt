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
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class XBoxLiveAuth {

    companion object {
        const val XBOX_LIVE_AUTH_URL = "https://user.auth.xboxlive.com/user/authenticate"
        const val XSTS_AUTH_URL = "https://xsts.auth.xboxlive.com/xsts/authorize"
    }

    private val gson = Gson()
    private val header = mapOf(
        "Content-Type" to "application/json",
        "Accept" to "application/json"
    )

    fun getXBoxLiveAuthResponse(accessToken: MicrosoftAccessToken): XBoxLiveAuthModel {
        // Step 2
        val properties = Properties(
            "RPS",
            "user.auth.xboxlive.com",
            "d=${accessToken.access_token}"
        )
        val requestBodyData = RequestBodyData(
            properties,
            "http://auth.xboxlive.com",
            "JWT"
        )
        val requestBodyJson = gson.toJson(requestBodyData)
        val requestBody = requestBodyJson.toRequestBody("application/json".toMediaType())
        val response = Http.post(XBOX_LIVE_AUTH_URL, requestBody, this.header)
        return gson.fromJson(response, XBoxLiveAuthModel::class.java)
    }

    fun getXSTSAuthAccessToken(xblToken: XBoxLiveAuthModel): XBoxLiveAuthModel {
        // Step 3
        val propertiesXSTS = PropertiesXSTS(
            "RETAIL",
            listOf(xblToken.Token)
        )
        val requestBodyData = RequestBodyDataXSTS(
            propertiesXSTS,
            "rp://api.minecraftservices.com/",
            "JWT"
        )
        val requestBodyJson = gson.toJson(requestBodyData)
        val requestBody = requestBodyJson.toRequestBody("application/json".toMediaType())
        val response = Http.post(XSTS_AUTH_URL, requestBody, this.header)
        return gson.fromJson(response, XBoxLiveAuthModel::class.java)
    }

    private data class PropertiesXSTS(
        val SandboxId: String,
        val UserTokens: List<String>
    )

    private data class RequestBodyDataXSTS(
        val Properties: PropertiesXSTS,
        val RelyingParty: String,
        val TokenType: String
    )

    private data class Properties(
        val AuthMethod: String,
        val SiteName: String,
        val RpsTicket: String
    )

    private data class RequestBodyData(
        val Properties: Properties,
        val RelyingParty: String,
        val TokenType: String
    )

    data class XBoxLiveAuthModel(
        val Token: String,
        val DisplayClaims: DisplayClaims
    )

    data class DisplayClaims(
        val xui: List<UHS>
    )

    data class UHS(
        val uhs: String
    )

}