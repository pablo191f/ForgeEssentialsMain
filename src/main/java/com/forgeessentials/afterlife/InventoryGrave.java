package com.forgeessentials.afterlife;

import com.forgeessentials.util.UserIdent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventoryGrave extends InventoryBasic {
    private Grave grave;

    public InventoryGrave(Grave grave)
    {
        super(new UserIdent(grave.owner).getUsername() + "'s grave.", false, grave.getSize());
        this.grave = grave;
    }

    @Override
    public void openInventory()
    {
        for (int i = 0; i < getSizeInventory(); i++)
        {
            setInventorySlotContents(i, (ItemStack) null);
        }

        for (int i = 0; i < grave.inv.length; i++)
        {
            if (grave.inv[i] != null)
            {
                setInventorySlotContents(i, grave.inv[i].copy());
            }
        }

        super.openInventory();
    }

    @Override
    public void closeInventory()
    {
        List<ItemStack> list = new ArrayList<ItemStack>();
        for (int i = 0; i < getSizeInventory(); i++)
        {
            ItemStack is = getStackInSlot(i);
            if (is != null)
            {
                list.add(is);
            }
        }
        grave.inv = list.toArray(new ItemStack[list.size()]);

        grave.checkGrave();
        grave.setOpen(false);
        super.closeInventory();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return grave.canOpen(player);
    }
}
