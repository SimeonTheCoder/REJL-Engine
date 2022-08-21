package terrain;

import memory.Loader;
import models.Model;
import org.lwjgl.util.vector.Vector3f;
import textures.TerrainTexturePack;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Terrain {
    private static final float SIZE = 100;
    private static final int VERT = 256;

    private float x, z;

    private Model model;
    private TerrainTexturePack texture;

    public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texture, String hmapLocation) {
        this.texture = texture;

        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;

        this.model = generateTerrain(loader, hmapLocation);
    }

    private Model generateTerrain(Loader loader, String hmapLocation){
        BufferedImage hmap = null;

        try {
            hmap = ImageIO.read(new File("resources/" + hmapLocation));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int count = VERT * VERT;

        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count*2];

        int[] indices = new int[6*(VERT-1)*(VERT-1)];

        int vertexPointer = 0;

        for(int i=0;i<VERT;i++){
            for(int j=0;j<VERT;j++){
                vertices[vertexPointer*3] = (float)j/((float)VERT - 1) * SIZE;
                vertices[vertexPointer*3+1] = sampleHeight(j, i, hmap) * 20;
                vertices[vertexPointer*3+2] = (float)i/((float)VERT - 1) * SIZE;

                Vector3f normalVector = calculateNormal(j, i, hmap);

                normals[vertexPointer * 3] = normalVector.x;
                normals[vertexPointer * 3 + 1] = normalVector.y;
                normals[vertexPointer * 3 + 2] = normalVector.z;

                textureCoords[vertexPointer*2] = (float)j/((float)VERT - 1);
                textureCoords[vertexPointer*2+1] = (float)i/((float)VERT - 1);
                vertexPointer++;
            }
        }

        int pointer = 0;

        for(int gz=0;gz<VERT-1;gz++){
            for(int gx=0;gx<VERT-1;gx++){
                int topLeft = (gz*VERT)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*VERT)+gx;
                int bottomRight = bottomLeft + 1;

                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }

        return loader.loadToVAO(vertices, textureCoords, normals, indices);
    }

    private Vector3f calculateNormal(int x, int z, BufferedImage image) {
        float height1 = sampleHeight(x - 3, z, image) * 20 * 4; //L
        float height2 = sampleHeight(x + 3, z, image) * 20 * 4; //R
        float height3 = sampleHeight(x, z + 3, image) * 20 * 4; //D
        float height4 = sampleHeight(x, z - 3, image) * 20 * 4; //U

        Vector3f normal = new Vector3f(height1 - height2, 2f, height4 - height3);
        normal.normalise();

        return normal;
    }

    public static float sampleHeight(int x, int z, BufferedImage image) {
        int sx = (int) ((x + 0f) / VERT * 1024);
        int sz = (int) ((z + 0f) / VERT * 1024);

        if(sx < 3 || sx >= 2048 / 2 - 3 || sz < 3 || sz >= 2048 / 2 - 3) return 0;

        return new Color(image.getRGB(sx, sz)).getRed() / 255f;
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public Model getModel() {
        return model;
    }

    public TerrainTexturePack getTexturePack() {
        return texture;
    }
}
