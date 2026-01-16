package mod.mistericecat.hoppergoldenedition.handler;

import mod.mistericecat.hoppergoldenedition.gui.GuiGoldenHopper;
import mod.mistericecat.hoppergoldenedition.container.ContainerGoldenHopper;
import mod.mistericecat.hoppergoldenedition.tileentity.TileEntityGoldenHopper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    public static final int GOLDEN_HOPPER_GUI_ID = 0;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == GOLDEN_HOPPER_GUI_ID) {
            TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
            if (tile instanceof IInventory) {
                return new ContainerGoldenHopper(player.inventory, (IInventory) tile);
            }
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == GOLDEN_HOPPER_GUI_ID) {
            TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
            if (tile instanceof TileEntityGoldenHopper) {
                return new GuiGoldenHopper(player.inventory, (TileEntityGoldenHopper) tile);
            }
        }
        return null;
    }
}
