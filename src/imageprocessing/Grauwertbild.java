package imageprocessing;

import main.PicsiSWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import utils.Parallel;

import javax.swing.*;

public class Grauwertbild implements IImageProcessor
{
    @Override
    public boolean isEnabled(int imageType)
    {
        return imageType == PicsiSWT.IMAGE_TYPE_RGB;
    }

    @Override
    public Image run(Image input, int imageType)
    {
        Object[] outputTypes = {"Gray", "Red", "Green", "Blue"};
        int ch = JOptionPane.showOptionDialog(null, "Choose the output", "Convert to gray", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,null, outputTypes, outputTypes[0]);
        if(ch < 0) return null;

        // create palette and output image data
        ImageData inData = input.getImageData();
        RGB[] rgbs = new RGB[256];
        for (int i = 0; i< 256; i++) rgbs[i] = new RGB(i,i,i);
        PaletteData pd = new PaletteData(rgbs);
        ImageData outData = new ImageData(inData.width, inData.height, 8, pd);

        if (imageType == PicsiSWT.IMAGE_TYPE_RGB) {

            Parallel.For(0, inData.height, v -> {
                for (int u=0; u < inData.width; u++) {
                    int pixel = inData.getPixel(u,v);
                    RGB rgb = inData.palette.getRGB(pixel);
                    int color = 0;

                    if(ch == 0) color = (int) (0.25 * rgb.red + 0.5 * rgb.green + 0.25 * rgb.blue);     // Grauwertbild
                    else if(ch == 1){ color = rgb.red;}
                    else if(ch == 2){ color = rgb.green; }
                    else if(ch == 3){  color = rgb.blue;}
                    outData.setPixel(u, v, color);
                }
            });

            return new Image(input.getDevice(), outData);
        }
        else {
            return null;
        }
    }
}
