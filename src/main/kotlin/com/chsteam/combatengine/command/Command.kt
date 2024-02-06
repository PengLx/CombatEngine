package com.chsteam.combatengine.command

import com.chsteam.combatengine.network.Encode
import org.bukkit.entity.Player
import taboolib.common.platform.command.*
import taboolib.expansion.createHelper
import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.ItemTagList
import taboolib.module.nms.getItemTag
import taboolib.module.nms.setItemTag

@CommandHeader("combatengine", aliases = ["combat", "ce", "cegine"])
object Command {

    @CommandBody
    val help = mainCommand {
        createHelper()
    }

    @CommandBody
    val addTag = subCommand {
        dynamic {
            execute<Player> { sender, context, _ ->
                val item = sender.inventory.itemInMainHand
                val tag = item.getItemTag()
                tag["weapon_attributes"] = ItemTagData(Encode.loadToString(context.argument(0)))
                sender.inventory.setItemInMainHand(item.setItemTag(tag))
            }
        }
    }

    @CommandBody
    val addEnchantments = subCommand {
        dynamic {
            dynamic {
                execute<Player> { sender, context, _ ->
                    val item = sender.inventory.itemInMainHand
                    val tag = item.getItemTag()["Enchantments"]
                    val level = ItemTagData((context.argument(0)).toShort())
                    val id = ItemTagData(context.argument(-1))
                    val createTag = ItemTag()
                    createTag["lvl"] = level
                    createTag["id"] = id

                    tag?.asList()?.add(createTag) ?: run {
                        val subtag = item.getItemTag()
                        val listTag = ItemTagList()
                        listTag.add(createTag)
                        subtag["Enchantments"] = listTag
                    }
                }
            }
        }
    }
}