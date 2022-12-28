package cn.rtast.rauth.commands

import cn.rtast.rauth.utils.PlayerStatusUtil
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType.*
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.CommandManager.*
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text

class RegisterCommand {
    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        dispatcher.register(
            literal("register").then(
                argument(
                    "pass", string()
                ).then(argument("retry", string()).executes {
                    registerOfflineUser(
                        it, getString(it, "pass"), getString(it, "retry")
                    );1
                })
            )
        )
    }

    private fun registerOfflineUser(s: CommandContext<ServerCommandSource>, password: String, retry: String) {
        val playerName = s.source.player?.name?.string
        val playerStatus = playerName?.let { PlayerStatusUtil(it) }
        val source = s.source
        if (playerStatus?.isExists() == true) {
            source?.sendFeedback(Text.literal("§3[MultiAuth]§4你已经注册过了, 请不要重复注册!"), false)
            return
        } else if (password == retry) {
            source?.sendFeedback(Text.literal("§3[MultiAuth]§a注册成功,已自动登录!"), false)
            playerStatus?.register(
                playerName, password, source.player?.uuid.toString(), true
            )
            source.player?.isInvulnerable = false
            return
        } else {
            source?.sendFeedback(Text.literal("§3[MultiAuth]§4两次输入的密码不一致, 请重新输入!"), false)
        }
    }
}