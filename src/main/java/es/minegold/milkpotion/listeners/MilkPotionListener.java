package es.minegold.milkpotion.listeners;

import es.minegold.milkpotion.MilkPotion;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class MilkPotionListener implements Listener {

    public MilkPotionListener() {
        Bukkit.getPluginManager().registerEvents(this, MilkPotion.get());
    }

    private final String POTION_NAME = MilkPotion.get().getConfig().getString("potions.MILK_POTION_NAME");
    private final String COLORED_POTION_NAME = translateAlternateColorCodes('&', Objects.requireNonNull(POTION_NAME));

    private final String TARGET_REMOVED_EFFECTS = MilkPotion.get().getConfig().getString("messages.TARGET_REMOVED_EFFECTS");
    private final String TARGET_REMOVED_BAD_EFFECTS = MilkPotion.get().getConfig().getString("messages.TARGET_REMOVED_BAD_EFFECTS");
    private final String COLORED_TARGET_REMOVED_EFFECTS = translateAlternateColorCodes('&', Objects.requireNonNull(TARGET_REMOVED_EFFECTS));
    private final String COLORED_TARGET_REMOVED_BAD_EFFECTS = translateAlternateColorCodes('&', Objects.requireNonNull(TARGET_REMOVED_BAD_EFFECTS));

    @EventHandler
    public void onPlayerHitOtherPlayer(PotionSplashEvent e) {
        if (e.getEntity().getItem().hasItemMeta())
        if (e.getEntity().getItem().getItemMeta().getDisplayName().equalsIgnoreCase(COLORED_POTION_NAME)) {
            e.setCancelled(true);
            for (LivingEntity en : e.getAffectedEntities()) {
                for (PotionEffect effect : en.getActivePotionEffects())
                    if (MilkPotion.get().getConfig().getBoolean("options.remove-only-debuff")) {
                        en.removePotionEffect(PotionEffectType.BAD_OMEN);
                        en.removePotionEffect(PotionEffectType.BLINDNESS);
                        en.removePotionEffect(PotionEffectType.CONFUSION);
                        en.removePotionEffect(PotionEffectType.HARM);
                        en.removePotionEffect(PotionEffectType.HUNGER);
                        en.removePotionEffect(PotionEffectType.POISON);
                        en.removePotionEffect(PotionEffectType.SLOW);
                        en.removePotionEffect(PotionEffectType.SLOW_DIGGING);
                        en.removePotionEffect(PotionEffectType.SLOW_FALLING);
                        en.removePotionEffect(PotionEffectType.UNLUCK);
                        en.removePotionEffect(PotionEffectType.WEAKNESS);
                        en.removePotionEffect(PotionEffectType.WITHER);
                        en.sendMessage(COLORED_TARGET_REMOVED_BAD_EFFECTS);
                    } else {
                        en.removePotionEffect(effect.getType());
                        en.sendMessage(COLORED_TARGET_REMOVED_EFFECTS);
                    }
            }
        }
    }
}
