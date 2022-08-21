package rendering;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;

public class WindowManager {
    private static final int WIDTH = 1920;
    private static final int HEIGHT = 1080;

    private static final int FPS_CAP = 60;

    public static void create() {
        ContextAttribs attribs = new ContextAttribs(3,2)
                .withForwardCompatible(true)
                .withProfileCore(true);

        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create(new PixelFormat(), attribs);
        } catch (LWJGLException e) {
            throw new RuntimeException(e);
        }

        GL11.glViewport(0, 0, 1920, 1080);

        Display.setTitle("REJL Engine v0.1");
    }

    public static void update() {
        Display.sync(FPS_CAP);

        Display.update();
    }

    public static void close() {
        Display.destroy();
    }
}
