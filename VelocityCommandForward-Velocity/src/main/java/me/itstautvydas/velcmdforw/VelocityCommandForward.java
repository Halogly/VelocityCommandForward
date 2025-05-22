package me.itstautvydas.velcmdforw;

import com.google.common.io.ByteStreams;
import com.google.inject.Inject;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import org.slf4j.Logger;

import java.util.UUID;

@Plugin(
        id = "velocity-command-forward",
        name = "VelocityCommandForward",
        version = BuildConstants.VERSION,
        url = "https://itstautvydas.me",
        authors = { "ItsTauTvyDas" }
)
public class VelocityCommandForward {

    private final Logger logger;
    private final ProxyServer proxy;

    private static final MinecraftChannelIdentifier CHANNEL = MinecraftChannelIdentifier.from("velocity_command_forward:main");

    @Inject
    public VelocityCommandForward(ProxyServer proxy, Logger logger) {
        this.proxy = proxy;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        proxy.getChannelRegistrar().register(CHANNEL);
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        if (!CHANNEL.equals(event.getIdentifier()))
            return;

        event.setResult(PluginMessageEvent.ForwardResult.handled());

        if (!(event.getSource() instanceof ServerConnection))
            return;

        @SuppressWarnings("UnstableApiUsage")
        var in = ByteStreams.newDataInput(event.getData());
        String uuidRaw = in.readUTF();
        String command = in.readUTF();

        logger.info("Received proxy command packet from {} => /{}", uuidRaw.isEmpty() ? "console" : "player", command);

        if (uuidRaw.isEmpty()) { // Console
            proxy.getCommandManager().executeAsync(proxy.getConsoleCommandSource(), command);
        } else { // Player
            var uuid = UUID.fromString(uuidRaw);
            proxy.getPlayer(uuid).ifPresent(player ->
                    proxy.getCommandManager().executeAsync(player, command)
            );
        }
    }
}
