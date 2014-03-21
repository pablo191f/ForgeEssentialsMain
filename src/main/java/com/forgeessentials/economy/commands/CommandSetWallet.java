package com.forgeessentials.economy.commands;

import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

import com.forgeessentials.api.APIRegistry;
import com.forgeessentials.core.commands.ForgeEssentialsCommandBase;
import com.forgeessentials.util.ChatUtils;
import com.forgeessentials.util.Localization;
import com.forgeessentials.util.OutputHandler;

import cpw.mods.fml.common.FMLCommonHandler;

public class CommandSetWallet extends ForgeEssentialsCommandBase
{
	@Override
	public String getCommandName()
	{
		return "setwallet";
	}

	@Override
	public void processCommandPlayer(EntityPlayer sender, String[] args)
	{
		if (args.length == 2)
		{
			EntityPlayer player = FMLCommonHandler.instance().getSidedDelegate().getServer().getConfigurationManager().getPlayerForUsername(args[0]);
			int amountToSet = Integer.parseInt(args[1]);

			if (player == null)
			{
				OutputHandler.chatError(sender, Localization.get(Localization.ERROR_NOPLAYER));
			}
			else
			{
				APIRegistry.wallet.setWallet(amountToSet, player);

				if (sender != player)
				{
					ChatUtils.sendMessage(sender, Localization.get(Localization.wallet_SET_TARGET) + APIRegistry.wallet.getMoneyString(player.username));
				}
				ChatUtils.sendMessage(player, Localization.get(Localization.wallet_SET_SELF) + APIRegistry.wallet.getMoneyString(player.username));
			}
		}
		else
		{
			OutputHandler.chatError(sender, Localization.get(Localization.ERROR_BADSYNTAX) + getSyntaxPlayer(sender));
		}

	}

	@Override
	public void processCommandConsole(ICommandSender sender, String[] args)
	{
		if (args.length == 2)
		{
			EntityPlayer player = FMLCommonHandler.instance().getSidedDelegate().getServer().getConfigurationManager().getPlayerForUsername(args[0]);
			int amountToSet = Integer.parseInt(args[1]);

			if (player == null)
			{
				ChatUtils.sendMessage(sender, Localization.get(Localization.ERROR_NOPLAYER));
			}
			else
			{
				APIRegistry.wallet.setWallet(amountToSet, player);

				ChatUtils.sendMessage(sender, Localization.get(Localization.wallet_SET_TARGET) + APIRegistry.wallet.getMoneyString(player.username));
				ChatUtils.sendMessage(player, Localization.get(Localization.wallet_SET_SELF) + APIRegistry.wallet.getMoneyString(player.username));
			}
		}
		else
		{
			ChatUtils.sendMessage(sender, Localization.get(Localization.ERROR_BADSYNTAX) + getSyntaxConsole());
		}
	}

	@Override
	public boolean canConsoleUseCommand()
	{
		return true;
	}

	@Override
	public String getCommandPerm()
	{
		return "ForgeEssentials.Economy." + getCommandName();
	}

	@Override
	public List<?> addTabCompletionOptions(ICommandSender sender, String[] args)
	{
		if (args.length == 1)
			return getListOfStringsMatchingLastWord(args, FMLCommonHandler.instance().getMinecraftServerInstance().getAllUsernames());
		else
			return null;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

}