package com.golem.skyblockutils.models.Overlay.TextOverlay;

import com.golem.skyblockutils.models.gui.GuiElement;
import com.golem.skyblockutils.models.gui.TextStyle;
import com.golem.skyblockutils.utils.ScreenUtils;
import com.golem.skyblockutils.utils.TimeHelper;
import com.golem.skyblockutils.utils.rendering.RenderableString;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

import static com.golem.skyblockutils.Main.configFile;
import static com.golem.skyblockutils.Main.mc;

public class AlertOverlay {
    public static GuiElement element = new GuiElement("Alert Overlay", 50, 10);
    private static final TimeHelper time = new TimeHelper();
    private static long endTime = 0;

    public static RenderableString display = new RenderableString("", 0, 0);

    private final TextStyle textStyle = TextStyle.fromInt(1);

    public static int renderWidth(String text) {
        return mc.fontRendererObj.getStringWidth(text);
    }



    public static void newAlert(String string, int ticks) {
        display.setText(string);
        if (configFile.mainAlert) {
            display.setPosition(element.position.getX(), element.position.getY());
            display.setScale(element.position.getScale());
        } else {
            int[] pos = ScreenUtils.getCenter();
            display.setPosition(pos[0], pos[1]);
            display.setScale(4.0f);
        }

        element.setHeight((int) (10 * display.getScale()));

        endTime = time.getCurrentMS() + 50L*ticks;
    }


    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT || Objects.equals(display.getText(), "")) return;

        if (time.getCurrentMS() >= endTime) display.setText("");

        display.render();

    }

}


