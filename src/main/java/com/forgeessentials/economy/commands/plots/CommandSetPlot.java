package com.forgeessentials.economy.commands.plots;

import com.forgeessentials.util.OutputHandler;
import com.forgeessentials.util.selections.SelectionHandler;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.permissions.PermissionsManager;

import com.forgeessentials.api.APIRegistry;
import com.forgeessentials.api.permissions.AreaZone;
import com.forgeessentials.api.permissions.Zone;
import com.forgeessentials.core.commands.ForgeEssentialsCommandBase;
import com.forgeessentials.economy.ModuleEconomy;
import com.forgeessentials.economy.plots.PlotManager;
import com.forgeessentials.util.PlayerInfo;
import com.forgeessentials.util.UserIdent;
import com.forgeessentials.util.events.EventCancelledException;
import com.forgeessentials.util.events.PlotEvent;
import net.minecraftforge.permissions.PermissionsManager.RegisteredPermValue;

public class CommandSetPlot extends ForgeEssentialsCommandBase
{

    @Override
    public void processCommandPlayer(EntityPlayerMP player, String[] args)
    {
        try
        {
            int price;

            if (args[1] != null)
            {
                price = Integer.parseInt(args[1]); // checks if it's a valid number
            }
            else
            {
                price = SelectionHandler.selectionProvider.getSelection(player).getXLength() * SelectionHandler.selectionProvider.getSelection(player).getZLength() * ModuleEconomy.psfPrice;
            }

            if (!PermissionsManager.checkPermission(player, getPermissionNode() + ".free"))
            {
                if (!APIRegistry.wallet.removeFromWallet(price, new UserIdent(player).getUuid()))
                {
                    throw new CommandException("You can't afford to set this plot!");
                }
            }

            AreaZone zone = new AreaZone(APIRegistry.perms.getServerZone().getWorldZone(player.worldObj), PlotManager.PLOT_NAME_ID + args[0], SelectionHandler.selectionProvider.getSelection(player));
            if (!APIRegistry.getFEEventBus().post(new PlotEvent.Define(zone, player)))
            {
                zone.setGroupPermission(Zone.GROUP_DEFAULT, PlotManager.DATA_PERM, true);
                zone.setGroupPermissionProperty(Zone.GROUP_DEFAULT, PlotManager.PLOT_OWNER, new UserIdent(player).getUuid().toString());
                zone.setHidden(true);
                zone.setGroupPermissionProperty(Zone.GROUP_DEFAULT, PlotManager.PLOT_VALUE, Integer.toString(price));
                OutputHandler.chatConfirmation(player, "Plot defined. " + ModuleEconomy.formatCurrency(price) + " has been deducted from your account.");
            }
        }
        catch (EventCancelledException e)
        {
            throw new CommandException("Something went wrong - couldn't create plot");
        }
    }

    @Override
    public boolean canConsoleUseCommand()
    {
        return false;
    }

    @Override
    public String getPermissionNode()
    {
        return "fe.economy.plots.set";
    }

    @Override
    public PermissionsManager.RegisteredPermValue getDefaultPermission()
    {
        return RegisteredPermValue.TRUE;
    }

    @Override
    public String getCommandName()
    {
        return "setplot";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_)
    {
        return "/setplot <name> [value] Set the current selection as a tradeable plot";
    }
}
