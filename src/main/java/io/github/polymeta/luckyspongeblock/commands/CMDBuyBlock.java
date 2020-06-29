package io.github.polymeta.luckyspongeblock.commands;

import io.github.polymeta.luckyspongeblock.LuckySpongeBlock;
import io.github.polymeta.luckyspongeblock.configuration.GeneralConfig;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.math.BigDecimal;

public class CMDBuyBlock implements CommandExecutor
{
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
    {
        if(!(src instanceof Player))
            throw new CommandException(Text.of(TextColors.RED, "Only Players can do this!"));
        GeneralConfig.LuckyBlockConfig type = args.<GeneralConfig.LuckyBlockConfig>getOne("luckyblock").orElseThrow(() -> new CommandException(Text.of(TextColors.RED, "Couldn't find specified luckyblock!")));
        EconomyService economyService = LuckySpongeBlock.getInstance().getEcoService();

        economyService.getOrCreateAccount(((Player) src).getUniqueId()).ifPresent(uniqueAccount ->
        {
            Player player = (Player) src;
            EventContext eventContext = EventContext.builder()
                    .add(EventContextKeys.PLUGIN, LuckySpongeBlock.getInstance().getContainer())
                    .add(EventContextKeys.PLAYER, player).build();

            //attempting to withdraw monies
            TransactionResult result = uniqueAccount.withdraw(
                    economyService.getDefaultCurrency(),
                    BigDecimal.valueOf(type.luckyBlockCost),
                    Cause.of(eventContext, LuckySpongeBlock.getInstance()));
            if (result.getResult() == ResultType.SUCCESS)
            {
                // Success!
                ItemStack luckyBlock = type.luckyBlockItem.createStack();
                luckyBlock.setQuantity(1);
                if(player.getInventory().offer(luckyBlock).getType() == InventoryTransactionResult.Type.SUCCESS)
                {
                    src.sendMessage(Text.of(TextColors.GREEN, "Successfully bought " + type.name + "!"));
                }
                else{
                    src.sendMessage(Text.of(TextColors.RED, "An error occurred while giving the luckyblocks to your inventory! Was your inventory full?"));
                    //give money back in that case
                    uniqueAccount.deposit(
                            economyService.getDefaultCurrency(),
                            BigDecimal.valueOf(type.luckyBlockCost),
                            Cause.of(eventContext, LuckySpongeBlock.getInstance()));
                }
            }
            else if (result.getResult() == ResultType.FAILED || result.getResult() == ResultType.ACCOUNT_NO_FUNDS)
            {
                // Something went wrong!
                player.sendMessage(Text.of(TextColors.RED, "An error occurred taking the money from you! Did you have enough funds?"));
            }
            else {
                // Handle other conditions
                player.sendMessage(Text.of(TextColors.RED, "An error occurred taking the money from you!"));
            }
        });

        return CommandResult.success();
    }
}