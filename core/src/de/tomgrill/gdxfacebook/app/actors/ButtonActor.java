package de.tomgrill.gdxfacebook.app.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ButtonActor extends Actor {

	Texture texture;

	public ButtonActor(Texture region) {
		this.texture = region;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		Color color = getColor();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		batch.draw(texture, getX(), getY(), getWidth(), getHeight());
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}
}
