package nostalgic.cardboardboxes.client.renderer.texture;

import nostalgic.cardboardboxes.client.renderer.texture.custom.BoxRenderer;

public class CardboardTextures {
    public static BoxRenderer CARDBOARD_CRATE;

    public static void preInit() {
        CARDBOARD_CRATE = new BoxRenderer("storage/crates/cardboard_crate");
    }
}

