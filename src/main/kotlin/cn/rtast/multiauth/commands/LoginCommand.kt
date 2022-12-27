package cn.rtast.multiauth.commands

import cn.rtast.multiauth.utils.PlayerStatusUtil
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType.*
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.CommandManager.*
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text

class LoginCommand {
    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        dispatcher.register(literal("login").then(argument("pass", string()).executes {
            loginUser(
                it, getString(it, "pass")
            );1
        }))
    }

    private fun loginUser(s: CommandContext<ServerCommandSource>, password: String) {
        val playerName = s.source.player?.name?.string
        val playerStatus = playerName?.let { PlayerStatusUtil(it) }
        val source = s.source.player
        if (playerStatus?.isLoggedIn() == true) {
            source?.sendMessage(Text.literal("§3[MultiAuth]§4你已经登录过了!"))
            return
        } else if (playerStatus?.isExists() == false) {
            source?.sendMessage(Text.literal("§3[MultiAuth]§4该账号不存在请先使用/register注册!"))
            return
        } else if (playerStatus?.isCorrectPassword(password) == true) {
            source?.sendMessage(Text.literal("§3[MultiAuth]§a登陆成功!"))
            source?.isInvulnerable = false
            playerStatus.setLogged(true)
            return
        } else {
            source?.sendMessage(Text.literal("§3[MultiAuth]§4密码错误!"))
        }
    }
}