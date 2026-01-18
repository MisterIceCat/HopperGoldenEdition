package mod.mistericecat.hoppergoldenedition.tileentity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

import mod.mistericecat.hoppergoldenedition.container.ContainerGoldenHopper;

public class TileEntityGoldenHopper extends TileEntityLockable
        implements ISidedInventory, ITickable {

    public static final int FILTER_SLOT = 5;
    private static final int[] STORAGE_SLOTS = new int[]{0, 1, 2, 3, 4};

    private NonNullList<ItemStack> inventory = NonNullList.withSize(6, ItemStack.EMPTY);
    private int transferCooldown = 0;

    @Override
    public void update() {
        if (!world.isRemote) {
            if (transferCooldown > 0) {
                transferCooldown--;
            }

            if (transferCooldown <= 0) {
                boolean moved = false;

                if (!isEmpty()) {
                    moved = pushItemsDown();
                }

                if (!isFull()) {
                    moved |= pullItems();
                }

                if (moved) {
                    transferCooldown = 8;
                    markDirty();
                }
            }
        }
    }

    private boolean matchesFilter(ItemStack stack) {
        ItemStack filter = inventory.get(FILTER_SLOT);
        if (filter.isEmpty()) return true;
        return ItemStack.areItemsEqual(filter, stack);
    }

    private boolean pushItemsDown() {
        TileEntity te = world.getTileEntity(pos.down());
        if (!(te instanceof IInventory)) return false;

        IInventory target = (IInventory) te;

        for (int i = 0; i < 5; i++) {
            ItemStack stack = inventory.get(i);
            if (!stack.isEmpty()) {
                ItemStack one = stack.splitStack(1);

                if (insertIntoInventory(target, one)) {
                    if (stack.isEmpty()) {
                        inventory.set(i, ItemStack.EMPTY);
                    }
                    return true;
                }

                stack.grow(1);
            }
        }
        return false;
    }

    private boolean pullItems() {
        TileEntity te = world.getTileEntity(pos.up());
        if (te instanceof IInventory) {
            return pullFromInventory((IInventory) te);
        }

        List<EntityItem> items = world.getEntitiesWithinAABB(
                EntityItem.class,
                new AxisAlignedBB(pos.up())
        );

        for (EntityItem entity : items) {
            ItemStack stack = entity.getItem();
            if (!matchesFilter(stack)) continue;

            ItemStack copy = stack.copy();
            if (insertIntoInventory(this, copy)) {
                entity.setDead();
                return true;
            }
        }
        return false;
    }

    private boolean pullFromInventory(IInventory inv) {
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty() && matchesFilter(stack)) {
                ItemStack one = stack.splitStack(1);

                if (insertIntoInventory(this, one)) {
                    inv.markDirty();
                    return true;
                }

                stack.grow(1);
            }
        }
        return false;
    }

    private boolean insertIntoInventory(IInventory inv, ItemStack stack) {
        for (int i = 0; i < 5; i++) {
            ItemStack slot = inv.getStackInSlot(i);

            if (slot.isEmpty()) {
                inv.setInventorySlotContents(i, stack);
                return true;
            }

            if (ItemStack.areItemsEqual(slot, stack)
                    && slot.getCount() < slot.getMaxStackSize()) {
                slot.grow(1);
                return true;
            }
        }
        return false;
    }

    @Override
    public int getSizeInventory() {
        return 6;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < 5; i++) {
            if (!inventory.get(i).isEmpty()) return false;
        }
        return true;
    }

    private boolean isFull() {
        for (int i = 0; i < 5; i++) {
            ItemStack s = inventory.get(i);
            if (s.isEmpty() || s.getCount() < s.getMaxStackSize()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return inventory.get(index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        inventory.set(index, stack);
        markDirty();
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return STORAGE_SLOTS;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction) {
        return index < 5 && matchesFilter(stack);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return index < 5;
    }

    @Override
    public String getName() {
        return "container.golden_hopper";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return world.getTileEntity(pos) == this &&
                player.getDistanceSq(
                        pos.getX() + 0.5D,
                        pos.getY() + 0.5D,
                        pos.getZ() + 0.5D
                ) <= 64.0D;
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerGoldenHopper(playerInventory, this);
    }

    @Override
    public String getGuiID() {
        return "hoppergoldenedition:golden_hopper";
    }
}
