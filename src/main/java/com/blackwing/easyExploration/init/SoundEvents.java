package com.blackwing.easyExploration.init;

import net.minecraft.init.Bootstrap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class SoundEvents {
    public static final SoundEvent BLOCK_DEATHCHEST_CLOSE;
    public static final SoundEvent BLOCK_DEATHCHEST_OPEN;

    private static SoundEvent getRegisteredSoundEvent(String id) {
        SoundEvent soundevent = SoundEvent.REGISTRY.getObject(new ResourceLocation(id));
        if (soundevent == null) throw new IllegalStateException("Invalid Sound requested: " + id);
        else return soundevent;
    }

    static {
        if (!Bootstrap.isRegistered()) throw new RuntimeException("Accessed Sounds before Bootstrap!");
        else {
            BLOCK_DEATHCHEST_CLOSE = getRegisteredSoundEvent("block.enderchest.close");
            BLOCK_DEATHCHEST_OPEN = getRegisteredSoundEvent("block.enderchest.open");
        }
    }
}