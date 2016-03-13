package com.krecior.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.krecior.Manager;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 540;
		config.height = 960;

		/**
		 * .----------------.  .----------------.  .----------------.  .----------------.
		 | .--------------. || .--------------. || .--------------. || .--------------. |
		 | |     ____     | || |    _______   | || |      __      | || |  ___  ____   | |
		 | |   .'    `.   | || |   /  ___  |  | || |     /  \     | || | |_  ||_  _|  | |
		 | |  /  .--.  \  | || |  |  (__ \_|  | || |    / /\ \    | || |   | |_/ /    | |
		 | |  | |    | |  | || |   '.___`-.   | || |   / ____ \   | || |   |  __'.    | |
		 | |  \  `--'  /  | || |  |`\____) |  | || | _/ /    \ \_ | || |  _| |  \ \_  | |
		 | |   `.____.'   | || |  |_______.'  | || ||____|  |____|| || | |____||____| | |
		 | |              | || |              | || |              | || |              | |
		 | '--------------' || '--------------' || '--------------' || '--------------' |
		   '----------------'  '----------------'  '----------------'  '----------------'



		 COMMMMITITEEEDEDEDED     fas 214214124 421421 12412
		 *421124412
		 */
		//Wojtas test to moja linijka
		//test kolizja
		new LwjglApplication(new Manager(null, null, null, null), config);

	}
}