package mcgd.cameralock.client;

public class CameraLockState {
    private static boolean locked = false;
    public static boolean separateKeybinds = false;

    public static boolean isLocked() { return locked; }
    public static void setLocked(boolean value) { locked = value; }
    public static void toggle() { locked = !locked; }
}