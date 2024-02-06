package com.chsteam.combatengine.network

import com.chsteam.combatengine.CombatEngine
import org.bukkit.entity.Player
import org.bukkit.plugin.messaging.PluginMessageListener

object RollAnimation : PluginMessageListener{
    override fun onPluginMessageReceived(p0: String, p1: Player, p2: ByteArray) {
        p1.getNearbyEntities(64.0, 64.0, 64.0).filterIsInstance<Player>().forEach {
            it.sendPluginMessage(CombatEngine.plugin, "${CombatEngine.COMBAT_ROLL}:animation", p2)
        }
    }
}