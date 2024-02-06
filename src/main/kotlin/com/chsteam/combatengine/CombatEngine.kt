package com.chsteam.combatengine

import com.chsteam.combatengine.hook.mmoitems.CombatEffect
import com.chsteam.combatengine.network.*
import net.Indyuce.mmoitems.MMOItems
import org.bukkit.Bukkit
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.info
import taboolib.platform.BukkitPlugin

object CombatEngine : Plugin() {

    val plugin by lazy {
        BukkitPlugin.getInstance()
    }

    const val BETTER_COMBAT = "bettercombat"
    const val COMBAT_ROLL = "combatroll"

    override fun onEnable() {
        info("Welcome to use CombatEngine Version 1.2.0")

        Bukkit.getMessenger().registerIncomingPluginChannel(plugin, "$BETTER_COMBAT:c2s_request_attack", RequestAttack)
        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "$BETTER_COMBAT:config_sync")
        Bukkit.getMessenger().registerIncomingPluginChannel(plugin, "$BETTER_COMBAT:attack_animation", CombatAnimation)
        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "$BETTER_COMBAT:attack_animation")

        Bukkit.getMessenger().registerIncomingPluginChannel(plugin, "$BETTER_COMBAT:network", CombatForge)
        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "$BETTER_COMBAT:network")

        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "$COMBAT_ROLL:config_sync")
        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "$COMBAT_ROLL:animation")
        Bukkit.getMessenger().registerIncomingPluginChannel(plugin, "$COMBAT_ROLL:publish", RollAnimation)

        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "$COMBAT_ROLL:network")
        Bukkit.getMessenger().registerIncomingPluginChannel(plugin, "$COMBAT_ROLL:network", RollForge)

        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "$BETTER_COMBAT:attack_sound")


        if(Bukkit.getPluginManager().isPluginEnabled("MMOItems")) {
            MMOItems.plugin.stats.register(CombatEffect())
            MMOItems.plugin.templates.reload()
        }

    }

    override fun onDisable() {
        Bukkit.getMessenger().unregisterIncomingPluginChannel(plugin)
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(plugin)
    }
}