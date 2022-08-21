package rendering.shaders;

import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import utils.Maths;

import java.util.List;

public class StaticShader extends ShaderProgram{
    private static final int MAX_LIGHTS = 4;

    private static final String VERT_SHADER = "src/rendering/shaders/vertexShader.txt";
    private static final String FRAG_SHADER = "src/rendering/shaders/fragmentShader.txt";

    private int l_transformationMatrix;
    private int l_projectionMatrix;
    private int l_viewMatrix;

    private int[] l_lightPos;
    private int[] l_lightCol;

    private int l_reflectivity;
    private int l_shineDamper;

    private int l_useNormals;

    private int l_skyColor;

    private int l_useDiffuse;
    private int l_useFog;

    private int l_plane;

    public StaticShader() {
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

        l_useNormals = super.getUniformLocation("useNormals");

        l_skyColor = super.getUniformLocation("skyColor");

        l_useDiffuse = super.getUniformLocation("useDiffuse");
        l_useFog = super.getUniformLocation("useFog");

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

    public void loadUseNormals(float useNormals) {
        super.loadFloat(l_useNormals, useNormals);
    }

    public void loadSkyColor(Vector3f skyColor) {
        super.loadVector(l_skyColor, skyColor);
    }

    public void loadTextureParams(float useDiffuse, float useFog) {
        super.loadFloat(l_useDiffuse, useDiffuse);
        super.loadFloat(l_useFog, useFog);
    }

    public void loadPlane(Vector4f plane) {
        super.loadVector(l_plane, plane);
    }
}
