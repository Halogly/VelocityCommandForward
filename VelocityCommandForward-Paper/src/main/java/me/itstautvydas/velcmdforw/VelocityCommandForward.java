package me.itstautvydas.velcmdforw;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class VelocityCommandForward extends JavaPlugin {

    public static final String CHANNEL = "velocity_command_forward:main";

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onEnable() {
        getServer().getMessenger().registerOutgoingPluginChannel(this, CHANNEL);

        saveDefaultConfig();
        reloadConfig();

        var customCommandName = getConfig().getString("custom-command", "proxyexec");

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(customCommandName, (commandSourceStack, args) -> {
                var sender = commandSourceStack.getSender();
                if (args.length == 0) {
                    sender.sendMessage(Component.text("Usage: /" + customCommandName + " <command>", NamedTextColor.RED));
                    return;
                }

                var command = String.join(" ", args);
                ByteArrayDataOutput out = ByteStreams.newDataOutput();

                if (sender instanceof Player player) {
                    out.writeUTF(player.getUniqueId().toString());
                    out.writeUTF(command);
                    player.sendPluginMessage(VelocityCommandForward.this, CHANNEL, out.toByteArray());
                    log(player.getName(), command);
                    sender.sendMessage(Component.text("Command sent to proxy", NamedTextColor.DARK_GREEN)
                            .append(Component.text(" => ", NamedTextColor.WHITE))
                            .append(Component.text("/" + command, NamedTextColor.GREEN)));
                } else if (sender instanceof ConsoleCommandSender) {
                    out.writeUTF(""); // Console
                    out.writeUTF(command);
                    var any = Bukkit.getOnlinePlayers().stream().findFirst().orElse(null);
                    if (any == null) {
                        sender.sendMessage(Component.text("There must be at least 1 online player to be able to execute proxy console commands!", NamedTextColor.RED));
                        return;
                    }
                    any.sendPluginMessage(VelocityCommandForward.this, CHANNEL, out.toByteArray());
                    log("CONSOLE", command);
                    sender.sendMessage(Component.text("Command sent as console to proxy", NamedTextColor.DARK_GREEN)
                            .append(Component.text(" => ", NamedTextColor.WHITE))
                            .append(Component.text("/" + command, NamedTextColor.GREEN)));
                }
            });
        });
    }

    private void log(String type, String command) {
        this.getLogger().info("[" + type + "] Sending command packet to proxy => /" + command);
    }
}
