package water;

import memory.Loader;
import models.Model;

public class Water {
    private static final float SIZE = 100;
    private static final int VERT = 256;

    private float x, z;

    private Model model;

    public Water(int gridX, int gridZ, Loader loader) {
        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;

        this.model = generateTerrain(loader);
    }

    private Model generateTerrain(Loader loader){
        int count = VERT * VERT;

        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count*2];

        int[] indices = new int[6*(VERT-1)*(VERT-1)];

        int vertexPointer = 0;

        for(int i=0;i<VERT;i++){
            for(int j=0;j<VERT;j++){
                vertices[vertexPointer*3] = (float)j/((float)VERT - 1) * SIZE;
                vertices[vertexPointer*3+1] = 1.5f;
                vertices[vertexPointer*3+2] = (float)i/((float)VERT - 1) * SIZE;

                normals[vertexPointer * 3] = 0;
                normals[vertexPointer * 3 + 1] = 1;
                normals[vertexPointer * 3 + 2] = 0;

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

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public Model getModel() {
        return model;
    }
}
