package data.imageTypes;

import data.vectors.Vector;
import data.vectors.Vector2by2;
import data.vectors.Vector4by4;
import enums.EVectorMode;

/**
 * Common Image Implementation
 */
public class AbstractImage
{
    // Height/Width of Pixel  (8 Bits per Pixel)
    public static final int PIX_DIM = 256;

    // Codeword Adjuster Offset
    public static final int CW_OFFSET = 5;

    // Image Width/Height
    protected int _width;
    protected int _height;

    // Number of Codewords
    protected int _n;

    // Vector Mode
    protected EVectorMode _mode;

    /**
     * Constructor
     *
     * @param width  - The Width of the Image
     * @param height - The Height of the Image
     * @param n      - The Number of Vectors
     * @param mode   - How pixels should be grouped to form vectors
     */
    public AbstractImage(final int width, final int height, final int n, final EVectorMode mode)
    {
        // Initialize Width/Height of Image
        _width = width;
        _height = height;

        // Initialize number of codewords
        _n = n;

        // Initialize Vector Mode
        _mode = mode;
    }

    /**
     * getError - Gets the Error Difference between
     *            between two Codewords
     *
     * param v1 - The Old Codeword
     * parma v2 - The New Codeword
     * @return double - The Error Difference
     */
    protected double getError(final Vector v1, final Vector v2)
    {
        // Initialize Error
        final double error;

        // Get X/Y Differences
        final int xDiff = v2.getPixel1() - v1.getPixel1();
        final int yDiff = v2.getPixel2() - v1.getPixel2();

        // Get Error
        error = Math.pow(xDiff, 2) + Math.pow(yDiff, 2);

        return error;
    }

    /**
     * getError - Get the Error Difference between
     *            between two Codewords
     *
     * @param v1 - The Old Codeword
     * @param v2 - The New Codword
     * @return double = The Error Difference
     */
    protected double getError(final Vector2by2 v1, final Vector2by2 v2)
    {
        // Initialize Error
        final double error;

        // Get A,B,C,D Differences
        final int aDiff = v2.getPixel1() - v1.getPixel1();
        final int bDiff = v2.getPixel2() - v1.getPixel2();
        final int cDiff = v2.getPixel3() - v1.getPixel3();
        final int dDiff = v2.getPixel4() - v2.getPixel4();

        // Get Error
        error = Math.pow(aDiff, 2) + Math.pow(bDiff, 2) +
                Math.pow(cDiff, 2) + Math.pow(dDiff, 2);

        return error;
    }

    /**
     * getError - Get the Error Difference between
     *            between two Codewords
     *
     * @param v1 - The Old Codeword
     * @param v2 - The New Codword
     * @return double = The Error Difference
     */
    protected double getError(final Vector4by4 v1, final Vector4by4 v2)
    {
        // Initialize Error
        final double error;

        // Get A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P Differences
        final int aDiff = v2.getPixel1() - v1.getPixel1();
        final int bDiff = v2.getPixel2() - v1.getPixel2();
        final int cDiff = v2.getPixel3() - v1.getPixel3();
        final int dDiff = v2.getPixel4() - v2.getPixel4();
        
        final int eDiff = v2.getPixel5() - v1.getPixel5();
        final int fDiff = v2.getPixel6() - v1.getPixel6();
        final int gDiff = v2.getPixel7() - v1.getPixel7();
        final int hDiff = v2.getPixel8() - v2.getPixel8();
        
        final int iDiff = v2.getPixel9() - v1.getPixel9();
        final int jDiff = v2.getPixel10() - v1.getPixel10();
        final int kDiff = v2.getPixel11() - v1.getPixel11();
        final int lDiff = v2.getPixel12() - v2.getPixel12();
        
        final int mDiff = v2.getPixel13() - v1.getPixel13();
        final int nDiff = v2.getPixel14() - v1.getPixel14();
        final int oDiff = v2.getPixel15() - v1.getPixel15();
        final int pDiff = v2.getPixel16() - v2.getPixel16();

        // Get Error
        error = Math.pow(aDiff, 2) + Math.pow(bDiff, 2) + Math.pow(cDiff, 2) + Math.pow(dDiff, 2) +
                Math.pow(eDiff, 2) + Math.pow(fDiff, 2) + Math.pow(gDiff, 2) + Math.pow(hDiff, 2) +
                Math.pow(iDiff, 2) + Math.pow(jDiff, 2) + Math.pow(kDiff, 2) + Math.pow(lDiff, 2) +
                Math.pow(mDiff, 2) + Math.pow(nDiff, 2) + Math.pow(oDiff, 2) + Math.pow(pDiff, 2);

        return error;
    }

    /**
     * updatePixelPosition - Updates the Pixel Position 
     *                       in Vector Space
     *
     * @param pixel - The Pixel
     */
    protected int updatePixelPosition(final int pixel)
    {
        // Initialize New Pixel Position
        int newPosition = pixel;

        // If Codeword Position
        // is less than Midpoint
        if(pixel < (PIX_DIM/2))
        {
            newPosition+=CW_OFFSET;
        }
        // If Codeword Position
        // is greater than Midpoint
        else
        {
            newPosition-=CW_OFFSET;
        }

        return newPosition;
    }
}