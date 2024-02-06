package com.chsteam.combatengine.network

import com.chsteam.combatengine.CombatEngine
import io.netty.buffer.Unpooled
import org.bukkit.entity.Player
import org.bukkit.plugin.messaging.PluginMessageListener

object RollForge : PluginMessageListener  {
    override fun onPluginMessageReceived(p0: String, player: Player, message: ByteArray) {
        val buffer = Unpooled.wrappedBuffer(message)
        buffer.readByte()
        buffer.readBoolean()
        val type = buffer.readString(32767)
        val data = buffer.readBytes(buffer.readableBytes())

        val buf = Unpooled.buffer()
        buf.writeByte(0)
        buf.writeString(type)
        buf.writeBytes(data)

        player.getNearbyEntities(64.0, 64.0, 64.0).filterIsInstance<Player>().forEach {
            it.sendPluginMessage(CombatEngine.plugin, "${CombatEngine.COMBAT_ROLL}:network", buf.array())
        }
    }
}