package models;

import memory.Loader;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OBJLoader {
    public static Model loadModel(String filename, Loader loader) {
        File file = new File("resources/" + filename);

        Scanner scanner = null;

        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> uv = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        float[] verticesArray = null;
        float[] normalsArray = null;
        float[] uvArray = null;

        int[] indicesArray = null;

        String line = null;

        while(scanner.hasNextLine()) {
            line = scanner.nextLine();

            String[] tokens = line.split(" ");

            if(line.startsWith("v ")) {
                Vector3f vertex = new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));

                vertices.add(vertex);
            }else if(line.startsWith("vt ")) {
                Vector2f uvCoord = new Vector2f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));

                uv.add(uvCoord);
            }else if(line.startsWith("vn ")) {
                Vector3f normal = new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));

                normals.add(normal);
            }else if(line.startsWith("f ")) {
                uvArray = new float[vertices.size() * 2];
                normalsArray = new float[vertices.size() * 3];

                break;
            }
        }

        while(scanner.hasNextLine()) {
            String[] tokens = line.split(" ");

            String[] v1 = tokens[1].split("/");
            String[] v2 = tokens[2].split("/");
            String[] v3 = tokens[3].split("/");

            processVertex(v1, indices, uv, normals, uvArray, normalsArray);
            processVertex(v2, indices, uv, normals, uvArray, normalsArray);
            processVertex(v3, indices, uv, normals, uvArray, normalsArray);

            line = scanner.nextLine();
        }

        scanner.close();

        verticesArray = new float[vertices.size() * 3];
        indicesArray = new int[indices.size()];

        int vp = 0;

        for (Vector3f vertex : vertices) {
            verticesArray[vp ++] = vertex.x;
            verticesArray[vp ++] = vertex.y;
            verticesArray[vp ++] = vertex.z;
        }

        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }

        return loader.loadToVAO(verticesArray, uvArray, normalsArray, indicesArray);
    }

    private static void processVertex(String[] data, List<Integer> indices, List<Vector2f> uv, List<Vector3f> normal, float[] uvArray, float[] normalsArray) {
        int currentPointer = Integer.parseInt(data[0]) - 1;
        indices.add(currentPointer);

        Vector2f currentUv = uv.get(Integer.parseInt(data[1]) - 1);

        uvArray[currentPointer * 2] = currentUv.x;
        uvArray[currentPointer * 2 + 1] = currentUv.y;

        Vector3f currentNormal = normal.get(Integer.parseInt(data[2]) - 1);

        normalsArray[currentPointer * 3] = currentNormal.x;
        normalsArray[currentPointer * 3 + 1] = currentNormal.y;
        normalsArray[currentPointer * 3 + 2] = currentNormal.z;
    }
}
