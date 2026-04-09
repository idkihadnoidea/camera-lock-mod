package mcgd.cameralock.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Path;

public class ConfigData {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger("cameralock");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("cameralock.json");

    public boolean separateKeybinds = false;
    public String lockKey = "unknown";
    public String unlockKey = "unknown";
    public String toggleKey = "unknown";

    public static ConfigData load() {
        if (!CONFIG_FILE.toFile().exists()) return new ConfigData();
        try (Reader reader = new FileReader(CONFIG_FILE.toFile())) {
            return GSON.fromJson(reader, ConfigData.class);
        } catch (Exception e) {
            return new ConfigData();
        }
    }

    public static void save(ConfigData data) {
        try (Writer writer = new FileWriter(CONFIG_FILE.toFile())) {
            GSON.toJson(data, writer);
        } catch (Exception e) {
            LOGGER.error("Failed to save cameralock config", e);
        }
    }
}
