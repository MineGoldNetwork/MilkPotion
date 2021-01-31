package es.minegold.milkpotion;

import org.bukkit.ChatColor;

public enum ChatConstant {

    COMMAND_ERROR(MilkPotion.get().getConfig().getString("commands.COMMAND_ERROR")),
    COMMAND_NO_PERMISSION(MilkPotion.get().getConfig().getString("commands.COMMAND_NO_PERMISSION")),
    COMMAND_NUMBER(MilkPotion.get().getConfig().getString("commands.COMMAND_NUMBER")),
    NO_PLAYER(MilkPotion.get().getConfig().getString("commands.NO_PLAYER")),
    NO_CONSOLE(MilkPotion.get().getConfig().getString("commands.NO_CONSOLE")),

    PREFIX("");

    private String message;

    ChatConstant(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getFormattedMessage(ChatColor color) {
        return PREFIX.getMessage() + color + message;
    }
}
