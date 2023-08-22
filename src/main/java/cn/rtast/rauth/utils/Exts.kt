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
import kotlin.random.Random

fun Any.toJsonString(): String {
    // Any data class to json string
    return Gson().toJson(this)
}

inline fun <reified T> String.fromJson(): T {
    // String.fromJson<Example>()
    val gson = Gson()
    return gson.fromJson(this, T::class.java)
}

fun rnd(length: Int): String {
    // generate a random string
    val lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz"
    val upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val digits = "0123456789"
    val symbols = "!@#$%^&*()-_=+[{]}|;:',<.>?/"

    val allChars = lowerCaseLetters + upperCaseLetters + digits + symbols

    val password = buildString {
        repeat(length) {
            val randomIndex = Random.nextInt(allChars.length)
            append(allChars[randomIndex])
        }
    }

    return password
}
