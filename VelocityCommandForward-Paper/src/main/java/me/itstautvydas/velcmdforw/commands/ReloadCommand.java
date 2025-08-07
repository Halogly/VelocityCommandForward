package me.itstautvydas.velcmdforw.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.itstautvydas.velcmdforw.MessageUtil;
import me.itstautvydas.velcmdforw.VelocityCommandForward;
import org.bukkit.command.CommandSender;

@SuppressWarnings("ClassCanBeRecord")
public class ReloadCommand {
    private final VelocityCommandForward plugin;
    private final MessageUtil messageUtil;

    public ReloadCommand(VelocityCommandForward plugin, MessageUtil messageUtil) {
        this.plugin = plugin;
        this.messageUtil = messageUtil;
    }

    public LiteralCommandNode<CommandSourceStack> vcfCommand() {
        return Commands.literal("vcf")
                .requires(sender -> sender.getSender().hasPermission("velocitycommandforward.admin"))
                .then(Commands.literal("reload")
                        .executes(ctx -> {
                            if (plugin.hasNoConfig()) plugin.saveDefaultConfig();
                            plugin.reloadConfig();
                            CommandSender sender = ctx.getSource().getSender();
                            messageUtil.sendMessage(sender, "messages.reload");
                            return Command.SINGLE_SUCCESS;
                        }))
                .build();
    }
}
