package cn.rtast.multiauth.listeners

import cn.rtast.multiauth.utils.PlayerStatusUtil
import net.minecraft.server.network.ServerPlayNetworkHandler

class OnPlayerMove {
    /**
     * 如果玩家在没有登录的情况下移动, 玩家移动的事件会被取消
     * 并将玩家传送到原地
     */
    fun canMove(networkHandler: ServerPlayNetworkHandler): Boolean {
        val player = networkHandler.player
        val playerStatus = PlayerStatusUtil(player.name.string)
        if (!playerStatus.isLoggedIn()) {
            player.teleport(player.x, player.y, player.z)  // don't let player move
        }
        return playerStatus.isLoggedIn()
    }
}