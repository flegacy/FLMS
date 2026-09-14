package io.github.flegacy.flms.ui

import org.bukkit.entity.Player

interface RefreshableInterface {
    fun refresh()
    fun openRefreshable(player: Player)
}