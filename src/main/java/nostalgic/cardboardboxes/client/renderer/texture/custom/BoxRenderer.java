package nostalgic.cardboardboxes.client.renderer.texture.custom;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.ColourMultiplier;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Matrix4;
import gregtech.api.util.GTUtility;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.renderer.texture.custom.CrateRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nostalgic.cardboardboxes.client.renderer.texture.CardboardTextures;
import org.apache.commons.lang3.ArrayUtils;

public class BoxRenderer extends CrateRenderer {
    private final String basePath;

    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite sideSprite;
    private TextureAtlasSprite cardboardTop;

    public BoxRenderer(String basePath) {
        super(basePath);
        this.basePath = basePath;
        Textures.iconRegisters.add(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(TextureMap textureMap) {
        this.sideSprite = textureMap.registerSprite(GTUtility.gregtechId("blocks/" + basePath));
        this.cardboardTop = textureMap.registerSprite(GTUtility.gregtechId("blocks/storage/crates/cardboard_crate_top"));
    }

    @Override
    public void render(CCRenderState renderState, Matrix4 translation, int baseColor, IVertexOperation[] pipeline) {
        IVertexOperation[] basePipeline = ArrayUtils.add(pipeline, new ColourMultiplier(baseColor));

        for (EnumFacing renderSide : EnumFacing.VALUES) {
            if (renderSide == EnumFacing.UP) {
                Textures.renderFace(renderState, translation, basePipeline, renderSide, Cuboid6.full, cardboardTop,
                        BlockRenderLayer.CUTOUT_MIPPED);
            } else {
                Textures.renderFace(renderState, translation, basePipeline, renderSide, Cuboid6.full, sideSprite,
                        BlockRenderLayer.CUTOUT_MIPPED);
            }
        }
    }
}
