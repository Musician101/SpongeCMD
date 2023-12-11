package io.musician101.spongecmd.help;

import io.musician101.spongecmd.CMDExecutor;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.ArgumentParseException;
import org.spongepowered.api.command.parameter.ArgumentReader.Mutable;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.Parameter.Value;
import org.spongepowered.api.command.parameter.managed.ValueParameter.Simple;
import org.spongepowered.plugin.PluginContainer;

import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;

public abstract class HelpSubCMD extends HelpCMD {

    private final Value<CMDExecutor> executor = Parameter.builder(Parameter.key("executor", CMDExecutor.class)).addParser(new CMDParser()).build();
    @NotNull private final CMDExecutor root;

    public HelpSubCMD(@NotNull CMDExecutor root, @NotNull PluginContainer pluginContainer) {
        super(pluginContainer);
        this.root = root;
    }

    private Component defaultCommandInfo(CMDExecutor command, CommandCause cause) {
        return text().append(command.getUsage(cause), command.getDescription(cause)).build();
    }

    @Override
    public CommandResult execute(@NotNull CommandContext context) {
        if (root instanceof HelpMainCMD) {
            return root.execute(context);
        }

        return context.one(executor).filter(cmd -> cmd.canUse(context.cause())).map(cmd -> {
            context.sendMessage(text().append(header(), newline(), defaultCommandInfo(context.requireOne(executor.key()), context.cause())).build());
            return CommandResult.success();
        }).orElseGet(() -> {
            context.sendMessage(text().append(Stream.concat(Stream.of(header(), newline()), getChildren().stream().filter(cmd -> cmd.canUse(context.cause())).map(cmd -> commandInfo(cmd, context.cause()))).toArray(Component[]::new)).build());
            return CommandResult.success();
        });
    }

    @Override
    public @NotNull Component getDescription(CommandCause cause) {
        return text().append(text(" - ", DARK_GRAY), text("Shows the help info for /" + root.getName(), GRAY)).build();
    }

    @NotNull
    @Override
    public List<Parameter> getParameters() {
        return List.of(executor);
    }

    @Override
    public @NotNull Component getUsage(CommandCause cause) {
        return text().append(root.getUsage(cause), text(" help")).build();
    }

    class CMDParser implements Simple<CMDExecutor> {

        @Override
        public List<CommandCompletion> complete(CommandCause cause, String currentInput) {
            return getChildren().stream().map(CMDExecutor::getName).filter(s -> s.startsWith(currentInput)).map(CommandCompletion::of).toList();
        }

        @Override
        public Optional<? extends CMDExecutor> parseValue(CommandCause commandCause, Mutable reader) throws ArgumentParseException {
            String value = reader.parseString();
            return getChildren().stream().filter(cmd -> value.equals(cmd.getName())).findFirst();
        }
    }
}
