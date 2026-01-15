package hoppergolden.mistericecat.hoppergoldenedition.blocks;

import hoppergolden.mistericecat.hoppergoldenedition.tileentity.TileEntityGoldenHopper;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockGoldenHopper extends BlockContainer {

    public static final PropertyDirection FACING = PropertyDirection.create("facing");

    public BlockGoldenHopper() {
        super(Material.IRON);
        setRegistryName("golden_hopper");
        setHardness(0.5F);
        setResistance(0.5F);
        setCreativeTab(CreativeTabs.REDSTONE);
        setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.DOWN));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityGoldenHopper();
    }

    @Override
    public IBlockState getStateForPlacement(
            World worldIn,
            BlockPos pos,
            EnumFacing facing,
            float hitX, float hitY, float hitZ,
            int meta,
            EntityLivingBase placer
    ) {
        EnumFacing enumfacing = facing.getOpposite();
        if (enumfacing == EnumFacing.UP) {
            enumfacing = EnumFacing.DOWN;
        }
        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, getFacing(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i = i | state.getValue(FACING).getIndex();
        return i;
    }

    public static EnumFacing getFacing(int meta) {
        return EnumFacing.byIndex(meta & 7);
    }
}
