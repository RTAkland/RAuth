package cn.rtast.rauth

import cn.rtast.rauth.commands.LoginCommand
import cn.rtast.rauth.commands.LogoutCommand
import cn.rtast.rauth.commands.RegisterCommand
import cn.rtast.rauth.utils.PlayerStatusUtil
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import java.io.File

class MultiAuth : ModInitializer {
    override fun onInitialize() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            LoginCommand().register(dispatcher)
            RegisterCommand().register(dispatcher)
            LogoutCommand().register(dispatcher)
        }
        val file = File("./registered.json")
        if (!file.exists()) {
            file.createNewFile()
            file.writeText("[]")
        }
        PlayerStatusUtil("").default()
    }
}