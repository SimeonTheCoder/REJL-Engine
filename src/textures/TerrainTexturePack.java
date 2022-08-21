package textures;

public class TerrainTexturePack {
    private TerrainTexture textureA;
    private TerrainTexture textureB;
    private TerrainTexture textureC;
    private TerrainTexture textureD;

    private TerrainTexture blendTexture;

    public TerrainTexturePack(TerrainTexture textureA, TerrainTexture textureB, TerrainTexture textureC,
                              TerrainTexture textureD, TerrainTexture blendTexture) {
        this.textureA = textureA;
        this.textureB = textureB;
        this.textureC = textureC;
        this.textureD = textureD;
        this.blendTexture = blendTexture;
    }

    public TerrainTexture getTextureA() {
        return textureA;
    }

    public void setTextureA(TerrainTexture textureA) {
        this.textureA = textureA;
    }

    public TerrainTexture getTextureB() {
        return textureB;
    }

    public void setTextureB(TerrainTexture textureB) {
        this.textureB = textureB;
    }

    public TerrainTexture getTextureC() {
        return textureC;
    }

    public void setTextureC(TerrainTexture textureC) {
        this.textureC = textureC;
    }

    public TerrainTexture getTextureD() {
        return textureD;
    }

    public void setTextureD(TerrainTexture textureD) {
        this.textureD = textureD;
    }

    public TerrainTexture getBlendTexture() {
        return blendTexture;
    }

    public void setBlendTexture(TerrainTexture blendTexture) {
        this.blendTexture = blendTexture;
    }
}
