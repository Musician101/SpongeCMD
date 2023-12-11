package io.musician101.spongecmd.help;

import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.plugin.PluginContainer;

import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;

public abstract class HelpMainCMD extends HelpCMD {

    public HelpMainCMD(@NotNull PluginContainer pluginContainer) {
        super(pluginContainer);
    }

    @Override
    public CommandResult execute(@NotNull CommandContext context) {
        Component component = text().append(Stream.concat(Stream.of(header(), newline()), getChildren().stream().filter(cmd -> cmd.canUse(context.cause())).map(cmd -> commandInfo(cmd, context.cause()))).toArray(Component[]::new)).build();
        context.sendMessage(component);
        return CommandResult.success();
    }

    @Override
    public @NotNull Component getDescription(CommandCause cause) {
        return text().append(text(" - ", DARK_GRAY), text("Displays help and plugin info.", GRAY)).build();
    }

    @Override
    public @NotNull Component getUsage(CommandCause cause) {
        return text("/" + getName());
    }
}
