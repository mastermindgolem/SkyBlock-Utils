package com.golem.skyblockutils;

import com.golem.skyblockutils.configs.*;
import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.Config;
import io.github.notenoughupdates.moulconfig.annotations.Category;

public class MoulConfig extends Config {


    @Expose
    @Category(name = "General", desc = "General SB features.")
    public GeneralCategory generalCategory = new GeneralCategory();

    @Expose
    @Category(name = "Kuudra", desc = "Kuudra related features.")
    public KuudraCategory kuudraCategory = new KuudraCategory();

    @Expose
    @Category(name = "Pricing", desc = "Pricing related features.")
    public PricingCategory pricingCategory = new PricingCategory();

    @Expose
    @Category(name = "Selling", desc = "Sorting/Selling related features.")
    public AuctionCategory auctionCategory = new AuctionCategory();

    @Expose
    @Category(name = "Overlays", desc = "All Overlays.")
    public OverlayCategory overlayCategory = new OverlayCategory();


    @Override
    public String getTitle() {
        return Main.MODID + " by mastermindgolem§r, config by §5Moulberry §rand §5nea89";
    }

    @Override
    public boolean shouldAutoFocusSearchbar() {
        return true;
    }
}
