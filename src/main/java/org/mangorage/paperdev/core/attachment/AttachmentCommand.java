package org.mangorage.paperdev.core.attachment;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AttachmentCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            // Display subcommands
            player.sendMessage(ChatColor.GREEN + "Attachment Subcommands:");
            player.sendMessage(ChatColor.YELLOW + "/attachment attach entityUUID attachmentID");
            player.sendMessage(ChatColor.YELLOW + "/attachment detach entityUUID [attachmentID]");
            player.sendMessage(ChatColor.YELLOW + "/attachment spawn attachmentID");
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "attach":
                if (args.length == 1) {
                    // Display parameters for attach subcommand
                    player.sendMessage(ChatColor.GREEN + "Usage: " + ChatColor.WHITE + "/attachment attach entityUUID attachmentID");
                } else if (args.length == 3) {
                    attachAttachment(player, args[1], args[2]);
                } else {
                    player.sendMessage(ChatColor.RED + "Usage: " + ChatColor.WHITE + "/attachment attach entityUUID attachmentID");
                }
                break;

            case "detach":
                if (args.length == 1) {
                    // Display parameters for detach subcommand
                    player.sendMessage(ChatColor.GREEN + "Usage: " + ChatColor.WHITE + "/attachment detach entityUUID [attachmentID]");
                } else if (args.length == 2) {
                    detachAllAttachments(player, args[1]);
                } else if (args.length == 3) {
                    detachAttachment(player, args[1], args[2]);
                } else {
                    player.sendMessage(ChatColor.RED + "Usage: " + ChatColor.WHITE + "/attachment detach entityUUID [attachmentID]");
                }
                break;

            case "spawn":
                if (args.length == 1) {
                    // Display parameters for spawn subcommand
                    player.sendMessage(ChatColor.GREEN + "Usage: " + ChatColor.WHITE + "/attachment spawn attachmentID");
                } else if (args.length == 2) {
                    spawnAttachment(player, args[1]);
                } else {
                    player.sendMessage(ChatColor.RED + "Usage: " + ChatColor.WHITE + "/attachment spawn attachmentID");
                }
                break;

            default:
                player.sendMessage(ChatColor.RED + "Unknown sub-command. Please use /attachment to see available subcommands.");
                break;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (sender instanceof Player player) {
            if (args.length == 1) {
                completions.add("attach");
                completions.add("detach");
                completions.add("spawn");
            } else if (args.length == 2) {
                // Add logic for entityUUID tab completion
                // Example: completions.addAll(getEntityUUIDs(args[1]));
                if (args[0].equals("attach")) {
                    completions.add(args[0]);
                }
                switch (args[0]) {
                    case "attach", "detach":
                        for (Entity entity : player.getWorld().getEntities()) {
                            completions.add(entity.getUniqueId().toString());
                        }
                        break;
                    case "spawn":
                        completions.addAll(AttachmentSystem.getAttachmentIDs(true));
                        break;
                }
            } else if (args.length == 3) {
                // Add logic for attachmentID tab completion
                // Example: completions.addAll(getAttachmentIDs(args[2]));
            }
        }

        return completions;
    }

    private void attachAttachment(Player player, String entityUUID, String attachmentID) {
        // Custom logic for attaching attachment to entity
        player.sendMessage(ChatColor.GREEN + "Attached attachment with ID " + attachmentID + " to entity with UUID " + entityUUID);
    }

    private void detachAttachment(Player player, String entityUUID, String attachmentID) {
        // Custom logic for detaching attachment from entity
        player.sendMessage(ChatColor.GREEN + "Detached attachment" + (attachmentID != null ? " with ID " + attachmentID : "") + " from entity with UUID " + entityUUID);
    }

    private void detachAllAttachments(Player player, String entityUUID) {
        // Custom logic for detaching all attachments from entity
        player.sendMessage(ChatColor.GREEN + "Detached all attachments from entity with UUID " + entityUUID);
    }

    private void spawnAttachment(Player player, String attachmentID) {
        // Custom logic for spawning attachment
        player.sendMessage(ChatColor.GREEN + "Spawned attachment with ID " + attachmentID);
        var registry = AttachmentSystem.findAttachment(attachmentID);
        if (registry != null) registry.spawn(player.getLocation());
    }
}
