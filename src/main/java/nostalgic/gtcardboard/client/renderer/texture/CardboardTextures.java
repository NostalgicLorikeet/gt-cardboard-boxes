package nostalgic.gtcardboard.client.renderer.texture;

import gregtech.client.renderer.texture.cube.OrientedOverlayRenderer;
import nostalgic.gtcardboard.client.renderer.texture.custom.BoxRenderer;

public class CardboardTextures {
    public static BoxRenderer CARDBOARD_CRATE;
    public static OrientedOverlayRenderer TAPER_OVERLAY;

    public static void preInit() {
        CARDBOARD_CRATE = new BoxRenderer("storage/crates/cardboard_crate");
        TAPER_OVERLAY = new OrientedOverlayRenderer("machines/taper");
    }
}

