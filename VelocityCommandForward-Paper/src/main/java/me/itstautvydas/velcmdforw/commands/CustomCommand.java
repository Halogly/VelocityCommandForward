package me.itstautvydas.velcmdforw.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.itstautvydas.velcmdforw.MessageUtil;
import me.itstautvydas.velcmdforw.VelocityCommandForward;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ClassCanBeRecord")
public class CustomCommand {
    private final VelocityCommandForward plugin;
    private final MessageUtil messageUtil;

    public CustomCommand(VelocityCommandForward plugin, MessageUtil messageUtil) {
        this.plugin = plugin;
        this.messageUtil = messageUtil;
    }

    public LiteralCommandNode<CommandSourceStack> buildCustomCommand() {
        return Commands.literal(plugin.customCommandName)
                .executes(context -> {
                    handleCommand(context.getSource(), new String[0]);
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.argument("command", StringArgumentType.greedyString())
                        .executes(context -> {
                            String command = StringArgumentType.getString(context, "command");
                            handleCommand(context.getSource(), new String[]{command});
                            return Command.SINGLE_SUCCESS;
                        }))
                .build();
    }

    private void handleCommand(CommandSourceStack source, String[] args) {
        CommandSender sender = source.getSender();

        if (args.length == 0 || args[0].isEmpty()) {
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("custom_command", plugin.customCommandName);
            messageUtil.sendMessage(sender, "messages.usage", placeholders);
            return;
        }

        String command = String.join(" ", args);
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        if (sender instanceof Player player) {
            handlePlayerCommand(player, command, out);
        } else if (sender instanceof ConsoleCommandSender) {
            handleConsoleCommand(sender, command, out);
        }
    }

    private void handlePlayerCommand(Player player, String command, ByteArrayDataOutput out) {
        Map<String, String> placeholders = createPlaceholders(player.getName(), command);

        byte flags = 0;
        if (messageUtil.shouldFilterCommand(command)) flags |= 0x01;

        out.writeUTF(player.getUniqueId().toString());
        out.writeUTF(command);
        out.writeByte(flags);
        out.writeUTF(messageUtil.velocityLog("messages.velocity-log", placeholders));

        player.sendPluginMessage(plugin, VelocityCommandForward.CHANNEL, out.toByteArray());

        if (messageUtil.shouldFilterCommand(command)) return;

        plugin.log(player.getName(), command);
        messageUtil.sendMessage(player, "messages.command-sent-as-player", placeholders);
    }

    private void handleConsoleCommand(CommandSender sender, String command, ByteArrayDataOutput out) {
        Map<String, String> placeholders = createPlaceholders(sender.getName(), command);

        byte flags = 0;
        if (messageUtil.shouldFilterCommand(command)) flags |= 0x01;

        out.writeUTF(""); // Console
        out.writeUTF(command);
        out.writeByte(flags);
        out.writeUTF(messageUtil.velocityLog("messages.velocity-log", placeholders));

        Player onlinePlayer = Bukkit.getOnlinePlayers().stream().findFirst().orElse(null);
        if (onlinePlayer == null) {
            messageUtil.sendMessage(sender, plugin.getConfig().getString("messages.no-online-player"));
            return;
        }

        onlinePlayer.sendPluginMessage(plugin, VelocityCommandForward.CHANNEL, out.toByteArray());

        if (messageUtil.shouldFilterCommand(command)) return;

        plugin.log("CONSOLE", command);
        messageUtil.sendMessage(sender, "messages.command-sent-as-console", placeholders);
    }

    private Map<String, String> createPlaceholders(String sender, String command) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("command", command);
        placeholders.put("sender", sender);
        return placeholders;
    }
}
