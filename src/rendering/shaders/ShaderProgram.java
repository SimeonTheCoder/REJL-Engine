package rendering.shaders;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.FloatBuffer;
import java.util.Scanner;

public abstract class ShaderProgram {
    private int id;

    private int vertShaderId;
    private int fragShaderId;

    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    public ShaderProgram(String vertFile, String fragFile) {
        vertShaderId = loadShader(vertFile, GL20.GL_VERTEX_SHADER);
        fragShaderId = loadShader(fragFile, GL20.GL_FRAGMENT_SHADER);

        id = GL20.glCreateProgram();

        GL20.glAttachShader(id, vertShaderId);
        GL20.glAttachShader(id, fragShaderId);

        bindAttributes();

        GL20.glLinkProgram(id);

        GL20.glValidateProgram(id);
    }

    public void start() {
        GL20.glUseProgram(id);
    }

    public void stop() {
        GL20.glUseProgram(0);
    }

    public void clean() {
        stop();

        GL20.glDetachShader(id, vertShaderId);
        GL20.glDetachShader(id, fragShaderId);

        GL20.glDeleteShader(vertShaderId);
        GL20.glDeleteShader(fragShaderId);

        GL20.glDeleteProgram(id);

        getAllUniformLocations();
    }

    protected abstract void bindAttributes();

    protected int getUniformLocation(String uniformName) {
        return GL20.glGetUniformLocation(id, uniformName);
    }

    protected void loadFloat(int location, float value) {
        GL20.glUniform1f(location, (float) value);
    }

    protected void loadVector(int location, Vector3f vector) {
        GL20.glUniform3f(location, vector.x, vector.y, vector.z);
    }

    protected void loadVector(int location, Vector4f vector) {
        GL20.glUniform4f(location, vector.x, vector.y, vector.z, vector.w);
    }

    protected void loadBool(int location, boolean value) {
        float toLoad = value ? 1 : 0;

        loadFloat(location, toLoad);
    }

    protected void loadMatrix(int location, Matrix4f matrix) {
        matrix.store(matrixBuffer);

        matrixBuffer.flip();

        GL20.glUniformMatrix4(location, false, matrixBuffer);
    }

    protected abstract void getAllUniformLocations();

    protected void bindAttribute(int attribute, String varName) {
        GL20.glBindAttribLocation(id, attribute, varName);
    }

    private static int loadShader(String filename, int type) {
        File shaderFile = new File(filename);

        Scanner scanner = null;

        try {
            scanner = new Scanner(shaderFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        StringBuilder builder = new StringBuilder();

        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();

            builder.append(line);
            builder.append(System.lineSeparator());
        }

        scanner.close();

        int shaderId = GL20.glCreateShader(type);

        GL20.glShaderSource(shaderId, builder);
        GL20.glCompileShader(shaderId);

        if(GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.out.println("ERR Couldn't compile shader " + shaderFile + "!");

            System.exit(-1);
        }

        return shaderId;
    }

    protected void loadInt(int location, int value) {
        GL20.glUniform1i(location, value);
    }
}
