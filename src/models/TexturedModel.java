package models;

import textures.ModelTexture;

public class TexturedModel {
    private Model model;
    private ModelTexture modelTexture;

    public TexturedModel(Model model, ModelTexture modelTexture) {
        this.model = model;
        this.modelTexture = modelTexture;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public ModelTexture getModelTexture() {
        return modelTexture;
    }

    public void setModelTexture(ModelTexture modelTexture) {
        this.modelTexture = modelTexture;
    }
}
