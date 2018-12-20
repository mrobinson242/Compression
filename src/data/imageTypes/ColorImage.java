package data.imageTypes;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import data.vectors.Vector;
import data.vectors.Vector2by2;
import data.vectors.Vector4by4;
import enums.EColor;
import enums.EVectorMode;

public class ColorImage extends AbstractImage
{
    // Lists of Input Vectors
    ArrayList<Vector> _redVectorList;   // Adjacent Red Pixel Input Vectors
    ArrayList<Vector> _greenVectorList; // Adjacent Green Pixel Input Vectors
    ArrayList<Vector> _blueVectorList;  // Adjacent Blue Pixel Input Vectors
    ArrayList<Vector2by2> _redVector2by2List;   // 2x2 Red Pixel Input Vectors
    ArrayList<Vector2by2> _greenVector2by2List; // 2x2 Green Pixel Input Vectors
    ArrayList<Vector2by2> _blueVector2by2List;  // 2x2 Blue Pixel Input Vectors
    ArrayList<Vector4by4> _redVector4by4List;   // 4x4 Red Pixel Input Vectors
    ArrayList<Vector4by4> _greenVector4by4List; // 4x4 Green Pixel Input Vectors
    ArrayList<Vector4by4> _blueVector4by4List;  // 4x4 Blue Pixel Input Vectors

    // The Codebook for Vector Quantization
    ArrayList<Vector> _redCodebook;
    ArrayList<Vector> _greenCodebook;
    ArrayList<Vector> _blueCodebook;
    ArrayList<Vector2by2> _red2by2Codebook;
    ArrayList<Vector2by2> _green2by2Codebook;
    ArrayList<Vector2by2> _blue2by2Codebook;
    ArrayList<Vector4by4> _red4by4Codebook;
    ArrayList<Vector4by4> _green4by4Codebook;
    ArrayList<Vector4by4> _blue4by4Codebook;

    // Cluster Maps
    HashMap<Vector, Vector> _redClusterMap;   // Adjacent Red Cluster Map
    HashMap<Vector, Vector> _greenClusterMap; // Adjacent Green Cluster Map
    HashMap<Vector, Vector> _blueClusterMap;  // Adjacent Blue Cluster Map
    HashMap<Vector2by2, Vector2by2> _red2by2ClusterMap;   // 2x2 Red Cluster Map
    HashMap<Vector2by2, Vector2by2> _green2by2ClusterMap; // 2x2 Green Cluster Map
    HashMap<Vector2by2, Vector2by2> _blue2by2ClusterMap;  // 2x2 Blue Cluster Map
    HashMap<Vector4by4, Vector4by4> _red4by4ClusterMap;   // 4x4 Red Cluster Map
    HashMap<Vector4by4, Vector4by4> _green4by4ClusterMap; // 4x4 Green Cluster Map
    HashMap<Vector4by4, Vector4by4> _blue4by4ClusterMap;  // 4x4 Blue Cluster Map

    /**
     * Constructor
     *
     * @param width  - The Width of the Image
     * @param height - The Height of the Image
     * @param n      - The Number of Vectors
     * @param mode   - How pixels should be grouped to form vectors
     */
    public ColorImage(final int width, final int height, final int n, final EVectorMode mode)
    {
        super(width, height, n, mode);

        // Initialize Codebooks
        _redCodebook = new ArrayList<Vector>(n);
        _greenCodebook = new ArrayList<Vector>(n);
        _blueCodebook = new ArrayList<Vector>(n);
        _red2by2Codebook = new ArrayList<Vector2by2>(n);
        _green2by2Codebook = new ArrayList<Vector2by2>(n);
        _blue2by2Codebook = new ArrayList<Vector2by2>(n);
        _red4by4Codebook = new ArrayList<Vector4by4>(n);
        _green4by4Codebook = new ArrayList<Vector4by4>(n);
        _blue4by4Codebook = new ArrayList<Vector4by4>(n);

        // Initialize Cluster Maps
        _redClusterMap = new HashMap<Vector, Vector>();
        _greenClusterMap = new HashMap<Vector, Vector>();
        _blueClusterMap = new HashMap<Vector, Vector>();
        _red2by2ClusterMap = new HashMap<Vector2by2, Vector2by2>();
        _green2by2ClusterMap = new HashMap<Vector2by2, Vector2by2>();
        _blue2by2ClusterMap = new HashMap<Vector2by2, Vector2by2>();
        _red4by4ClusterMap = new HashMap<Vector4by4, Vector4by4>();
        _green4by4ClusterMap = new HashMap<Vector4by4, Vector4by4>();
        _blue4by4ClusterMap = new HashMap<Vector4by4, Vector4by4>();
    }

    /**
     * generateInputVectors - Generates the Input Vectors
     *
     * @param image - Color Image to Generate Vectors from
     */
    public void generateInputVectors(final BufferedImage image)
    {
        // Adjacent Pixel Vector Mode
        if(EVectorMode.SIDE_BY_SIDE.equals(_mode))
        {
            // Initialize Pixels
            int pixel1, pixel2;

            // Get Size of Input Vectors
            int width = image.getWidth();
            int height = image.getHeight();
            int size = (width*height)/2;

            // Initialize List of Input Vectors
            _redVectorList = new ArrayList<Vector>(size);
            _greenVectorList = new ArrayList<Vector>(size);
            _blueVectorList = new ArrayList<Vector>(size);

            // Iterate over Image Height
            for(int y = 0; y < image.getHeight(); ++y)
            {
                // Iterate over Image Width
                for(int x = 0; x < image.getWidth(); x+=2)
                {
                    // Get RGB Values at each Pixel 1
                    pixel1 = image.getRGB(x,y);
                    int red1   = (pixel1 & 0x00ff0000) >> 16;
                    int green1 = (pixel1 & 0x0000ff00) >> 8;
                    int blue1 =  (pixel1 & 0x000000ff);

                    // Get RGB Values at each Pixel 2
                    pixel2 = image.getRGB(x+1,y);
                    int red2   = (pixel2 & 0x00ff0000) >> 16;
                    int green2 = (pixel2 & 0x0000ff00) >> 8;
                    int blue2 =  (pixel2 & 0x000000ff);

                    // Create new Adjacent Pixel Vectors
                    Vector redV = new Vector(red1, red2);
                    Vector greenV = new Vector(green1, green2);
                    Vector blueV = new Vector(blue1, blue2);
   
                    // Add Vector to List
                    _redVectorList.add(redV);
                    _greenVectorList.add(greenV);
                    _blueVectorList.add(blueV);
                }
            }
        }
        // 2x2 Pixel Vector Mode
        else if(EVectorMode.TWO_BY_TWO.equals(_mode))
        {
            // Initialize 2x2 Pixels
            int pixel1, pixel2;
            int pixel3, pixel4;

            // Get Size of Input Vectors
            int width = image.getWidth();
            int height = image.getHeight();
            int size = (width*height)/4;

            // Initialize List of Input Vectors
            _redVector2by2List = new ArrayList<Vector2by2>(size);
            _greenVector2by2List = new ArrayList<Vector2by2>(size);
            _blueVector2by2List = new ArrayList<Vector2by2>(size);

            // Iterate over Image Height
            for(int y = 0; y < image.getHeight(); y+=2)
            {
                // Iterate over Image Width
                for(int x = 0; x < image.getWidth(); x+=2)
                {
                    // Get RGB Values at each Pixel 1
                    pixel1 = image.getRGB(x,y);
                    int red1   = (pixel1 & 0x00ff0000) >> 16;
                    int green1 = (pixel1 & 0x0000ff00) >> 8;
                    int blue1 =  (pixel1 & 0x000000ff);

                    // Get RGB Values at each Pixel 2
                    pixel2 = image.getRGB(x+1,y);
                    int red2   = (pixel2 & 0x00ff0000) >> 16;
                    int green2 = (pixel2 & 0x0000ff00) >> 8;
                    int blue2 =  (pixel2 & 0x000000ff);

                    // Get RGB Values at each Pixel 3
                    pixel3 = image.getRGB(x,y+1);
                    int red3   = (pixel3 & 0x00ff0000) >> 16;
                    int green3 = (pixel3 & 0x0000ff00) >> 8;
                    int blue3 =  (pixel3 & 0x000000ff);

                    // Get RGB Values at each Pixel 4
                    pixel4 = image.getRGB(x+1,y+1);
                    int red4   = (pixel4 & 0x00ff0000) >> 16;
                    int green4 = (pixel4 & 0x0000ff00) >> 8;
                    int blue4 =  (pixel4 & 0x000000ff);

                    // Create new 2x2 Pixel Vectors
                    Vector2by2 redV = new Vector2by2(red1, red2, red3, red4);
                    Vector2by2 greenV = new Vector2by2(green1, green2, green3, green4);
                    Vector2by2 blueV = new Vector2by2(blue1, blue2, blue3, blue4);
   
                    // Add Vectors to Lists
                    _redVector2by2List.add(redV);
                    _greenVector2by2List.add(greenV);
                    _blueVector2by2List.add(blueV);
                }
            }
        }
    }

    /**
     * generateCodebooks - Generates the Initial Codewords
     *                     for the Codebooks
     *
     * @param n - The Number of Codwords to
     *            generate for the Codebook
     */
    public void generateCodebooks()
    {
        // Blocks in a particular Dimension
        final int blockCount = PIX_DIM/_n;

        // Adjacent Pixel Vector Mode
        if(EVectorMode.SIDE_BY_SIDE == _mode)
        {
            // Initialize Incrementors
            // for Codeword [x,y] Position
            int x = 0;
            int y = 0;

            // Initialize Codeword Counter
            int count = 0;

            // Initialize the Codebooks
            while(count < _n)
            {
                // Create new codeword
                Vector codeword = new Vector(x, y);

                // Add Codeword to Codebook
                _redCodebook.add(codeword);
                _greenCodebook.add(codeword);
                _blueCodebook.add(codeword);

                // Update Codeword [x,y] Position
                x += blockCount;
                y += blockCount;

                // Increment Codeword Counter
                count++;
            }
        }
        // 2x2 Matrix Vector Mode
        else if(EVectorMode.TWO_BY_TWO.equals(_mode))
        {
            // Initialize Incrementors
            // for Codeword Position [a,b]
            //                       [c,d]
            int a = 0;
            int b = 0;
            int c = 0;
            int d = 0;

            // Initialize Codeword Counter
            int count = 0;

            // Keep adding until there are n codewords
            while(count < _n)
            {
                // Create new codeword
                final Vector2by2 codeword = new Vector2by2(a,b,c,d);

                // Add Codewords to Codebooks
                _red2by2Codebook.add(codeword);
                _green2by2Codebook.add(codeword);
                _blue2by2Codebook.add(codeword);

                // Update Codeword [x,y] Position
                a += blockCount;
                b += blockCount;
                c += blockCount;
                d += blockCount;

                // Increment Codeword Counter
                count++;
            }
        }
        // 4x4 Matrix Vector Mode
        else if(EVectorMode.FOUR_BY_FOUR.equals(_mode))
        {

            // Initialize Codeword Counter
            int count = 0;

            // Initialize Incrementors
            // for Codeword Position [a,b,c,d] 
            //                       [e,f,g,h] 
            //                       [h,i,j,k]
            //                       [l,m,n,o]
            int a=0, b=0, c=0, d=0;
            int e=0, f=0, g=0, h=0;
            int i=0, j=0, k=0, l=0;
            int m=0, n=0, o=0, p=0;

            // Keep adding until there are n codewords
            while(count < _n)
            {
                // Create new codeword
                final Vector4by4 codeword = new Vector4by4(a,b,c,d,
                                                           e,f,g,h,
                                                           i,j,k,l,
                                                           m,n,o,p);

                // Add Codewords to Codebooks
                _red4by4Codebook.add(codeword);
                _green4by4Codebook.add(codeword);
                _blue4by4Codebook.add(codeword);

                // Update Codeword Position [a,b,c,d] 
                //                          [e,f,g,h] 
                //                          [i,j,k,l]
                //                          [m,n,o,p]
                a += blockCount;
                b += blockCount;
                c += blockCount;
                d += blockCount;
                e += blockCount;
                f += blockCount;
                g += blockCount;
                h += blockCount;
                i += blockCount;
                j += blockCount;
                k += blockCount;
                l += blockCount;
                m += blockCount;
                n += blockCount;
                o += blockCount;
                p += blockCount;

                // Increment Codeword Counter
                count++;
            }
        }
    }

    /**
     * verifyCodebook - Verifies the Codewords to best represent the data set
     *
     * @param n - Number of vectors to best represent Data Set
     * @return boolean
     */
    public boolean verifyCodebook(final EColor color)
    {
        // Indicator of Successful Codebook Generation
        boolean isSuccessful = true;

        // Check if Vector Mode is Side by Side
        if(EVectorMode.SIDE_BY_SIDE.equals(_mode))
        {
            // Generate Cluster Mapping
            // between Input Vectors and Codewords
            generateClusterMap(color);

            // If Red
            if(EColor.RED == color)
            {
                isSuccessful = verifyRedCodebook();
            }
            else if(EColor.GREEN == color)
            {
                isSuccessful = verifyGreenCodebook();
            }
            else if(EColor.BLUE == color)
            {
                isSuccessful = verifyBlueCodebook();
            }
        }
        // Check if Vector Mode is 2x2
        else if(EVectorMode.TWO_BY_TWO.equals(_mode))
        {
            // TODO: Implement
        }
        // Check if Vector MOde is 4x4
        else if(EVectorMode.FOUR_BY_FOUR.equals(_mode))
        {
           // TODO: Implement
        }

        return isSuccessful;
    }

    /**
     * verifyRedCodebook
     *
     * @return boolean
     */
    private boolean verifyRedCodebook()
    {
        // Indicator of Successful Codebook Generation
        boolean isSuccessful = true;

        // Iterate over Codebook
        for(Vector codeword : _redCodebook)
        {
            // Get Input Vectors in Cluster
            ArrayList<Vector> inputVectorList = getCluster(_redClusterMap, codeword);

            // Check if there are no Input Vectors in Cluster
            if(inputVectorList.size() == 0)
            {
                // Get Index of Codebook
                int index = _redCodebook.indexOf(codeword);

                // Get Codeword[x,y]
                int x = codeword.getPixel1();
                int y = codeword.getPixel2();

                x = updatePixelPosition(x);
                y = updatePixelPosition(y);

                // Generate New Codeword for Codebook
                Vector newCodeword = new Vector(x,y);
                _redCodebook.set(index, newCodeword);

                // Will need to reverify codebook
                // as Codebook has been updated
                isSuccessful = false;
            }
        }

        return isSuccessful;
    }
    
    /**
     * verifyGreenCodebook
     *
     * @return boolean
     */
    private boolean verifyGreenCodebook()
    {
        // Indicator of Successful Codebook Generation
        boolean isSuccessful = true;

        // Iterate over Codebook
        for(Vector codeword : _greenCodebook)
        {
            // Get Input Vectors in Cluster
            ArrayList<Vector> inputVectorList = getCluster(_greenClusterMap, codeword);

            // Check if there are no Input Vectors in Cluster
            if(inputVectorList.size() == 0)
            {
                // Get Index of Codebook
                int index = _greenCodebook.indexOf(codeword);

                // Get Codeword[x,y]
                int x = codeword.getPixel1();
                int y = codeword.getPixel2();

                // Update Codeword Position [x,y]
                x = updatePixelPosition(x);
                y = updatePixelPosition(y);

                // Generate New Codeword for Codebook
                Vector newCodeword = new Vector(x,y);
                _greenCodebook.set(index, newCodeword);

                // Will need to reverify codebook
                // as Codebook has been updated
                isSuccessful = false;
            }
        }

        return isSuccessful;
    }
    
    /**
     * verifyBlueCodebook
     *
     * @return boolean
     */
    private boolean verifyBlueCodebook()
    {
        // Indicator of Successful Codebook Generation
        boolean isSuccessful = true;

        // Iterate over Codebook
        for(Vector codeword : _blueCodebook)
        {
            // Get Input Vectors in Cluster
            ArrayList<Vector> inputVectorList = getCluster(_blueClusterMap, codeword);

            // Check if there are no Input Vectors in Cluster
            if(inputVectorList.size() == 0)
            {
                // Get Index of Codebook
                int index = _blueCodebook.indexOf(codeword);

                // Get Codeword[x,y]
                int x = codeword.getPixel1();
                int y = codeword.getPixel2();

                // Update Codeword Position [x,y]
                x = updatePixelPosition(x);
                y = updatePixelPosition(y);

                // Generate New Codeword for Codebook
                Vector newCodeword = new Vector(x,y);
                _blueCodebook.set(index, newCodeword);

                // Will need to reverify codebook
                // as Codebook has been updated
                isSuccessful = false;
            }
        }

        return isSuccessful;
    }

    /**
     * quantizeGreenComponents
     *
     * @return ArrayList<Integer>
     */
    public ArrayList<Integer> quantizeRedComponents()
    {
        // Get the Size of the Input Vector array
        int size = (_width*_height)/2;

        // Initialize List of redIndices
        ArrayList<Integer> redIndices = new ArrayList<Integer>(size);

        // Iterate over each vector in Vector List
        for(final Vector v: _redVectorList)
        {
            // Get Codeword associated with that Vector
            final Vector codeword = _redClusterMap.get(v);

            // Get Index of Codeword
            final int codewordIndex = _redCodebook.indexOf(codeword);

            // Add Index of Codeword to Indexes Array
            redIndices.add(codewordIndex);
        }

        return redIndices;
    }

    /**
     * quantizeGreenComponents
     *
     * @return ArrayList<Integer>
     */
    public ArrayList<Integer> quantizeGreenComponents()
    {
        // Get the Size of the Input Vector array
        int size = (_width*_height)/2;

        // Initialize List of Green Indices
        ArrayList<Integer> greenIndices = new ArrayList<Integer>(size);

        // Iterate over each vector in Vector List
        for(final Vector v: _greenVectorList)
        {
            // Get Codeword associated with that Vector
            final Vector codeword = _greenClusterMap.get(v);

            // Get Index of Codeword
            final int codewordIndex = _greenCodebook.indexOf(codeword);

            // Add Index of Codeword to Indexes Array
            greenIndices.add(codewordIndex);
        }

        return greenIndices;
    }

    /**
     * quantizeBlueComponents
     *
     * @return ArrayList<Integer>
     */
    public ArrayList<Integer> quantizeBlueComponents()
    {
        // Get the Size of the Input Vector array
        int size = (_width*_height)/2;

        // Initialize List of Green Indices
        ArrayList<Integer> blueIndices = new ArrayList<Integer>(size);

        // Iterate over each vector in Vector List
        for(final Vector v: _blueVectorList)
        {
            // Get Codeword associated with that Vector
            final Vector codeword = _blueClusterMap.get(v);

            // Get Index of Codeword
            final int codewordIndex = _blueCodebook.indexOf(codeword);

            // Add Index of Codeword to Indexes Array
            blueIndices.add(codewordIndex);
        }

        return blueIndices;
    }

    /**
     * reconstructImage - Reconstructs a Color Image from a Vector Codebook
     *
     * @param mode - Pixel Grouping Mode to form Vectors
     * @return BufferedImage - Reconstructed Compressed Image
     */
    public BufferedImage reconstructImage(final ArrayList<Integer> redIndices, 
                                          final ArrayList<Integer> greenIndices,
                                          final ArrayList<Integer> blueIndices)
    {
        // Initialize Raw Image
        BufferedImage image = new BufferedImage(_width, _height, BufferedImage.TYPE_INT_RGB);

        int count = 0;

        // Check if Vector Mode is Side by Side
        if(EVectorMode.SIDE_BY_SIDE.equals(_mode))
        {
            // Iterate over Y Values
            for(int y = 0; y < _height; y++)
            {
                // Iterate over X Values
                for(int x = 0; x < _width; x+=2)
                {
                    // Get the Index into the Codebook from the Indexes Array
                    final int redIndex = redIndices.get(count);
                    final int greenIndex = greenIndices.get(count);
                    final int blueIndex = blueIndices.get(count);

                    // Get Code Vector from Codebook
                    final Vector redV = _redCodebook.get(redIndex);
                    final Vector greenV = _greenCodebook.get(greenIndex);
                    final Vector blueV = _blueCodebook.get(blueIndex);

                    // Get Pixel 1
                    final int pix1Red = redV.getPixel1();
                    final int pix1Green = greenV.getPixel1();
                    final int pix1Blue = blueV.getPixel1();
                    byte r1 = (byte) pix1Red;
                    byte g1 = (byte) pix1Green;
                    byte b1 = (byte) pix1Blue;
                    final int pix1 = 0xff000000 | ((r1 & 0xff) << 16) | ((g1 & 0xff) << 8) | (b1 & 0xff);

                    // Get Pixel 2
                    final int pix2Red = redV.getPixel2();
                    final int pix2Green = greenV.getPixel2();
                    final int pix2Blue = blueV.getPixel2();
                    byte r2 = (byte) pix2Red;
                    byte g2 = (byte) pix2Green;
                    byte b2 = (byte) pix2Blue;
                    final int pix2 = 0xff000000 | ((r2 & 0xff) << 16) | ((g2 & 0xff) << 8) | (b2 & 0xff);

                    // Set Pixels
                    image.setRGB(x,y,pix1);
                    image.setRGB(x+1,y,pix2);

                    // Increment Count
                    count++;
                }
            }
        }

        return image;
    }

    /**
     * generateClusterMap - Updates the Cluster Map of a Color
     *                      by Mapping the Closest Codeword
     *                      to each Input Vector
     */
    public void generateClusterMap(final EColor color)
    {
        if(EColor.RED.equals(color))
        {
            generateRedClusterMap();
        }
        else if(EColor.GREEN.equals(color))
        {
            generateGreenClusterMap();
        }
        else if(EColor.BLUE.equals(color))
        {
            generateBlueClusterMap();
        }
    }

    /**
     * updateCodebook
     */
    public double updateCodebook(final EColor color)
    {
        // Update the Cluster Mapping
        generateClusterMap(color);

        // Initialize Error Accumulator
        double error = 0.0;

        switch(color)
        {
            case RED:
                error = updateRedCodebook();
                break;

            case GREEN:
                error = updateGreenCodebook();
                break;

            case BLUE:
                error = updateBlueCodebook();
                break;

            default:
                break;
        }

        return error;
    }

    /**
     * updateRedCodebook
     *
     * @return double
     */
    private double updateRedCodebook()
    {
        // Initialize Error Accumulator
        double error = 0.0;

        // Initialize New Codeword List
        final ArrayList<Vector> newCodewordList = new ArrayList<Vector>();

        // Iterate over each codeword
        for(Vector codeword : _redCodebook)
        {
            // Get Cluster associated with codeword
            final ArrayList<Vector> clusterVectors = getCluster(_redClusterMap, codeword);

            // Get Number of Cluster Vectors
            final int m = clusterVectors.size();

            // Ensure Cluster has Vectors
            if(m != 0)
            {
                // Initialize Accumulations
                int sumClusterX = 0;
                int sumClusterY = 0;

                // Iterate over Vectors in Cluster
                for(Vector v : clusterVectors)
                {
                    sumClusterX += v.getPixel1();
                    sumClusterY += v.getPixel2();
                }

                // Get New Codeword
                Vector newCodeword = new Vector(sumClusterX/m, sumClusterY/m);
                newCodewordList.add(newCodeword);

                // Get Error between old Codeword and new Codeword
                error += getError(codeword, newCodeword);
            }
        }

        // Update Codebook with Latest Codewords
        _redCodebook.clear();
        _redCodebook.addAll(newCodewordList);

        return error;
    }
    
    /**
     * updateRedCodebook
     *
     * @return double
     */
    private double updateGreenCodebook()
    {
        // Initialize Error Accumulator
        double error = 0.0;

        // Initialize New Codeword List
        final ArrayList<Vector> newCodewordList = new ArrayList<Vector>();

        // Iterate over each codeword
        for(Vector codeword : _greenCodebook)
        {
            // Get Cluster associated with codeword
            final ArrayList<Vector> clusterVectors = getCluster(_greenClusterMap, codeword);

            // Get Number of Cluster Vectors
            final int m = clusterVectors.size();

            // Ensure Cluster has Vectors
            if(m != 0)
            {
                // Initialize Accumulations
                int sumClusterX = 0;
                int sumClusterY = 0;

                // Iterate over Vectors in Cluster
                for(Vector v : clusterVectors)
                {
                    sumClusterX += v.getPixel1();
                    sumClusterY += v.getPixel2();
                }

                // Get New Codeword
                Vector newCodeword = new Vector(sumClusterX/m, sumClusterY/m);
                newCodewordList.add(newCodeword);

                // Get Error between old Codeword and new Codeword
                error += getError(codeword, newCodeword);
            }
        }

        // Update Codebook with Latest Codewords
        _greenCodebook.clear();
        _greenCodebook.addAll(newCodewordList);

        return error;
    }

    /**
     * updateBlueCodebook
     *
     * @return double
     */
    private double updateBlueCodebook()
    {
        // Initialize Error Accumulator
        double error = 0.0;

        // Initialize New Codeword List
        final ArrayList<Vector> newCodewordList = new ArrayList<Vector>();

        // Iterate over each codeword
        for(Vector codeword : _blueCodebook)
        {
            // Get Cluster associated with codeword
            final ArrayList<Vector> clusterVectors = getCluster(_blueClusterMap, codeword);

            // Get Number of Cluster Vectors
            final int m = clusterVectors.size();

            // Ensure Cluster has Vectors
            if(m != 0)
            {
                // Initialize Accumulations
                int sumClusterX = 0;
                int sumClusterY = 0;

                // Iterate over Vectors in Cluster
                for(Vector v : clusterVectors)
                {
                    sumClusterX += v.getPixel1();
                    sumClusterY += v.getPixel2();
                }

                // Get New Codeword
                Vector newCodeword = new Vector(sumClusterX/m, sumClusterY/m);
                newCodewordList.add(newCodeword);

                // Get Error between old Codeword and new Codeword
                error += getError(codeword, newCodeword);
            }
        }

        // Update Codebook with Latest Codewords
        _blueCodebook.clear();
        _blueCodebook.addAll(newCodewordList);

        return error;
    }

    /**
     * generateRedClusterMap
     */
    private void generateRedClusterMap()
    {
        synchronized(_redClusterMap)
        {
            // Iterate over each Vector
            for(final Vector v: _redVectorList)
            {
                // Get Closest Codeword
                final Vector codeword = getClosestCodeword(_redCodebook, v);

                // Update Cluster Mapping
                _redClusterMap.put(v, codeword);
            }
        }
    }

    /**
     * generateGreenClusterMap
     */
    private void generateGreenClusterMap()
    {
        synchronized(_greenClusterMap)
        {
            // Iterate over each Vector
            for(final Vector v: _greenVectorList)
            {
                // Get Closest Codeword
                final Vector codeword = getClosestCodeword(_greenCodebook, v);

                // Update Cluster Mapping
                _greenClusterMap.put(v, codeword);
            }
        }
    }

    /**
     * generateBlueClusterMap
     */
    private void generateBlueClusterMap()
    {
        synchronized(_blueClusterMap)
        {
            // Iterate over each Vector
            for(final Vector v: _blueVectorList)
            {
                // Get Closest Codeword
                final Vector codeword = getClosestCodeword(_blueCodebook, v);

                // Update Cluster Mapping
                _blueClusterMap.put(v, codeword);
            }
        }
    }

    /**
     * getClosestCodeword - Gets the Closest Codeword for an Input Vector
     * 
     * @param vector   - The Input Vector
     * @param codeword - The Codebook of Codewords
     */
    private Vector getClosestCodeword(final ArrayList<Vector> codebook, final Vector vector)
    {
        // Initialize the Closest Codeword
        Vector closestCodeword = new Vector(0,0);

        // Initialize the Minimum Distance
        double minDist = Double.MAX_VALUE;

        // Iterate over Codebook
        for(final Vector codeword : codebook)
        {
            // Get Distance between Codeword and Vector
            final int xDiff = codeword.getPixel1()-vector.getPixel1();
            final int yDiff = codeword.getPixel2()-vector.getPixel2();
            final double dist = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));

            // Check if now Minimum Distance
            if(dist < minDist)
            {
                // Update Closest Codeword
                closestCodeword = codeword;

                // Update Minimum Distance
                minDist = dist;
            }
        }

        return closestCodeword;
    }

    /**
     * getCluster - Gets the Clsuter for a Specific Codeword
     *
     * @param clusterMap - The Cluster Map
     * @param codeword   - The Codeword
     * @return ArrayList<Vector> - The Input Vectors associated with the Codeword
     */
    private ArrayList<Vector> getCluster(final HashMap<Vector, Vector> clusterMap, final Vector codeword)
    {
        // Create List of Vectors in Cluster
        ArrayList<Vector> vectorsInCluster = new ArrayList<Vector>();

        // Iterate over Input Vector Keys in Cluster Mapping
        for(final Vector inputVector : clusterMap.keySet())
        {
            // Get Codeword Value from Map
            final Vector cv = clusterMap.get(inputVector);

            // Get Codeword Value of Input Vector Key
            if(cv.getPixel1() == codeword.getPixel1() && cv.getPixel2() == codeword.getPixel2())
            {
                vectorsInCluster.add(inputVector);
            }
        }

        return vectorsInCluster;
    }
}