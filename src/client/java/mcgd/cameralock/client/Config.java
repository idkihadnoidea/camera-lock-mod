package mcgd.cameralock.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;

public class Config extends Screen {

    private KeyMapping listeningFor = null;

    public Config() {
        super(Component.literal("Camera Lock Config"));
    }

    @Override
    protected void init() {
        // Title
        int titleWidth = this.font.width(this.title.getVisualOrderText());
        this.addRenderableWidget(new StringWidget(
                this.width / 2 - titleWidth / 2, 10, titleWidth, 20,
                this.title,
                this.font
        ));

        // Separate keybinds checkbox
        this.addRenderableWidget(Checkbox.builder(
                Component.literal("Separate Keybinds"),
                this.font
        ).pos(this.width / 2 - 100, 50).selected(CameraLockState.separateKeybinds).onValueChange((checkbox, value) -> {
            CameraLockState.separateKeybinds = value;
            saveConfig();
            this.rebuildWidgets();
        }).build());

        if (CameraLockState.separateKeybinds) {
            addKeybindButton("Lock View", CameraLockClient.lockKey, 90);
            addKeybindButton("Unlock View", CameraLockClient.unlockKey, 120);
        } else {
            addKeybindButton("Lock/Unlock View", CameraLockClient.toggleKey, 90);
        }

        // Done button
        this.addRenderableWidget(Button.builder(
                Component.literal("Done"),
                btn -> this.onClose()
        ).bounds(this.width / 2 - 50, this.height - 40, 100, 20).build());
    }

    private void addKeybindButton(String label, KeyMapping key, int y) {
        int totalWidth = 220;
        int buttonWidth = 100;
        int gap = 10;
        int leftWidth = totalWidth - buttonWidth - gap;
        int startX = this.width / 2 - totalWidth / 2;

        this.addRenderableWidget(new StringWidget(
                startX, y + 2, leftWidth, 20,
                Component.literal(label),
                this.font
        ));

        this.addRenderableWidget(Button.builder(
                getKeybindLabel(key),
                btn -> {
                    listeningFor = key;
                    rebuildWidgets();
                }
        ).bounds(startX + leftWidth + gap, y, buttonWidth, 20).build());
    }

    @Override
    public boolean keyPressed(KeyEvent keyEvent) {
        if (listeningFor != null) {
            if (keyEvent.isEscape()) {
                listeningFor.setKey(InputConstants.UNKNOWN);
                KeyMapping.resetMapping();
                saveConfig();
                listeningFor = null;
                rebuildWidgets();
                return true;
            }
            listeningFor.setKey(InputConstants.Type.KEYSYM.getOrCreate(keyEvent.key()));
            KeyMapping.resetMapping();
            saveConfig();
            listeningFor = null;
            rebuildWidgets();
            return true;
        }
        return super.keyPressed(keyEvent);
    }

    private Component getKeybindLabel(KeyMapping key) {
        if (listeningFor == key) {
            return Component.literal("> Press a key <");
        }
        return key.getTranslatedKeyMessage();
    }

    private void saveConfig() {
        ConfigData data = new ConfigData();
        data.separateKeybinds = CameraLockState.separateKeybinds;
        data.lockKey = CameraLockClient.lockKey.saveString().replace("key.keyboard.", "");
        data.unlockKey = CameraLockClient.unlockKey.saveString().replace("key.keyboard.", "");
        data.toggleKey = CameraLockClient.toggleKey.saveString().replace("key.keyboard.", "");
        ConfigData.save(data);
    }

    @Override
    public void render(net.minecraft.client.gui.GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}