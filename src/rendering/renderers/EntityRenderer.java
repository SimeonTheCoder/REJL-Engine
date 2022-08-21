package rendering.renderers;

import entities.Entity;
import models.Model;
import models.TexturedModel;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import rendering.MasterRenderer;
import rendering.shaders.StaticShader;
import textures.ModelTexture;
import utils.Maths;

import java.util.List;
import java.util.Map;

public class EntityRenderer {
    private Matrix4f projectionMatrix;

    private StaticShader shader;

    public EntityRenderer(StaticShader staticShader, Matrix4f projectionMatrix) {
        this.shader = staticShader;

        this.projectionMatrix = projectionMatrix;

        staticShader.start();
        staticShader.loadProjectionMatrix(projectionMatrix);
        staticShader.stop();
    }

    public void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);

        GL11.glClearColor(MasterRenderer.skyColor.x, MasterRenderer.skyColor.y, MasterRenderer.skyColor.z, 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
    }

    private void turnCullingOff() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    private void turnCullingOn() {
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    public void render(Map<TexturedModel, List<Entity>> entities) {
        for (TexturedModel model : entities.keySet()) {
            prepareTexturedModel(model);

            List<Entity> batch = entities.get(model);

            for (Entity entity : batch) {
                shader.loadUseNormals(entity.getModel().getModelTexture().useNormals ? 1 : 0);
                shader.loadTextureParams(entity.getModel().getModelTexture().useDiffuse ? 1 : 0, entity.getModel().getModelTexture().useFog ? 1 : 0);

                if(entity.getModel().getModelTexture().useAlpha) {
                    turnCullingOff();
                }else{
                    turnCullingOn();
                }

                prepareInstance(entity);

                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getModel().getVertCount(), GL11.GL_UNSIGNED_INT, 0);
            }

            unbindTextureModel();
        }
    }

    private void prepareTexturedModel(TexturedModel texturedModel) {
        Model model = texturedModel.getModel();

        GL30.glBindVertexArray(model.getVaoID());

        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        ModelTexture texture = texturedModel.getModelTexture();
        shader.loadShine(texture.getShineDamper(), texture.getReflectivity());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getModelTexture().getTexId());
    }

    private void unbindTextureModel() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);

        GL30.glBindVertexArray(0);
    }

    private void prepareInstance(Entity entity) {
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());

        shader.loadTransformationMatrix(transformationMatrix);
    }
}
