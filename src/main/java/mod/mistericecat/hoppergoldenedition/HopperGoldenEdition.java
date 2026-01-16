package mod.mistericecat.hoppergoldenedition;

import mod.mistericecat.hoppergolden.Tags;
import mod.mistericecat.hoppergoldenedition.handler.GuiHandler;
import mod.mistericecat.hoppergoldenedition.init.RegistrationHandler;
import mod.mistericecat.hoppergoldenedition.tileentity.TileEntityGoldenHopper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = HopperGoldenEdition.MOD_ID, name = HopperGoldenEdition.NAME, version = HopperGoldenEdition.VERSION, acceptedMinecraftVersions = HopperGoldenEdition.ACCEPTED_VERSIONS)
public class HopperGoldenEdition {

    public static final String MOD_ID = Tags.MOD_ID;
    public static final String NAME = Tags.NAME;
    public static final String VERSION = Tags.VERSION;
    public static final String ACCEPTED_VERSIONS = "[1.12.2]";

    public static final Logger LOGGER = LogManager.getLogger(NAME);

    @Mod.Instance
    public static HopperGoldenEdition INSTANCE;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        RegistrationHandler.init();

        GameRegistry.registerTileEntity(
                TileEntityGoldenHopper.class,
                new ResourceLocation(MOD_ID, "golden_hopper")
        );
        LOGGER.info("TileEntityGoldenHopper registered!");

        NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
        LOGGER.info("GuiHandler registered!");
    }
}
