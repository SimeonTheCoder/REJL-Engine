package water.shaders;

import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import rendering.shaders.ShaderProgram;
import utils.Maths;

import java.util.List;

public class WaterShader extends ShaderProgram {
    private static final String VERT_SHADER = "src/water/shaders/waterVertexShader.txt";
    private static final String FRAG_SHADER = "src/water/shaders/waterFragmentShader.txt";

    private int l_transformationMatrix;
    private int l_projectionMatrix;
    private int l_viewMatrix;

    private int l_skyColor;

    private int l_reflectionTexture;
    private int l_refractionTexture;
    private int l_dudv;

    private int l_time;
    private int l_cameraPos;
    private int l_normal;

    private int[] l_lightPos;
    private int[] l_lightCol;

    public WaterShader() {
        super(VERT_SHADER, FRAG_SHADER);

        l_lightPos = new int[4];
        l_lightCol = new int[4];

        getAllUniformLocations();
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "uv");
        super.bindAttribute(2, "normal");
    }

    @Override
    protected void getAllUniformLocations() {
        l_transformationMatrix = super.getUniformLocation("transformationMatrix");
        l_projectionMatrix = super.getUniformLocation("projectionMatrix");
        l_viewMatrix = super.getUniformLocation("viewMatrix");

        l_skyColor = super.getUniformLocation("skyColor");

        l_reflectionTexture = super.getUniformLocation("reflection");
        l_refractionTexture = super.getUniformLocation("refraction");

        l_dudv = super.getUniformLocation("dudv");
        l_time = super.getUniformLocation("time");

        l_cameraPos = super.getUniformLocation("cameraPos");
        l_normal = super.getUniformLocation("normal");

        for(int i = 0; i < 4; i ++) {
            l_lightPos[i] = super.getUniformLocation("lightPos[" + i + "]");
            l_lightCol[i] = super.getUniformLocation("lightColor[" + i + "]");
        }
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(l_transformationMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(l_projectionMatrix, matrix);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);

        super.loadMatrix(l_viewMatrix, viewMatrix);
    }

    public void loadSkyColor(Vector3f skyColor) {
        super.loadVector(l_skyColor, skyColor);
    }

    public void loadTextures() {
        super.loadInt(l_reflectionTexture, 0);
        super.loadInt(l_reflectionTexture, 1);
        super.loadInt(l_dudv, 2);
        super.loadInt(l_normal, 3);
    }

    public void loadTime(float time) {
        super.loadFloat(l_time, time);
    }

    public void loadCameraPos(Camera camera) {
        super.loadVector(l_cameraPos, camera.getPosition());
    }

    public void loadLights(List<Light> lights) {
        for(int i = 0; i < 4; i ++) {
            if(i < lights.size()) {
                super.loadVector(l_lightPos[i], lights.get(i).getPosition());
                super.loadVector(l_lightCol[i], lights.get(i).getColor());
            }else{
                super.loadVector(l_lightPos[i], new Vector3f(0, 0, 0));
                super.loadVector(l_lightCol[i], new Vector3f(0, 0, 0));
            }
        }
    }
}
