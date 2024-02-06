package com.chsteam.combatengine.nms

import net.minecraft.world.EnumHand
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftLivingEntity
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import taboolib.module.nms.NMSMap

class NMSImpl : NMS() {

    override fun attack(player: Player, entity: LivingEntity) {
        try {
            (player as CraftPlayer).handle.attack((entity as CraftLivingEntity).handle)
        } catch (ex: Throwable) {
            ex.printStackTrace()
        }
    }


}