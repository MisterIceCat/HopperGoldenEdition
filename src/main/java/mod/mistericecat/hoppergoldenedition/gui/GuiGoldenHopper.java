package mod.mistericecat.hoppergoldenedition.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import mod.mistericecat.hoppergoldenedition.HopperGoldenEdition;
import mod.mistericecat.hoppergoldenedition.container.ContainerGoldenHopper;

public class GuiGoldenHopper extends GuiContainer {

    private static final ResourceLocation GUI_TEXTURE =
            new ResourceLocation(HopperGoldenEdition.MOD_ID, "textures/gui/golden_hopper_gui.png");

    public GuiGoldenHopper(InventoryPlayer playerInv, IInventory hopperInv) {
        super(new ContainerGoldenHopper(playerInv, hopperInv));
        this.xSize = 176;
        this.ySize = 133;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.mc.getTextureManager().bindTexture(GUI_TEXTURE);
        drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }
}