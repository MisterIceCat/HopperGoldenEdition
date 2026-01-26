package mod.mistericecat.hoppergoldenedition.tileentity;

import net.minecraft.block.BlockHopper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.List;

import mod.mistericecat.hoppergoldenedition.container.ContainerGoldenHopper;

public class TileEntityGoldenHopper extends TileEntityLockableLoot
        implements ISidedInventory, ITickable {

    public static final int FILTER_SLOT = 5;
    private static final int[] STORAGE_SLOTS = new int[]{0, 1, 2, 3, 4};

    private NonNullList<ItemStack> inventory = NonNullList.withSize(6, ItemStack.EMPTY);
    private int transferCooldown = 0;

    @Override
    public void update() {
        if (!world.isRemote) {
            if (transferCooldown > 0) transferCooldown--;

            if (transferCooldown <= 0) {
                boolean moved = false;

                if (!isEmpty()) moved = pushItems();
                if (!isFull()) moved |= pullItems();

                if (moved) {
                    transferCooldown = 8;
                    markDirty();
                }
            }
        }
    }

    private boolean matchesFilter(ItemStack stack) {
        ItemStack filter = inventory.get(FILTER_SLOT);
        return filter.isEmpty() || ItemStack.areItemsEqual(filter, stack);
    }

    private boolean pushItems() {
        IBlockState state = world.getBlockState(pos);
        if (!(state.getBlock() instanceof BlockHopper)) return false;
        
        EnumFacing facing = state.getValue(BlockHopper.FACING);
        BlockPos targetPos = pos.offset(facing);
        TileEntity te = world.getTileEntity(targetPos);
        
        if (!(te instanceof IInventory)) return false;

        IInventory target = (IInventory) te;

        for (int i = 0; i < 5; i++) {
            ItemStack stack = inventory.get(i);
            if (stack.isEmpty()) continue;
            
            // Only push items that match the filter
            if (!matchesFilter(stack)) continue;

            ItemStack toMove = stack.copy();
            toMove.setCount(1);

            if (insertIntoInventory(target, toMove, facing)) {
                stack.shrink(1);
                if (stack.getCount() <= 0) {
                    inventory.set(i, ItemStack.EMPTY);
                }
                target.markDirty();
                return true;
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

            ItemStack toMove = stack.copy();
            toMove.setCount(1);

            if (insertIntoInventory(this, toMove, EnumFacing.DOWN)) {
                stack.shrink(1);
                if (stack.getCount() <= 0) entity.setDead();
                return true;
            }
        }
        return false;
    }

    private boolean pullFromInventory(IInventory inv) {
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty() || !matchesFilter(stack)) continue;

            ItemStack toMove = stack.copy();
            toMove.setCount(1);

            if (insertIntoInventory(this, toMove, EnumFacing.DOWN)) {
                stack.shrink(1);
                if (stack.getCount() <= 0) {
                    inv.setInventorySlotContents(i, ItemStack.EMPTY);
                }
                inv.markDirty();
                return true;
            }
        }
        return false;
    }

    private boolean insertIntoInventory(IInventory inv, ItemStack stack, EnumFacing direction) {
        // Handle ISidedInventory properly
        if (inv instanceof ISidedInventory) {
            ISidedInventory sidedInv = (ISidedInventory) inv;
            // Get slots accessible from the given direction
            // For items coming from a direction, we need the opposite face on the target
            EnumFacing targetFace = direction.getOpposite();
            int[] slots = sidedInv.getSlotsForFace(targetFace);
            
            // For hopper-to-hopper transfers, bypass canInsertItem filter check
            // since we already validated against source hopper's filter
            boolean isHopperToHopper = (inv instanceof TileEntityGoldenHopper);
            
            // First try to merge with existing stacks
            for (int slotIndex : slots) {
                // Skip filter check for hopper-to-hopper transfers
                if (!isHopperToHopper && !sidedInv.canInsertItem(slotIndex, stack, targetFace)) continue;
                
                ItemStack slot = inv.getStackInSlot(slotIndex);
                if (!slot.isEmpty() 
                        && ItemStack.areItemsEqual(slot, stack)
                        && ItemStack.areItemStackTagsEqual(slot, stack)
                        && slot.getCount() < slot.getMaxStackSize()) {
                    slot.grow(1);
                    return true;
                }
            }
            
            // Then try empty slots
            for (int slotIndex : slots) {
                // Skip filter check for hopper-to-hopper transfers
                if (!isHopperToHopper && !sidedInv.canInsertItem(slotIndex, stack, targetFace)) continue;
                
                ItemStack slot = inv.getStackInSlot(slotIndex);
                if (slot.isEmpty()) {
                    inv.setInventorySlotContents(slotIndex, stack);
                    return true;
                }
            }
        } else {
            // Regular inventory - try all slots
            for (int i = 0; i < inv.getSizeInventory(); i++) {
                ItemStack slot = inv.getStackInSlot(i);

                if (slot.isEmpty()) {
                    inv.setInventorySlotContents(i, stack);
                    return true;
                }

                if (ItemStack.areItemsEqual(slot, stack)
                        && ItemStack.areItemStackTagsEqual(slot, stack)
                        && slot.getCount() < slot.getMaxStackSize()) {
                    slot.grow(1);
                    return true;
                }
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
    public void clear() {
        for (int i = 0; i < inventory.size(); i++) {
            inventory.set(i, ItemStack.EMPTY);
        }
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        // Allow manual placement of any item in storage slots
        return index < 5;
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
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
    }

    @Override
    public int getFieldCount() {
        return 0;
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

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        
        if (!this.checkLootAndRead(compound)) {
            net.minecraft.inventory.ItemStackHelper.loadAllItems(compound, this.inventory);
        }
        
        if (compound.hasKey("TransferCooldown", 99)) {
            this.transferCooldown = compound.getInteger("TransferCooldown");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        
        if (!this.checkLootAndWrite(compound)) {
            net.minecraft.inventory.ItemStackHelper.saveAllItems(compound, this.inventory);
        }
        
        compound.setInteger("TransferCooldown", this.transferCooldown);
        return compound;
    }
}
