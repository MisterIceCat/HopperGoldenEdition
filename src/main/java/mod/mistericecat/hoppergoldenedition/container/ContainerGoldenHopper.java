package mod.mistericecat.hoppergoldenedition.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.Container;
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
    public boolean canInteractWith(net.minecraft.entity.player.EntityPlayer playerIn) {
        return hopperInv.isUsableByPlayer(playerIn);
    }
}
