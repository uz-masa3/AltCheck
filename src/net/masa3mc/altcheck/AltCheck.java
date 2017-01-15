package net.masa3mc.altcheck;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AltCheck extends JavaPlugin {

  public static AltCheck instance;
  public static String prefix = new Util().color("&c[&7AltCheck&c]&r ");

  public void onEnable() {
    instance = this;
    saveDefaultConfig();
    if (getConfig().getInt("configVersion") != 3) {
      getLogger().warning("Outdated configuration file! Please delete old config.yml and restart!");
    }
    Bukkit.getPluginManager().registerEvents(new Listeners(), this);
  }

  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    if (cmd.getName().equalsIgnoreCase("altcheck")) {
      Util u = new Util();
      if (args.length == 0) {
        sender.sendMessage(u.color("&7=============================="));
        sender.sendMessage(u.color("&e AltCheck v" + u.getVersion() + " by uz_masa3"));
        sender.sendMessage(u.color("&7 - /altcheck check [IP / Player(Online)]"));
        sender.sendMessage(u.color("&7 - /altcheck reload"));
        sender.sendMessage(u.color("&7 - /altcheck kickmessage [Message]"));
        sender.sendMessage(u.color("&7 - /altcheck maxalt [max]"));
        sender.sendMessage(u.color("&7 - /altcheck ignore [UUID]"));
      } else if (args.length != 0) {
        if (args[0].equalsIgnoreCase("check")) {
          if (sender.hasPermission("AltCheck.admin")) {
            if (args.length == 2) {
              u.checkLog(sender.getName(), args[1]);
              if (Bukkit.getPlayerExact(args[1]) == null) {
                String ip = args[1].replace('.', '_');
                if (u.check(ip) == null) {
                  sender.sendMessage(AltCheck.prefix + u.color("&7Data not found."));
                } else {
                  sender.sendMessage(u.color("&7============&c" + args[1] + "&7============"));

                  Thread t = new Thread() {
                    public void run() {
                      for (String list : u.check(ip)) {
                        String opn = Bukkit.getOfflinePlayer(UUID.fromString(list)).getName();
                        if (opn == null) {
                          sender.sendMessage(u.color("&c" + u.utn(list.replace("-", "")) + "&7(" + list + ")"));

                        } else {
                          sender.sendMessage(u.color("&c" + opn + "&7(" + list + ")"));
                        }
                      }
                    }
                  };
                  t.start();

                }
              } else {
                Player p = Bukkit.getPlayer(args[1]);
                String getIP = u.getPlayerIP(p);
                String ip = getIP.replace('.', '_');
                if (u.check(ip) == null) {
                  sender.sendMessage(AltCheck.prefix + u.color("&7Data not found."));
                } else {
                  sender.sendMessage(u.color("&7============&c" + getIP + "&7============"));
                  Thread t = new Thread() {
                    public void run() {
                      for (String list : u.check(ip)) {
                        String opn = Bukkit.getOfflinePlayer(UUID.fromString(list)).getName();
                        if (opn == null) {
                          sender.sendMessage(u.color("&c" + u.utn(list.replace("-", "")) + "&7(" + list + ")"));
                        } else {
                          sender.sendMessage(u.color("&c" + opn + "&7(" + list + ")"));
                        }

                      }
                    }
                  };
                  t.start();

                }
              }
            } else {
              sender.sendMessage(AltCheck.prefix + u.color("&7/altcheck check [IP / Player(Online)]"));
            }
          } else {
            sender.sendMessage(AltCheck.prefix + u.color("&cYou don't have permission."));
          }
        } else if (args[0].equalsIgnoreCase("reload")) {
          if (sender.hasPermission("AltCheck.admin")) {
            instance.reloadConfig();
            sender.sendMessage(AltCheck.prefix + u.color("&7Reload complete."));
          } else {
            sender.sendMessage(AltCheck.prefix + u.color("&cYou don't have permission."));
          }
        } else if (args[0].equalsIgnoreCase("kickmessage")) {
          if (sender.hasPermission("AltCheck.admin")) {
            if (args.length > 1) {
              StringBuilder message = new StringBuilder();
              for (int i = 1; i < args.length; i++) {
                message.append(args[i]).append(" ");
              }
              instance.getConfig().set("kickMessage", message.toString().trim());
              instance.saveConfig();
              instance.reloadConfig();
              sender.sendMessage(AltCheck.prefix + u.color("&aSuccessfully save settings."));
            } else {
              sender.sendMessage(u.color("&7/altcheck kickmessage [Message]"));
            }
          } else {
            sender.sendMessage(AltCheck.prefix + u.color("&cYou don't have permission."));
          }
        } else if (args[0].equalsIgnoreCase("maxalt")) {
          if (sender.hasPermission("AltCheck.admin")) {
            if (args.length == 2) {
              try {
                int max = Integer.parseInt(args[1]);
                if (max > 0 && max < 1000) {
                  instance.getConfig().set("maxAlt", max);
                  instance.saveConfig();
                  instance.reloadConfig();
                  sender.sendMessage(AltCheck.prefix + u.color("&aSuccessfully save settings."));
                } else {
                  sender.sendMessage(u.color("&7/altcheck maxalt [1 - 999]"));
                }
              } catch (NumberFormatException e) {
                sender.sendMessage(AltCheck.prefix + u.color("&cPlease enter a number."));
              }
            } else {
              sender.sendMessage(u.color("&7/altcheck maxalt [1 - 999]"));
            }
          } else {
            sender.sendMessage(AltCheck.prefix + u.color("&cYou don't have permission."));
          }
        } else if (args[0].equalsIgnoreCase("ignore")) {
          if (sender.hasPermission("AltCheck.admin")) {
            if (args.length == 2) {
              if (args[1].length() == 36) {
                if (!instance.getConfig().getStringList("IgnoreUUID").contains(args[1])) {
                  ArrayList<String> arraylist = new ArrayList<String>();
                  arraylist.add(args[1]);
                  for (String list : instance.getConfig().getStringList("IgnoreUUID")) {
                    arraylist.add(list);
                  }
                  instance.getConfig().set("IgnoreUUID", arraylist);
                  instance.saveConfig();
                  instance.reloadConfig();
                }
                sender.sendMessage(AltCheck.prefix + u.color("&aSuccessfully save settings."));
              } else {
                sender.sendMessage(u.color("&7/altcheck ignore [UUID]"));
              }
            } else {
              sender.sendMessage(u.color("&7/altcheck ignore [UUID]"));
            }
          } else {
            sender.sendMessage(AltCheck.prefix + u.color("&cYou don't have permission."));
          }
        } else {
          sender.sendMessage(u.color("&7=============================="));
          sender.sendMessage(u.color("&e AltCheck v" + u.getVersion() + " by uz_masa3"));
          sender.sendMessage(u.color("&7 - /altcheck check [IP / Player(Online)]"));
          sender.sendMessage(u.color("&7 - /altcheck reload"));
          sender.sendMessage(u.color("&7 - /altcheck kickmessage [Message]"));
          sender.sendMessage(u.color("&7 - /altcheck maxalt [max]"));
          sender.sendMessage(u.color("&7 - /altcheck ignore [UUID]"));
        }
      }
      return true;
    }
    return false;
  }

}
