package cn.rtast.multiauth.listeners

import cn.rtast.multiauth.utils.PlayerStatusUtil
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

class OnPlayerConnect {
    /**
     * 在玩家链接的时候发送提示消息
     */
    fun listen(player: ServerPlayerEntity) {
        val playerStatus = PlayerStatusUtil(player.name.string)
        player.isInvulnerable = true
        if (!playerStatus.isExists()) {
            player.sendMessage(Text.literal("§3[MultiAuth]§4该账户未注册, 请使用/register <online/offline> <密码> <重复密码> 注册账户"))
        } else {
            if (playerStatus.isOnlineMode()) {
                playerStatus.setLogged(true)
                player.sendMessage(Text.literal("§3[MultiAuth]§a你的账号为正版账号已自动登录"))
                player.isInvulnerable = false
            } else {
                player.sendMessage(Text.literal("§3[MultiAuth]§a请使用/login <密码> 登录"))
            }
        }
    }
}