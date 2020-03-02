package com.blackwing.easy_exploration.handlers;

import com.blackwing.easy_exploration.EasyExploration;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.GameRules;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.Logger;

public class ShowDeathLocationHandler extends BaseForgeEventHandler {

    private static final Logger LOGGER = EasyExploration.getLogger(ShowDeathLocationHandler.class);

    private ITextComponent getMessage(PlayerEntity player) {
        return player.getCombatTracker().getDeathMessage().appendText(" in " + player.dimension.getRegistryName()
                + " at X/Y/Z " + (int) Math.ceil(player.getPosition().getX()) + "/" + (int) Math.ceil(player.getPosition().getY()) + "/" + (int) Math.ceil(player.getPosition().getZ())
        );
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        // not on client
        if (event.getEntity().world.isRemote) return;
        // something else died
        if (!(event.getEntity() instanceof ServerPlayerEntity)) return;
        ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
        // feature is off
        if (!event.getEntity().world.getGameRules().getBoolean(GameRules.SHOW_DEATH_MESSAGES)) return;

        ITextComponent message = getMessage(player);
        player.sendMessage(message);
        Team team = player.getTeam();
        if (team == null) return;
        switch (team.getDeathMessageVisibility()) {
            case ALWAYS:
                player.server.getPlayerList().sendMessage(message);
                break;
            case NEVER:
                break;
            case HIDE_FOR_OTHER_TEAMS:
                player.server.getPlayerList().sendMessageToAllTeamMembers(player, message);
                break;
            case HIDE_FOR_OWN_TEAM:
                player.server.getPlayerList().sendMessageToTeamOrAllPlayers(player, message);
                break;
        }
    }
}