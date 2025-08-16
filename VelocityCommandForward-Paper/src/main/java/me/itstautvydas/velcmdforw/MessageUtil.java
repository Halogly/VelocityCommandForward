package me.itstautvydas.velcmdforw;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtil {

    private static JavaPlugin plugin;
    private volatile List<String> filteredCommands;
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{(\\w+)}");

    public MessageUtil(JavaPlugin plugin) {
        MessageUtil.plugin = plugin;
        this.filteredCommands = new CopyOnWriteArrayList<>(plugin.getConfig().getStringList("filtered-commands"));
    }

    public void sendMessage(CommandSender sender, String key) {
        sendMessage(sender, key, new HashMap<>());
    }

    public void sendMessage(CommandSender sender, String key, Map<String, String> placeholders) {
        Component message = getMessage(key, placeholders);
        if (message.equals(Component.empty())) return;
        sender.sendMessage(message);
    }

    public Component getMessage(String key, Map<String, String> placeholders) {
        String rawMessage = plugin.getConfig().getString(key);
        if (rawMessage != null) {
            String processedMessage = replacePlaceholders(rawMessage, placeholders);
            return LegacyComponentSerializer.legacyAmpersand().deserialize(processedMessage);
        }
        return PlainTextComponentSerializer.plainText().deserialize("Missing message key: " + key);
    }

    public void consoleLog(String key, Map<String, String> placeholders) {
        Component log = getMessage(key, placeholders);
        if (log.equals(Component.empty())) return;
        plugin.getLogger().info(LegacyComponentSerializer.legacySection().serialize(log));
    }

    public String velocityLog(String key, Map<String, String> placeholders) {
        return LegacyComponentSerializer.legacySection().serialize(getMessage(key, placeholders));
    }

    private static String replacePlaceholders(String message, Map<String, String> placeholders) {
        if (placeholders.isEmpty()) return message;
        StringBuilder result = new StringBuilder();
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(message);
        while (matcher.find()) {
            String placeholder = matcher.group(1);
            String replacement = placeholders.getOrDefault(placeholder, matcher.group());
            matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result);
        return result.toString();
    }

    public void updateFilteredCommands(List<String> Commands) {
        this.filteredCommands = new CopyOnWriteArrayList<>(Commands);
    }

    public boolean shouldFilterCommand(String command) {
        if (this.filteredCommands.isEmpty()) return false;
        String root = command.split(" ")[0].replace("/", "");
        return filteredCommands.contains(root.toLowerCase());
    }
}
