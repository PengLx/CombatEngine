package com.chsteam.combatengine.hook.mmoitems

import com.chsteam.combatengine.network.Encode
import io.lumine.mythic.lib.api.item.ItemTag
import net.Indyuce.mmoitems.api.item.build.ItemStackBuilder
import net.Indyuce.mmoitems.stat.data.StringData
import net.Indyuce.mmoitems.stat.type.StringStat
import org.bukkit.Material

class CombatEffect() : StringStat(
    "COMBAT_EFFECT",
    Material.NETHERITE_SWORD,
    "Combat Effect",
    arrayOf("This stat can make weapon has", "attack animation and effect"),
    arrayOf("all")
) {

    override fun whenApplied(item: ItemStackBuilder, data: StringData) {
        try {
            item.addItemTag(ItemTag("weapon_attributes", Encode.loadToString(data.string ?: "sword")))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}