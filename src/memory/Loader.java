package memory;

import de.matthiasmann.twl.utils.PNGDecoder;
import models.Model;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import textures.TextureData;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader {
    private List<Integer> VAO = new ArrayList<>();
    private List<Integer> VBO = new ArrayList<>();

    private List<Integer> textures = new ArrayList<>();

    public Model loadToVAO(float[] pos, float[] uv, float[] normals, int[] indices) {
        int vaoID = createVAO();

        bindIndexBuffer(indices);

        storeData(0, 3, pos);
        storeData(1, 2, uv);
        storeData(2, 3, normals);

        emptyVAO();

        return new Model(vaoID, indices.length);
    }

    public Model loadToVAO(float[] pos, int dimensions) {
        int vaoID = createVAO();

        this.storeData(0, dimensions, pos);

        emptyVAO();

        return new Model(vaoID, pos.length / dimensions);
    }

    public int loadTexture(String filename) {
        Texture texture = new Texture("resources/" + filename);

        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -.4f);

        int textureId = texture.getTextureID();

        textures.add(textureId);

        return textureId;
    }

    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays();

        VAO.add(vaoID);

        GL30.glBindVertexArray(vaoID);

        return vaoID;
    }

    private void storeData(int attributeListId, int cSize, float[] data) {
        int vboID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);

        VBO.add(vboID);

        FloatBuffer asBuffer = storeInBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, asBuffer, GL15.GL_STATIC_DRAW);

        GL20.glVertexAttribPointer(attributeListId, cSize, GL11.GL_FLOAT, false, 0, 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void bindIndexBuffer(int[] indices) {
        int vboID = GL15.glGenBuffers();
        VBO.add(vboID);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);

        IntBuffer buffer = storeInIntBuffer(indices);

        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private IntBuffer storeInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);

        buffer.put(data);
        buffer.flip();

        return buffer;
    }

    private void emptyVAO() {
        GL30.glBindVertexArray(0);
    }

    private FloatBuffer storeInBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);

        buffer.flip();

        return buffer;
    }

    public void clear() {
        for (Integer vao : VAO) {
            GL30.glDeleteVertexArrays(vao);
        }

        for (Integer vbo : VBO) {
            GL15.glDeleteBuffers(vbo);
        }

        for (Integer texture : textures) {
            GL11.glDeleteTextures(texture);
        }
    }
}
