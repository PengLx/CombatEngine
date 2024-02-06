package com.chsteam.combatengine.network

import com.chsteam.combatengine.nms.NMS
import io.lumine.mythic.lib.api.item.NBTItem
import io.netty.buffer.Unpooled
import org.bukkit.Bukkit
import org.bukkit.SoundCategory
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.messaging.PluginMessageListener
import taboolib.common.platform.function.submit
import taboolib.common.platform.function.submitAsync
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type


object RequestAttack : PluginMessageListener{
    override fun onPluginMessageReceived(p0: String, player: Player, message: ByteArray) {
        val buffer = Unpooled.wrappedBuffer(message)

        var comboCount: Int = buffer.readInt()
        val isSneaking: Boolean = buffer.readBoolean()
        val selectedSlot: Int = buffer.readInt()
        val ids: IntArray = buffer.readIntArray()

        val event = PlayerInteractEvent(player, Action.LEFT_CLICK_AIR, player.inventory.itemInMainHand, null, BlockFace.UP)
        Bukkit.getPluginManager().callEvent(event)


        ids.forEach { id ->
            player.world.livingEntities.forEach {
                if(it.entityId == id) {
                    NMS.INSTANCE.attack(player, it)
                }
            }
        }

        submitAsync {
            try {
                val attributes = NBTItem.get(player.inventory.itemInMainHand).getString("weapon_attributes")
                val config = Configuration.loadFromString(attributes, Type.JSON)

                val attacks = config.getConfigurationSection("attributes")?.getMapList("attacks")!!.size

                if (comboCount < 0) {
                    comboCount = 0
                }
                val index: Int = comboCount % attacks

                val sound = config.getConfigurationSection("attributes")?.getMapList("attacks")!![index]["swing_sound"] as Map<String, Any>

                val id = sound["id"] as String? ?: ""

                val pitch = sound["pitch"]?.toString()?.toDouble()?.toFloat() ?: 1f
                val volume = sound["volume"]?.toString()?.toDouble()?.toFloat() ?: 1f

                submit {
                    player.world.playSound(player.location, id, SoundCategory.PLAYERS, volume, pitch)
                }
            } catch (ignore: Exception) {

            }
        }
    }
}