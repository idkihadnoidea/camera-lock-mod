package mcgd.cameralock.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

public class CameraLockClient implements ClientModInitializer {

	public static KeyMapping lockKey;
	public static KeyMapping unlockKey;
	public static KeyMapping toggleKey;

	@Override
	public void onInitializeClient() {
		Commands.register();

		lockKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
				"key.cameralock.lock",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_UNKNOWN,
				KeyMapping.Category.MISC
		));

		unlockKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
				"key.cameralock.unlock",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_UNKNOWN,
				KeyMapping.Category.MISC
		));

		toggleKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
				"key.cameralock.toggle",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_UNKNOWN,
				KeyMapping.Category.MISC
		));

		final ConfigData data = ConfigData.load();
		CameraLockState.separateKeybinds = data.separateKeybinds;

		// Apply saved keybinds after options.txt is loaded to prevent it from overwriting them
		final boolean[] applied = {false};
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (!applied[0] && client.player != null) {
				applied[0] = true;
				applyKey(lockKey, data.lockKey);
				applyKey(unlockKey, data.unlockKey);
				applyKey(toggleKey, data.toggleKey);
			}
		});

		// Poll keybind presses every tick and update lock state accordingly.
		// consumeClick() returns true once per press and resets, preventing repeated triggers.
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (CameraLockState.separateKeybinds) {
				if (lockKey.consumeClick()) CameraLockState.setLocked(true);
				if (unlockKey.consumeClick()) CameraLockState.setLocked(false);
			} else {
				if (toggleKey.consumeClick()) CameraLockState.toggle();
			}
		});
	}

	private static void applyKey(KeyMapping mapping, String keyName) {
		if (!keyName.equals("unknown")) {
			mapping.setKey(InputConstants.getKey("key.keyboard." + keyName.toLowerCase()));
			KeyMapping.resetMapping();
		}
	}
}