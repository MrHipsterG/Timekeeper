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
    // create a variable to store the world time
    @Unique
    long worldTime = 0;

    @Inject(at = @At("HEAD"), method = "doTick", remap = false)
    public void countWorldTime(CallbackInfo ci) {

        if (worldTime == 0) {
            try {
                File file = new File("logs/worldTime.txt");
                BufferedReader br = new BufferedReader(new FileReader(file));
                String st;
                while ((st = br.readLine()) != null) {
                    worldTime = Long.parseLong(st);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (worldTime % 200 == 0) {
            try {
                FileWriter fileWriter = new FileWriter("logs/worldTime.txt");
                PrintWriter printWriter = new PrintWriter(fileWriter);
                printWriter.println(worldTime);
                printWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
