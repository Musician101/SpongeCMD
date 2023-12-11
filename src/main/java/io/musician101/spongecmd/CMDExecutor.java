package io.musician101.spongecmd;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.Command.Parameterized;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.plugin.PluginContainer;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public interface CMDExecutor extends CommandExecutor {

    static void register(@NotNull RegisterCommandEvent<Parameterized> event, @NotNull PluginContainer pluginContainer, @NotNull CMDExecutor command) {
        event.register(pluginContainer, command.toCommand(), command.getName(), command.getAliases().toArray(new String[0]));
    }

    default boolean canUse(@NotNull CommandCause cause) {
        return getPermission().map(cause::hasPermission).orElse(true);
    }

    @Override
    default CommandResult execute(@NotNull CommandContext context) {
        return CommandResult.error(text().append(text("Unknown or incomplete command, see below for error", RED), newline(), context.cause().context().get(EventContextKeys.COMMAND).map(Component::text).orElse(empty()), text("<---[HERE]", RED)).build());
    }

    default List<String> getAliases() {
        return List.of();
    }

    @NotNull
    default List<CMDExecutor> getChildren() {
        return List.of();
    }

    @NotNull
    default Component getDescription(CommandCause cause) {
        return empty();
    }

    @NotNull String getName();

    @NotNull
    default List<Parameter> getParameters() {
        return List.of();
    }

    @NotNull
    default Optional<String> getPermission() {
        return Optional.empty();
    }

    @NotNull Component getUsage(CommandCause cause);

    @NotNull
    default Command.Parameterized toCommand() {
        Command.Builder builder = Command.builder().executor(this).shortDescription(cause -> Optional.of(getUsage(cause))).extendedDescription(cause -> Optional.of(getDescription(cause))).executionRequirements(this::canUse).addChildren(getChildren().stream().collect(Collectors.toMap(c -> List.of(c.getName()), CMDExecutor::toCommand))).addParameters(getParameters());
        getPermission().ifPresent(builder::permission);
        return builder.build();
    }
}
