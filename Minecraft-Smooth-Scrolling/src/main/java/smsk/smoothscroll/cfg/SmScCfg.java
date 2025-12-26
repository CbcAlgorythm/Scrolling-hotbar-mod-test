package smsk.smoothscroll.cfg;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SmScCfg {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir().resolve("smoothscroll.json");
    
    public double scrollSpeed = 1.0;
    public double acceleration = 0.15;
    public double friction = 0.85;
    public boolean enableInCreative = true;
    public boolean enableInChat = true;
    public boolean enableInWidgets = true;
    
    public static SmScCfg load() {
        if (Files.exists(CONFIG_PATH)) {
            try {
                String json = Files.readString(CONFIG_PATH);
                return GSON.fromJson(json, SmScCfg.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        SmScCfg config = new SmScCfg();
        config.save();
        return config;
    }
    
    public void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, GSON.toJson(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public double getScrollSpeed() {
        return scrollSpeed;
    }
    
    public double getAcceleration() {
        return acceleration;
    }
    
    public double getFriction() {
        return friction;
    }
}
