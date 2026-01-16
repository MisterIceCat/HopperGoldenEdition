package mod.mistericecat.hoppergoldenedition.tileentity;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;

public class TileEntityGoldenHopper extends TileEntityHopper implements ISidedInventory {

    public static final int FILTER_SLOT = 5;

    private boolean matchesFilter(ItemStack stack) {
        ItemStack filter = this.getStackInSlot(FILTER_SLOT);
        if (filter.isEmpty()) return true;
        return ItemStack.areItemsEqual(stack, filter);
    }

    @Override
    public int getSizeInventory() {
        return 6;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[] {0, 1, 2, 3, 4};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction) {
        if (index == FILTER_SLOT) return false;
        return matchesFilter(stack);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return index != FILTER_SLOT;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == FILTER_SLOT) return true;
        return matchesFilter(stack);
    }
}
