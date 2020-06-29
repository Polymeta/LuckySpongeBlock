package io.github.polymeta.luckyspongeblock;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;

@Plugin(
        id = "luckyspongeblock",
        name = "LuckySpongeBlock",
        description = "Give out LuckyBlocks for players containing pre configured items",
        authors = {
                "Polymeta"
        }
)
public class LuckySpongeBlock {

    @Inject
    private Logger logger;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
    }
}
