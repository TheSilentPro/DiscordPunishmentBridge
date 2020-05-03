package tsp.dpb.command;

import net.dv8tion.jda.api.JDAInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import tsp.dpb.Core;
import tsp.dpb.util.FileUtils;
import tsp.dpb.util.Utils;

public class Command_dpb implements CommandExecutor {

    private Core plugin = (Core) Utils.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command,String s, String[] args) {
        if (args.length == 0) {
            Utils.sendMessage(sender, "&7========== [ &9DiscordPunishmentBridge Info &7] ==========");
            Utils.sendMessage(sender, " ");
            Utils.sendMessage(sender, "&b [ Plugin ] ");
            Utils.sendMessage(sender, "&7&oVersion: &b" + plugin.getDescription().getVersion());
            Utils.sendMessage(sender, "&7&oAuthors: &b" + Utils.DEVELOPERS);
            Utils.sendMessage(sender, "&7&oRunning on &b" + Bukkit.getServer().getVersion());
            Utils.sendMessage(sender, " ");
            Utils.sendMessage(sender, "&5 [ JDAInfo ]");
            Utils.sendMessage(sender, "&7&oVersion: &5" + JDAInfo.VERSION);
            Utils.sendMessage(sender, "&7&oRest Version: &5" + JDAInfo.DISCORD_REST_VERSION);
            Utils.sendMessage(sender, " ");
            Utils.sendMessage(sender, "&7&oYou may reload the config using &6/dpb reload");
            Utils.sendMessage(sender, "&7==========================================================");
            return true;
        }
        if (!sender.hasPermission("dpb.reload")) {
            Utils.sendMessage(sender, "&cNo permission!");
            return true;
        }
        String sub = args[0];
        if (sub.equalsIgnoreCase("reload")) {
            FileUtils.config = YamlConfiguration.loadConfiguration(FileUtils.getConfigFile());
            Utils.sendMessage(sender, "&aConfiguration file reloaded!");
            return true;
        }
        Utils.sendMessage(sender, "&cUse '/dpb reload' to reload the config file");
        return true;
    }

}
