package com.bwhe.easyExploration.saveInventory;

public class SaveInventoryEventHandlerClient extends SaveInventoryEventHandlerCommon {
/*

@SideOnly(Side.CLIENT)
public class GameStagesClient extends GameStagesServer {

    @Override
    public IStageData getPlayerData (EntityPlayer player) {

        final EntityPlayerSP clientPlayer = PlayerUtils.getClientPlayerSP();

        if (clientPlayer != null && clientPlayer.getUniqueID().equals(player.getUniqueID())) {

            return GameStageSaveHandler.clientData;
        }

        // A client can not have stage data for other players at this time.
        return GameStageSaveHandler.EMPTY_STAGE_DATA;
    }
}


public class GameStagesServer {

    public IStageData getPlayerData (EntityPlayer player) {

        // On server, do basic lookup on player data map.
       return GLOBAL_STAGE_DATA.computeIfAbsent(player.getPersistentID(), playerUUID -> new StageData());
    }
}


 */

}
