package com.chsteam.combatengine.nms

import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import taboolib.module.nms.nmsProxy

abstract class NMS {

    abstract fun attack(player: Player, entity: LivingEntity)

    companion object {

        @JvmStatic
        val INSTANCE = nmsProxy<NMS>()
    }
}