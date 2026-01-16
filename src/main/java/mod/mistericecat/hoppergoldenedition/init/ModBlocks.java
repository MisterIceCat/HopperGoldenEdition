package mod.mistericecat.hoppergoldenedition.init;

import mod.mistericecat.hoppergoldenedition.blocks.BlockGoldenHopper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ModBlocks {

    public static final Block GOLDEN_HOPPER;

    static {
        GOLDEN_HOPPER = new BlockGoldenHopper();
    }

    public static void register() {
        registerBlock(GOLDEN_HOPPER);
    }

    public static void registerBlock(Block block) {
        registerBlock(block, new ItemBlock(block));
    }

    public static void registerBlock(Block block, ItemBlock item) {
        if (block.getRegistryName() == null) {
            throw new IllegalArgumentException("A block being registered does not have a registry name and could not be successfully registered.");
        }

        RegistrationHandler.Blocks.add(block);

        if (item != null) {
            item.setRegistryName(block.getRegistryName());
            RegistrationHandler.Items.add(item);
        }
    }
}
