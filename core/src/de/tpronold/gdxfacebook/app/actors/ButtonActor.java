package de.tpronold.gdxfacebook.app.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ButtonActor extends Actor {

	TextureRegion region;

	public ButtonActor(TextureRegion region) {
		this.region = region;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		Color color = getColor();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		batch.draw(region, getX(), getY(), 0, 0, getWidth(), getHeight(), 1, 1, 0);
	}
}
