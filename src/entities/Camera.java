package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
    private static final float MOVEMENT_SPEED = .2f;
    private Vector3f position;
    private Vector3f rotation;

    public void move() {
        rotation.y += Mouse.getDX() / 2.;
        rotation.x += -Mouse.getDY();

        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            position.x -= Math.cos((rotation.getY() + 90) * Math.PI / 180.) * MOVEMENT_SPEED;
            position.z -= Math.sin((rotation.getY() + 90) * Math.PI / 180.) * MOVEMENT_SPEED;

//            position.z -= MOVEMENT_SPEED;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            position.x += Math.cos((rotation.getY() + 90) * Math.PI / 180.) * MOVEMENT_SPEED;
            position.z += Math.sin((rotation.getY() + 90) * Math.PI / 180.) * MOVEMENT_SPEED;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            position.x -= Math.cos((rotation.getY()) * Math.PI / 180.) * MOVEMENT_SPEED;
            position.z -= Math.sin((rotation.getY()) * Math.PI / 180.) * MOVEMENT_SPEED;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            position.x += Math.cos((rotation.getY()) * Math.PI / 180.) * MOVEMENT_SPEED;
            position.z += Math.sin((rotation.getY()) * Math.PI / 180.) * MOVEMENT_SPEED;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
            position.y -= MOVEMENT_SPEED;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
            position.y += MOVEMENT_SPEED;
        }
    }

    public Camera(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }
}
