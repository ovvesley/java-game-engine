package jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;
import util.Time;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

public class Window {
    private int width, heigth;
    private String title;

    private long glfwWindow;
    private Scene scene = null;

    private static Window window = null;

    private Window() {
        this.width = 1280;
        this.heigth = 720;
        this.title = "Mario";
    }

    public static Window get(){
        if(Window.window == null){
            Window.window = new Window();
        }
        return Window.window;
    }

    public static void changeScene(Scene scene) {
        window.scene = scene;
    }
    public void run(){
        System.out.println("Hello LWJGL" + Version.getVersion() + "!");

        init();
        this.scene = new LevelEditorScene();
        loop();

        cleanMemory();
    }


    private void configureGlfw(){
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
    }

    private void cleanMemory()
    {
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private long createWindow () {
        long glfwWindow = glfwCreateWindow(this.width, this.heigth, this.title, MemoryUtil.NULL, MemoryUtil.NULL);

        if(glfwWindow == MemoryUtil.NULL) throw new IllegalStateException("Failed to create the glfw window.");
        return glfwWindow;
    }

    public void init(){
        // setup an erro callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLWF

        if(!glfwInit()) throw new IllegalStateException("unable to initialize GLFW");

        configureGlfw();

        glfwWindow = createWindow();

        registerCallbacks(glfwWindow);

        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1);//enable v-sync
        glfwShowWindow(glfwWindow);

        GL.createCapabilities();

    }

    private void registerCallbacks(long glfwWindow) {
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePositionCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
    }

    public void loop(){
        float beginTime = Time.getTime();
        float endTime   = Time.getTime();
        float delta     = -1.0f;

        while (!glfwWindowShouldClose(glfwWindow)){
            glfwPollEvents();

            if(delta > 0)
            {
                this.scene.update(delta);
            }

            endTime = Time.getTime();
            delta   = endTime - beginTime;
            beginTime = endTime;

            glfwSwapBuffers(glfwWindow);
        }
    }



}
