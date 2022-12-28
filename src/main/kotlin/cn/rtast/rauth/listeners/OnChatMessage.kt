package cn.rtast.rauth.listeners

import cn.rtast.rauth.utils.PlayerStatusUtil
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket
import net.minecraft.server.network.ServerPlayNetworkHandler

class OnChatMessage {
    /**
     * 玩家没有登陆只允许发送以/login 或 /register开头的命令消息
     * 其余的消息会被取消
     */
    fun canSendMessage(
        networkHandler: ServerPlayNetworkHandler,
        packet: ChatMessageC2SPacket
    ): Boolean {
        val message = packet.chatMessage
        val playerStatus = PlayerStatusUtil(networkHandler.player.name.string)
        if (!playerStatus.isLoggedIn() && (message.startsWith("/login") || message.startsWith("/register"))) {
            return true
        }
        return playerStatus.isLoggedIn()
    }
}