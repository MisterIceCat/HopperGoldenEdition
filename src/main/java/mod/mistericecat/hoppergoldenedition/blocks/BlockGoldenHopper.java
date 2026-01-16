package mod.mistericecat.hoppergoldenedition.blocks;

import mod.mistericecat.hoppergoldenedition.tileentity.TileEntityGoldenHopper;
import net.minecraft.block.BlockHopper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockGoldenHopper extends BlockHopper {

    public BlockGoldenHopper() {
        super();
        setRegistryName("golden_hopper");
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityGoldenHopper();
    }
}
