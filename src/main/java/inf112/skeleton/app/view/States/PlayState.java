package inf112.skeleton.app.view.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;

import inf112.skeleton.app.model.SkadedyrModel;
import inf112.skeleton.app.model.catmenu.CatMenu;
import inf112.skeleton.app.model.entities.Projectile;
import inf112.skeleton.app.model.entities.cat.Cat;
import inf112.skeleton.app.model.entities.rat.Rat;
import inf112.skeleton.app.view.SkadedyrView;
import inf112.skeleton.app.view.buttons.ButtonFactory;

public class PlayState extends State {
    private ShapeRenderer shapeRenderer;
    private SkadedyrModel model;
    private BitmapFont font;
    private Stage stage;
    private ImageButton pauseButton;
    private CatMenu catMenu;


    protected PlayState(GameStateManager gsm, SkadedyrModel model) {
        super(gsm);
        this.model = model;
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();
        this.catMenu = model.getBuyMenu();
        catMenu.init();
        this.stage = new Stage();

        setupPauseButton();

        Gdx.input.setInputProcessor(stage);
    }

    private void setupPauseButton() {
        pauseButton = ButtonFactory.createImageButton("pauseUp.png", "playUp.png");
        pauseButton.setSize(100, 100);
        pauseButton.setPosition(1000, 200);

        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                model.setPause(); // Use the model's method to toggle pause state
                updatePauseButton(); // Update the button's appearance based on the new pause state
            }
        });

        stage.addActor(pauseButton);
        updatePauseButton(); // Ensure the button's appearance is correct at start
    }

    private void updatePauseButton() {
        Drawable newDrawable;
        if (model.isPaused()) {
            newDrawable = new TextureRegionDrawable(new TextureRegion(new Texture("playUp.png")));
        } else {
            newDrawable = new TextureRegionDrawable(new TextureRegion(new Texture("pauseUp.png")));
        }
        pauseButton.getStyle().up = newDrawable;


    }

    @Override
    public void handleInput() {
        if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
            GameStateManager.set(new MenuState(gsm, model));
        }
    }

    @Override
    public void update(float dt) {

        if (!model.isPaused()) {
            handleInput();
        }
    }

    @Override
    public void render(SpriteBatch batch) {

        ScreenUtils.clear(Color.GREEN);

        batch.begin();
        batch.draw(SkadedyrView.mapTexture, 0, 0); // Use the preloaded texture
        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Cat cat : model.getCats()) {
            Circle range = cat.getRangeCircle();
            shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 0.5f);
            shapeRenderer.circle(range.x, range.y, range.radius);
        }
        shapeRenderer.end();

        drawCatMenu(batch);

        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();
        drawCats(batch);
        drawRats(batch);

        font.draw(batch, "Velkommen til Skadedyrkontrollørerne", 200, 10);
        font.draw(batch, "Dine liv: " + model.getLives(), 1000, 760);
        font.draw(batch, "Dine penger: " + model.getMoney(), 1000, 840);
        font.draw(batch, "Din Score: " + model.getPoints(), 1000, 800);
        font.draw(batch, "Level: " + model.getLevel(), 1000, 720);
        font.draw(batch, "" + model.nextWaveText(), 850, 620);
        drawCats(batch);
        drawRats(batch);
        drawProjectiles(batch);
        batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

    }

    public void drawProjectiles(SpriteBatch batch) {
        for (Projectile projectile : model.getProjectiles()) {
            Rectangle projectileRect = projectile.getRectangle();
            float width = projectileRect.width / 15; 
            float height = projectileRect.height / 15;
            batch.draw(projectile.getTexture(), projectileRect.x, projectileRect.y, width, height);
        }
    }

    private void drawCatMenu(SpriteBatch batch){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        catMenu.draw(shapeRenderer);
        shapeRenderer.end();

        batch.begin();
        catMenu.draw(batch);
        batch.end();
    }

    public void drawCats(SpriteBatch batch) {
        for (Cat cat : model.getCats()) {
            Sprite catSprite = cat.getSprite();
            catSprite.draw(batch);
        }
    }

    public void drawRats(SpriteBatch batch) {
        for (Rat rat : model.getRats()) {
            Rectangle ratRect = rat.getRectangle();
            batch.draw(rat.getTexture(), ratRect.x, ratRect.y, ratRect.width, ratRect.height);
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
        shapeRenderer.dispose();
        font.dispose();

    }

}
