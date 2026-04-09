package mcgd.cameralock.client.mixin;

import mcgd.cameralock.client.CameraLockState;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
	@Inject(at = @At("HEAD"), method = "turnPlayer", cancellable = true)
	private void onTurnPlayer(CallbackInfo ci) {
		if (CameraLockState.isLocked()) {
			ci.cancel();
		}
	}
}