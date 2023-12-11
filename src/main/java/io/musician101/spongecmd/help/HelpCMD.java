package io.musician101.spongecmd.help;

import io.musician101.spongecmd.CMDExecutor;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.metadata.PluginMetadata;
import org.spongepowered.plugin.metadata.model.PluginContributor;

import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.space;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.TextColor.color;

abstract class HelpCMD implements CMDExecutor {

    @NotNull protected final PluginContainer pluginContainer;

    HelpCMD(@NotNull PluginContainer pluginContainer) {
        this.pluginContainer = pluginContainer;
    }

    protected @NotNull Component commandInfo(@NotNull CMDExecutor command, @NotNull CommandCause cause) {
        return text().append(command.getUsage(cause), space(), command.getDescription(cause)).build();
    }

    protected @NotNull Component header() {
        PluginMetadata pmd = pluginContainer.metadata();
        Component begin = text("> ===== ", DARK_GREEN);
        Component middle = text(pmd.name().orElse("Unnamed Plugin"), GREEN);
        Component end = text(" ===== <", DARK_GREEN);
        Component developed = text("Developed by ", GOLD);
        List<String> authors = pmd.contributors().stream().map(PluginContributor::name).toList();
        int last = authors.size() - 1;
        Component authorsComponent = text(switch (last) {
            case 0 -> authors.get(0);
            case 1 -> String.join(" and ", authors);
            default -> String.join(", and ", String.join(", ", authors.subList(0, last)), authors.get(last));
        }, color(0xBDB76B));
        HoverEvent<Component> hoverEvent = HoverEvent.showText(text().append(developed, authorsComponent));
        Component click = text("Click a command for more info.", GOLD);
        return text().append(begin, middle, end, newline(), click).hoverEvent(hoverEvent).build();
    }
}
