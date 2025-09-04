package com.example.examplemod.client;

import com.example.examplemod.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;

public class TimeTabOverlay implements IGuiOverlay {

    private static String buildTimeText(Level level) {
        long dayTime = level.getDayTime() % 24000L;
        int totalTicksInDay = 24000;
        int ticksPerHour = 1000;
        int hour24 = (int)((dayTime + 6000) % totalTicksInDay) / ticksPerHour;
        int minute = (int)(((dayTime + 6000) % ticksPerHour) * 60 / ticksPerHour);
        String hh = hour24 < 10 ? ("0" + hour24) : String.valueOf(hour24);
        String mm = minute < 10 ? ("0" + minute) : String.valueOf(minute);

        // Sleep window reference: see TIME.md (Java Edition typical sleep time ~ 12544 to 23460)
        boolean canSleepNow = level.isNight();
        String period = canSleepNow ? "夜晚 · 可睡觉" : "白天";
        return hh + ":" + mm + "  " + period;
    }

    @Override
    public void render(net.minecraftforge.client.gui.overlay.ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;
        if (!Config.showClientOverlay) return; // avoid duplicate when server header exists (default off)
        if (!mc.options.keyPlayerList.isDown()) return;

        String text = buildTimeText(mc.level);
        var font = mc.font;
        int textWidth = font.width(text);
        int x = (screenWidth - textWidth) / 2;
        int y = 6;

        int bg = mc.options.getBackgroundColor(0.4F);
        guiGraphics.fill(x - 4, y - 2, x + textWidth + 4, y + 9 + 2, bg);
        guiGraphics.drawString(font, text, x, y, 0xFFFFFF);
    }

    public static ResourceLocation abovePlayerList() {
        return VanillaGuiOverlay.PLAYER_LIST.id();
    }
}


