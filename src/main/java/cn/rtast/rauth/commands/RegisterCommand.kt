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

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

class RegisterCommand : CommandRegistrationCallback {
    override fun register(
        dispatcher: CommandDispatcher<ServerCommandSource>,
        registryAccess: CommandRegistryAccess,
        environment: CommandManager.RegistrationEnvironment
    ) {
        dispatcher.register(
            CommandManager.literal("register").then(
                CommandManager.literal("online")
                    .then(CommandManager.literal("browser").then(CommandManager.literal("confirm")
                        .then(CommandManager.argument("url", StringArgumentType.string()).executes {
                            this.executeRegisterConfirm(
                                it, StringArgumentType.getString(it, "url")
                            );1
                        })
                    ).executes { this.executeOpenBrowser(it);1 }).then(
                        CommandManager.literal("in-game").then(
                            CommandManager.argument("account", StringArgumentType.string())
                                .then(CommandManager.argument("password", StringArgumentType.string()).executes {
                                    this.executeRegisterOnlineInGame(
                                        it,
                                        StringArgumentType.getString(it, "account"),
                                        StringArgumentType.getString(it, "password")
                                    );1
                                })
                        )
                    )
            ).then(
                CommandManager.literal("offline").then(
                    CommandManager.argument("password", StringArgumentType.string())
                        .then(CommandManager.argument("re-password", StringArgumentType.string()).executes {
                            this.executeRegisterOffline(
                                it,
                                StringArgumentType.getString(it, "password"),
                                StringArgumentType.getString(it, "re-password")
                            );1
                        })
                )
            ).then(
                CommandManager.literal("")
            )
        )
    }

    private fun executeOpenBrowser(context: CommandContext<ServerCommandSource>) {

    }

    private fun executeRegisterOnlineBrowser(context: CommandContext<ServerCommandSource>) {

    }

    private fun executeRegisterConfirm(context: CommandContext<ServerCommandSource>, url: String) {

    }

    private fun executeRegisterOnlineInGame(
        context: CommandContext<ServerCommandSource>, account: String, password: String
    ) {

    }

    private fun executeRegisterOffline(
        context: CommandContext<ServerCommandSource>, password: String, passwordRetry: String
    ) {

    }
}