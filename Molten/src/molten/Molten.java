/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package molten;

import com.elliotkroo.GifSequenceWriter;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

/**
 *
 * @author zar
 */
public class Molten {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws IOException {
		int numImages = 10;
		for (String name : new String[] {"src", "bang", "exist", "lake", "toohot"}) {
			BufferedImage[] imgs = new BufferedImage[numImages];
			System.out.println(name);
			for (int i = 0; i < numImages; i++) {
				int maxLightDist = 200;
				int baseLightProb = 200;
				double intensity = /*i == 0 ? 0.065 : 0.067;*/0.07 + Math.sin(i * Math.PI * 2 / numImages) * 0.002;
				BufferedImage src = ImageIO.read(new File("/home/zar/Desktop/Projects/Crucible/images/" + name + ".png"));
				imgs[i] = src;
				int blue = new Color(0, 2, 45).getRGB();
				int orange = new Color(255, 178, 10).getRGB();
				int darkorange = new Color(128, 89, 5).getRGB();
				int yellow = new Color(208, 255, 29).getRGB();
				int[][] img = new int[src.getHeight()][src.getWidth()];
				for (int y = 0; y < src.getHeight(); y++) {
					System.out.println(y);
					for (int x = 0; x < src.getWidth(); x++) {
						img[y][x] = src.getRGB(x, y);
					}
				}
				Random r = new Random(23948 + i);
				for (int y = 0; y < src.getHeight(); y++) {
					System.out.println(y);
					for (int x = 0; x < src.getWidth(); x++) {
						int c = img[y][x];
						if (c == orange) {
							int closestSq = 1000000;
							for (int y2 = Math.max(0, y - maxLightDist); y2 < Math.min(src.getHeight(), y + maxLightDist); y2++) {
								for (int x2 = Math.max(0, x - maxLightDist); x2 < Math.min(src.getWidth(), x + maxLightDist); x2++) {
									int c2 = img[y2][x2];
									if (c2 == yellow) {
										closestSq = Math.min(closestSq, (x - x2) * (x - x2) + (y - y2) * (y - y2));
									}
								}
							}
							if (r.nextInt(maxLightDist) * intensity < Math.pow(closestSq, 0.25) && r.nextInt(baseLightProb) != 1) {
								src.setRGB(x, y, blue);
							}
						}
						if (c == darkorange) {
							src.setRGB(x, y, r.nextInt(baseLightProb) == 1 ? orange : blue);
						}
					}
				}
				//ImageIO.write(src, "PNG", new File("/home/zar/Desktop/dst" + i + ".png"));
			}
			ImageOutputStream ios = ImageIO.createImageOutputStream(new File("/home/zar/Desktop/Projects/Crucible/gifs/" + name + ".gif"));
			GifSequenceWriter gsw = new GifSequenceWriter(ios, BufferedImage.TYPE_INT_RGB, 16, true);
			for (BufferedImage img : imgs) {
				gsw.writeToSequence(img);
			}
			gsw.close();
		}
	}
	
}
