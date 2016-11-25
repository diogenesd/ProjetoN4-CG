package n4;



public enum Cor {
    RED(new float[]{1.0f, 0.0f, 0.0f, 1.0f}),
    GREEN(new float[]{0.0f, 1.0f, 0.0f, 1.0f}),
    BLUE(new float[]{0.0f, 0.0f, 1.0f, 1.0f}),
    LBLUE(new float[]{0.6f, 0.8f, 1.0f, 1.0f}),
    YELLOW(new float[]{1.0f, 1.0f, 0.0f, 1.0f}),
    WHITE(new float[]{1.0f, 1.0f, 1.0f, 1.0f}),
    BLACK(new float[]{0.0f, 0.0f, 0.0f, 1.0f}),
    GREY(new float[]{0.4f, 0.4f, 0.4f, 1.0f}),
    DGREY(new float[]{0.2f, 0.2f, 0.2f, 1.0f}),
    BROWN(new float[]{0.3f, 0.15f, 0.0f, 1.0f}),
    LBROWN(new float[]{0.7f, 0.6f, 0.4f, 1.0f});

    private final float[] rgba;

    Cor(float[] rgba) {
        this.rgba = rgba;
    }

    public float[] getRgba() {
        return rgba;
    }

    public float getR() {
        return rgba[0];
    }

    public float getG() {
        return rgba[1];
    }

    public float getB() {
        return rgba[2];
    }

    public float getA() {
        return rgba[3];
    }

}
