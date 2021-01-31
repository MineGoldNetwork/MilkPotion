package es.minegold.milkpotion.commands;

import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.annotations.CommandPermissions;
import cl.bgmp.minecraft.util.commands.annotations.CommandScopes;
import cl.bgmp.minecraft.util.commands.exceptions.CommandException;
import es.minegold.milkpotion.MilkPotion;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class MilkPotionCommand {

    private static final String POTION_NAME = MilkPotion.get().getConfig().getString("potions.MILK_POTION_NAME");
    private static final String COLORED_POTION_NAME = translateAlternateColorCodes('&', Objects.requireNonNull(POTION_NAME));

    private static final String MESSAGE_RECEIVE = MilkPotion.get().getConfig().getString("messages.RECEIVE_POTION");
    private static final String COLORED_MESSAGE_RECEIVE = translateAlternateColorCodes('&', String.valueOf(MESSAGE_RECEIVE));

    private static final int RGB_RED = MilkPotion.get().getConfig().getInt("potions.MILK_POTION_RGB_COLOR.RED");
    private static final int RGB_GREEN = MilkPotion.get().getConfig().getInt("potions.MILK_POTION_RGB_COLOR.GREEN");
    private static final int RGB_BLUE = MilkPotion.get().getConfig().getInt("potions.MILK_POTION_RGB_COLOR.BLUE");

    @Command(aliases = {"milkpotion"}, usage = "<amount>", desc = "Give amount of MilkPotions.")
    @CommandPermissions("milkpotion.command.use")
    @CommandScopes("player")
    public static void milkpotion(final CommandContext args, final CommandSender sender) throws CommandException {

        Player p = (Player) sender;
        int amount = args.getInteger(0);
        ItemStack milkPotion = new ItemStack(Material.SPLASH_POTION, amount);
        PotionMeta meta = (PotionMeta) milkPotion.getItemMeta();
        meta.setColor(Color.fromRGB(RGB_RED,RGB_GREEN,RGB_BLUE));
        milkPotion.setItemMeta(meta);
        meta.setDisplayName(COLORED_POTION_NAME);
        List<String> potLore = MilkPotion.get().getConfig().getStringList("potions.MILK_POTION_LORE");
        meta.setLore(colorList(potLore));
        milkPotion.setItemMeta(meta);
        p.getInventory().addItem(milkPotion);
        p.sendMessage(COLORED_MESSAGE_RECEIVE);
    }

    public static List<String> colorList(List<String> input) {
        List<String> list = new ArrayList<String>();
        for (String line : input) list.add(ChatColor.translateAlternateColorCodes('&', line));
        return list;
    }
}
