package com.golem.skyblockutils.configs;

import com.golem.skyblockutils.configs.kuudra.*;
import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.Category;

public class KuudraCategory {

    @Expose
    @Category(name = "General", desc = "General Kuudra features.")
    public KuudraConfig kuudraConfig = new KuudraConfig();

    @Expose
    @Category(name = "Party Finder", desc = "Party Finder features.")
    public PartyFinderConfig partyFinder = new PartyFinderConfig();

    @Expose
    @Category(name = "Phase 1", desc = "Phase 1 features.")
    public PhaseOneConfig phase1 = new PhaseOneConfig();

    @Expose
    @Category(name = "Phase 2", desc = "Phase 2 features.")
    public PhaseTwoConfig phase2 = new PhaseTwoConfig();

    @Expose
    @Category(name = "Phase 4", desc = "Phase 4 features.")
    public PhaseFourConfig phase4 = new PhaseFourConfig();
}
