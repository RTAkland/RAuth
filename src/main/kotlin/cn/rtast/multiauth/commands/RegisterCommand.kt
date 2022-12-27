package cn.rtast.multiauth.commands

import cn.rtast.multiauth.utils.PlayerStatusUtil
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
                literal("offline").then(
                    argument(
                        "pass", string()
                    ).then(argument("retry", string()).executes {
                        registerOfflineUser(
                            it, getString(it, "pass"), getString(it, "retry")
                        );1
                    })
                )
            ).then(literal("online").executes { openBrowser(it);1 }
                .then(literal("code").then(argument("url", string()).executes { getCode(it, getString(it, "url"));1 })))
        )
    }

    private fun registerOfflineUser(s: CommandContext<ServerCommandSource>, password: String, retry: String) {
        val playerName = s.source.player?.name?.string
        val playerStatus = playerName?.let { PlayerStatusUtil(it) }
        val source = s.source
        if (playerStatus?.isExists() == true) {
            source?.sendFeedback(Text.literal("§3[MultiAuth]§4你已经注册过了, 请不要重复注册!"), false)
            return
        } else if (playerStatus?.isOnlineMode() == true) {
            source?.sendFeedback(Text.literal("§3[MultiAuth]§4你的账号为正版账号无需注册!"), false)
            return
        } else if (password == retry) {
            source?.sendFeedback(Text.literal("§3[MultiAuth]§a注册成功,已自动登录!"), false)
            playerStatus?.register(
                playerName, password, source.player?.uuid.toString(), isOnlineMode = false, isLoggedIn = true
            )
            source.player?.isInvulnerable = false
            return
        } else {
            source?.sendFeedback(Text.literal("§3[MultiAuth]§4两次输入的密码不一致, 请重新输入!"), false)
        }
    }

    private fun openBrowser(s: CommandContext<ServerCommandSource>) {
        s.source.sendFeedback(Text.literal("§3[MultiAuth]§4Not Available"), false)
//        OnlineModeCheckUtil().openBrowser(
//            "https://login.live.com/oauth20_authorize.srf\n" + "?client_id=00000000402b5328\n" + "&response_type=code\n" + "&scope=service::user.auth.xboxlive.com::MBI_SSL\n" + "&redirect_uri=https://login.live.com/oauth20_desktop.srf"
//        )
//        s.source.sendFeedback(
//            Text.literal("§3[MultiAuth]§a请在打开的浏览器内输入你的微软账号密码, 登录完成后将网页跳转后的URL地址以/register online code <地址>的命令发送"),
//            false
//        )
//
    }

    private fun getCode(s: CommandContext<ServerCommandSource>, codeURL: String) {
//        OnlineModeCheckUtil().getCode(codeURL, s)
    }
}