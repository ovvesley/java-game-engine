package jade;

import java.awt.event.KeyEvent;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL20.*;

public class LevelScene extends Scene{


    @Override
    public void update(float delta) {
        System.out.println("Level Scene");

        if (KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            Window.changeScene(new LevelEditorScene());
        }
    }


    @Override
    public void init() {

    }
}
