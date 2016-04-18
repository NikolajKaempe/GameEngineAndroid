package dk.kea.class2016february.emilmadsen.helloworld.BreakoutGame;

/**
 * Created by Nikol_000 on 11-04-2016.
 */
public class Block
{
    public static final float HEIGHT = 18,WIDTH = 40;
    float x,y;
    int type;

    public Block(float x, float y, int type)
    {
        this.x = x;
        this.y = y;
        this.type = type;
    }
}
