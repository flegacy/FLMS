package io.github.flegacy.flms.ui

import io.github.flegacy.flms.FLMS
import io.github.flegacy.flms.items.ItemLibrary
import io.github.flegacy.flms.ui.element.FillerElement
import io.github.flegacy.flms.ui.element.InterfaceElement
import io.github.flegacy.flms.ui.element.TransferButton
import org.bukkit.entity.Player

private const val ITEMS_PER_PAGE = 28

abstract class BookInterface(private val title: String): FLMSInterface(54, "$title (Page 1)") {

    private val pages = mutableListOf<PageInterface>()
    protected val bookElements = mutableListOf<InterfaceElement>()

    init {
        setupBorder(this)
    }

    // TODO test if multiple people editing the same inventory will break something

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

    protected open fun removeElement(element: InterfaceElement) {
        if (!bookElements.remove(element))
            return
        val copiedList = mutableListOf<InterfaceElement>()
        copiedList.addAll(bookElements)
        clear()
        for (copied in copiedList)
            addElement(copied)
    }

    protected fun clear() {
        pages.clear()
        elements.clear()
        bookElements.clear()
        inventory.clear()
        setupBorder(this)
    }


    protected fun setGlobalBorderElement(slot: Int, element: InterfaceElement) {
        require(slot in EDITABLE_BORDER_INDEXES)
        setElement(slot, element)
        for (page in pages)
            page.setElement(slot, element)
    }

    private fun append() {
        val new = PageInterface()
        val previous = 
            if (pages.isEmpty())
                this
            else pages.last()

        val newConnector = TransferButton(ItemLibrary.LEFT_POINTER, previous)
        val prevConnector = TransferButton(ItemLibrary.RIGHT_POINTER, new)
        val jumpFirst = TransferButton(ItemLibrary.JUMP_FIRST, this)
        val jumpLast = TransferButton(ItemLibrary.JUMP_LAST, new)

        new.setElement(45, newConnector)
        previous.setElement(53, prevConnector)
        this.setElement(45, jumpLast)
        new.setElement(53, jumpFirst)

        pages.add(new)
    }

    inner class PageInterface: FLMSInterface(54, "$title (Page ${pages.size + 2})") {
        init {
            setupBorder(this)
        }
    }

    companion object {

        private val BORDER_INDEXES = arrayOf(0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,44,45,46,47,48,49,50,51,52,53)
        private val EDITABLE_BORDER_INDEXES = BORDER_INDEXES.filter { it !in listOf(45, 53) }

        fun setupBorder(page: FLMSInterface) {
            require(page is BookInterface || page is PageInterface)

            val filler = FillerElement()
            for (int in BORDER_INDEXES)
                page.setElement(int, filler)
        }
    }
}
