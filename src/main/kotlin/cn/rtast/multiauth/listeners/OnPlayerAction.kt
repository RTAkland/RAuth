package cn.rtast.multiauth.listeners

import cn.rtast.multiauth.utils.PlayerStatusUtil
import net.minecraft.server.network.ServerPlayNetworkHandler

class OnPlayerAction {
    /**
     * 玩家没有登陆操作会被取消
     */
    fun canInteract(
        networkHandler: ServerPlayNetworkHandler
    ): Boolean {
        val player = networkHandler.player
        return PlayerStatusUtil(player.name.string).isLoggedIn()
    }
}