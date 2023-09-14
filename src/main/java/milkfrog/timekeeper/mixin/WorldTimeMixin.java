package milkfrog.timekeeper.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.*;

// create a mixin for the LevelData class (the class that stores the world time) and inject into the getWorldTime() method
@Mixin(MinecraftServer.class)
public class WorldTimeMixin {
    private long worldTime;
    private File worldTimeFile = new File("logs/worldTime.txt");

    public long getWorldTime() {
        return worldTime;
    }

    public void setWorldTime(long worldTime) {
        this.worldTime = worldTime;
    }
    public long getFileTime() {
        // read the world time file and return the time
        long fileTime = 0;
        try {
            FileReader fileReader = new FileReader(worldTimeFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            fileTime = Long.parseLong(bufferedReader.readLine());
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileTime;
    }
    public boolean setFileTime() {
        try {
            FileWriter fileWriter = new FileWriter(worldTimeFile);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println(worldTime);
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Inject(at = @At("HEAD"), method = "doTick", remap = false)
    public void countWorldTime(CallbackInfo ci) {
        MinecraftServer server = (MinecraftServer) (Object) this;
        long worldTime = server.getWorldManager(0).getWorldTime();
        setWorldTime(worldTime);
        if (worldTime % 20 == 0) {
            if (getWorldTime() < getFileTime()) {
                // if the world time is less than the time in the file, the server has been restarted
                // set the world time to the time in the file
                server.getWorldManager(0).setWorldTime(getFileTime());
            }
            // update the time in the file
            setFileTime();
        }
    }
}
