package cn.rtast.rauth.commands

import cn.rtast.rauth.utils.PlayerStatusUtil
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.CommandManager.*
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text

class LogoutCommand {
    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        dispatcher.register(literal("logout")
            .executes { logout(it);1 })
    }

    private fun logout(s: CommandContext<ServerCommandSource>) {
        val playerStatus = s.source.player?.name?.string?.let { PlayerStatusUtil(it) }
        if (playerStatus?.isLoggedIn() == false) {
            s.source.sendFeedback(Text.literal("§3[RAuth]§4账号未登入无法登出!"), false)
            return
        } else if (playerStatus?.isExists() == false) {
            s.source.sendFeedback(Text.literal("§3[RAuth]§4账号未注册请先注册!"), false)
            return
        } else {
            playerStatus?.setLogged(false)
            s.source.player?.isInvulnerable = true
            s.source.sendFeedback(Text.literal("§3[RAuth]§a登出成功!"), false)
        }
    }
}