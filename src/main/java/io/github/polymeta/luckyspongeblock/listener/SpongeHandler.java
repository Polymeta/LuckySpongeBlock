package io.github.polymeta.luckyspongeblock.listener;

import io.github.polymeta.luckyspongeblock.LuckySpongeBlock;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackComparators;

import java.util.List;

public class SpongeHandler
{
    @Listener
    public void onInteractBlock(InteractBlockEvent.Secondary.MainHand event, @Root Player player)
    {
        player.getItemInHand(HandTypes.MAIN_HAND)
                .ifPresent(handItem ->
                        LuckySpongeBlock.getInstance().getConfig().luckyBlocks
                                .forEach(luckyBlockConfig ->
                                {
                                    ItemStack itemStack = luckyBlockConfig.luckyBlockItem.createStack();
                                    if(ItemStackComparators.IGNORE_SIZE.compare(handItem, itemStack) == 0)
                                    {
                                        //we found a luckyblock that matches the block we placed
                                        //we remove the placed block and and run the commands stored within
                                        event.setCancelled(true);
                                        this.runCommands(player, luckyBlockConfig.commands);
                                        //after commands we reduce amount of blocks in hand
                                        player.getItemInHand(HandTypes.MAIN_HAND).get().setQuantity(handItem.getQuantity() - 1);
                                    }
                                }));
    }

    @Listener
    public void onBlockPlace(InteractBlockEvent.Secondary.MainHand event, @Root Player player)
    {
        player.getItemInHand(HandTypes.MAIN_HAND)
                .ifPresent(handItem ->
                        LuckySpongeBlock.getInstance().getConfig().luckyBlocks
                                .forEach(luckyBlockConfig ->
                                {
                                    ItemStack itemStack = luckyBlockConfig.luckyBlockItem.createStack();
                                    if(ItemStackComparators.IGNORE_SIZE.compare(handItem, itemStack) == 0)
                                    {
                                        //we found a luckyblock that matches the block we placed
                                        //we remove the placed block and and run the commands stored within
                                        event.setCancelled(true);
                                        this.runCommands(player, luckyBlockConfig.commands);
                                        //after commands we reduce amount of blocks in hand
                                        player.getItemInHand(HandTypes.MAIN_HAND).get().setQuantity(handItem.getQuantity() - 1);
                                    }
                                }));
    }

    private void runCommands(Player player, List<String> commands)
    {
        commands.forEach(command ->
                Sponge.getCommandManager().process(
                        Sponge.getServer().getConsole(),    //we run all commands from console
                        command.replace("%p", player.getName())
                ));
    }
}
