package nostalgic.gtcardboard.client.renderer.texture;

import nostalgic.gtcardboard.client.renderer.texture.custom.BoxRenderer;

public class CardboardTextures {
    public static BoxRenderer CARDBOARD_CRATE;

    public static void preInit() {
        CARDBOARD_CRATE = new BoxRenderer("storage/crates/cardboard_crate");
    }
}

