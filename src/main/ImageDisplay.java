package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.*;

import data.imageTypes.ColorImage;
import data.imageTypes.GrayscaleImage;
import enums.EColor;
import enums.EVectorMode;

/**
 * ImageDisplay
 */
public class ImageDisplay 
{
    // Content Pane
    JFrame frame;

    // Original/Compressed Images
    JLabel lbIm1;
    JLabel lbIm2;

    /**
     * Constructor
     */
    public ImageDisplay()
    {
        //N/A
    }

    /**
     * showImages - Shows the Original and the Compressed Image
     *
     * @param image - The image to apply compression
     * @param n     - The number of Vectors for quantization
     * @param mode  - How pixels should be grouped to form vectors
     * @param isRGB - TRUE is a Color Image, FALSE is a Grayscale Image
     */
    public void showImages(final BufferedImage image, final int n, final EVectorMode mode, final boolean isRGB)
    {
        // Initialize Frame to display the images
        frame = new JFrame();
        GridBagLayout gLayout = new GridBagLayout();
        frame.getContentPane().setLayout(gLayout);
        GridBagConstraints c = new GridBagConstraints();

        // Initialize Original Image Label
        final JLabel lbText1 = new JLabel("Original image (Left)");
        lbText1.setHorizontalAlignment(SwingConstants.CENTER);

        // Initialize Compressed Image Label
        final JLabel lbText2 = new JLabel("Image after modification (Right)");
        lbText2.setHorizontalAlignment(SwingConstants.CENTER);

        // Initialize Original Image
        lbIm1 = new JLabel(new ImageIcon(image));

        // Initialize Compressed Image
        final BufferedImage compressedImage;

        // Check if Colored Image
        if(isRGB)
        {
            // Compress the Color Image
            compressedImage = compressColorImage(image, n, mode);
        }
        // Check if GrayScale Image
        else
        {
            // Compress the Grayscale Image
            compressedImage = compressGrayscaleImage(image, n, mode);
        }

        // Add Compressed Image to Image Label
        lbIm2 = new JLabel(new ImageIcon(compressedImage));

        // Initialize Original Label Location
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        frame.getContentPane().add(lbText1, c);

        // Initialize Modified Image Label Location
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 0;
        frame.getContentPane().add(lbText2, c);

        // Initialize Original Image Location
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 1;
        frame.getContentPane().add(lbIm1, c);

        // Initialize Modified Image Location
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 1;
        c.gridy = 1;
        frame.getContentPane().add(lbIm2, c);

        frame.pack();
        frame.setSize(1500, 1000);
        frame.setVisible(true);
    }

    /**
     * compressGrayscaleImage - Compresses a Grayscale Image
     *
     * @param image - The image to apply compression
     * @param n     - The number of Vectors for quantization
     * @param mode  - How pixels should be grouped to form vectors
     * @return BufferedImage - The Compressed Image
     */
    private BufferedImage compressGrayscaleImage(final BufferedImage image, final int n, final EVectorMode mode)
    {
        // Create new Color Image
        GrayscaleImage grayImage = new GrayscaleImage(image.getWidth(), image.getHeight(), n, mode);

        // Generate Input Vectors
        grayImage.generateInputVectors(image);

        // Generate Initial Codebook Values
        grayImage.generateCodebook();

        // Codebook Generated Indicator
        boolean codebookGenerated = false;

        // Generate Codebook until All Clusters
        // have at least one input vector in Cluster
        while(!codebookGenerated)
        {
            codebookGenerated = grayImage.verifyCodebook();
        }

        // Initialize Error Margin
        double error = Double.MAX_VALUE;

        // Converge Codebook Vectors
        while(error >= 1.0)
        {
            // Converge Codewords in Codebook
            error = grayImage.updateCodebook();

            // Update Cluster Map with Latest Codebook
            grayImage.generateClusterMap();
        }

        // Quantize Image
        final ArrayList<Integer> indexes = grayImage.quantizeImage();

        // Reconstruct Image
        BufferedImage compressedImage = grayImage.reconstructImage(indexes);

        return compressedImage;
    }

    /**
     * compressColorImage - Compresses the Color Image
     *
     * @param image - The image to apply compression
     * @param n     - The number of Vectors for quantization
     * @param mode  - How pixels should be grouped to form vectors
     * @return BufferedImage - The Compressed Image
     */
    private BufferedImage compressColorImage(final BufferedImage image, final int n, final EVectorMode mode)
    {
        // Create new Color Image
        ColorImage colorImage = new ColorImage(image.getWidth(), image.getHeight(), n, mode);

        // Generate Input Vectors
        colorImage.generateInputVectors(image);

        // Generate Codebook
        colorImage.generateCodebooks();

        // Codebook Generation Indicators
        boolean redCodebookGenerated = false;
        boolean greenCodebookGenerated = false;
        boolean blueCodebookGenerated = false;

        // Generate Codebooks until All Clusters
        // have at least one input vector in Cluster
        while(!redCodebookGenerated)
        {
            redCodebookGenerated = colorImage.verifyCodebook(EColor.RED);
        }
        while(!greenCodebookGenerated)
        {
            greenCodebookGenerated = colorImage.verifyCodebook(EColor.GREEN);
        }
        while(!blueCodebookGenerated)
        {
            blueCodebookGenerated = colorImage.verifyCodebook(EColor.BLUE);
        }

        // Initialize Error Margins
        double redError = Double.MAX_VALUE;
        double greenError = Double.MAX_VALUE;
        double blueError = Double.MAX_VALUE;

        // Converge Red Codebook Vectors
        while(redError >= 1.0)
        {
            // Converge Codewords in Codebook
            redError = colorImage.updateCodebook(EColor.RED);

            // Update Cluster Map with Latest Codebook
            colorImage.generateClusterMap(EColor.RED);
        }

        // Converge Green Codebook Vectors
        while(greenError >= 1.0)
        {
            // Converge Codewords in Codebook
            greenError = colorImage.updateCodebook(EColor.GREEN);

            // Update Cluster Map with Latest Codebook
            colorImage.generateClusterMap(EColor.GREEN);
        }

        // Converge Blue Codebook Vectors
        while(blueError >= 1.0)
        {
            // Converge Codewords in Codebook
            blueError = colorImage.updateCodebook(EColor.BLUE);

            // Update Cluster Map with Latest Codebook
            colorImage.generateClusterMap(EColor.BLUE);
        }

        // Quantize Image
        final ArrayList<Integer> redIndices = colorImage.quantizeRedComponents();
        final ArrayList<Integer> greenIndices = colorImage.quantizeGreenComponents();
        final ArrayList<Integer> blueIndices = colorImage.quantizeBlueComponents();

        // Reconstruct Image
        BufferedImage compressedImage = colorImage.reconstructImage(redIndices, greenIndices, blueIndices);

        return compressedImage;
    }
}