package com.krecior.menu.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.krecior.Manager;
import com.krecior.utils.Container;
import com.krecior.utils.TextLabel;

/**
 * Created by Wojciech Osak on 2015-09-29.
 */
public class MenuMsgBox extends Group {
    private static boolean IS_SHOWING = false;

    private Image backgroundDescription;
    private String description;
    private String leftButtonText;
    private String rightButtonText;
    private TextLabel descriptionLabel;
    private Stage stageHandle;
    private MenuMsgBoxButton rightButton, leftButton;
    private onButtonClickedListener buttonClickListener;
    private InputMultiplexer inputMultuplexer;

    public MenuMsgBox(Stage stage, String description, String leftButtonText, String rightButtonText) {
        this.description = description;
        this.leftButtonText = leftButtonText;
        this.rightButtonText = rightButtonText;
        stageHandle = stage;
        initImages();
        initTextLabels();
        scaleAndPosition();
    }

    public MenuMsgBox(Stage stage, String description, String okButtonText) {
        this.description = description;
        this.leftButtonText = okButtonText;
        stageHandle = stage;
        initImages();
        initTextLabels();
        scaleAndPosition();
    }

    public void setDescription(String description) {
        this.description = description;
        descriptionLabel.setText(description);
        scaleAndPosition();
    }


    private void scaleAndPosition() {
        backgroundDescription.setSize(Gdx.app.getGraphics().getWidth() * 0.8f, Gdx.app.getGraphics().getWidth() * 0.8f * 0.59f);

        backgroundDescription.setPosition(Gdx.app.getGraphics().getWidth() / 2 - backgroundDescription.getWidth() / 2,
                Gdx.app.getGraphics().getHeight() / 2 - backgroundDescription.getHeight() / 2);

        descriptionLabel.setPosition(backgroundDescription.getX() + backgroundDescription.getWidth() / 2,
                backgroundDescription.getY() + backgroundDescription.getHeight() / 2 + descriptionLabel.getHeight() / 8);

        if (rightButton != null)
            rightButton.setPosition(backgroundDescription.getX() + backgroundDescription.getWidth() - rightButton.getWidth()
                    , backgroundDescription.getY() - rightButton.getHeight() * 1.2f);
        if (leftButton != null) {
            if (rightButton == null)//center
                leftButton.setPosition(backgroundDescription.getX() + backgroundDescription.getWidth() / 2 - leftButton.getWidth() / 2
                        , backgroundDescription.getY() - leftButton.getHeight() * 1.2f);
            else
                leftButton.setPosition(backgroundDescription.getX()
                        , backgroundDescription.getY() - leftButton.getHeight() * 1.2f);
        }

    }

    private void initImages() {
        backgroundDescription = new Image(Container.getTextureRegion("gfx/msgBox/score_label.png"));

        if (leftButtonText != null)
            leftButton = new MenuMsgBoxButton(leftButtonText) {
                @Override
                public void onTouchDown() {
                    if (buttonClickListener != null)
                        buttonClickListener.onClick(0);
                }
            };

        if (rightButtonText != null)
            rightButton = new MenuMsgBoxButton(rightButtonText) {
                @Override
                public void onTouchDown() {
                    if (buttonClickListener != null)
                        buttonClickListener.onClick(1);
                }
            };
    }

    private void initTextLabels() {
        descriptionLabel = new TextLabel(TextLabel.Font.ROBOTO, description);
        descriptionLabel.setAlign(Align.center);
    }

    @Override
    public float getWidth() {
        return backgroundDescription.getWidth();
    }

    @Override
    public float getHeight() {
        return backgroundDescription.getHeight();
    }

    @Override
    public float getX() {
        return backgroundDescription.getX();
    }

    @Override
    public float getY() {
        return backgroundDescription.getY();
    }

    public void show() {
        IS_SHOWING = true;

        for (Actor actor : stageHandle.getActors())
            actor.setTouchable(Touchable.disabled);

        stageHandle.addActor(this);
        if (rightButton != null)
            stageHandle.addActor(rightButton);
        if (leftButton != null)
            stageHandle.addActor(leftButton);

        if (rightButton != null)
            Manager.inputMultiplexer.addProcessor(rightButton);
        if (leftButton != null)
            Manager.inputMultiplexer.addProcessor(leftButton);
        stageHandle.getBatch().setColor(stageHandle.getBatch().getColor().r, stageHandle.getBatch().getColor().g,
                stageHandle.getBatch().getColor().b, 0.2f);
    }

    public void show(InputMultiplexer inputMultiplexer) {
        IS_SHOWING = true;
        this.inputMultuplexer = inputMultiplexer;
        for (Actor actor : stageHandle.getActors())
            actor.setTouchable(Touchable.disabled);

        stageHandle.addActor(this);
        if (rightButton != null)
            stageHandle.addActor(rightButton);
        if (leftButton != null)
            stageHandle.addActor(leftButton);

        if (rightButton != null)
            inputMultiplexer.addProcessor(rightButton);
        if (leftButton != null)
            inputMultiplexer.addProcessor(leftButton);
        stageHandle.getBatch().setColor(stageHandle.getBatch().getColor().r, stageHandle.getBatch().getColor().g,
                stageHandle.getBatch().getColor().b, 0.2f);
    }


    public void hide() {
        IS_SHOWING = false;
        stageHandle.getBatch().setColor(stageHandle.getBatch().getColor().r, stageHandle.getBatch().getColor().g,
                stageHandle.getBatch().getColor().b, 0f);

        this.remove();
        if (rightButton != null)
            rightButton.remove();
        if (leftButton != null)
            leftButton.remove();

        for (Actor actor : stageHandle.getActors())
            actor.setTouchable(Touchable.enabled);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (IS_SHOWING) {
            super.draw(batch, parentAlpha);
            setBounds(getX(), getY(), getWidth(), getHeight());
            backgroundDescription.draw(batch, parentAlpha);
            descriptionLabel.draw(batch, parentAlpha);
        }

    }

    public MenuMsgBox setButtonClickListener(onButtonClickedListener buttonClickListener) {
        this.buttonClickListener = buttonClickListener;
        return this;
    }

    public interface onButtonClickedListener {
        /**
         * @param button 0 - left or center button, 1 - right button
         */
        void onClick(int button);
    }

    private class MenuMsgBoxButton extends Actor implements InputProcessor {
        private TextLabel textLabel;
        private Image image;
        private boolean canClick = true;

        public MenuMsgBoxButton(String text) {
            textLabel = new TextLabel(TextLabel.Font.ROBOTO, text);
            textLabel.setAlign(Align.left);
            image = new Image(Container.getTextureRegion("gfx/msgBox/score_label.png"));
            image.setOrigin(image.getWidth() / 2, image.getHeight() / 2);
            image.setSize(Gdx.app.getGraphics().getWidth() * 0.35f, Gdx.app.getGraphics().getWidth() * 0.18f * 0.59f);
        }

        public void setCanClick(boolean canClick) {
            this.canClick = canClick;
        }

        public void setTextLabel(String text) {
            textLabel.setText(text);
        }

        public void onTouchDown() {

        }

        @Override
        public void setPosition(float x, float y) {
            image.setPosition(x, y);
            textLabel.setPosition(x + image.getWidth() / 2 - textLabel.getWidth() / 2
                    , y + image.getHeight() - image.getHeight() / 2 + textLabel.getHeight() / 2);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            setBounds(image.getX(), image.getY(), image.getWidth(), image.getHeight());
            image.draw(batch, parentAlpha);
            textLabel.draw(batch, parentAlpha);
        }

        @Override
        public float getY() {
            return image.getY();
        }

        @Override
        public float getX() {
            return image.getX();
        }

        @Override
        public float getWidth() {
            return image.getWidth();
        }

        @Override
        public boolean remove() {
            image.remove();
            textLabel.remove();
            return super.remove();
        }

        @Override
        public float getHeight() {
            return image.getHeight();
        }

        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            if (isImageClicked(screenX, (Gdx.app.getGraphics().getHeight() - screenY))) {
                image.setScale(0.8f);
            }
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            if (isImageClicked(screenX, (Gdx.app.getGraphics().getHeight() - screenY))) {
                image.setScale(1f);
                if (canClick)
                    onTouchDown();
            }

            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
        }

        private boolean isImageClicked(float x, float y) {
            return x > image.getX() && x < image.getX() + image.getWidth()
                    && y > image.getY() && y < image.getY() + image.getHeight();
        }
    }
}
