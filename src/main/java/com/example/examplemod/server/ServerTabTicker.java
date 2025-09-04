package com.example.examplemod.server;

import com.example.examplemod.LvjiaTimeMod;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = LvjiaTimeMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerTabTicker {

    private static int tickCounter = 0;

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        tickCounter++;
        if (tickCounter < 20) return; // update roughly once per second
        tickCounter = 0;

        MinecraftServer server = event.getServer();
        if (server == null) return;

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            ServerLevel level = player.serverLevel();
            String text = buildDetailedText(level);
            player.setTabListHeader(Component.literal(text));
        }
    }

    private static String buildDetailedText(Level level) {
        long dayTime = level.getDayTime() % 24000L;
        int totalTicksInDay = 24000;
        int ticksPerHour = 1000;
        int hour24 = (int)((dayTime + 6000) % totalTicksInDay) / ticksPerHour;
        int minute = (int)(((dayTime + 6000) % ticksPerHour) * 60 / ticksPerHour);
        String hh = hour24 < 10 ? ("0" + hour24) : String.valueOf(hour24);
        String mm = minute < 10 ? ("0" + minute) : String.valueOf(minute);
        boolean canSleep = level.isNight();
        String period = canSleep ? "夜晚 · 可睡觉" : "白天";

        // Phase details using TIME.md guidance (approximate milestones)
        String phase;
        if (dayTime < 450) phase = "日出中";           // ~06:27 日出结束
        else if (dayTime < 6000) phase = "白天";       // 正午前
        else if (dayTime < 11616) phase = "正午前后";   // ~10:17 光照最大-正午附近
        else if (dayTime < 12544) phase = "日落前";     // ~17:37 日落开始
        else if (dayTime < 13800) phase = "傍晚/可睡";  // ~18:32 可睡, ~19:48 日落结束
        else if (dayTime < 17843) phase = "深夜";       // 午夜前
        else if (dayTime < 22300) phase = "午夜后";     // ~04:18 太阳升起
        else if (dayTime < 23460) phase = "黎明";       // ~05:27 生物燃烧
        else phase = "清晨";                            // ~05:59 光照最大值12

        return hh + ":" + mm + "  " + period + "  (" + phase + ")";
    }
}


