package models;

public class Model {
    private int vaoID;
    private int vertCount;

    public Model(int vaoID, int vertCount) {
        this.vaoID = vaoID;
        this.vertCount = vertCount;
    }

    public int getVaoID() {
        return vaoID;
    }

    public void setVaoID(int vaoID) {
        this.vaoID = vaoID;
    }

    public int getVertCount() {
        return vertCount;
    }

    public void setVertCount(int vertCount) {
        this.vertCount = vertCount;
    }
}
