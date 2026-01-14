package hoppergolden.mistericecat.hoppergoldenedition;

import hoppergolden.mistericecat.hoppergolden.Tags;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = HopperGoldenEdition.MOD_ID, name = HopperGoldenEdition.NAME, version = HopperGoldenEdition.VERSION, acceptedMinecraftVersions = HopperGoldenEdition.ACCEPTED_VERSIONS)
public class HopperGoldenEdition
{
    public static final String MOD_ID = Tags.MOD_ID;
    public static final String NAME = Tags.NAME;
    public static final String VERSION = Tags.VERSION;
    public static final String ACCEPTED_VERSIONS = "[1.12.2]";
    public static final Logger LOGGER = LogManager.getLogger(NAME);
}
