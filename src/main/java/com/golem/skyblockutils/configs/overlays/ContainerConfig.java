package com.golem.skyblockutils.configs.overlays;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorDropdown;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;
import lombok.Getter;

public class ContainerConfig {

    @ConfigEditorDropdown
    @ConfigOption(name = "Container Value Overlay", desc = "Show value of items in a container.")
    @Expose
    public ContainerValuePosition containerValueOverlay = ContainerValuePosition.OFF;

    @ConfigEditorBoolean
    @ConfigOption(name = "Compact Display Mode", desc = "Compacts text by removing \"Terror\", \"Aurora\", etc. when it doesn't affect value.")
    @Expose
    public boolean compactDisplayMode = false;

    @ConfigEditorDropdown
    @ConfigOption(name = "Sort By", desc = "Sorts items in the container by this order.")
    @Expose
    public SortOrder sortBy = SortOrder.DESCENDING_PRICE;

    @Getter
    public enum ContainerValuePosition {
        OFF("Off"),
        NEXT_TO_GUI("Next to GUI"),
        CUSTOM("Custom");

        private final String name;

        @Override
        public String toString() {
            return name;
        }

        ContainerValuePosition(String name) {
            this.name = name;
        }
    }

    public enum SortOrder {
        DESCENDING_PRICE("Descending Price"),
        ASCENDING_PRICE("Ascending Price"),
        ALPHABETICAL("Alphabetical"),
        ATTRIBUTE_TIER("Attribute Tier"),
        ITEM_TYPE("Item Type");

        private final String name;

        @Override
        public String toString() {
            return name;
        }

        SortOrder(String name) {
            this.name = name;
        }
    }

}
