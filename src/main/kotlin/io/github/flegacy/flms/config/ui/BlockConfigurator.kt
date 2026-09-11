package io.github.flegacy.flms.config.ui

import io.github.flegacy.flms.FLMS
import io.github.flegacy.flms.config.ui.element.ItemStackInputDevice
import io.github.flegacy.flms.config.ui.element.StringInputDevice
import io.github.flegacy.flms.items.ItemLibrary
import io.github.flegacy.flms.items.ItemStackBuilder
import io.github.flegacy.flms.registry.RegisteredBlock
import io.github.flegacy.flms.ui.FLMSInterface
import io.github.flegacy.flms.ui.RefreshableInterface
import io.github.flegacy.flms.ui.element.FillerElement
import io.github.flegacy.flms.ui.element.FunctionalElement
import io.github.flegacy.flms.ui.element.InterfaceElement
import io.github.flegacy.flms.ui.element.TransferButton
import io.github.flegacy.flms.util.FLMS_GRAY
import io.github.flegacy.flms.util.FLMS_WHITE
import io.github.flegacy.flms.util.errPrefixed
import io.github.flegacy.flms.util.resolveName
import io.github.flegacy.flms.util.soundDelay
import io.github.flegacy.flms.util.soundDestroy
import io.github.flegacy.flms.util.soundSuccess
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class BlockConfigurator(private val plugin: FLMS, private val origin: BlockConfigMenu) : FLMSInterface(27, "Editing Block..."),
    RefreshableInterface {

    // TODO check for duplicate block types from other players configuring on finalization
    // TODO drops, for now just see if it works

    private val filler = FillerElement()

    private val notReadyElement = object: InterfaceElement {
        override fun getDisplay(): ItemStack {
            return ItemLibrary.NOT_READY_ICON
        }
    }

    private val typeInput = ItemStackInputDevice(
        this,
        "Current Block Type",
        ItemLibrary.BLOCK_TYPE_EMPTY_ICON

    ) { it.type.isBlock && plugin.registry().findBlock(it.type) == null }

    private val nameInput = StringInputDevice(
        plugin,
        this,
        { it.length in 1..100 },
        "Block Name"
    )

    private val postTypeInput = ItemStackInputDevice(
        this,
        "Post-Break Type",
        ItemLibrary.POST_BREAK_EMPTY_ICON
    ) { it.type.isBlock }

    private val xpInput = StringInputDevice(
        plugin,
        this,
        { it.toIntOrNull() != null && it.toInt() >= 0 },
        "Dropped XP",
        Material.EXPERIENCE_BOTTLE,
        "${FLMS_GRAY}Must be a whole number above",
        "${FLMS_GRAY}or equal to 0."
    )

    private val hardnessInput = StringInputDevice(
        plugin,
        this,
        { it.toFloatOrNull() != null && it.toFloat() >= 0 },
        "Block Hardness",
        Material.HEAVY_CORE,
        "${FLMS_GRAY}The block's hardness is a scalable",
        "${FLMS_GRAY}value representing how hard the block",
        "${FLMS_GRAY}is to break. Insta-mined blocks have",
        "${FLMS_GRAY}a locked hardness value of 0.",
        "${FLMS_GRAY}Must be a ${FLMS_WHITE}positive, floating-point integer${FLMS_GRAY}."
    )

    init {
        fill(filler)
        val back = TransferButton(ItemLibrary.BACK_BUTTON, origin)
        setElement(18, back)
        xpInput.string = "0"
        hardnessInput.string = "0"
        postTypeInput.item = ItemStack(Material.AIR)
        refresh()
    }

    constructor(plugin: FLMS, block: RegisteredBlock, origin: BlockConfigMenu) : this(plugin, origin) {
        typeInput.item = ItemStack(block.type)
        postTypeInput.item = ItemStack(block.postType)
        xpInput.string = block.xp.toString()
        hardnessInput.string = block.hardness.toString()
        nameInput.string = block.name
        refresh()
    }


    private fun isReady(): Boolean {
        return (typeInput.item != null)
    }

    // Any errors from here should result in looking over the code preventing users from entering invalid inputs
    private fun finish(player: Player) {
        val blockType = typeInput.item!!.type
        val postType = postTypeInput.item!!.type
        val xp = xpInput.string.toInt()
        val hardness = hardnessInput.string.toFloat()
        val name = nameInput.string

        val block = RegisteredBlock(blockType, hardness, xp, postType, name, emptyList())
        plugin.registry().register(block)
        origin.refresh()
        origin.open(player)
        soundSuccess(player, plugin)
    }

    override fun refresh() {
        if (typeInput.item != null && nameInput.string == "Nothing")
            nameInput.string = resolveName(typeInput.item!!.type)
        if (hardnessInput.string.length > 5)
            hardnessInput.string = hardnessInput.string.substring(0, 5)
        setElement(11, typeInput)
        setElement(12, nameInput)
        setElement(13, postTypeInput)
        setElement(14, hardnessInput)
        setElement(15, xpInput)

        if (isReady())
            setElement(26, FunctionalElement(ItemLibrary.FINISH_ICON) { finish(it) })
        else
            setElement(26, notReadyElement)
    }

    override fun openRefreshable(player: Player) {
        open(player)
    }

}