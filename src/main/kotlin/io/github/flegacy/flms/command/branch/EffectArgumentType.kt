package io.github.flegacy.flms.command.branch

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import io.github.flegacy.flms.util.errPrefixed
import io.papermc.paper.command.brigadier.MessageComponentSerializer
import io.papermc.paper.command.brigadier.argument.CustomArgumentType
import net.kyori.adventure.text.Component
import java.util.Locale
import java.util.concurrent.CompletableFuture

enum class EffectType {
    HASTE,
    FATIGUE
}

class EffectArgumentType: CustomArgumentType.Converted<EffectType, String> {

    companion object {
        private val ERROR_INVALID_EFFECT_TYPE = DynamicCommandExceptionType {
            type -> MessageComponentSerializer.message().serialize(errPrefixed("'$type' isn't an effect type."))
        }
            
    }

    override fun convert(nativeType: String): EffectType {
        try {
            return EffectType.valueOf(nativeType.uppercase(Locale.ROOT))
        } catch (_: IllegalArgumentException) {
            throw ERROR_INVALID_EFFECT_TYPE.create(nativeType)
        }
    }

    override fun <S : Any> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        for (type in EffectType.entries.map { it.toString().lowercase() }) {
            if (type.startsWith(builder.remainingLowerCase))
                builder.suggest(type)
        }

        return builder.buildFuture()
    }

    override fun getNativeType(): ArgumentType<String> {
        return StringArgumentType.word()
    }

}
