package textures;

public class ModelTexture {
    private int texId;

    private float shineDamper = 1;
    private float reflectivity = 0;

    public boolean useAlpha;
    public boolean useNormals;
    public boolean useFog;
    public boolean useDiffuse;

    public ModelTexture(int id) {
        this.texId = id;

        this.useAlpha = false;
        this.useNormals = true;

        this.useFog = true;
        this.useDiffuse = true;
    }

    public int getTexId() {
        return texId;
    }

    public void setTexId(int texId) {
        this.texId = texId;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }
}
