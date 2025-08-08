package me.itstautvydas.velcmdforw;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class VelocityCommandForward extends JavaPlugin {

    public static final String CHANNEL = "velocity_command_forward:main";

    public String customCommandName;
    private MessageUtil messageUtil;


    @Override
    public void onEnable() {
        getServer().getMessenger().registerOutgoingPluginChannel(this, CHANNEL);

        if (hasNoConfig()) saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();

        this.messageUtil = new MessageUtil(this);
        this.customCommandName = getConfig().getString("custom-command", "proxyexec");
        CommandUtil commandUtil = new CommandUtil(this, messageUtil);

        registerCommand(commandUtil.buildCustomCommand());
        this.getLogger().info("Custom Command: " + customCommandName);
    }

    public boolean hasNoConfig() {
        File configFile = new File(getDataFolder(), "config.yml");
        return !configFile.exists();
    }

    public void registerCommand(LiteralCommandNode<CommandSourceStack> commandNode) {
        this.getLifecycleManager().registerEventHandler(
                LifecycleEvents.COMMANDS,
                commands -> commands.registrar().register(commandNode)
        );
    }

    public void log(String sender, String command) {
        if (messageUtil.shouldFilterCommand(command)) return;
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("sender", sender);
        placeholders.put("command", command);
        messageUtil.consoleLog("messages.console-log", placeholders);
    }
}
