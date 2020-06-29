package io.github.polymeta.luckyspongeblock.commands;

import io.github.polymeta.luckyspongeblock.configuration.GeneralConfig;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class CMDGiveBlock implements CommandExecutor
{
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
    {
        Player player = args.<Player>getOne("player").orElseThrow(() -> new CommandException(Text.of(TextColors.RED, "Couldn't find specified player!")));
        GeneralConfig.LuckyBlockConfig type = args.<GeneralConfig.LuckyBlockConfig>getOne("luckyblock").orElseThrow(() -> new CommandException(Text.of(TextColors.RED, "Couldn't find specified luckyblock!")));
        int amount = args.<Integer>getOne("amount").orElse(1);

        if(amount < 1)
            throw new CommandException(Text.of(TextColors.RED, "Amount needs to be greater 1!"));

        ItemStack luckyBlock = type.luckyBlockItem.createStack();
        luckyBlock.setQuantity(amount);

        if(player.getInventory().offer(luckyBlock).getType() == InventoryTransactionResult.Type.SUCCESS)
        {
            src.sendMessage(Text.of(TextColors.GREEN, "Successfully gave " + amount + "x " + type.name + (amount == 1 ? "" : "s") + " to " + player.getName() + "!"));
        }
        else{
            src.sendMessage(Text.of(TextColors.RED, "An error occurred while giving the luckyblocks to " + player.getName() + "! Was their inventory full?"));
        }

        return CommandResult.success();
    }
}
