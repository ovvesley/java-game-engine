package jade;

public abstract class Scene {
    protected Camera camera;
    public abstract void update(float delta);
    public abstract void init();
}
