package terrain.shaders;

import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import rendering.shaders.ShaderProgram;
import utils.Maths;

import java.util.List;

public class TerrainShader extends ShaderProgram {
    private static final String VERT_SHADER = "src/terrain/shaders/terrain_vertexShader.txt";
    private static final String FRAG_SHADER = "src/terrain/shaders/terrain_fragmentShader.txt";
    private static final int MAX_LIGHTS = 4;

    private int l_transformationMatrix;
    private int l_projectionMatrix;
    private int l_viewMatrix;

    private int[] l_lightPos;
    private int[] l_lightCol;

    private int l_reflectivity;
    private int l_shineDamper;

    private int l_skyColor;

    private int l_texA_sampler;
    private int l_texB_sampler;
    private int l_texC_sampler;
    private int l_texD_sampler;
    private int l_texBlend_sampler;

    private int l_plane;

    public TerrainShader() {
        super(VERT_SHADER, FRAG_SHADER);

        l_lightPos = new int[MAX_LIGHTS];
        l_lightCol  = new int[MAX_LIGHTS];

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

        for(int i = 0; i < MAX_LIGHTS; i ++) {
            l_lightPos[i] = super.getUniformLocation("lightPosition[" + i + "]");
            l_lightCol[i] = super.getUniformLocation("lightColor[" + i + "]");
        }

        l_shineDamper = super.getUniformLocation("shineDamper");
        l_reflectivity = super.getUniformLocation("reflectivity");

        l_skyColor = super.getUniformLocation("skyColor");

        l_texA_sampler = super.getUniformLocation("textureA");
        l_texB_sampler = super.getUniformLocation("textureB");
        l_texC_sampler = super.getUniformLocation("textureC");
        l_texD_sampler = super.getUniformLocation("textureD");
        l_texBlend_sampler = super.getUniformLocation("blendTexture");

        l_plane = super.getUniformLocation("plane");
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

    public void loadTexturePack() {
        super.loadInt(l_texA_sampler, 0);
        super.loadInt(l_texB_sampler, 1);
        super.loadInt(l_texC_sampler, 2);
        super.loadInt(l_texD_sampler, 3);
        super.loadInt(l_texBlend_sampler, 4);
    }

    public void loadLights(List<Light> lightList) {
        for(int i = 0; i < MAX_LIGHTS; i ++) {
            if(i < lightList.size()) {
                super.loadVector(l_lightPos[i], lightList.get(i).getPosition());
                super.loadVector(l_lightCol[i], lightList.get(i).getColor());
            }else{
                super.loadVector(l_lightPos[i], new Vector3f(0, 0, 0));
                super.loadVector(l_lightCol[i], new Vector3f(0, 0, 0));
            }
        }
    }

    public void loadShine(float shineDamper, float reflectivity) {
        super.loadFloat(l_shineDamper, shineDamper);
        super.loadFloat(l_reflectivity, reflectivity);
    }

    public void loadSkyColor(Vector3f skyColor) {
        super.loadVector(l_skyColor, skyColor);
    }

    public void loadPlane(Vector4f plane) {
        super.loadVector(l_plane, plane);
    }
}
