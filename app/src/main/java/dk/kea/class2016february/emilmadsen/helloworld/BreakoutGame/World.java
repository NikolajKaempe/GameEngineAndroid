package dk.kea.class2016february.emilmadsen.helloworld.BreakoutGame;

import java.util.ArrayList;
import java.util.List;

import dk.kea.class2016february.emilmadsen.helloworld.CollisionListener;

public class World
{
    public static final float MIN_X = 0;
    public static final float MAX_X = 319;
    public static final float MIN_Y = 32;
    public static final float MAX_Y = 479;
    boolean gameOver = false;
    int level = 1;
    int points = 0;
    Ball ball = new Ball();
    Paddle paddle = new Paddle();
    List<Block> blocks = new ArrayList<>();
    CollisionListener collisionListener;

    public World(CollisionListener collisionListener)
    {
        generateBlocks();
        this.collisionListener = collisionListener;

    }

    public void update(float deltaTime, float accelX, int touchX)
    {

        if(ball.y + Ball.HEIGHT > MAX_Y)
        {
            gameOver = true;
            return;
        }

        if(blocks.size() == 0)
        {
            generateBlocks();
            ball = new Ball();
            ball.velocityX = ball.velocityX * (1.0f + (float)level*0.1f);
            ball.velocityY = ball.velocityY * (1.0f + (float)level*0.1f);
            paddle = new Paddle();
        }

        //ball.velocityY += 20;
        ball.x = ball.x + ball.velocityX * deltaTime;
        ball.y = ball.y + ball.velocityY * deltaTime;

        paddle.x = paddle.x - (accelX *50 * deltaTime);
        if(touchX > 0) { paddle.x = touchX - (int) paddle.WIDTH /2; }

        this.collideWalls();
        this.collideBallPaddle();
        this.collideBallBlocks(deltaTime);
    }

    private void collideBallBlocks(float deltaTime)
    {
        Block block;
        int stop = blocks.size();
        for(int i = 0; i < stop;i++)
        {
            block = blocks.get(i);
            if (collideRects(ball.x, ball.y, ball.WIDTH,ball.HEIGHT,block.x,block.y,Block.WIDTH,Block.HEIGHT))
            {
                points = points + ((10-block.type)* 20);
                collisionListener.collisionBlock();
                blocks.remove(i);
                i--; stop--;
                float oldVelocityX = ball.velocityX;
                float oldVelocityY = ball.velocityY;
                reflectBall(ball,block);
                ball.x = ball.x - oldVelocityX * deltaTime * 1.01f;
                ball.y = ball.y - oldVelocityY * deltaTime * 1.01f;

            }
        }
    }

    private void reflectBall(Ball ball, Block block)
    {
        // Top-Left corner of the block is hit
        if (collideRects(ball.x,ball.y,Ball.WIDTH,Ball.HEIGHT,
                block.x,block.y,0,0))
        {
            if (ball.velocityX > 0) { ball.velocityX = -ball.velocityX;}
            if (ball.velocityY > 0) { ball.velocityY = -ball.velocityY; }
        }

        // Top-Right corner of the block is hit
        else if (collideRects(ball.x,ball.y,Ball.WIDTH,Ball.HEIGHT,
                block.x + Block.WIDTH,block.y,0,0))
        {
            if (ball.velocityX < 0) { ball.velocityX = -ball.velocityX;}
            if (ball.velocityY > 0) { ball.velocityY = -ball.velocityY; }
        }
        // Lower-Left corner of the block is hit
        else if (collideRects(ball.x,ball.y,Ball.WIDTH,Ball.HEIGHT,
                block.x,block.y + Block.HEIGHT,0,0))
        {
            if (ball.velocityX > 0) { ball.velocityX = -ball.velocityX;}
            if (ball.velocityY < 0) { ball.velocityY = -ball.velocityY; }
        }
        // Lower-Right corner of the block is hit
        else if (collideRects(ball.x,ball.y,Ball.WIDTH,Ball.HEIGHT,
                block.x + Block.WIDTH,block.y + Block.HEIGHT,0,0))
        {
            if (ball.velocityX < 0) { ball.velocityX = -ball.velocityX;}
            if (ball.velocityY < 0) { ball.velocityY = -ball.velocityY; }
        }

        // Top-Edge of the block is hit
        else if (collideRects(ball.x,ball.y,Ball.WIDTH,Ball.HEIGHT,
                block.x ,block.y ,Block.WIDTH,0))
        {
            if (ball.velocityY > 0) { ball.velocityY = -ball.velocityY; }
        }

        // Lower-Edge of the block is hit
        else if (collideRects(ball.x,ball.y,Ball.WIDTH,Ball.HEIGHT,
                block.x ,block.y + Block.HEIGHT ,Block.WIDTH,0))
        {
            if (ball.velocityY < 0) { ball.velocityY = -ball.velocityY; }
        }

        // Left-Edge of the block is hit
        else if (collideRects(ball.x,ball.y,Ball.WIDTH,Ball.HEIGHT,
                block.x ,block.y ,0,Block.HEIGHT))
        {
            if (ball.velocityX > 0) { ball.velocityX = -ball.velocityX; }
        }

        // Right-Edge of the block is hit
        else if (collideRects(ball.x,ball.y,Ball.WIDTH,Ball.HEIGHT,
                block.x + Block.WIDTH ,block.y ,0,Block.HEIGHT))
        {
            if (ball.velocityX < 0) { ball.velocityX = -ball.velocityX; }
        }
    }

    private boolean collideRects(float rect1X, float rect1Y, float rect1Width, float rect1Height,
                                 float rect2X, float rect2Y, float rect2Width, float rect2Height)
    {

        // Top and Bottom Collision
        if (rect1X < rect2X+rect2Width &&
                rect1X + rect1Width > rect2X &&
                rect1Y < rect2Y + rect2Height &&
                rect1Y + rect1Height > rect2Y)
        {
            return true;
        }

        return false;
    }

    private void collideBallPaddle()
    {
        if(ball.y > paddle.y) return;

        // Checks if ball hits left side of paddle
        if(ball.x + Ball.WIDTH >= paddle.x && // If the ball hits the sides of paddle
                ball.x + Ball.WIDTH < paddle.x + 3 &&
                ball.y + Ball.HEIGHT + 2 >= paddle.y)
        {
            ball.y = paddle.y - Ball.HEIGHT - 0; // "Resetter" ball til oversiden af paddle
            ball.velocityY = -ball.velocityY; //ændrer retning på bold
            if(ball.velocityX > 0) ball.velocityX = -ball.velocityX;
            collisionListener.collisionPaddle();
            return;
        }

        // Checks if ball hits right side of paddle
        if(ball.x < paddle.x+Paddle.WIDTH &&
                ball.x > paddle.x + Paddle.WIDTH - 3 &&
                ball.y + Ball.HEIGHT + 2 >= paddle.y)
        {
            ball.y = paddle.y - Ball.HEIGHT - 0;
            ball.velocityY = -ball.velocityY;
            if(ball.velocityX < 0) ball.velocityX = -ball.velocityX;
            collisionListener.collisionPaddle();
            return;
        }

        //Checks if the ball hits the paddle (middle).
        if(ball.x + Ball.WIDTH >= paddle.x &&
                ball.x < paddle.x + Paddle.WIDTH &&
                ball.y + Ball.HEIGHT + 2 >= paddle.y)
        {
            if (ball.velocityY > 0)
            {
                ball.y = paddle.y - Ball.HEIGHT - 0;
                ball.velocityY = -ball.velocityY;
                collisionListener.collisionPaddle();
            }
        }
    }

    private void collideWalls()
    {
        if(ball.x < MIN_X)
        {
            ball.velocityX = -ball.velocityX;
            ball.x = MIN_X;
            collisionListener.collisionWall();
        }
        else if(ball.x + Ball.WIDTH > MAX_X)
        {
            ball.velocityX = -ball.velocityX;
            ball.x = MAX_X - Ball.WIDTH;
            collisionListener.collisionWall();
        }
        if(ball.y < MIN_Y)
        {
            ball.velocityY = -ball.velocityY;
            ball.y = MIN_Y;
            collisionListener.collisionWall();
        }

        if (paddle.x < MIN_X)
        {
            paddle.x = MIN_X;
        }
        else if (paddle.x + paddle.WIDTH > MAX_X)
        {
            paddle.x = MAX_X - paddle.WIDTH;
        }
    }

    private void generateBlocks()
    {
        blocks.clear();
        int startY = (int)(MIN_Y + Ball.HEIGHT * 1.8) + level*20;
        int startX = (int)(MIN_X + Block.WIDTH / 2);
        int type = 0;
        if (startY > 200) { startY = 200; } // dont go to low
        for(int y = startY; y < startY + 8*Block.HEIGHT; y = y+ (int)Block.HEIGHT, type++)
        {
            for(int x=startX; x < 320-startX ;x = x+(int)Block.WIDTH)
            {
                   blocks.add(new Block(x,y,type));
            }
        }
    }
}
