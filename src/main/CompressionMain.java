package main;
import java.awt.image.BufferedImage;

import enums.EVectorMode;
import util.ImageUtil;

/**
 * CompressionMain
 *
 * This Program deals with increasing understanding of 
 * Image Compression. This program utilizes Vector Quantization
 * on contrary codes a group or block of samples using a 
 * single discrete value or index
 */
public class CompressionMain 
{
   // Image Extensions
   private static final String RAW = "raw";
   private static final String RGB = "rgb";

   // Width/Height of the Image
   private static final int WIDTH = 352;
   private static final int HEIGHT = 288;

   /**
    * main
    *
    * @param args
    */
   public static void main(String[] args)
   {
      // Generate the Display for the Images
      ImageDisplay ren = new ImageDisplay();

      // Initialize is Image Color/GrayScale Indicator
      boolean isRgb = false;

      // Ensure the Program has 3 arguments passed
      if(args.length < 3 || args.length >= 4)
      {
         // Print Usage Statement and End Program
         System.out.println("Usage: ./CompressionMain Image numVectors mode");
         System.exit(1);
      }
      else
      {
         // Read a parameters from command line
         final String imageName = args[0];
         final String numVectorsStr = args[1];
         final String modeStr = args[2];

         try
         {
            // Get Number of Vectors
            final int numVectors = Integer.parseInt(numVectorsStr);

            // Get Vector Mode
            final int m = Integer.parseInt(modeStr);
            final EVectorMode mode = EVectorMode.getMode(m);

            // Split String at File Extension
            String[] parts = imageName.split("\\.");

            // Get Image Extension Index
            final int extIndex = parts.length-1;

            // Initialize Image
            final BufferedImage image;

            StringBuilder builder = new StringBuilder();
            builder.append("images/");
            builder.append(imageName);
            String filePath = builder.toString();

            // Check RAW
            if(RAW.equals(parts[extIndex]))
            {
               // Get Grayscale Image
               image = ImageUtil.convertRawImage(filePath, WIDTH, HEIGHT);
            }
            else if(RGB.equals(parts[extIndex]))
            {
               // Get Color Image
               image = ImageUtil.convertRGBImage(filePath, WIDTH, HEIGHT);
               isRgb = true;
            }
            else
            {
               // Print Error Statement and End Program
               image = null;
               System.out.println("Extension not supported by program");
               System.exit(1);
            }

            // Show the Images
            ren.showImages(image, numVectors, mode, isRgb);
         }
         catch(Exception e)
         {
            // Print Stack Trace
            e.printStackTrace();

            // Print Usage Statement and End Program
            System.out.println("Usage: ./CompressionMain Image numVectors mode");
            System.exit(1);
         }
      }
   }
}