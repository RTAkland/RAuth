package cn.rtast.multiauth.listeners

import cn.rtast.multiauth.utils.PlayerStatusUtil
import net.minecraft.server.network.ServerPlayerEntity

class OnPlayerLeave {
    /**
     * 玩家离开时设置玩家状态为登出
     */
    fun listen(player: ServerPlayerEntity) {
        PlayerStatusUtil(player.name.string).setLogged(false)
    }
}