package com.theironyard.superkoalio;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;;


public class SuperKoalio extends ApplicationAdapter {
    SpriteBatch batch;
    TextureRegion stand;
    TextureRegion jump;
    Animation walk;
    FitViewport viewport;
    OrthogonalTiledMapRenderer renderer;
    OrthographicCamera camera;


    float x = 0;
    float y = 0;
    float xVelocity = 0;
    float yVelocity = 0;
    float time = 0;
    boolean canJump = true;

    final float MAX_VELOCITY = 1000;
    final float MAX_JUMP_VELOCITY = 1000;
    final int WIDTH = 18;
    final int HEIGHT = 26;
    final int DRAW_WIDTH = WIDTH*3;
    final int DRAW_HEIGHT = HEIGHT*3;
    final int GRAVITY = -50;

    @Override
    public void create () {
        batch = new SpriteBatch();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        TmxMapLoader loader = new TmxMapLoader();
        renderer = new OrthogonalTiledMapRenderer(loader.load("level1.tmx"), 1/DRAW_HEIGHT);
        camera = new OrthographicCamera();

        Texture sheet = new Texture("koalio.png");
        TextureRegion[][] tiles = TextureRegion.split(sheet, WIDTH, HEIGHT);
        stand = tiles[0][0];
        jump = tiles [0][1];
        walk = new Animation(0.1f, tiles[0][2], tiles [0][3], tiles [0][4]);
    }

    @Override
    public void render () {
        move();
        draw();

    }
    @Override
    public void resize(int width, int height){
        viewport.update(width, height);
        camera.setToOrtho(false, width, height);
    }

    void move() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)&& canJump) {
            yVelocity = MAX_JUMP_VELOCITY;
            canJump = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            yVelocity = MAX_VELOCITY * -1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            xVelocity = MAX_VELOCITY;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            xVelocity = MAX_VELOCITY * -1;
        }

        yVelocity += GRAVITY;

        x += xVelocity * Gdx.graphics.getDeltaTime();
        y += yVelocity * Gdx.graphics.getDeltaTime();

        if (y < 0){
            y = 0;
            canJump = true;
        }

        xVelocity *= 0.7;
        yVelocity *= 0.9;


    }

    void draw() {
        renderer.setView(camera);
        renderer.render();
        time += Gdx.graphics.getDeltaTime();

        TextureRegion img;
        if (y >0) {
            img= jump;
        }
        else if (Math.round(xVelocity) != 0) {
            img= walk.getKeyFrame(time, true);
        }
        else {
            img = stand;
        }


        Gdx.gl.glClearColor( 0.5f, 0.5f ,1 ,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        if (xVelocity >= 0){
            batch.draw(img, x, y, DRAW_WIDTH, DRAW_HEIGHT);
        }
        else {
            batch.draw(img, x + DRAW_WIDTH, y, DRAW_WIDTH * -1, DRAW_HEIGHT);
        }
        batch.end();

    }
}
