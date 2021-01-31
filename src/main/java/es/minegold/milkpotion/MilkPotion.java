package es.minegold.milkpotion;

import cl.bgmp.bukkit.util.BukkitCommandsManager;
import cl.bgmp.bukkit.util.CommandsManagerRegistration;
import cl.bgmp.minecraft.util.commands.CommandsManager;
import cl.bgmp.minecraft.util.commands.annotations.TabCompletion;
import cl.bgmp.minecraft.util.commands.exceptions.CommandException;
import cl.bgmp.minecraft.util.commands.exceptions.CommandPermissionsException;
import cl.bgmp.minecraft.util.commands.exceptions.CommandUsageException;
import cl.bgmp.minecraft.util.commands.exceptions.MissingNestedCommandException;
import cl.bgmp.minecraft.util.commands.exceptions.ScopeMismatchException;
import cl.bgmp.minecraft.util.commands.exceptions.WrappedCommandException;
import es.minegold.milkpotion.commands.MilkPotionCommand;
import es.minegold.milkpotion.commands.ReloadConfigCommand;
import es.minegold.milkpotion.listeners.MilkPotionListener;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public final class MilkPotion extends JavaPlugin {

    public static MilkPotion MPot;
    @SuppressWarnings("rawtypes")
    private CommandsManager commandsManager;
    private CommandsManagerRegistration defaultRegistration;


    public static MilkPotion get() {
        return MPot;
    }

    @Override
    public void onEnable() {
        MPot = this;

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        commandsManager = new BukkitCommandsManager();
        defaultRegistration = new CommandsManagerRegistration(this, commandsManager);
        registerCommands(
                MilkPotionCommand.class,
                ReloadConfigCommand.class
        );

        getServer().getPluginManager().registerEvents(new MilkPotionListener(), this);
    }

    @Override
    public void onDisable() { }


    @SuppressWarnings("unchecked")
    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, @NotNull String[] args) {
        try {
            this.commandsManager.execute(command.getName(), args, sender, sender);
        } catch (ScopeMismatchException exception) {
            String[] scopes = exception.getScopes();
            if (!Arrays.asList(scopes).contains("player")) {
                sender.sendMessage(ChatConstant.NO_PLAYER.getFormattedMessage(ChatColor.RED));
            } else {
                sender.sendMessage(ChatConstant.NO_CONSOLE.getFormattedMessage(ChatColor.RED));
            }
        } catch (CommandPermissionsException exception) {
            sender.sendMessage(ChatConstant.COMMAND_NO_PERMISSION.getFormattedMessage(ChatColor.RED));
        } catch (MissingNestedCommandException exception) {
            sender.sendMessage(ChatColor.YELLOW + "⚠ " + ChatColor.RED + exception.getUsage());
        } catch (CommandUsageException exception) {
            sender.sendMessage(ChatColor.RED + exception.getMessage());
            sender.sendMessage(ChatColor.RED + exception.getUsage());
        } catch (WrappedCommandException exception) {
            if (exception.getCause() instanceof NumberFormatException) {
                sender.sendMessage(ChatConstant.COMMAND_NUMBER.getFormattedMessage(ChatColor.RED));
            } else {
                sender.sendMessage(ChatConstant.COMMAND_ERROR.getFormattedMessage(ChatColor.RED));
                exception.printStackTrace();
            }
        } catch (CommandException exception) {
            sender.sendMessage(ChatColor.RED + exception.getMessage());
        }
        return true;
    }

    public void registerCommands(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            final Class<?>[] subclasses = clazz.getClasses();

            if (subclasses.length == 0) defaultRegistration.register(clazz);
            else {
                TabCompleter tabCompleter = null;
                Class<?> nestNode = null;
                for (Class<?> subclass : subclasses) {
                    if (subclass.isAnnotationPresent(TabCompletion.class)) {
                        try {
                            tabCompleter = (TabCompleter) subclass.newInstance();
                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    } else nestNode = subclass;
                }

                if (tabCompleter == null) defaultRegistration.register(subclasses[0]);
                else {
                    CommandsManagerRegistration customRegistration = new CommandsManagerRegistration(this, this, tabCompleter, commandsManager);
                    if (subclasses.length == 1) customRegistration.register(clazz);
                    else customRegistration.register(nestNode);
                }
            }
        }
    }
}
