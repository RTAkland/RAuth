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

import cn.rtast.rauth.exceptions.MicrosoftAccountException
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class XBoxLiveAuth {
    companion object {
        const val XBOX_AUTH_URL = "https://user.auth.xboxlive.com/user/authenticate"
    }

    private val gson = Gson()

    private val header = mapOf(
        "Content-Type" to "application/json",
        "Accept" to "application/json"
    )

    fun authXBoxLive(accessToken: OAuthDeviceCode.DeviceAccessToken): XBoxLiveAuthResponse {
        val properties = PropertiesXBox(
            "RPS",
            "user.auth.xboxlive.com",
            "d=${accessToken.access_token}"
        )
        val requestBodyData = XBoxLiveAuth(
            properties,
            "http://auth.xboxlive.com",
            "JWT"
        )
        val requestBodyJson = gson.toJson(requestBodyData)
        val requestBody = requestBodyJson.toRequestBody("application/json".toMediaType())
        return gson.fromJson(
            Http.post(XBOX_AUTH_URL, requestBody, this.header).body.string(),
            XBoxLiveAuthResponse::class.java
        )
    }

    fun authXSTS(token: XBoxLiveAuthResponse): XBoxLiveAuthResponse {
        val properties = PropertiesXSTS(
            "RETAIL",
            listOf(token.Token)
        )
        val requestBodyData = XSTSAuth(
            properties,
            "rp://api.minecraftservices.com/",
            "JWT"
        )
        val requestBodyJson = gson.toJson(requestBodyData)
        val requestBody = requestBodyJson.toRequestBody("application/json".toMediaType())
        val response = Http.post(XBOX_AUTH_URL, requestBody, this.header)
        if (response.code != 200) {
            val errResponse = gson.fromJson(response.body.string(), XErrResponse::class.java)
            val errDescription = when (errResponse.XErr) {
                MicrosoftErrorCode.NoXBoxAccount.code -> {
                    MicrosoftErrorCode.NoXBoxAccount.description
                }

                MicrosoftErrorCode.AccountForbidden.code -> {
                    MicrosoftErrorCode.AccountForbidden.description
                }

                MicrosoftErrorCode.AccountRequireAdultVerify.code,
                MicrosoftErrorCode.AccountRequireAdultVerify2.code -> {
                    MicrosoftErrorCode.AccountRequireAdultVerify.description
                }

                else -> {
                    MicrosoftErrorCode.ChildAccount.description
                }
            }
            throw MicrosoftAccountException("Microsoft account exception: $errDescription. Go to ${errResponse.Redirect} for help")
        }
        return gson.fromJson(response.body.string(), XBoxLiveAuthResponse::class.java)
    }

    data class PropertiesXBox(
        val AuthMethod: String,
        val SiteName: String,
        val RpsTicket: String
    )

    data class XBoxLiveAuth(
        val Properties: PropertiesXBox,
        val RelyingParty: String,
        val TokenType: String
    )

    data class XBoxLiveAuthResponse(
        val IssueInstant: String,
        val NotAfter: String,
        val Token: String,
        val DisplayClaims: DisplayClaims
    )

    data class DisplayClaims(
        val xui: List<UHS>
    )

    data class UHS(
        val uhs: String
    )

    data class PropertiesXSTS(
        val SandboxId: String,
        val UserTokens: List<String>
    )

    data class XSTSAuth(
        val Properties: PropertiesXSTS,
        val RelyingParty: String,
        val TokenType: String
    )

    data class XErrResponse(
        val Identity: String,
        val XErr: Long,
        val Message: String,
        val Redirect: String
    )

    enum class MicrosoftErrorCode(val code: Long, val description: String) {
        NoXBoxAccount(2148916233, "This account don't have Xbox account."),
        AccountForbidden(2148916235, "This account is forbidden."),
        AccountRequireAdultVerify(2148916236, "This account requires adult verification (Korea)."),
        AccountRequireAdultVerify2(2148916237, "This account requires adult verification (Korea)."),
        ChildAccount(2148916238, "This account is a child account.")
    }
}