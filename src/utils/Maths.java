package utils;

import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Maths {
    public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, Vector3f scale) {
        Matrix4f matrix = new Matrix4f();

        matrix.setIdentity();

        Matrix4f.translate(translation, matrix, matrix);

        Matrix4f.rotate((float) Math.toRadians(rotation.getX()), new Vector3f(1, 0, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rotation.getY()), new Vector3f(0, 1, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rotation.getZ()), new Vector3f(0, 0, 1), matrix, matrix);

        Matrix4f.scale(scale, matrix, matrix);

        return matrix;
    }

    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.setIdentity();

        Matrix4f.rotate((float) Math.toRadians(camera.getRotation().x), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getRotation().y), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getRotation().z), new Vector3f(0, 0, 1), viewMatrix, viewMatrix);

        Vector3f negativeCameraPos = new Vector3f(-camera.getPosition().x, -camera.getPosition().y, -camera.getPosition().z);

        Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);

        return viewMatrix;
    }
}
