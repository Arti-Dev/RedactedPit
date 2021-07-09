package com.articreep.redactedpit.content;

import com.articreep.redactedpit.Main;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ContentExpansion extends PlaceholderExpansion {
    private final Main plugin;
    public ContentExpansion(Main plugin) {
        this.plugin = plugin;
    }
    /**
     * Because this is an internal class,
     * you must override this method to let PlaceholderAPI know to not unregister your expansion class when
     * PlaceholderAPI is reloaded
     *
     * @return true to persist through reloads
     */
    @Override
    public boolean persist(){
        return true;
    }

    /**
     * Because this is a internal class, this check is not needed
     * and we can simply return {@code true}
     *
     * @return Always true since it's an internal class.
     */
    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "redacted";
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    /**
     * This is the method called when a placeholder with our identifier
     * is found and needs a value.
     * <br>We specify the value identifier in this method.
     * <br>Since version 2.9.1 can you use OfflinePlayers in your requests.
     *
     * @param  player
     *         A bukkit player
     * @param  identifier
     *         A String containing the identifier/value.
     *
     * @return possibly-null String of the requested identifier.
     */
    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier){

        if(player == null){
            return "ERROR";
        }

        // %someplugin_placeholder1%
        if(identifier.equals("percent_content")){
            //TODO PLACEHOLDER FOR PLACEHOLDER LMAO
            if (ContentListeners.getRedactedPlayer(player) == null) {
                return "0.00%";
            }
            return ContentListeners.getRedactedPlayer(player).getPercentContent() + "%";
        }
        if (identifier.equals("frac_content")) {
            if (ContentListeners.getRedactedPlayer(player) == null) {
                return "0/0";
            }
            return ContentListeners.getRedactedPlayer(player).getFracContent();
        }
        if (identifier.equals("gold")) {
            if (ContentListeners.getRedactedPlayer(player) == null) {
                return "0.0";
            }
            return ContentListeners.getRedactedPlayer(player).getGold() + "g";
        }

        // We return null if an invalid placeholder (f.e. %someplugin_placeholder3%)
        // was provided
        return null;
    }
}

