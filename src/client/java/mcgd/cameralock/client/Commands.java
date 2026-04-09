package mcgd.cameralock.client;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class Commands {
    private static boolean pendingOpen = false;

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> registerCommands(dispatcher)
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (pendingOpen) {
                pendingOpen = false;
                client.setScreen(new Config());
            }
        });
    }

    private static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("cameralock")
                .executes(context -> {
                    pendingOpen = true;
                    return 1;
                })
        );
    }
}
