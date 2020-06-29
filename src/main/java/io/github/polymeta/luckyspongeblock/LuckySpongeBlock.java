package io.github.polymeta.luckyspongeblock;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import io.github.polymeta.luckyspongeblock.commands.CMDBuyBlock;
import io.github.polymeta.luckyspongeblock.commands.CMDGiveBlock;
import io.github.polymeta.luckyspongeblock.commands.CMDReload;
import io.github.polymeta.luckyspongeblock.configuration.GeneralConfig;
import io.github.polymeta.luckyspongeblock.listener.SpongeHandler;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.text.Text;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Plugin(id = "luckyspongeblock",
        name = "LuckySpongeBlock",
        description = "Give out LuckyBlocks for players containing pre configured items",
        authors = {"Polymeta"},
        version = "1.0")
public class LuckySpongeBlock
{
    private static LuckySpongeBlock instance;

    @Inject
    private PluginContainer container;

    @Inject
    private Logger logger;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private ConfigurationLoader<CommentedConfigurationNode> configLoader;
    private CommentedConfigurationNode node;
    private GeneralConfig config;


    private EconomyService ecoService;


    @Listener
    public void onServerStart(GameInitializationEvent event)
    {
        logger.info("LuckySpongeBlock by Polymeta starting!");
        instance = this;
        this.ecoService = Sponge.getServiceManager().provide(EconomyService.class).orElse(null);
        try
        {
            loadConfig();
            //register listener
            Sponge.getEventManager().registerListeners(this, new SpongeHandler());

            this.registerCommands();
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
            //remove all mappings
            Sponge.getCommandManager().getOwnedBy(this).forEach(Sponge.getCommandManager()::removeMapping);
            //re-register them again
            this.registerCommands();
        }
        catch (IOException | ObjectMappingException e)
        {
            logger.error("Something went wrong during startup! The plugin will not work properly");
            e.printStackTrace();
        }
    }

    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    public void registerCommands()
    {
        HashMap<String, GeneralConfig.LuckyBlockConfig> choices = new HashMap<>();
        config.luckyBlocks.forEach(luckyBlockConfig ->
                choices.put(luckyBlockConfig.name, luckyBlockConfig));

        HashMap<List<String>, CommandSpec> children = new HashMap<>();

        CommandSpec reloadSpec = CommandSpec.builder()
                .executor(new CMDReload())
                .permission("luckyspongeblock.reload")
                .build();
        children.put(Arrays.asList("reload"), reloadSpec);

        CommandSpec giveSpec = CommandSpec.builder()
                .executor(new CMDGiveBlock())
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.playerOrSource(Text.of("player"))),
                        GenericArguments.choicesInsensitive(Text.of("luckyblock"), choices),
                        GenericArguments.optionalWeak(GenericArguments.integer(Text.of("amount")))
                )
                .permission("luckyspongeblock.give")
                .build();
        children.put(Arrays.asList("give"), giveSpec);

        if(ecoService != null)
        {
            CommandSpec buySpec = CommandSpec.builder()
                    .executor(new CMDBuyBlock())
                    .arguments(GenericArguments.choicesInsensitive(Text.of("luckyblock"), choices))
                    .permission("luckyspongeblock.buy")
                    .build();
            children.put(Arrays.asList("buy"), buySpec);
        }
        else{
            this.logger.warn("Economy service missing, buy command not enabled.");
        }

        CommandSpec base = CommandSpec.builder()
                .children(children)
                .build();

        Sponge.getCommandManager().register(this, base, "luckyspongeblock", "luckyblock", "lsb");
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

    public EconomyService getEcoService()
    {
        return this.ecoService;
    }

    public PluginContainer getContainer() {
        return this.container;
    }
}
