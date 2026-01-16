package mod.mistericecat.hoppergoldenedition.blocks;

import mod.mistericecat.hoppergoldenedition.HopperGoldenEdition;
import mod.mistericecat.hoppergoldenedition.tileentity.TileEntityGoldenHopper;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockGoldenHopper extends BlockHopper {

    public BlockGoldenHopper() {
        super();
        setRegistryName("golden_hopper");
        setTranslationKey("golden_hopper");
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityGoldenHopper();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state,
                                    EntityPlayer playerIn, EnumHand hand, EnumFacing facing,
                                    float hitX, float hitY, float hitZ) {

        if (!worldIn.isRemote) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof TileEntityGoldenHopper) {
                playerIn.openGui(HopperGoldenEdition.INSTANCE, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
            }
        }

        return true;
    }
}
