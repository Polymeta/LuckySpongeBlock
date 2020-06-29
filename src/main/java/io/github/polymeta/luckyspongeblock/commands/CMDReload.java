package io.github.polymeta.luckyspongeblock.commands;

import io.github.polymeta.luckyspongeblock.LuckySpongeBlock;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.io.IOException;

public class CMDReload implements CommandExecutor
{
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
    {
        try
        {
            LuckySpongeBlock.getInstance().loadConfig();
        }
        catch (IOException | ObjectMappingException e)
        {
            e.printStackTrace();
            throw new CommandException(Text.of(TextColors.RED, "Something went wrong loading the config! Check the console for details"));
        }
        return CommandResult.success();
    }
}
