package rendering.renderers;

import entities.Camera;
import entities.Light;
import memory.Loader;
import models.Model;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import textures.ModelTexture;
import utils.Maths;
import water.Water;
import water.WaterFrameBuffer;
import water.shaders.WaterShader;

import java.util.List;

public class WaterRenderer {
    private WaterShader shader;
    private Matrix4f projectionMatrix;

    private WaterFrameBuffer fbo;

    public WaterRenderer(WaterShader waterShader, Matrix4f projectionMatrix, WaterFrameBuffer fbo) {
        this.shader = waterShader;
        this.projectionMatrix = projectionMatrix;

        this.fbo = fbo;

        waterShader.start();

        waterShader.loadTextures();
        waterShader.loadProjectionMatrix(projectionMatrix);

        waterShader.stop();
    }

    public void render(Water water, float time, Camera camera, List<Light> lights) {
        shader.start();

        shader.loadTime(time);
        shader.loadLights(lights);

        shader.loadCameraPos(camera);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbo.getReflectionTexture());

        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbo.getRefractionDepthTexture());

        Loader loader = new Loader();
        ModelTexture texture = new ModelTexture(loader.loadTexture("dudv.jpg"));
        ModelTexture texture2 = new ModelTexture(loader.loadTexture("normal.jpg"));

        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTexId());

        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture2.getTexId());

        prepareWater(water);
        loadModelMatrix(water);

        GL11.glDrawElements(GL11.GL_TRIANGLES, water.getModel().getVertCount(), GL11.GL_UNSIGNED_INT, 0);

        unbindTextureModel();

        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbo.getRefractionDepthTexture());

        shader.stop();
    }

    private void prepareWater(Water water) {
        Model model = water.getModel();

        GL30.glBindVertexArray(model.getVaoID());

        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
    }

    private void unbindTextureModel() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);

        GL30.glBindVertexArray(0);
    }

    private void loadModelMatrix(Water water) {
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(water.getX(), 0, water.getZ()),
                new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));

        shader.loadTransformationMatrix(transformationMatrix);
    }
}
