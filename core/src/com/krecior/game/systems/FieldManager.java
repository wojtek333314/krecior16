package com.krecior.game.systems;

import com.krecior.game.GameScreen;
import com.krecior.game.enums.FieldType;
import com.krecior.game.objects.Field;

import java.util.concurrent.CopyOnWriteArrayList;

public class FieldManager {
	private CopyOnWriteArrayList<Field> pFields;


	public FieldManager() {
		pFields = new CopyOnWriteArrayList<Field>();
		createFields();
	}

	public CopyOnWriteArrayList<Field> getFields() {
		return pFields;
	}

	private void createFields() {
		pFields = new CopyOnWriteArrayList<Field>();
		
		for(int i = 0; i < GameScreen.COLUMNS * GameScreen.ROWS; i++) {
			pFields.add(new Field(i, FieldType.NORMAL));
		}
	}
}
