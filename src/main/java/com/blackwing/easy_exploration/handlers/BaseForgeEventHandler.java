package com.blackwing.easy_exploration.handlers;

import com.blackwing.easy_exploration.EasyExploration;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import org.apache.logging.log4j.Logger;

public abstract class BaseForgeEventHandler {

    private static final Logger LOGGER = EasyExploration.getLogger(BaseForgeEventHandler.class);

    /**
     * Returns true if the given entity is the player using this client
     *
     * @param entity Entity of the event
     * @return true if the given entity is the player using this client
     */
    protected boolean isNotClientPlayer(Entity entity) {
        try {
            if (Minecraft.getInstance().player == null) return false;
            return entity.getUniqueID() != Minecraft.getInstance().player.getUniqueID();
        } catch (Exception e) {
            LOGGER.catching(e);
        }
        return true;
    }
}
