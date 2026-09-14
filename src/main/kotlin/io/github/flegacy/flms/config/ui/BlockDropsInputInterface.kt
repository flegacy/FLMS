package io.github.flegacy.flms.config.ui

import io.github.flegacy.flms.config.ui.element.DropRepresentation
import io.github.flegacy.flms.items.ItemLibrary
import io.github.flegacy.flms.ui.BookInterface
import io.github.flegacy.flms.ui.FLMSInterface
import io.github.flegacy.flms.ui.RefreshableInterface
import io.github.flegacy.flms.ui.element.FunctionalElement
import io.github.flegacy.flms.ui.element.TransferButton
import io.github.flegacy.flms.util.soundError
import io.github.flegacy.flms.util.soundPickup
import io.github.flegacy.flms.util.soundPiston
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.math.BigDecimal
import kotlin.math.ceil

// TODO test drop compression extensively

class BlockDropsInputInterface(origin: FLMSInterface) : FLMSInterface(54, "<bold>Editing Block Drops..."),
    RefreshableInterface {

    var drops = mutableListOf<ItemStack>()

    init {
        BookInterface.setupBorder(this)
        val backButton = TransferButton(ItemLibrary.BACK_BUTTON, origin)
        setElement(45, backButton)

        val inputButton = FunctionalElement(
            ItemLibrary.ADD_DROP_ICON,
            function = { player, cursor ->
                run {
                    if (cursor.type.isAir) {
                        soundError(player)
                        return@run
                    }

                    if (drops.size == 28) {
                        soundError(player)
                        return@run
                    }

                    soundPickup(player)
                    drops.add(cursor.clone())
                    refresh()
                }
            }
        )

        val item: ItemStack? = null
        val compressButton = FunctionalElement(
            ItemLibrary.BLOCK_DROPS_COMPRESS_ICON


        ) { player, stack ->
            run {

                val dropMap = mutableMapOf<ItemStack, Int>()
                for (drop in drops)  {
                    var contains = false
                    for (key in dropMap.keys) {
                        if (!key.isSimilar(drop))
                            continue
                        contains = true
                        dropMap[key] = (dropMap[key] ?: break) + drop.amount
                        break
                    }
                    if (!contains)
                        dropMap[drop] = drop.amount
                }
                val newDrops = mutableListOf<ItemStack>()
                for ((key, value) in dropMap) {
                    var amount = value
                    var stacks = 0
                    while (amount>key.maxStackSize) {
                        stacks++
                        amount-=key.maxStackSize
                    }
                    for (int in 0..<stacks) {
                        val stack = ItemStack(key)
                        stack.amount = key.maxStackSize
                        newDrops.add(stack)
                    }
                    val remainder = ItemStack(key)
                    remainder.amount = amount
                    newDrops.add(remainder)
                }

                drops = newDrops
                refresh()
                soundPiston(player)
            }
        }

        setElement(49, inputButton)
        setElement(52, compressButton)

    }

    override fun refresh() {
        for (index in 0..<28) {
            elements.remove(BookInterface.PAGE_ITEM_INDEXES[index])
            inventory.setItem(BookInterface.PAGE_ITEM_INDEXES[index], null)
            if (index in drops.indices) {
                setElement(BookInterface.PAGE_ITEM_INDEXES[index], DropRepresentation(drops[index], this))
            }
        }

    }

    override fun openRefreshable(player: Player) {
        open(player)
    }


}
