package com.chsteam.combatengine.network

import com.chsteam.combatengine.CombatEngine
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import org.bukkit.event.player.*
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.releaseResourceFile
import java.io.File

object Encode {

    private lateinit var combat_config : String
    private lateinit var roll_config: String

    private val fabric_combat_config : ByteBuf = Unpooled.buffer()
    private val forge_combat_config : ByteBuf = Unpooled.buffer()
    private val fabric_roll_config : ByteBuf = Unpooled.buffer()
    private val forge_roll_config : ByteBuf = Unpooled.buffer()

    fun loadToString(name: String) : String {
        val file = File(getDataFolder(), "weapon_attributes/$name.json")
        return file.readText().trim()
    }

    @Awake(LifeCycle.ENABLE)
    fun load() {
        val file = File(getDataFolder(),"weapon_attributes")
        if(!file.exists()) {
            releaseResourceFile("weapon_attributes/anchor.json", true)
            releaseResourceFile("weapon_attributes/axe.json", true)
            releaseResourceFile("weapon_attributes/battlestaff.json", true)
            releaseResourceFile("weapon_attributes/claw.json", true)
            releaseResourceFile("weapon_attributes/claymore.json", true)
            releaseResourceFile("weapon_attributes/coral_blade.json", true)
            releaseResourceFile("weapon_attributes/cutlass.json", true)
            releaseResourceFile("weapon_attributes/dagger.json", true)
            releaseResourceFile("weapon_attributes/double_axe.json", true)
            releaseResourceFile("weapon_attributes/fist.json", true)
            releaseResourceFile("weapon_attributes/glaive.json", true)
            releaseResourceFile("weapon_attributes/halberd.json", true)
            releaseResourceFile("weapon_attributes/hammer.json", true)
            releaseResourceFile("weapon_attributes/heavy_axe.json", true)
            releaseResourceFile("weapon_attributes/katana.json", true)
            releaseResourceFile("weapon_attributes/mace.json", true)
            releaseResourceFile("weapon_attributes/pickaxe.json", true)
            releaseResourceFile("weapon_attributes/rapier.json", true)
            releaseResourceFile("weapon_attributes/scythe.json", true)
            releaseResourceFile("weapon_attributes/sickle.json", true)
            releaseResourceFile("weapon_attributes/soul_knife.json", true)
            releaseResourceFile("weapon_attributes/spear.json", true)
            releaseResourceFile("weapon_attributes/staff.json", true)
            releaseResourceFile("weapon_attributes/sword.json", true)
            releaseResourceFile("weapon_attributes/trident.json", true)
            releaseResourceFile("weapon_attributes/twin_blade.json", true)
            releaseResourceFile("weapon_attributes/wand.json", true)

            releaseResourceFile("combat.json", true)
            releaseResourceFile("roll.json", true)
        }

        var configFile = File(getDataFolder(), "combat.json")
        combat_config = configFile.readText().replace("\\s".toRegex(), "").replace("\\n".toRegex(), "")

        configFile = File(getDataFolder(), "roll.json")
        roll_config = configFile.readText().replace("\\s".toRegex(), "").replace("\\n".toRegex(), "")

        fabric_combat_config.writeString(combat_config)
        forge_combat_config.writeBoolean(false)
        forge_combat_config.writeString("${CombatEngine.BETTER_COMBAT}:config_sync")
        forge_combat_config.writeString(combat_config)

        fabric_roll_config.writeString(roll_config)
        forge_roll_config.writeBoolean(false)
        forge_roll_config.writeString("${CombatEngine.COMBAT_ROLL}:config_sync")
        forge_roll_config.writeString(roll_config)
    }

    @SubscribeEvent
    fun e(e: PlayerRegisterChannelEvent) {
        e.player.sendPluginMessage(CombatEngine.plugin, "${CombatEngine.BETTER_COMBAT}:config_sync", fabric_combat_config.array())
        e.player.sendPluginMessage(CombatEngine.plugin, "${CombatEngine.BETTER_COMBAT}:network", forge_combat_config.array())
        e.player.sendPluginMessage(CombatEngine.plugin, "${CombatEngine.COMBAT_ROLL}:config_sync", fabric_roll_config.array())
        e.player.sendPluginMessage(CombatEngine.plugin, "${CombatEngine.COMBAT_ROLL}:network", forge_roll_config.array())
    }
}