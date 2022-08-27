package renderer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;

public class Shader {

    private int shaderProgramId;
    private int vertexId;
    private int fragmentId;

    private String vertextSource;
    private String fragmentSource;

    private String vertextFilepath;
    private String fragmentFilepath;

    public Shader(String vertextPath, String fragmentPath){
        this.vertextFilepath = vertextPath;
        this.fragmentFilepath = fragmentPath;

        this.vertextSource = this.readFile(vertextPath);
        this.fragmentSource = this.readFile(fragmentPath);

    }

    private String readFile(String filepath) {
        String source = null;
        try {
            source = new String(Files.readAllBytes(Paths.get(filepath)));
            System.out.println(source);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return source;
    }

    private void compileShadersVertex(){
        vertexId = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexId, this.vertextSource);
        glCompileShader(vertexId);

        int success = glGetShaderi(vertexId, GL_COMPILE_STATUS);

        if(success == GL_FALSE){
            int len = glGetShaderi(vertexId, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR! Compile default.vertex.glsl");
            System.out.println(glGetShaderInfoLog(vertexId, len));
            throw new RuntimeException();
        }
    }

    private void compileShaderFragment(){
        fragmentId = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentId, this.fragmentSource);
        glCompileShader(fragmentId);

        int success = glGetShaderi(fragmentId, GL_COMPILE_STATUS);

        if(success == GL_FALSE){
            int len = glGetShaderi(fragmentId, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR! Compile default.fragment.glsl");
            System.out.println(glGetShaderInfoLog(fragmentId, len));
            throw new RuntimeException();
        }

    }

    private void linkShaders(){
        shaderProgramId = glCreateProgram();
        glAttachShader(shaderProgramId, vertexId);
        glAttachShader(shaderProgramId, fragmentId);
        glLinkProgram(shaderProgramId);

        int success = glGetProgrami(shaderProgramId, GL_LINK_STATUS);

        if(success == GL_FALSE){
            int len = glGetProgrami(shaderProgramId, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR! Linking shaders in the program failed");
            System.out.println(glGetProgramInfoLog(fragmentId, len));
            throw new RuntimeException();
        }

    }

    public void compile(){
        compileShadersVertex();
        compileShaderFragment();
        linkShaders();
    }

    public void use(){
        glUseProgram(shaderProgramId);
    }

    public void detach(){
        glUseProgram(0);
    }

    public void uploadMat4f(String varName, Matrix4f mat4){
        int varLocation = glGetUniformLocation(shaderProgramId, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }
}
