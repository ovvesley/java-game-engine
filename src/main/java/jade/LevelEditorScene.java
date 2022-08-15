package jade;

import org.lwjgl.BufferUtils;
import renderer.Shader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;

public class LevelEditorScene extends Scene{
    Shader defaultShader;
    private static final String vertexShadersSrc     = "assets/shaders/default.vertex.glsl";
    private static final String fragmentShaderSrc    = "assets/shaders/default.fragment.glsl";
    public LevelEditorScene (){
        this.defaultShader = new Shader(vertexShadersSrc, fragmentShaderSrc);

        this.init();
    }

    private static float [] vertexies = {
            0.5f, -0.5f, 0.0f,          1.0f, 0.0f, 0.0f, 1.0f, // bottom right
            -0.5f, 0.5f, 0.0f,          0.0f, 1.0f, 0.0f, 1.0f, // top left
            0.5f, 0.5f, 0.0f,           0.0f, 0.0f, 1.0f, 1.0f, // top right
            -0.5f, -0.5f, 0.0f,         1.0f, 1.0f, 0.0f, 1.0f, // bottom left
    };

    private static int [] elements = {
            2, 1, 0,
            3, 1, 0
    };

    private int vertexArrayObjectId, vertextBufferObjectId, elementBufferObjectId;



    private void createBufferSendGpu(){
        vertexArrayObjectId = glGenVertexArrays();
        glBindVertexArray(vertexArrayObjectId);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexies.length);
        vertexBuffer.put(vertexies).flip();

        vertextBufferObjectId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertextBufferObjectId);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elements.length);
        elementBuffer.put(elements).flip();

        elementBufferObjectId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementBufferObjectId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        int positionSize = 3;
        int colorSize = 4;
        int floatSizeByte = Float.BYTES;
        int vertexSizeBytes = (positionSize + colorSize) * floatSizeByte;

        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * floatSizeByte);
        glEnableVertexAttribArray(1);

    }
    @Override
    public void init() {
        defaultShader.compile();
        createBufferSendGpu();
    }

    @Override
    public void update(float delta) {

        System.out.println("Level Editor Scene");
        defaultShader.use();

        glBindVertexArray(vertexArrayObjectId);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elements.length, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        defaultShader.detach();
    }
}
