package app;

import entities.Camera;
import entities.Entity;
import entities.Light;
import memory.Loader;
import models.Model;
import models.OBJLoader;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import rendering.MasterRenderer;
import rendering.WindowManager;
import rendering.renderers.WaterRenderer;
import terrain.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import water.Water;
import water.WaterFrameBuffer;
import water.shaders.WaterShader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        WindowManager.create();

        Loader loader = new Loader();

        Camera camera = new Camera(new Vector3f(50f, 10f, 50f), new Vector3f(0f, 180f, 0f));

        Model model = OBJLoader.loadModel("tree.obj", loader);
        Model grass_model = OBJLoader.loadModel("plane.obj", loader);

        ModelTexture texture = new ModelTexture(loader.loadTexture("brown.jpg"));

        texture.setReflectivity(0);
        texture.setShineDamper(10);

        ModelTexture grass_texture = new ModelTexture(loader.loadTexture("grass.png"));
        grass_texture.useAlpha = true;
        grass_texture.useNormals = false;

        grass_texture.setReflectivity(0);

        TexturedModel texturedModel = new TexturedModel(model, texture);
        TexturedModel grassTextured = new TexturedModel(grass_model, grass_texture);

        List<Entity> entityList = new ArrayList<>();

        Random random = new Random();

        BufferedImage grassMap = null;
        BufferedImage hmap = null;

        Model model2 = OBJLoader.loadModel("cube.obj", loader);

        ModelTexture model2Texture = new ModelTexture(loader.loadTexture("sunset2.jpg"));

        model2Texture.useAlpha = true;
        model2Texture.useFog = false;
        model2Texture.useDiffuse = false;

        model2Texture.setReflectivity(0f);

        TexturedModel tmodel2 = new TexturedModel(model2, model2Texture);

        Water water = new Water(0, 0, loader);

        try {
            grassMap = ImageIO.read(new File("resources/gmap.jpg"));
            hmap = ImageIO.read(new File("resources/hmap.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < 100; i += 10) {
            for (int k = 0; k < 100; k += 10) {
                float br = new Color(grassMap.getRGB((int) (2048 / 100. * i), (int) (2048 / 100. * k))).getRed();

                float value = random.nextFloat();

                if (value < br) {
                    float ex = i + random.nextInt(15) - 7.5f;
                    float ey = k + random.nextInt(15) - 7.5f;

//                    entityList.add(new Entity(texturedModel, new Vector3f(ex, Terrain.sampleHeight((int) (ex / 100f * 256), (int) (ey / 100f * 256), hmap) * 5,
//                            ey), new Vector3f(0, random.nextInt(360), 0), new Vector3f(1, 1, 1)));
                }
            }
        }

        for (float i = 0; i < 100; i += 1) {
            for (float k = 0; k < 100; k += 1) {
                float br = new Color(grassMap.getRGB((int) (2048 / 100. * i), (int) (2048 / 100. * k))).getRed();

                float value = random.nextFloat();

                if (value < br) {
                    float ex = i + random.nextInt(2) - 1f;
                    float ey = k + random.nextInt(2) - 1f;

//                    entityList.add(new Entity(grassTextured, new Vector3f(ex, Terrain.sampleHeight((int) (ex / 100f * 256), (int) (ey / 100f * 256), hmap) * 5 + 1,
//                            k + random.nextInt(2) - 1), new Vector3f(-90, 0, random.nextInt(360)), new Vector3f(1, 1, 1)));
                }
            }
        }

        Entity skybox = new Entity(tmodel2, new Vector3f(0, 0, 0), new Vector3f(180, 0, 0), new Vector3f(1f, 1f, 1f));

        entityList.add(skybox);

        TerrainTexture terrainTextureA = new TerrainTexture(loader.loadTexture("rock.jpg"));
        TerrainTexture terrainTextureB = new TerrainTexture(loader.loadTexture("grass.jpg"));
        TerrainTexture terrainTextureC = new TerrainTexture(loader.loadTexture("sand.jpg"));
        TerrainTexture terrainTextureD = new TerrainTexture(loader.loadTexture("rock2.jpg"));
        TerrainTexture terrainBlendTexture = new TerrainTexture(loader.loadTexture("blend.jpg"));

        TerrainTexturePack pack = new TerrainTexturePack(terrainTextureA, terrainTextureB,
                terrainTextureC, terrainTextureD, terrainBlendTexture);

        Terrain terrain = new Terrain(0, 0, loader, pack, "hmap.jpg");

        MasterRenderer master = new MasterRenderer(loader);

        List<Light> lights = new ArrayList<>();

        //lights.add(new Light(new Vector3f(15, 8, 15), new Vector3f(0, 0, 1)));
        lights.add(new Light(new Vector3f(500, 1000, 23), new Vector3f(-1, 1, 1)));

        WaterFrameBuffer fbos = new WaterFrameBuffer();

        WaterShader waterShader = new WaterShader();
        WaterRenderer waterRenderer = new WaterRenderer(waterShader, master.projectionMatrix, fbos);

        float time = 0;

        Model portModel = OBJLoader.loadModel("port.obj", loader);
        ModelTexture portTexture = new ModelTexture(loader.loadTexture("brown.jpg"));

        TexturedModel port = new TexturedModel(portModel, portTexture);
        Entity portEntity = new Entity(port, new Vector3f(50, -.25f, 35), new Vector3f(0, 0, 0), new Vector3f(.5f, 1, .5f));

        entityList.add(portEntity);

        while (!Display.isCloseRequested()) {

            camera.move();
            skybox.setPosition(camera.getPosition());
            skybox.rotate(new Vector3f(0, .1f, 0));

//            lights.get(0).setPosition(camera.getPosition());
            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

            for (Entity entity : entityList) {
                master.processEntity(entity);
            }

            master.processTerrain(terrain);

            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

//            fbos.cleanUp();

            fbos.bindReflectionFrameBuffer();

            camera.setPosition(new Vector3f(camera.getPosition().x, -(camera.getPosition().y - 1.5f) + 1.5f, camera.getPosition().z));
            camera.setRotation(new Vector3f(-camera.getRotation().x, camera.getRotation().y, camera.getRotation().z));
            master.render(lights, camera, new Vector4f(0, 1, 0, 1.5f));

            fbos.unbindCurrentFrameBuffer();

            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            fbos.bindRefractionFrameBuffer();

            camera.setPosition(new Vector3f(camera.getPosition().x, -(camera.getPosition().y - 1.5f) + 1.5f, camera.getPosition().z));
            camera.setRotation(new Vector3f(-camera.getRotation().x, camera.getRotation().y, camera.getRotation().z));
            master.render(lights, camera, new Vector4f(0, -1, 0, 1.5f));

            fbos.unbindCurrentFrameBuffer();

            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glDisable(GL30.GL_CLIP_DISTANCE0);

            master.render(lights, camera, new Vector4f(0, 1, 0, -1000f));

            waterShader.start();

            waterShader.loadViewMatrix(camera);
            waterShader.loadSkyColor(MasterRenderer.skyColor);

            waterRenderer.render(water, time, camera, lights);

            waterShader.stop();

            master.removeObjects();

            WindowManager.update();

            time += .002f;
        }

        fbos.cleanUp();

        master.clean();

        loader.clear();

        WindowManager.close();
    }
}
