package com.blackwing.easyExploration.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class SoundEvents {
    public static final SoundEvent BLOCK_DEATHCHEST_CLOSE = getRegisteredSoundEvent("block.enderchest.close");
    public static final SoundEvent BLOCK_DEATHCHEST_OPEN = getRegisteredSoundEvent("block.enderchest.open");

    private static SoundEvent getRegisteredSoundEvent(String id) {
        SoundEvent soundevent = SoundEvent.REGISTRY.getObject(new ResourceLocation(id));
        if (soundevent == null) throw new IllegalStateException("Invalid Sound requested: " + id);
        return soundevent;
    }
}