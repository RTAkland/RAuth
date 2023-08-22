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
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Properties

object ConfigUtil {

    private val properties = Properties()

    private const val CONFIG_FILE = "config.properties"

    private fun loadProperties() {
        FileInputStream(this.CONFIG_FILE).use { inputStream ->
            properties.load(inputStream)
        }
    }

    fun init() {
        if (!File("./config.properties").exists()) {
            File("./config.properties").createNewFile()
            this.setProperties("Salt", rnd(10))
        }
        loadProperties()

    }

    fun getProperties(propertyName: String): String {
        return properties.getProperty(propertyName)
    }

    private fun setProperties(propertyName: String, propertyValue: String) {
        properties.setProperty(propertyName, propertyValue)
        saveProperties()
    }

    private fun saveProperties() {
        FileOutputStream(this.CONFIG_FILE).use { outputStream ->
            properties.store(outputStream, null)
        }
    }
}
