package nostalgic.cardboardboxes.common.metatileentities.storage;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.unification.material.Material;
import gregtech.common.metatileentities.storage.MetaTileEntityCrate;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import nostalgic.cardboardboxes.client.renderer.texture.CardboardTextures;

public class MetaTileEntityBox extends MetaTileEntityCrate {
    private final Material material;
    private final int inventorySize;
    private boolean isTaped;
    private final String TAPED_NBT = "Taped";

    public MetaTileEntityBox(ResourceLocation metaTileEntityId, Material material, int inventorySize) {
        super(metaTileEntityId, material, inventorySize);
        this.material = material;
        this.inventorySize = inventorySize;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Pair<TextureAtlasSprite, Integer> getParticleTexture() {
        return Pair.of(CardboardTextures.CARDBOARD_CRATE.getParticleTexture(), getPaintingColorForRendering());
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityBox(metaTileEntityId, material, inventorySize);
    }
}
