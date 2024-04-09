package inf112.skeleton.app.view.States;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;

import inf112.skeleton.app.controller.buttons.menu.ExitButton;
import inf112.skeleton.app.controller.buttons.menu.PauseButton;
import inf112.skeleton.app.controller.buttons.menu.RestartButton;
import inf112.skeleton.app.controller.buttons.menu.SpeedButton;

import inf112.skeleton.app.controller.buttons.upgrade.UpgradeDamageButton;
import inf112.skeleton.app.controller.buttons.upgrade.UpgradeFireRateButton;
import inf112.skeleton.app.controller.buttons.upgrade.UpgradeRangeButton;
import inf112.skeleton.app.model.SkadedyrModel;
import inf112.skeleton.app.model.catmenu.CatMenu;
import inf112.skeleton.app.model.entities.Projectile;
import inf112.skeleton.app.model.entities.cat.Cat;
import inf112.skeleton.app.model.entities.rat.Rat;
import inf112.skeleton.app.view.SkadedyrView;

public class PlayState extends State {
    private ShapeRenderer shapeRenderer;
    private SkadedyrModel model;
    private BitmapFont font;
    private Stage stage;
    private CatMenu catMenu;
    private UpgradeFireRateButton upgradeFireRateButton;
    private UpgradeRangeButton upgradeRangeButton;
    private UpgradeDamageButton upgradeDamageButton;
    private PauseButton pauseButton;



    protected PlayState(GameStateManager gsm, SkadedyrModel model) {
        super(gsm);
        this.model = model;
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();
        font.setColor(Color.BLACK);
        this.catMenu = model.getBuyMenu();
        catMenu.init();
        this.stage = new Stage();
        
        //new PauseButton(model, stage);
        new SpeedButton(model, stage);
        new RestartButton(model, stage);
        new ExitButton(model, stage);
        pauseButton = new PauseButton(model, stage);
       
        
        upgradeFireRateButton = new UpgradeFireRateButton(model, stage);
        upgradeRangeButton = new UpgradeRangeButton(model, stage);
        upgradeDamageButton = new UpgradeDamageButton(model, stage);
        

        Gdx.input.setInputProcessor(stage);
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
        pauseButton.updateButtonAppearance();
        upgradeFireRateButton.updateButtonAppearance();
        upgradeRangeButton.updateButtonAppearance();
        upgradeDamageButton.updateButtonAppearance();



        batch.begin();
        batch.draw(SkadedyrView.mapTexture, 0, 0); 
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
        drawProjectiles(batch);
        batch.end();

        // Draw game status continuously
        batch.begin();
        drawGameStatus(batch);
        batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
       

    }

    private void drawGameStatus(SpriteBatch batch) {
        font.draw(batch, "Velkommen til Skadedyrkontrollørerne", 200, 10);
        font.draw(batch, "Dine liv: " + model.getLives(), 1000, 760);
        font.draw(batch, "Dine penger: " + model.getMoney(), 1000, 840);
        font.draw(batch, "Din Score: " + model.getPoints(), 1000, 800);
        font.draw(batch, "Level: " + model.getLevel(), 1000, 720);
        font.draw(batch, model.nextWaveText(), 900, 600);
    }

    public void drawProjectiles(SpriteBatch batch) {
        for (Projectile projectile : model.getProjectiles()) {
            Rectangle projectileRect = projectile.getRectangle();
            float width = projectileRect.width / 15;
            float height = projectileRect.height / 15;
            batch.draw(projectile.getTexture(), projectileRect.x, projectileRect.y, width, height);
        }
    }


    private void drawCatMenu(SpriteBatch batch) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        catMenu.draw(shapeRenderer);
        shapeRenderer.end();

        int playerMoney = model.getMoney();
        batch.begin();
        catMenu.draw(batch, playerMoney);
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
