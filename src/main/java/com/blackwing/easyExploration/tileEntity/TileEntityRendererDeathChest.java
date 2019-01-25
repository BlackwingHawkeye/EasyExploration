package com.blackwing.easyExploration.tileEntity;

import com.blackwing.easyExploration.block.BlockDeathChest;
import com.blackwing.easyExploration.tileEntity.base.ITileEntityRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityRendererDeathChest extends TileEntitySpecialRenderer<TileEntityDeathChest> implements ITileEntityRenderer {

    public void register() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDeathChest.class, this);
    }

    private static final ResourceLocation DEATH_CHEST_TEXTURE = new ResourceLocation("textures/entity/chest/deathchest.png");
    private final ModelChest modelChest = new ModelChest();

    public void render(TileEntityDeathChest te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        int i = 0;

        if (te.hasWorld()) {
            Block block = te.getBlockType();
            i = te.getBlockMetadata();

            if (block instanceof BlockDeathChest && i == 0) {
                ((BlockDeathChest) block).checkForSurroundingChests(te.getWorld(), te.getPos(), te.getWorld().getBlockState(te.getPos()));
                i = te.getBlockMetadata();
            }
        }

        if (destroyStage >= 0) {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 4.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        } else {
            this.bindTexture(DEATH_CHEST_TEXTURE);
        }

        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();

        if (destroyStage < 0) GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);

        GlStateManager.translate((float) x, (float) y + 1.0F, (float) z + 1.0F);
        GlStateManager.scale(1.0F, -1.0F, -1.0F);
        GlStateManager.translate(0.5F, 0.5F, 0.5F);
        int j = 0;

        switch (i) {
            case 2:
                j = 180;
                break;
            case 3:
                j = 0;
                break;
            case 4:
                j = 90;
                break;
            case 5:
                j = -90;
                break;
            default:
        }

        GlStateManager.rotate((float) j, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
        float f = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;

        f = 1.0F - f;
        f = 1.0F - f * f * f;
        modelChest.chestLid.rotateAngleX = -(f * ((float) Math.PI / 2F));
        modelChest.renderAll();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
}
