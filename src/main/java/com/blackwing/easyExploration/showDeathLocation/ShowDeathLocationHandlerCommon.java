package com.blackwing.easyExploration.showDeathLocation;

import com.blackwing.easyExploration.config.Configuration;
import com.blackwing.easyExploration.utilities.EventHandlerBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.DimensionType;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ShowDeathLocationHandlerCommon extends EventHandlerBase {

    private TextComponentString getMessage(EntityPlayerMP player) {
        return new TextComponentString(player.getName() + " died in "
                + DimensionType.getById(player.dimension).getName() + " (id:" + player.dimension + ") at X/Y/Z "
                + (int) Math.ceil(player.posX) + "/" + (int) Math.ceil(player.posY) + "/" + (int) Math.ceil(player.posZ)
        );
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        // someone else / something else died
        if (!(event.getEntity() instanceof EntityPlayerMP)) return;
        EntityPlayerMP player = (EntityPlayerMP) event.getEntity();

        switch (Configuration.showDeathLocation.sendTo) {
            case NOONE:
                break;
            case PLAYER:
                player.sendMessage(getMessage(player));
                break;
            case TEAM:
                player.sendMessage(getMessage(player));
                player.mcServer.getPlayerList().sendMessageToAllTeamMembers(player, getMessage(player));
                break;
            case EVERYONE:
                player.mcServer.getPlayerList().sendMessage(getMessage(player));
                break;
        }
    }
}
