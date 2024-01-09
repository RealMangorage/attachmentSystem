package org.mangorage.paperdev.core.attachment;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static org.mangorage.paperdev.core.Utils.getSafeUUID;

public class AttachmentCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("paperDev.command.attachment.use")) {
            player.sendMessage(Component.text("Lacking paperDev.command.attachment.use permission").color(NamedTextColor.RED));
            return true;
        }

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
                } else if (args.length == 3) {
                    detachAttachment(player, args[1], args[2]);
                } else {
                    player.sendMessage(ChatColor.RED + "Usage: " + ChatColor.WHITE + "/attachment detach entityUUID [attachmentID]");
                }
                break;
            case "detachall":
                if (args.length == 1) {
                    // Display parameters for detach subcommand
                    player.sendMessage(ChatColor.GREEN + "Usage: " + ChatColor.WHITE + "/attachment detachAll entityUUID");
                } else if (args.length == 2) {
                    detachAllAttachments(player, args[1]);
                } else {
                    player.sendMessage(ChatColor.RED + "Usage: " + ChatColor.WHITE + "/attachment detachAll entityUUID");
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
            Entity entity = null;
            if (args.length > 1) {
                entity = player.getTargetEntity(2);
            }


            if (args.length == 1) {
                completions.add("attach");
                completions.add("detach");
                completions.add("spawn");
                completions.add("detachAll");
            } else if (args.length == 2) {
                switch (args[0].toLowerCase()) {
                    case "attach", "detach", "detachall":
                        if (entity != null)
                            completions.add(entity.getUniqueId().toString());
                        break;
                    case "spawn":
                        completions.addAll(AttachmentSystem.getAttachmentIDs(true));
                        break;
                }
            } else if (args.length == 3) {
                switch (args[0].toLowerCase()) {
                    case "attach", "detach":
                        completions.addAll(AttachmentSystem.getAttachmentIDs(false));
                        break;
                }
            }
        }

        return completions;
    }


    @SuppressWarnings("unchecked")
    private void attachAttachment(Player player, String entityUUID, String attachmentID) {
        var uuid = getSafeUUID(entityUUID);
        if (uuid != null) {
            var entity = Bukkit.getEntity(uuid);
            if (entity != null) {
                var ro = AttachmentSystem.findAttachment(attachmentID);
                if (ro != null) {
                    if (ro.getClassType().isAssignableFrom(entity.getClass())) {
                        ro.createCast(entity);
                        player.sendMessage(ChatColor.GREEN + "Attached attachment with ID " + attachmentID + " to entity with UUID " + entityUUID);
                    } else {
                        player.sendMessage(ChatColor.RED + "Unable to attach attachment %s to entity, does not support Entity Type %s, only supports %s".formatted(attachmentID, entity.getClass().getSimpleName(), ro.getClassType().getSimpleName()));
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Attachment %s does not exist".formatted(attachmentID));
                }
            } else {
                player.sendMessage(ChatColor.RED + "Entity with UUID of %s does not exist!".formatted(entityUUID));
            }
        } else {
            player.sendMessage(ChatColor.RED + "Invalid UUID " + entityUUID);
        }
    }

    private void detachAttachment(Player player, String entityUUID, String attachmentID) {
        // Custom logic for detaching attachment from entity
        var uuid = getSafeUUID(entityUUID);
        if (uuid != null) {
            var entity = Bukkit.getEntity(uuid);
            if (entity != null) {
                AttachmentSystem.detachStatic(entity, NamespacedKey.fromString(attachmentID), DetachReason.REMOVED);
                player.sendMessage(ChatColor.GREEN + "Detached attachment" + (attachmentID != null ? " with ID " + attachmentID : "") + " from entity with UUID " + entityUUID);
            } else {
                player.sendMessage(ChatColor.RED + "Entity with UUID of %s does not exist!".formatted(entityUUID));
            }
        } else {
            player.sendMessage(ChatColor.RED + "Invalid UUID " + entityUUID);
        }
    }

    private void detachAllAttachments(Player player, String entityUUID) {
        // Custom logic for detaching all attachments from entity
        var uuid = getSafeUUID(entityUUID);
        if (uuid != null) {
            var entity = Bukkit.getEntity(uuid);
            if (entity != null) {
                AttachmentSystem.detachAllStatic(entity, DetachReason.REMOVED);
                player.sendMessage(ChatColor.GREEN + "Detached all attachments from entity with UUID " + entityUUID);
            } else {
                player.sendMessage(ChatColor.RED + "Entity with UUID of %s does not exist!".formatted(entityUUID));
            }
        } else {
            player.sendMessage(ChatColor.RED + "Invalid UUID " + entityUUID);
        }
    }

    private void spawnAttachment(Player player, String attachmentID) {
        // Custom logic for spawning attachment
        var registry = AttachmentSystem.findAttachment(attachmentID);
        if (registry != null) {
            if (registry.canSpawn()) {
                registry.spawn(player.getLocation());
                player.sendMessage(ChatColor.GREEN + "Spawned attachment with ID " + attachmentID);
            } else {
                player.sendMessage(ChatColor.RED + "Unable to spawn attachment, not possible to spawn attachment ID " + attachmentID);
            }
        } else {
            player.sendMessage(ChatColor.RED + "Unable to spawn attachment with ID doesn't exist " + attachmentID);
        }
    }
}
