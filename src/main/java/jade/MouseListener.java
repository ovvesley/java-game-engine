package jade;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double xPosition, yPosition, lastY, lastX;

    private boolean mouseButtonPressed[] = new boolean[3];
    private boolean isDragging;

    private MouseListener() {
        this.scrollX    = 0.0;
        this.scrollY    = 0.0;
        this.xPosition  = 0.0;
        this.yPosition  = 0.0;
        this.lastX      = 0.0;
        this.lastY      = 0.0;
    }

    public static MouseListener get(){
        if (MouseListener.instance == null){
            MouseListener.instance = new MouseListener();
        }
        return instance;
    }

    public static void mousePositionCallback(long window, double xPosition, double yPosition){
        get().lastX = get().xPosition;
        get().lastY = get().yPosition;

        get().xPosition = xPosition;
        get().yPosition = yPosition;

        get().isDragging = get().anyMouseButtonIsPressed();
    }

    private boolean anyMouseButtonIsPressed(){
        for (int i = 0; i< get().mouseButtonPressed.length; i++){
            if(get().mouseButtonPressed[i]) return true;
        }
        return false;
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods){
        if (button >= get().mouseButtonPressed.length) return;

        if(action == GLFW_PRESS){
            get().mouseButtonPressed[button] = true; return;
        }
        if (action == GLFW_RELEASE){
            get().mouseButtonPressed[button] = false;
            get().isDragging = false;
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset){
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame(){
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().xPosition;
        get().lastY = get().yPosition;
    }

    public static float getX(){
        return (float) get().xPosition;
    }

    public static float getY(){
        return (float) get().yPosition;
    }
    public static float getDx(){
        return (float) (get().lastX - get().xPosition);
    }

    public static float getDy(){
        return (float) (get().lastY - get().yPosition);
    }

    public static float getScrollX(){
        return (float) get().scrollX;
    }

    public static float getScrollY(){
        return (float) get().scrollY;
    }

    public static boolean isDragging(){
        return get().isDragging;
    }

    public static boolean mouseButtonDown(int button){
        if (button >= get().mouseButtonPressed.length) return false;

        return get().mouseButtonPressed[button];
    }

}
