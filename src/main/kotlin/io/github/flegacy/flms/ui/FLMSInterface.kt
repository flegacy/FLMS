package io.github.flegacy.flms.ui

import io.github.flegacy.flms.ui.element.InterfaceElement
import io.github.flegacy.flms.util.msgFormat
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

abstract class FLMSInterface(val size: Int, title: String) : InventoryHolder {

    private val inventory = Bukkit.createInventory(this, size, msgFormat(title))
    protected val elements = mutableMapOf<Int, InterfaceElement>()

    fun open(player: Player) {
        player.openInventory(inventory)
    }

    fun setElement(slot: Int, element: InterfaceElement?) {
        require(slot in 0..<size)

        if (element == null) {
            elements.remove(slot)
            inventory.setItem(slot, null)
        } else {
            elements[slot] = element
            inventory.setItem(slot, element.getDisplay())
        }
    }

    fun getElement(slot: Int): InterfaceElement? {
        return elements[slot]
    }

    override fun getInventory(): Inventory {
        return inventory
    }

    protected fun fill(element: InterfaceElement) {
        for (int in 0..<size)
            setElement(int, element)
    }
}
