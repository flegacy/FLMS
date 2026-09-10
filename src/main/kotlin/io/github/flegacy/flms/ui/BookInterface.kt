package io.github.flegacy.flms.ui

import io.github.flegacy.flms.FLMS
import io.github.flegacy.flms.ui.element.FillerElement
import io.github.flegacy.flms.ui.element.InterfaceElement
import io.github.flegacy.flms.ui.element.TransferButton
import org.bukkit.entity.Player

private const val ITEMS_PER_PAGE = 28

abstract class BookInterface(private val plugin: FLMS, private val title: String): FLMSInterface(54, "$title (Page 1)") {

    private val pages = mutableListOf<PageInterface>()
    protected val bookElements = mutableListOf<InterfaceElement>()

    init {
        setupBorder(plugin, this)
    }

    // TODO test if mutliple people editing the same inventory will break something

    fun open(page: Int, player: Player) {
        pages[page].open(player)
    }

    protected fun addElement(element: InterfaceElement) {
        bookElements.add(element)
        if (bookElements.size > ITEMS_PER_PAGE*(pages.size+1))
            append()
        val latest = 
            if (pages.isNotEmpty())
                pages.last()
            else 
                this
        latest.setElement(latest.inventory.firstEmpty(), element)
    }

    protected fun removeElement(element: InterfaceElement) {
        if (!bookElements.remove(element))
            return
        val copiedList = mutableListOf<InterfaceElement>()
        copiedList.addAll(bookElements)
        bookElements.clear()
        pages.clear()
        resetMain()
        for (copied in copiedList)
            addElement(copied)
    }

    protected fun clear() {
        pages.clear()
        elements.clear()
        resetMain()
    }

    private fun resetMain() {
        inventory.clear()
        setupBorder(plugin, this)
    }

    private fun append() {
        val new = PageInterface()
        val previous = 
            if (pages.isEmpty())
                this
            else pages.last()

        val newConnector = TransferButton(plugin.itemLib().leftPointer(), previous)
        val prevConnector = TransferButton(plugin.itemLib().rightPointer(), new)
        val jumpFirst = TransferButton(plugin.itemLib().jumpFirst(), this)
        val jumpLast = TransferButton(plugin.itemLib().jumpLast(), new)

        new.setElement(45, newConnector)
        previous.setElement(53, prevConnector)
        this.setElement(45, jumpLast)
        new.setElement(53, jumpFirst)

        pages.add(new)
    }

    inner class PageInterface: FLMSInterface(54, "$title (Page ${pages.size + 2})") {
        init {
            setupBorder(plugin, this)
        }
    }

    companion object {

        private fun setupBorder(plugin: FLMS, page: FLMSInterface) {
            require(page is BookInterface || page is PageInterface)

            val filler = FillerElement(plugin)
            for (int in 0..8) {
                page.setElement(int, filler)
                page.setElement(int+45, filler)
            }

            var side = 9
            for (int in 0..3) {
                page.setElement(side, filler)
                page.setElement(side + 8, filler)
                side+=9
            }
        }
    }
}
