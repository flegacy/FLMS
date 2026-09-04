package io.github.flegacy.flms.command.branch

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import io.github.flegacy.flms.FLMS
import io.github.flegacy.flms.data.Key
import io.github.flegacy.flms.util.FLMS_LIGHT_YELLOW
import io.github.flegacy.flms.util.FLMS_YELLOW
import io.github.flegacy.flms.util.prefixed
import io.github.flegacy.flms.util.resolveName
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder

private const val BRANCH_LITERAL = "effect"

class EffectCommandBranch(private val plugin: FLMS) : CommandBranch {

    override fun buildCommandTree(): LiteralArgumentBuilder<CommandSourceStack> {
        return Commands.literal(BRANCH_LITERAL)
            .then(Commands.argument("player", ArgumentTypes.player())
                .then(Commands.argument("effect", EffectArgumentType())
                    .then(Commands.literal("set")
                        .then(Commands.argument("level", IntegerArgumentType.integer(1, 255))
                            .executes { context -> executeSet(context) }))
                    .then(Commands.literal("get")
                        .executes { context -> executeGet(context) })
                    .then(Commands.literal("remove")
                        .executes { context -> executeRemove(context) })))
    }

    private fun executeGet(context: CommandContext<CommandSourceStack>): Int {
        val player =
            context.getArgument("player", PlayerSelectorArgumentResolver::class.java).resolve(context.source).first()
        val effect = context.getArgument("effect", EffectType::class.java)
        val profile = plugin.registry().findEffectProfile(player)
        val level = if (effect == EffectType.HASTE) profile.haste else profile.fatigue

        context.source.sender.sendMessage(
            prefixed(
                "The player ${FLMS_YELLOW}${player.name}${FLMS_LIGHT_YELLOW} has ${FLMS_YELLOW}${
                    effect.toString().lowercase()
                } ${level}${FLMS_LIGHT_YELLOW}."
            )
        )
        return Command.SINGLE_SUCCESS
    }

    private fun executeSet(context: CommandContext<CommandSourceStack>): Int {
        val player = context.getArgument("player", PlayerSelectorArgumentResolver::class.java).resolve(context.source).first()
        val effect = context.getArgument("effect", EffectType::class.java)
        val profile = plugin.registry().findEffectProfile(player)
        val setLevel = context.getArgument("level", Int::class.java)

        if (effect == EffectType.HASTE)
            profile.haste = setLevel.toUShort()
        else
            profile.fatigue = setLevel.toUShort()

        context.source.sender.sendMessage(prefixed("${FLMS_YELLOW}${resolveName(effect)} $setLevel$FLMS_LIGHT_YELLOW was given to ${FLMS_YELLOW}${player.name}${FLMS_LIGHT_YELLOW}."))

        val configMsg =
            if (effect == EffectType.HASTE)
                plugin.configValues().string(Key.HASTE_EFFECT_RECEIVE_MESSAGE)
            else
                plugin.configValues().string(Key.FATIGUE_EFFECT_RECEIVE_MESSAGE)
        // TODO test input occurrences
        val finalMsg = MiniMessage.miniMessage().deserialize(configMsg, Placeholder.unparsed("level", setLevel.toString()))

        player.sendMessage(finalMsg)
        return Command.SINGLE_SUCCESS
    }

    private fun executeRemove(context: CommandContext<CommandSourceStack>): Int {
        val player = context.getArgument("player", PlayerSelectorArgumentResolver::class.java).resolve(context.source).first()
        val effect = context.getArgument("effect", EffectType::class.java)
        val profile = plugin.registry().findEffectProfile(player)

        if (effect == EffectType.HASTE)
            profile.haste = 0u
        else
            profile.fatigue = 0u

        context.source.sender.sendMessage(prefixed("You removed ${FLMS_YELLOW}${resolveName(effect)}${FLMS_LIGHT_YELLOW} from ${FLMS_YELLOW}${player.name}${FLMS_LIGHT_YELLOW}."))

        val configMsg =
            if (effect == EffectType.HASTE)
                plugin.configValues().string(Key.HASTE_EFFECT_REMOVE_MESSAGE)
            else
                plugin.configValues().string(Key.FATIGUE_EFFECT_REMOVE_MESSAGE)
        val finalMsg = MiniMessage.miniMessage().deserialize(configMsg)

        player.sendMessage(finalMsg)
        return Command.SINGLE_SUCCESS
    }


}
