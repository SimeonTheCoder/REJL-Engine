package rendering.renderers;

import models.Model;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import terrain.shaders.TerrainShader;
import terrain.Terrain;
import textures.TerrainTexturePack;
import utils.Maths;

import java.util.List;

public class TerrainRenderer {
    private TerrainShader shader;
    private Matrix4f projectionMatrix;

    public TerrainRenderer(TerrainShader terrainShader, Matrix4f projectionMatrix) {
        this.shader = terrainShader;
        this.projectionMatrix = projectionMatrix;

        terrainShader.start();

        terrainShader.loadProjectionMatrix(projectionMatrix);
        terrainShader.loadTexturePack();

        terrainShader.stop();
    }

    public void render(List<Terrain> terrainList) {
        for (Terrain terrain : terrainList) {
            prepareTerrain(terrain);
            loadModelMatrix(terrain);

            GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertCount(), GL11.GL_UNSIGNED_INT, 0);

            unbindTextureModel();
        }
    }

    private void prepareTerrain(Terrain terrain) {
        Model model = terrain.getModel();

        GL30.glBindVertexArray(model.getVaoID());

        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        TerrainTexturePack texture = terrain.getTexturePack();
        shader.loadShine(1, 0);

        bindTextures(terrain);
    }

    private void bindTextures(Terrain terrain) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getTexturePack().getTextureA().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getTexturePack().getTextureB().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getTexturePack().getTextureC().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getTexturePack().getTextureD().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE4);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getTexturePack().getBlendTexture().getTextureID());
    }

    private void unbindTextureModel() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);

        GL30.glBindVertexArray(0);
    }

    private void loadModelMatrix(Terrain terrain) {
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()),
                new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));

        shader.loadTransformationMatrix(transformationMatrix);
    }
}
