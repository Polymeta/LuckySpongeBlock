package io.github.polymeta.luckyspongeblock;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import io.github.polymeta.luckyspongeblock.configuration.GeneralConfig;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.data.persistence.DataTranslators;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;

import java.io.IOException;

@Plugin(id = "luckyspongeblock",
        name = "LuckySpongeBlock",
        description = "Give out LuckyBlocks for players containing pre configured items",
        authors = {"Polymeta"},
        version = "1.0-SNAPSHOT")
public class LuckySpongeBlock
{
    private static LuckySpongeBlock instance;

    @Inject
    private Logger logger;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private ConfigurationLoader<CommentedConfigurationNode> configLoader;
    private CommentedConfigurationNode node;
    private GeneralConfig config;

    @Listener
    public void onServerStart(GameStartedServerEvent event)
    {
        logger.info("LuckySpongeBlock by Polymeta starting!");
        instance = this;
        try
        {
            loadConfig();
            //TODO register listeners
            //TODO register commands
        }
        catch (IOException | ObjectMappingException e)
        {
            logger.error("Something went wrong during startup! The plugin will not work.");
            e.printStackTrace();
        }
    }

    @Listener
    public void onSpongeReload(GameReloadEvent event)
    {
        try
        {
            this.loadConfig();
        }
        catch (IOException | ObjectMappingException e)
        {
            logger.error("Something went wrong during startup! The plugin will not work properly");
            e.printStackTrace();
        }
    }

    public void loadConfig() throws IOException, ObjectMappingException
    {
        this.node = this.configLoader.load();
        @SuppressWarnings("UnstableApiUsage") TypeToken<GeneralConfig> type = TypeToken.of(GeneralConfig.class);
        this.config = node.getValue(type, new GeneralConfig());
        node.setValue(type, this.config);
        this.configLoader.save(node);
    }

    public static LuckySpongeBlock getInstance()
    {
        return instance;
    }

    public Logger getLogger()
    {
        return this.logger;
    }

    public GeneralConfig getConfig()
    {
        return this.config;
    }
}
