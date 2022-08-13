package jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWWindowFocusCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;

public class Window {
    private int width, heigth;
    private String title;

    private long glfwWindow;

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

    public void run(){
        System.out.println("Hello LWJGL" + Version.getVersion() + "!");

        init();
        loop();
    }

    private void configureGlfw(){
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
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

        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1);//enable v-sync
        glfwShowWindow(glfwWindow);

        GL.createCapabilities();

    }

    public void loop(){
        while (!glfwWindowShouldClose(glfwWindow)){
            glfwPollEvents();

            glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            glfwSwapBuffers(glfwWindow);
        }
    }

}
