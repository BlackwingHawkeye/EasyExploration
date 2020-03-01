package com.blackwing.easy_exploration.handlers;


import com.blackwing.easy_exploration.config.Configuration;
import com.blackwing.easy_exploration.EasyExploration;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod.EventBusSubscriber
public class ConfigurationHandler extends BaseForgeEventHandler {

    private static final Logger LOGGER = EasyExploration.getLogger(ConfigurationHandler.class);

    private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec config;

    static {
        Configuration.init(builder);

        config = builder.build();
    }

    public static void loadConfig(ForgeConfigSpec config, String path) {
        LOGGER.info("Loading config " + path);
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();
        LOGGER.info("Built config " + path);
        file.load();
        LOGGER.info("Loaded config " + path);
        config.setConfig(file);
    }
}