package enums;

/**
 * EVectorMode - Enumeration detailing how pixels
 *               should be grouped to form vectors
 */
public enum EVectorMode
{
   SIDE_BY_SIDE, // Suggests two side by side pixels to form a vector
   TWO_BY_TWO,   // Suggests a 2x2 block of pixels to form a vector
   FOUR_BY_FOUR; // Suggests a 4x4 block of pixels to form a vector

   /**
    * getMode - Gets the Enumerated Value of the Vector Mode
    *           of how Pixels are to be grouped
    *
    * @param mode         - The input from Command Line
    * @return EVectorMode - The enumerated value 
    */
   public static EVectorMode getMode(final int mode)
   {
      // Initialize Return Vector Mode
      final EVectorMode vectorMode;

      switch(mode)
      {
         case 1:
            vectorMode = SIDE_BY_SIDE;
            break;
         
         case 2:
            vectorMode = TWO_BY_TWO;
            break;

         case 3:
            vectorMode = FOUR_BY_FOUR;
            break;

         default:
            vectorMode = SIDE_BY_SIDE;
            break;
      }
      
      return vectorMode;
   }
}

