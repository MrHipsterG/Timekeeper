package milkfrog.timekeeper;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeKeeper implements ModInitializer {
    public static final String MOD_ID = "timekeeper";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Timekeeper initialized.");
    }
}
