package data.vectors;

/**
 * Vector2by2 - Object Representing a 2x2 Pixel Vector
 */
public class Vector2by2 
{
   // Pixels in Vector
   int _pixel1;
   int _pixel2;
   int _pixel3;
   int _pixel4;

   /**
    * Constructor
    *
    * @param pixel1 - Top Left Pixel
    * @param pixel2 - Top Right Pixel
    * @param pixel3 - Bottom Left Pixel
    * @param pixel4 - Bottom Right Pixel
    */
   public Vector2by2(int pixel1, int pixel2, int pixel3, int pixel4)
   {
      // Initialize Pixel Value
      _pixel1 = pixel1;
      _pixel2 = pixel2;
      _pixel3 = pixel3;
      _pixel4 = pixel4;
   }

   /**
    * getPixel1
    *
    * @return int - The Pixel Value
    */
   public int getPixel1()
   {
      return _pixel1;
   }

   /**
    * getPixel2
    *
    * @return int - The Pixel Value
    */
   public int getPixel2()
   {
      return _pixel2;
   }

   /**
    * getPixel3
    *
    * @return int - The Pixel Value
    */
   public int getPixel3()
   {
      return _pixel3;
   }

   /**
    * getPixel4
    *
    * @return int - The Pixel Value
    */
   public int getPixel4()
   {
      return _pixel4;
   }
}