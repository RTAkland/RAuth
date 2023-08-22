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

package cn.rtast.rauth.commands

import cn.rtast.rauth.utils.MinecraftAccount
import cn.rtast.rauth.utils.OAuthDeviceCode
import cn.rtast.rauth.utils.XBoxLiveAuth
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

class LoginCommand : CommandRegistrationCallback {
    override fun register(
        dispatcher: CommandDispatcher<ServerCommandSource>,
        registryAccess: CommandRegistryAccess,
        environment: CommandManager.RegistrationEnvironment
    ) {
        dispatcher.register(
            CommandManager.literal("login")
                .then(
                    CommandManager.argument("password", StringArgumentType.string())
                        .executes { this.executeLogin(it, StringArgumentType.getString(it, "password"));1 }
                )
                .then(
                    CommandManager.literal("confirm")
                        .then(
                            CommandManager.argument("device-code", StringArgumentType.string())
                                .executes { exeCon(StringArgumentType.getString(it, "device-code"));1 }
                        )
                )

        )
    }

    private fun executeLogin(context: CommandContext<ServerCommandSource>, password: String) {
        println(OAuthDeviceCode().getDeviceCode().user_code)
//        println(OAuthDeviceCode().getDeviceCode().device_code)
    }

    private fun exeCon(deviceCode: String) {
        val confim = OAuthDeviceCode().confirmVerification(deviceCode)
        val xA = XBoxLiveAuth().authXBoxLive(confim)
        val xsts = XBoxLiveAuth().authXSTS(xA)
        val acc = MinecraftAccount().getBearerToken(xsts)
        println(acc)
    }
}