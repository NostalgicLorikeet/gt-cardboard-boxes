package nostalgic.cardboardboxes.mixin;

import codechicken.lib.colour.ColourRGBA;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.unification.material.Material;
import gregtech.api.util.GTUtility;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.metatileentities.storage.MetaTileEntityCrate;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import nostalgic.cardboardboxes.client.renderer.texture.CardboardTextures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = MetaTileEntityCrate.class, remap = false)
public abstract class MixinCrateRendering extends MetaTileEntity {
    @Shadow
    private Material material;

    @Shadow
    private CCRenderState renderState;

    @Shadow
    private Matrix4 translation;

    @Shadow
    private IVertexOperation[] pipeline;

    @Shadow
    private boolean isTaped;

    @Shadow
    private String TAPED_NBT;

    public MixinCrateRendering(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId);
    }

    //@Redirect(method = "renderMetaTileEntity", at = @At(value = "INVOKE", target = "if", ordinal=0))
    //public void checkCardboard() {
    //    if (material.toString().contains("wood")) {
    //        Textures.WOODEN_CRATE.render(renderState, translation,
    //                GTUtility.convertRGBtoOpaqueRGBA_CL(getPaintingColorForRendering()), pipeline);
    //    } else if (material.toString().contains("cardboard")) {
    //        CardboardTextures.CARDBOARD_CRATE.render(renderState, translation, GTUtility.convertRGBtoOpaqueRGBA_CL(getPaintingColorForRendering()), pipeline);
    //    } else {
    //        int baseColor = ColourRGBA.multiply(GTUtility.convertRGBtoOpaqueRGBA_CL(material.getMaterialRGB()),
    //                GTUtility.convertRGBtoOpaqueRGBA_CL(getPaintingColorForRendering()));
    //        Textures.METAL_CRATE.render(renderState, translation, baseColor, pipeline);
    //    }
    //}

    @Overwrite
    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        if (material.toString().contains("wood")) {
            Textures.WOODEN_CRATE.render(renderState, translation,
                    GTUtility.convertRGBtoOpaqueRGBA_CL(getPaintingColorForRendering()), pipeline);
        } else if (material.toString().contains("cardboard")) {
            CardboardTextures.CARDBOARD_CRATE.render(renderState, translation, GTUtility.convertRGBtoOpaqueRGBA_CL(getPaintingColorForRendering()), pipeline);
        } else {
            int baseColor = ColourRGBA.multiply(GTUtility.convertRGBtoOpaqueRGBA_CL(material.getMaterialRGB()),
                    GTUtility.convertRGBtoOpaqueRGBA_CL(getPaintingColorForRendering()));
            Textures.METAL_CRATE.render(renderState, translation, baseColor, pipeline);
        }
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
