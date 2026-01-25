package mod.mistericecat.hoppergoldenedition.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import mod.mistericecat.hoppergoldenedition.tileentity.TileEntityGoldenHopper;

public class ContainerGoldenHopper extends Container {

    private final IInventory hopperInv;

    public ContainerGoldenHopper(InventoryPlayer playerInv, IInventory hopperInv) {
        this.hopperInv = hopperInv;

        this.addSlotToContainer(new Slot(hopperInv, TileEntityGoldenHopper.FILTER_SLOT, 26, 20));

        for (int i = 0; i < 5; i++) {
            this.addSlotToContainer(new Slot(hopperInv, i, 62 + i * 18, 20));
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlotToContainer(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 51 + row * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlotToContainer(new Slot(playerInv, i, 8 + i * 18, 109));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return hopperInv.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            // Slot layout:
            // 0: Filter slot (hopperInv slot 5)
            // 1-5: Storage slots (hopperInv slots 0-4)
            // 6-32: Player main inventory (27 slots: 3 rows × 9)
            // 33-41: Player hotbar (9 slots)

            if (index < 6) {
                // Clicked slot is from hopper inventory (filter or storage)
                // Transfer to player inventory
                if (!this.mergeItemStack(itemstack1, 6, 42, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Clicked slot is from player inventory
                // Allow transfer of any item to hopper storage slots (manual placement)
                if (!this.mergeItemStack(itemstack1, 1, 6, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }
}
