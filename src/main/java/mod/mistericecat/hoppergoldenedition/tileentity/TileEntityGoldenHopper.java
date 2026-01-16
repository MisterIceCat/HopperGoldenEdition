package mod.mistericecat.hoppergoldenedition.tileentity;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;

public class TileEntityGoldenHopper extends TileEntityHopper implements ISidedInventory {

    public static final int FILTER_SLOT = 5;

    private NonNullList<ItemStack> inventory = NonNullList.withSize(6, ItemStack.EMPTY);

    private boolean matchesFilter(ItemStack stack) {
        ItemStack filter = inventory.get(FILTER_SLOT);
        if (filter.isEmpty()) return true;
        return ItemStack.areItemsEqual(stack, filter);
    }

    @Override
    public int getSizeInventory() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return inventory.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = inventory.get(index);
        if (!stack.isEmpty()) {
            ItemStack result;
            if (stack.getCount() <= count) {
                result = stack;
                inventory.set(index, ItemStack.EMPTY);
            } else {
                result = stack.splitStack(count);
                if (stack.getCount() == 0) inventory.set(index, ItemStack.EMPTY);
            }
            markDirty();
            return result;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack stack = inventory.get(index);
        inventory.set(index, ItemStack.EMPTY);
        markDirty();
        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        inventory.set(index, stack);
        markDirty();
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == FILTER_SLOT) return true;
        return matchesFilter(stack);
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
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        ItemStackHelper.saveAllItems(compound, inventory);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        inventory = NonNullList.withSize(6, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, inventory);
    }
}
