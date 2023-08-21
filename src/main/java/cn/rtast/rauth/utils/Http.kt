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

import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

object Http {

    private val client = OkHttpClient.Builder().retryOnConnectionFailure(true).build()

    fun get(url: String, params: Map<String, String>? = null, headers: Map<String, String>?): String {
        val urlWithParams = buildUrlWithParams(url, params)
        val request = buildRequest(urlWithParams, headers)

        return executeRequest(request.build())
    }

    fun post(url: String, requestBody: RequestBody, headers: Map<String, String>?): String {
        val request = buildRequest(url, headers)
            .post(requestBody)
            .build()

        return executeRequest(request)
    }

    private fun buildUrlWithParams(url: String, params: Map<String, String>?): String {
        if (params.isNullOrEmpty()) {
            return url
        }

        val urlBuilder = url.toHttpUrlOrNull()?.newBuilder()
        params.forEach { (key, value) ->
            urlBuilder?.addQueryParameter(key, value)
        }

        return urlBuilder?.build().toString()
    }

    private fun buildRequest(url: String, headers: Map<String, String>?): Request.Builder {
        val requestBuilder = Request.Builder()
            .url(url)

        headers?.forEach { (key, value) ->
            requestBuilder.addHeader(key, value)
        }

        return requestBuilder
    }

    private fun executeRequest(request: Request): String {
        val response = client.newCall(request).execute()
        println(response.code)
        val resp = response.body.string()
        println(resp)
        return resp
    }
}
