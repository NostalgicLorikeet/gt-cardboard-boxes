package nostalgic.cardboardboxes.common.metatileentities.storage;

import codechicken.lib.colour.ColourRGBA;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import gregtech.api.recipes.ModHandler;
import gregtech.api.unification.material.Material;
import gregtech.api.util.GTUtility;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.metatileentities.storage.MetaTileEntityCrate;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import nostalgic.cardboardboxes.client.renderer.texture.CardboardTextures;

public class MetaTileEntityBox extends MetaTileEntityCrate {
    private boolean isTaped;
    private final String TAPED_NBT = "Taped";

    public MetaTileEntityBox(ResourceLocation metaTileEntityId, Material material, int inventorySize) {
        super(metaTileEntityId, material, inventorySize);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Pair<TextureAtlasSprite, Integer> getParticleTexture() {
        return Pair.of(CardboardTextures.CARDBOARD_CRATE.getParticleTexture(), getPaintingColorForRendering());
    }

    @Override
    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        CardboardTextures.CARDBOARD_CRATE.render(renderState, translation,
                    GTUtility.convertRGBtoOpaqueRGBA_CL(getPaintingColorForRendering()), pipeline);
        boolean taped = isTaped;
        if (renderContextStack != null && renderContextStack.getTagCompound() != null) {
            NBTTagCompound tag = renderContextStack.getTagCompound();
            if (tag.hasKey(TAPED_NBT) && tag.getBoolean(TAPED_NBT)) {
                taped = true;
            }
        }
        if (taped) {
            Textures.TAPED_OVERLAY.render(renderState, translation, pipeline);
        }
    }
}
