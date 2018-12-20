package data.vectors;

/**
 * Vector - Object Representing a Side by Side Pixel Vector
 */
public class Vector
{
   // Pixels in Vector
   int _pixel1;
   int _pixel2;

   /**
    * Constructor
    *
    * @param pixel1 - Left Adjacent Pixel
    * @param pixel2 - Right Adjacent Pixel
    */
   public Vector(int pixel1, int pixel2)
   {
      // Initialize Pixel Value
      _pixel1 = pixel1;
      _pixel2 = pixel2;
   }

   /**
    * getPixel1
    *
    * @return int
    */
   public int getPixel1()
   {
      return _pixel1;
   }

   /**
    * getPixel2
    *
    * @return int
    */
   public int getPixel2()
   {
      return _pixel2;
   }
}