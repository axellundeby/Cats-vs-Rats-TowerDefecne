package inf112.skeleton.app.controller.buttons.menu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import inf112.skeleton.app.controller.buttons.ButtonFactory;
import inf112.skeleton.app.controller.buttons.Buttons;
import inf112.skeleton.app.model.SkadedyrModel;

public class PauseButton extends Buttons {


    public PauseButton(SkadedyrModel model, Stage stage) {
        super(model, stage);
    }

    @Override
    protected void setupButton() {
        button = ButtonFactory.createImageButton("buttons_game/Spill_Play:Play.png", "buttons_game/Spill_Play:Pause.png");
        button.setSize(100, 100);
        button.setPosition(600, 50);

        

        button.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                model.setPause();
                updateButtonAppearance();
            }
        });
    }

        @Override
        public void updateButtonAppearance() {
    //     if(model.isPaused()){
    //         button.getStyle().up = new TextureRegionDrawable(new TextureRegion(new Texture("buttons_game/Spill_Play:Play.png")));
    //     } else {
            
    //         button.getStyle().up = new TextureRegionDrawable(new TextureRegion(new Texture("buttons_game/Spill_Play:Pause.png")));
    //     }
        }

        
    }

