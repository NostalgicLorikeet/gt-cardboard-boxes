package nostalgic.cardboardboxes.mixin;

import gregtech.common.metatileentities.storage.MetaTileEntityCrate;
import nostalgic.cardboardboxes.IMixinCrate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = MetaTileEntityCrate.class, remap = false)
public abstract class MixinCrateAccessTaped implements IMixinCrate {
    //@Accessor("isTaped")
    //boolean isTaped();
    @Shadow(remap = false)
    protected boolean isTaped;

    @Override
    public boolean isTaped() {
        return this.isTaped;
    }
}
