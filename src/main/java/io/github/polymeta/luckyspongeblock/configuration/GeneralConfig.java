package io.github.polymeta.luckyspongeblock.configuration;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.item.EnchantmentData;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ConfigSerializable
public class GeneralConfig
{
    @Setting(comment = "If eco Plugin installed, players have the chance to buy luckyblocks for below price")
    public int luckyBlockCost = 100;
    @Setting(comment = "A collection of Lucky Blocks and what kind of reward those will issue")
    public List<LuckyBlockConfig> luckyBlocks = Collections.singletonList(new LuckyBlockConfig());

    @ConfigSerializable
    public static class LuckyBlockConfig
    {
        @Setting(comment = "this node contains info about the appearance of the lucky block, what block it is, lore, name, etc")
        public static ItemStackSnapshot luckyBlockItem = ItemStack.builder()
                .itemType(ItemTypes.SPONGE)
                .add(Keys.DISPLAY_NAME, Text.of(TextColors.RED, "Lucky Block!"))
                .add(Keys.ITEM_LORE, Arrays.asList(Text.of(TextColors.GREEN, "place me down to get something!"), Text.of("Lore Line 2")))
                .add(Keys.HIDE_MISCELLANEOUS, true)
                .add(Keys.HIDE_ENCHANTMENTS, true)
                .add(Keys.ITEM_ENCHANTMENTS, Collections.singletonList(Enchantment.builder().type(EnchantmentTypes.UNBREAKING).level(1).build()))
                .build().createSnapshot();

        @Setting(comment = "The commands to be executed (from console!!) when a player places down a lucky block, %p is the placeholder for the player that placed the block")
        public static List<String> commands = Arrays.asList("say Hello %p", "give %p minecraft:diamon 1");
    }
}
