package dk.kea.class2016february.emilmadsen.helloworld.BreakoutGame;

import android.graphics.Bitmap;

import dk.kea.class2016february.emilmadsen.helloworld.Game;

public class WorldRenderer
{
    Game game;
    World world;
    Bitmap ballImage, paddleImage, blocksImage;

    public WorldRenderer(Game game, World world)
    {
        this.game = game;
        this.world = world;
        this.ballImage = game.loadBitmap("ball.png");
        this.paddleImage = game.loadBitmap("paddle.png");
        this.blocksImage = game.loadBitmap("blocks.png");
    }

    public void render()
    {
        game.drawBitmap(ballImage, (int)world.ball.x, (int)world.ball.y);
        game.drawBitmap(paddleImage,(int)world.paddle.x,(int)world.paddle.y);
        int blocksAmount = world.blocks.size();
        Block currentBlock;
        for (int i = 0; i < blocksAmount;i++)
        {
            currentBlock = world.blocks.get(i);
            game.drawBitmap(blocksImage,(int)currentBlock.x,(int)currentBlock.y,
                    0,currentBlock.type*(int)Block.HEIGHT,
                    (int)Block.WIDTH,(int)Block.HEIGHT);
        }
    }
}
