package entities;

import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

public class Entity {
    private TexturedModel model;

    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;

    public Entity(TexturedModel model, Vector3f position, Vector3f rotation, Vector3f scale) {
        this.model = model;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public void translate(Vector3f offset) {
        position = new Vector3f(position.x + offset.x, position.y + offset.y, position.z + offset.z);
    }

    public void rotate(Vector3f amount) {
        rotation = new Vector3f(rotation.x + amount.x, rotation.y + amount.y, rotation.z + amount.z);
    }

    public void scale(Vector3f amount) {
        scale = new Vector3f(scale.x + amount.x, scale.y + amount.y, scale.z + amount.z);
    }

    public TexturedModel getModel() {
        return model;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
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

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }
}
