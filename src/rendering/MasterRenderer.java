package rendering;

import entities.Camera;
import entities.Entity;
import entities.Light;
import memory.Loader;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import rendering.renderers.EntityRenderer;
import rendering.renderers.TerrainRenderer;
import rendering.shaders.StaticShader;
import terrain.Terrain;
import terrain.shaders.TerrainShader;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRenderer {
    public static Vector3f skyColor = new Vector3f(1f, 203/255f, 134/255f);
//    public static Vector3f skyColor = new Vector3f(0, .5f, 1);
//    public static Vector3f skyColor = new Vector3f(0, 0, 0);
    private static final float FOV = 90;

    private static final float NEAR = .01f;
    private static final float FAR = 700f;

    public Matrix4f projectionMatrix;

    private StaticShader shader = new StaticShader();
    private EntityRenderer renderer;
    private TerrainRenderer terrainRenderer;

    private TerrainShader terrainShader = new TerrainShader();

    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
    private List<Terrain> terrainList = new ArrayList<>();

    public MasterRenderer(Loader loader) {
        projectionMatrix = createProjectionMatrix();

        renderer = new EntityRenderer(shader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
    }

    public void render(List<Light> lights, Camera camera, Vector4f plane) {
        renderer.prepare();

        shader.start();

        shader.loadLights(lights);
        shader.loadViewMatrix(camera);

        shader.loadPlane(plane);

        shader.loadSkyColor(skyColor);

        renderer.render(entities);

        shader.stop();

        terrainShader.start();

        terrainShader.loadPlane(plane);

        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(camera);

        terrainShader.loadSkyColor(skyColor);

        terrainRenderer.render(terrainList);

        terrainShader.stop();
    }

    public void removeObjects() {
        terrainList.clear();
        entities.clear();
    }

    public void clean() {
        shader.clean();
        terrainShader.clean();
    }

    public void processTerrain(Terrain terrain) {
        terrainList.add(terrain);
    }

    public void processEntity(Entity entity) {
        TexturedModel model = entity.getModel();

        List<Entity> batch = entities.get(model);

        if(batch != null) {
            batch.add(entity);
        }else{
            List<Entity> newBatch = new ArrayList<>();

            newBatch.add(entity);

            entities.put(model, newBatch);
        }
    }

    private Matrix4f createProjectionMatrix() {
        Matrix4f projectionMatrix = new Matrix4f();

        Matrix4f.setIdentity(projectionMatrix);

        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV/2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR - NEAR;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR + NEAR) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR * FAR) / frustum_length);
        projectionMatrix.m33 = 0;

        return projectionMatrix;
    }
}
