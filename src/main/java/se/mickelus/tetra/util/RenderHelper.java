package se.mickelus.tetra.util;

public class RenderHelper {
	public static int getIntFromColor(float Red, float Green, float Blue) {
		int R = Math.round(255 * Red);
		int G = Math.round(255 * Green);
		int B = Math.round(255 * Blue);

		R = (R << 16) & 0x00FF0000;
		G = (G << 8) & 0x0000FF00;
		B = B & 0x000000FF;

		return 0xFF000000 | R | G | B;
	}
	public static int getIntFromColor(float Red, float Green, float Blue, float Alpha) {
		int R = Math.round(255 * Red);
		int G = Math.round(255 * Green);
		int B = Math.round(255 * Blue);
		int A = Math.round(255 * Alpha);

		R = (R << 16) & 0x00FF0000;
		G = (G << 8) & 0x0000FF00;
		B = B & 0x000000FF;
		A = A & 0xFF000000;

		return A | R | G | B;
	}
}
