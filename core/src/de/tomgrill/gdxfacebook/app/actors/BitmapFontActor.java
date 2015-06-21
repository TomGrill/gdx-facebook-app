package de.tomgrill.gdxfacebook.app.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BitmapFontActor extends Actor {

	private BitmapFont font;
	private String text = "";

	public BitmapFontActor(BitmapFont font) {
		this.font = font;

	}

	public void setText(String text) {
		if (!text.equals(this.text)) {
			this.text = text;
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		font.draw(batch, text, getX(), getY());
	}

}
