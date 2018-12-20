package data.imageTypes;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import data.vectors.Vector;
import data.vectors.Vector2by2;
import data.vectors.Vector4by4;
import enums.EVectorMode;

public class GrayscaleImage extends AbstractImage
{
    // Lists of Input Vectors
    ArrayList<Vector> _vectorList;         // Adjacent Pixel Vector List
    ArrayList<Vector2by2> _vector2by2List; // 2x2 Pixel Vector List
    ArrayList<Vector4by4> _vector4by4List; // 4x4 Pixel Vector List

    // The Codebook for Vector Quantization
    ArrayList<Vector> _codebook;
    ArrayList<Vector2by2> _codebook2by2;
    ArrayList<Vector4by4> _codebook4by4;

    // Mapping of Input Vectors to a Codeword
    HashMap<Vector, Vector> _clusterMap;
    HashMap<Vector2by2, Vector2by2> _cluster2by2Map;
    HashMap<Vector4by4, Vector4by4> _cluster4by4Map;

    /**
     * Constructor
     *
     * @param width  - The Width of the Image
     * @param height - The Height of the Image
     * @param n      - The Number of Vectors
     * @param mode   - How pixels should be grouped to form vectors
     */
    public GrayscaleImage(final int width, final int height, final int n, final EVectorMode mode)
    {
        super(width, height, n, mode);

        // Initialize the Codebook
        _codebook = new ArrayList<Vector>(n);
        _codebook2by2 = new ArrayList<Vector2by2>(n);
        _codebook4by4 = new ArrayList<Vector4by4>(n);

        // Initialize Cluster Mapping
        _clusterMap = new HashMap<Vector,Vector>();
        _cluster2by2Map = new HashMap<Vector2by2, Vector2by2>();
        _cluster4by4Map = new HashMap<Vector4by4, Vector4by4>();
    }

    /**
     * generateInputVectors - Generates the Input Vectors of a Grayscale Image
     *
     * @param image - Grayscale Image to Generate Vectors for
     * @param mode  - Pixel Grouping Mode to form Vectors
     */
    public void generateInputVectors(final BufferedImage image)
    {
        // Adjacent Pixel Vector Mode
        if(EVectorMode.SIDE_BY_SIDE == _mode)
        {
            // Initialize Pixels
            int pixel1, pixel2;

            // Get Size of Input Vectors
            int width = image.getWidth();
            int height = image.getHeight();
            int size = (width*height)/2;

            // Initialize List of Vectors
            _vectorList = new ArrayList<Vector>(size);

            // Iterate over Image Height
            for(int y = 0; y < image.getHeight(); ++y)
            {
                // Iterate over Image Width
                for(int x = 0; x < image.getWidth(); x+=2)
                {
                    // Get Pixel 1
                    pixel1 = image.getRGB(x,y);
                    int pix1 = (pixel1 & 0x0000ff00) >> 8;

                    // Get Pixel2
                    pixel2 = image.getRGB(x+1,y);
                    int pix2 = (pixel2 & 0x0000ff00) >> 8;

                    // Create new Adjacent Pixel Vector
                    Vector v = new Vector(pix1, pix2);

                    // Add Vector to List
                    _vectorList.add(v);
                }
            }
        }
        // 2x2 Pixel Vector Mode
        else if(EVectorMode.TWO_BY_TWO == _mode)
        {
            // Initialize 2x2 Pixels
            int pixel1, pixel2;
            int pixel3, pixel4;

            // Get Size of Input Vectors
            int width = image.getWidth();
            int height = image.getHeight();
            int size = (width*height)/4;

            // Initialize List of Vectors
            _vector2by2List = new ArrayList<Vector2by2>(size);

            // Iterate over Image Height
            for(int y = 0; y < image.getHeight(); y+=2)
            {
                // Iterate over Image Width
                for(int x = 0; x < image.getWidth(); x+=2)
                {
                    // Get First Row in Matrix
                    pixel1 = image.getRGB(x,y);
                    int pix1 = (pixel1 & 0x0000ff00) >> 8;
                    pixel2 = image.getRGB(x+1,y);
                    int pix2 = (pixel2 & 0x0000ff00) >> 8;

                    // Get Second Row in Matrix
                    pixel3 = image.getRGB(x,y+1);
                    int pix3 = (pixel3 & 0x0000ff00) >> 8;
                    pixel4 = image.getRGB(x+1, y+1);
                    int pix4 = (pixel4 & 0x0000ff00) >> 8;

                    // Create new 2x2 Pixel Vector
                    Vector2by2 v = new Vector2by2(pix1, pix2, pix3, pix4);

                    // Add Input Vectors to List
                    _vector2by2List.add(v);
                }
            }
        }
        else if(EVectorMode.FOUR_BY_FOUR == _mode)
        {
            // Initialize 4x4 Pixels
            int pixel1, pixel2, pixel3, pixel4;
            int pixel5, pixel6, pixel7, pixel8;
            int pixel9, pixel10, pixel11, pixel12;
            int pixel13, pixel14, pixel15, pixel16;

            // Get Size of Input Vectors
            int width = image.getWidth();
            int height = image.getHeight();
            int size = (width*height)/16;

            // Initialize List of Vectors
            _vector4by4List = new ArrayList<Vector4by4>(size);

            // Iterate over Image Height
            for(int y = 0; y < image.getHeight(); y+=4)
            {
                // Iterate over Image Width
                for(int x = 0; x < image.getWidth(); x+=4)
                {
                    // First Row of Matrix
                    pixel1 = image.getRGB(x,y);
                    int pix1 = (pixel1 & 0x0000ff00) >> 8;
                    pixel2 = image.getRGB(x+1,y);
                    int pix2 = (pixel2 & 0x0000ff00) >> 8;
                    pixel3 = image.getRGB(x+2,y);
                    int pix3 = (pixel3 & 0x0000ff00) >> 8;
                    pixel4 = image.getRGB(x+3,y);
                    int pix4 = (pixel4 & 0x0000ff00) >> 8;

                    // Second Row of Matrix
                    pixel5 = image.getRGB(x,y+1);
                    int pix5 = (pixel5 & 0x0000ff00) >> 8;
                    pixel6 = image.getRGB(x+1,y+1);
                    int pix6 = (pixel6 & 0x0000ff00) >> 8;
                    pixel7 = image.getRGB(x+2,y+1);
                    int pix7 = (pixel7 & 0x0000ff00) >> 8;
                    pixel8 = image.getRGB(x+3, y+1);
                    int pix8 = (pixel8 & 0x0000ff00) >> 8;

                    // Third Row of Matrix
                    pixel9 = image.getRGB(x,y+1);
                    int pix9 = (pixel9 & 0x0000ff00) >> 8;
                    pixel10 = image.getRGB(x+1,y+1);
                    int pix10 = (pixel10 & 0x0000ff00) >> 8;
                    pixel11 = image.getRGB(x+2,y+1);
                    int pix11 = (pixel11 & 0x0000ff00) >> 8;
                    pixel12 = image.getRGB(x+3, y+1);
                    int pix12 = (pixel12 & 0x0000ff00) >> 8;

                    // Fourth Row of Matrix
                    pixel13 = image.getRGB(x,y+1);
                    int pix13 = (pixel13 & 0x0000ff00) >> 8;
                    pixel14 = image.getRGB(x+1,y+1);
                    int pix14 = (pixel14 & 0x0000ff00) >> 8;
                    pixel15 = image.getRGB(x+2,y+1);
                    int pix15 = (pixel15 & 0x0000ff00) >> 8;
                    pixel16 = image.getRGB(x+3, y+1);
                    int pix16 = (pixel16 & 0x0000ff00) >> 8;

                    // Create new 4x4 Pixel Vector
                    Vector4by4 v = new Vector4by4(pix1,  pix2,  pix3,  pix4,
                        pix5,  pix6,  pix7,  pix8,
                        pix9,  pix10, pix11, pix12,
                        pix13, pix14, pix15, pix16);

                    // Add Input Vectors to List
                    _vector4by4List.add(v);
                }
            }
        }
    }

    /**
     * generateCodebook - Generates the Initial Codewords
     *                    for the Codebook
     *
     * @param n - The Number of Codwords to
     *            generate for the Codebook
     */
    public void generateCodebook()
    {
        // Blocks in a particular Dimension
        final int blockCount = PIX_DIM/_n;

        // If Adjacent Pixel Vector Mode
        if(EVectorMode.SIDE_BY_SIDE == _mode)
        {
            // Initialize Incrementors
            // for Codeword [x,y] Position
            int x = 0;
            int y = 0;

            // Keep adding until there are n codewords
            while(_codebook.size() < _n)
            {
                // Create new codeword
                Vector codeword = new Vector(x, y);

                // Add Codeword to Codebook
                _codebook.add(codeword);

                // Update Codeword [x,y] Position
                x += blockCount;
                y += blockCount;
            }
        }
        // If 2x2 Matrix Vector Mode
        else if(EVectorMode.TWO_BY_TWO.equals(_mode))
        {
            // Initialize Incrementors
            // for Codeword Position [a,b]
            //                       [c,d]
            int a = 0;
            int b = 0;
            int c = 0;
            int d = 0;

            // Keep adding until there are n codewords
            while(_codebook2by2.size() < _n)
            {
                // Create new codeword
                final Vector2by2 codeword = new Vector2by2(a,b,c,d);

                // Add Codeword to Codebook
                _codebook2by2.add(codeword);

                // Update Codeword [x,y] Position
                a += blockCount;
                b += blockCount;
                c += blockCount;
                d += blockCount;
            }
        }
        // If 4x4 Matrix Vector Mode
        else if(EVectorMode.FOUR_BY_FOUR.equals(_mode))
        {
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
            while(_codebook4by4.size() < _n)
            {
                // Create new codeword
                final Vector4by4 codeword = new Vector4by4(a,b,c,d,
                                                           e,f,g,h,
                                                           i,j,k,l,
                                                           m,n,o,p);

                // Add Codeword to Codebook
                _codebook4by4.add(codeword);

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
            }
        }
    }

    /**
     * verifyCodebook - Verifies the Codewords to best represent the data set
     *
     * @param n - Number of vectors to best represent Data Set
     * @return boolean
     */
    public boolean verifyCodebook()
    {
        // Indicator of Successful Codebook Generation
        boolean isSuccessful = true;

        // Generate Cluster Mapping
        // between Input Vectors and Codewords
        generateClusterMap();

        // Check if Vector Mode is Side by Side
        if(EVectorMode.SIDE_BY_SIDE.equals(_mode))
        {
            // Iterate over Codebook
            for(final Vector codeword : _codebook)
            {
                // Get Input Vectors in Cluster
                ArrayList<Vector> inputVectorList = getCluster(codeword);

                // Check if there are no Input Vectors in Cluster
                if(inputVectorList.size() == 0)
                {
                    // Get Index of Codebook
                    int index = _codebook.indexOf(codeword);
 
                    // Get Codeword[x,y]
                    int x = codeword.getPixel1();
                    int y = codeword.getPixel2();

                    // Update Codeword[x,y] Position
                    x = updatePixelPosition(x);
                    y = updatePixelPosition(y);

                    // Generate New Codeword for Codebook
                    Vector newCodeword = new Vector(x,y);
                    _codebook.set(index, newCodeword);

                    // Will need to reverify codebook
                    // as Codebook has been updated
                    isSuccessful = false;
                }
            }
        }
        // Check if Vector Mode is 2x2
        else if(EVectorMode.TWO_BY_TWO.equals(_mode))
        {
            // Iterate over Codebook
            for(final Vector2by2 codeword : _codebook2by2)
            {
                // Get Input Vectors in Cluster
                ArrayList<Vector2by2> inputVectorList = getCluster(codeword);

                // Check if there are no Input Vectors in Cluster
                if(inputVectorList.size() == 0)
                {
                    // Get Index of Codebook
                    int index = _codebook2by2.indexOf(codeword);
 
                    // Get Codeword Position [a,b]
                    //                       [c,d]
                    int a = codeword.getPixel1();
                    int b = codeword.getPixel2();
                    int c = codeword.getPixel3();
                    int d = codeword.getPixel4();

                    // Update Codeword Position [a,b]
                    //                          [c,d]
                    a = updatePixelPosition(a);
                    b = updatePixelPosition(b);
                    c = updatePixelPosition(c);
                    d = updatePixelPosition(d);

                    // Generate New Codeword for Codebook
                    Vector2by2 newCodeword = new Vector2by2(a,b,c,d);
                    _codebook2by2.set(index, newCodeword);

                    // Will need to reverify codebook
                    // as Codebook has been updated
                    isSuccessful = false;
                }
            }
        }
        // Check if Vector Mode is 4x4
        else if(EVectorMode.FOUR_BY_FOUR.equals(_mode))
        {
            // Iterate over Codebook
            for(final Vector4by4 codeword : _codebook4by4)
            {
                // Get Input Vectors in Cluster
                ArrayList<Vector4by4> inputVectorList = getCluster(codeword);

                // Check if there are no Input Vectors in Cluster
                if(inputVectorList.size() == 0)
                {
                    // Get Index of Codebook
                    int index = _codebook4by4.indexOf(codeword);
 
                    // Get Codeword Position [a,b,c,d]
                    //                       [e,f,g,h]
                    //                       [i,j,k,l]
                    //                       [m,n,o,p]
                    int a = codeword.getPixel1();
                    int b = codeword.getPixel2();
                    int c = codeword.getPixel3();
                    int d = codeword.getPixel4();
                    int e = codeword.getPixel5();
                    int f = codeword.getPixel6();
                    int g = codeword.getPixel7();
                    int h = codeword.getPixel8();
                    int i = codeword.getPixel9();
                    int j = codeword.getPixel10();
                    int k = codeword.getPixel11();
                    int l = codeword.getPixel12();
                    int m = codeword.getPixel13();
                    int n = codeword.getPixel14();
                    int o = codeword.getPixel15();
                    int p = codeword.getPixel16();


                    // Update Codeword Position [a,b,c,d]
                    //                          [e,f,g,h]
                    //                          [i,j,k,l]
                    //                          [m,n,o,p]
                    a = updatePixelPosition(a);
                    b = updatePixelPosition(b);
                    c = updatePixelPosition(c);
                    d = updatePixelPosition(d);

                    e = updatePixelPosition(e);
                    f = updatePixelPosition(f);
                    g = updatePixelPosition(g);
                    h = updatePixelPosition(h);

                    i = updatePixelPosition(i);
                    j = updatePixelPosition(j);
                    k = updatePixelPosition(k);
                    l = updatePixelPosition(l);

                    m = updatePixelPosition(m);
                    n = updatePixelPosition(n);
                    o = updatePixelPosition(o);
                    p = updatePixelPosition(p);


                    // Generate New Codeword for Codebook
                    final Vector4by4 newCodeword = new Vector4by4(a,b,c,d,
                                                                  e,f,g,h,
                                                                  i,j,k,l,
                                                                  m,n,o,p);

                    _codebook4by4.set(index, newCodeword);

                    // Will need to reverify codebook
                    // as Codebook has been updated
                    isSuccessful = false;
                }
            }
        }

        return isSuccessful;
    }

    /**
     * updateCodebook
     */
    public double updateCodebook()
    {
        // Initialize Error Accumulator
        double error = 0.0;

        // Update the Cluster Mapping
        generateClusterMap();

        // Check if Adjacent Pixel Vector Mode
        if(EVectorMode.SIDE_BY_SIDE.equals(_mode))
        {
            // Initialize New Codeword List
            final ArrayList<Vector> newCodewordList = new ArrayList<Vector>();

            // Iterate over each codeword
            for(Vector codeword : _codebook)
            {
                // Get Cluster associated with codeword
                final ArrayList<Vector> clusterVectors = getCluster(codeword);

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
            _codebook.clear();
            _codebook.addAll(newCodewordList);
        }
        // Check if 2x2 Matrix Vector Mode
        else if(EVectorMode.TWO_BY_TWO.equals(_mode))
        {
            // Initialize New Codeword List
            final ArrayList<Vector2by2> newCodewordList = new ArrayList<Vector2by2>();

            // Iterate over each codeword
            for(final Vector2by2 codeword : _codebook2by2)
            {
                // Get Cluster associated with codeword
                final ArrayList<Vector2by2> clusterVectors = getCluster(codeword);

                // Get Number of Cluster Vectors
                final int m = clusterVectors.size();

                // Ensure Cluster has Vectors
                if(m != 0)
                {
                    // Initialize Accumulations
                    int sumClusterA = 0;
                    int sumClusterB = 0;
                    int sumClusterC = 0;
                    int sumClusterD = 0;

                    // Iterate over Vectors in Cluster
                    for(final Vector2by2 v : clusterVectors)
                    {
                        sumClusterA += v.getPixel1();
                        sumClusterB += v.getPixel2();
                        sumClusterC += v.getPixel3();
                        sumClusterD += v.getPixel4();
                    }

                    // Get New Codeword
                    Vector2by2 newCodeword = new Vector2by2(sumClusterA/m, sumClusterB/m, sumClusterC/m, sumClusterD/m);
                    newCodewordList.add(newCodeword);

                    // Get Error between old Codeword and new Codeword
                    error += getError(codeword, newCodeword);
                }
            }

            // Update Codebook with Latest Codewords
            _codebook2by2.clear();
            _codebook2by2.addAll(newCodewordList);
        }
        // Check if 4x4 Matrix Vector Mode
        else if(EVectorMode.FOUR_BY_FOUR.equals(_mode))
        {
            // Initialize New Codeword List
            final ArrayList<Vector4by4> newCodewordList = new ArrayList<Vector4by4>();

            // Iterate over each codeword
            for(final Vector4by4 codeword : _codebook4by4)
            {
                // Get Cluster associated with codeword
                final ArrayList<Vector4by4> clusterVectors = getCluster(codeword);

                // Get Number of Cluster Vectors
                final int m = clusterVectors.size();

                // Ensure Cluster has Vectors
                if(m != 0)
                {
                    // Initialize Accumulations
                    int sumClusterA = 0, sumClusterB = 0, sumClusterC = 0, sumClusterD = 0;
                    int sumClusterE = 0, sumClusterF = 0, sumClusterG = 0, sumClusterH = 0;
                    int sumClusterI = 0, sumClusterJ = 0, sumClusterK = 0, sumClusterL = 0;
                    int sumClusterM = 0, sumClusterN = 0, sumClusterO = 0, sumClusterP = 0;

                    // Iterate over Vectors in Cluster
                    for(final Vector4by4 v : clusterVectors)
                    {
                        sumClusterA += v.getPixel1();
                        sumClusterB += v.getPixel2();
                        sumClusterC += v.getPixel3();
                        sumClusterD += v.getPixel4();

                        sumClusterE += v.getPixel5();
                        sumClusterF += v.getPixel6();
                        sumClusterG += v.getPixel7();
                        sumClusterH += v.getPixel8();

                        sumClusterI += v.getPixel9();
                        sumClusterJ += v.getPixel10();
                        sumClusterK += v.getPixel11();
                        sumClusterL += v.getPixel12();

                        sumClusterM += v.getPixel13();
                        sumClusterN += v.getPixel14();
                        sumClusterO += v.getPixel15();
                        sumClusterP += v.getPixel16();
                    }

                    // Get New Codeword
                    Vector4by4 newCodeword = new Vector4by4(sumClusterA/m, sumClusterB/m, sumClusterC/m, sumClusterD/m,
                                                            sumClusterE/m, sumClusterF/m, sumClusterG/m, sumClusterH/m,
                                                            sumClusterI/m, sumClusterJ/m, sumClusterK/m, sumClusterL/m,
                                                            sumClusterM/m, sumClusterN/m, sumClusterO/m, sumClusterP/m);
                    newCodewordList.add(newCodeword);

                    // Get Error between old Codeword and new Codeword
                    error += getError(codeword, newCodeword);
                }
            }

            // Update Codebook with Latest Codewords
            _codebook4by4.clear();
            _codebook4by4.addAll(newCodewordList);
        }


        return error;
    }

    /**
     * generateClusterMap - Updates the Cluster Map
     *                      by Mapping the Closest Codeword
     *                      to each Input Vector
     */
    public void generateClusterMap()
    {
        // If Mode is Adjacent Pixels
        if(EVectorMode.SIDE_BY_SIDE.equals(_mode))
        {
            synchronized(_clusterMap)
            {
                // Iterate over each Vector
                for(final Vector v: _vectorList)
                {
                    // Get Closest Codeword
                    final Vector codeword = getClosestCodeword(v);

                    // Update Cluster Mapping
                    _clusterMap.put(v, codeword);
                }
            } 
        }
        // If Mode is 2x2 Vector Matrix
        else if(EVectorMode.TWO_BY_TWO.equals(_mode))
        {
            synchronized(_cluster2by2Map)
            {
                // Iterate over each Vector
                for(final Vector2by2 v: _vector2by2List)
                {
                    // Get Closest Codeword
                    final Vector2by2 codeword = getClosestCodeword(v);

                    // Update Cluster Mapping
                    _cluster2by2Map.put(v, codeword);
                }
            }
        }
        // If Mode is 4x4 Vector Matrix
        else if(EVectorMode.FOUR_BY_FOUR.equals(_mode))
        {
            synchronized(_cluster4by4Map)
            {
                // Iterate over each Vector
                for(final Vector4by4 v: _vector4by4List)
                {
                    // Get Closest Codeword
                    final Vector4by4 codeword = getClosestCodeword(v);
 
                    // Update Cluster Mapping
                    _cluster4by4Map.put(v, codeword);
                }
            }
        }
    }

    /**
     * quantizeImage
     */
    public ArrayList<Integer> quantizeImage()
    {
        //Create List of Indices
        ArrayList<Integer> indices;

        // Check if Adjacent Pixel Vector Mode
        if(EVectorMode.SIDE_BY_SIDE.equals(_mode))
        {
            // Get the Size of the Input Vector array
            int size = (_width*_height)/2;

            // Initialize List of Indices
            indices = new ArrayList<Integer>(size);

            // Iterate over each vector in Vector List
            for(final Vector v: _vectorList)
            {
                // Get Codeword associated with that Vector
                final Vector codeword = _clusterMap.get(v);

                // Get Index of Codeword
                final int codewordIndex = _codebook.indexOf(codeword);

                // Add Index of Codeword to Indexes Array
                indices.add(codewordIndex);
            }
        }
        // Check if 2x2 Pixel Vector Mode
        else if(EVectorMode.TWO_BY_TWO.equals(_mode))
        {
            // Get the Size of the Input Vector array
            int size = (_width*_height)/4;

            // Initialize List of Indices
            indices = new ArrayList<Integer>(size);

            // Iterate over each vector in Vector List
            for(final Vector2by2 v: _vector2by2List)
            {
                // Get Codeword associated with that Vector
                final Vector2by2 codeword = _cluster2by2Map.get(v);

                // Get Index of Codeword
                final int codewordIndex = _codebook2by2.indexOf(codeword);

                // Add Index of Codeword to Indexes Array
                indices.add(codewordIndex);
            }
        }
        // if 4x4 Pixel Vector Mode
        else
        {
            // Get the Size of the Input Vector array
            int size = (_width*_height)/16;

            // Initialize List of Indices
            indices = new ArrayList<Integer>(size);

            // Iterate over each vector in Vector List
            for(final Vector4by4 v: _vector4by4List)
            {
                // Get Codeword associated with that Vector
                final Vector4by4 codeword = _cluster4by4Map.get(v);

                // Get Index of Codeword
                final int codewordIndex = _codebook4by4.indexOf(codeword);

                // Add Index of Codeword to Indexes Array
                indices.add(codewordIndex);
            }
        }

        return indices;
    }

    /**
     * reconstructImage
     *
     * @param mode
     * @return BufferedImage
     */
    public BufferedImage reconstructImage(final ArrayList<Integer> indexes)
    {
        // Initialize Raw Image
        BufferedImage image = new BufferedImage(_width, _height, BufferedImage.TYPE_BYTE_GRAY);

        int pix;
        byte gray;
        int count = 0;

        // Check if Adjacent Pixel Vector Mode
        if(EVectorMode.SIDE_BY_SIDE.equals(_mode))
        {
            // Iterate over Y Values
            for(int y = 0; y < _height; y++)
            {
                // Iterate over X Values
                for(int x = 0; x < _width; x+=2)
                {
                    // Get the Index into the Codebook from the Indexes Array
                    final int index = indexes.get(count);

                    // Get Code Vector from Codebook
                    final Vector v = _codebook.get(index);

                    // Get Pixel 1
                    final int pix1 = v.getPixel1();
                    gray = (byte) pix1;
                    pix = 0xff000000 | ((gray & 0xff) << 8);
                    image.setRGB(x, y, pix);

                    // Get Pixel 2
                    final int pix2 = v.getPixel2();
                    gray = (byte) pix2;
                    pix = 0xff000000 | ((gray & 0xff) << 8);
                    image.setRGB(x+1, y, pix);

                    // Increment Count
                    count++;
                }
            }
        }
        // Check if 2x2 Pixel Matrix Vector Mode
        else if(EVectorMode.TWO_BY_TWO.equals(_mode))
        {
            // Iterate over Y Values
            for(int y = 0; y < _height; y+=2)
            {
                // Iterate over X Values
                for(int x = 0; x < _width; x+=2)
                {
                    // Get the Index into the Codebook from the Indexes Array
                    final int index = indexes.get(count);

                    // Get Code Vector from Codebook
                    final Vector2by2 v = _codebook2by2.get(index);

                    // Get Pixel 1
                    final int pix1 = v.getPixel1();
                    gray = (byte) pix1;
                    pix = 0xff000000 | ((gray & 0xff) << 8);
                    image.setRGB(x, y, pix);

                    // Get Pixel 2
                    final int pix2 = v.getPixel2();
                    gray = (byte) pix2;
                    pix = 0xff000000 | ((gray & 0xff) << 8);
                    image.setRGB(x+1, y, pix);

                    // Get Pixel 3
                    final int pix3 = v.getPixel3();
                    gray = (byte) pix3;
                    pix = 0xff000000 | ((gray & 0xff) << 8);
                    image.setRGB(x, y+1, pix);

                    // Get Pixel 4
                    final int pix4 = v.getPixel4();
                    gray = (byte) pix4;
                    pix = 0xff000000 | ((gray & 0xff) << 8);
                    image.setRGB(x+1, y+1, pix);

                    // Increment Count
                    count++;
                }
            }
        }
        else if(EVectorMode.FOUR_BY_FOUR.equals(_mode))
        {
            // Iterate over Y Values
            for(int y = 0; y < _height; y+=4)
            {
                // Iterate over X Values
                for(int x = 0; x < _width; x+=4)
                {
                    // Get the Index into the Codebook from the Indexes Array
                    final int index = indexes.get(count);

                    // Get Code Vector from Codebook
                    final Vector4by4 v = _codebook4by4.get(index);

                    // Get Pixel 1
                    final int pix1 = v.getPixel1();
                    gray = (byte) pix1;
                    pix = 0xff000000 | ((gray & 0xff) << 8);
                    image.setRGB(x, y, pix);

                    // Get Pixel 2
                    final int pix2 = v.getPixel2();
                    gray = (byte) pix2;
                    pix = 0xff000000 | ((gray & 0xff) << 8);
                    image.setRGB(x+1, y, pix);

                    // Get Pixel 3
                    final int pix3 = v.getPixel3();
                    gray = (byte) pix3;
                    pix = 0xff000000 | ((gray & 0xff) << 8);
                    image.setRGB(x+2, y, pix);

                    // Get Pixel 4
                    final int pix4 = v.getPixel4();
                    gray = (byte) pix4;
                    pix = 0xff000000 | ((gray & 0xff) << 8);
                    image.setRGB(x+3, y, pix);

                    // Get Pixel 5
                    final int pix5 = v.getPixel5();
                    gray = (byte) pix5;
                    pix = 0xff000000 | ((gray & 0xff) << 8);
                    image.setRGB(x, y+1, pix);

                    // Get Pixel 6
                    final int pix6 = v.getPixel6();
                    gray = (byte) pix6;
                    pix = 0xff000000 | ((gray & 0xff) << 8);
                    image.setRGB(x+1, y+1, pix);

                    // Get Pixel 7
                    final int pix7 = v.getPixel7();
                    gray = (byte) pix7;
                    pix = 0xff000000 | ((gray & 0xff) << 8);
                    image.setRGB(x+2, y+1, pix);

                    // Get Pixel 8
                    final int pix8 = v.getPixel8();
                    gray = (byte) pix8;
                    pix = 0xff000000 | ((gray & 0xff) << 8);
                    image.setRGB(x+3, y+1, pix);

                    // Get Pixel 9
                    final int pix9 = v.getPixel9();
                    gray = (byte) pix9;
                    pix = 0xff000000 | ((gray & 0xff) << 8);
                    image.setRGB(x, y+2, pix);

                    // Get Pixel 10
                    final int pix10 = v.getPixel10();
                    gray = (byte) pix10;
                    pix = 0xff000000 | ((gray & 0xff) << 8);
                    image.setRGB(x+1, y+2, pix);

                    // Get Pixel 11
                    final int pix11 = v.getPixel11();
                    gray = (byte) pix11;
                    pix = 0xff000000 | ((gray & 0xff) << 8);
                    image.setRGB(x+2, y+2, pix);

                    // Get Pixel 12
                    final int pix12 = v.getPixel12();
                    gray = (byte) pix12;
                    pix = 0xff000000 | ((gray & 0xff) << 8);
                    image.setRGB(x+3, y+2, pix);

                    // Get Pixel 13
                    final int pix13 = v.getPixel13();
                    gray = (byte) pix13;
                    pix = 0xff000000 | ((gray & 0xff) << 8);
                    image.setRGB(x, y+3, pix);

                    // Get Pixel 14
                    final int pix14 = v.getPixel14();
                    gray = (byte) pix14;
                    pix = 0xff000000 | ((gray & 0xff) << 8);
                    image.setRGB(x+1, y+3, pix);

                    // Get Pixel 15
                    final int pix15 = v.getPixel15();
                    gray = (byte) pix15;
                    pix = 0xff000000 | ((gray & 0xff) << 8);
                    image.setRGB(x+2, y+3, pix);

                    // Get Pixel 16
                    final int pix16 = v.getPixel16();
                    gray = (byte) pix16;
                    pix = 0xff000000 | ((gray & 0xff) << 8);
                    image.setRGB(x+3, y+3, pix);


                    // Increment Count
                    count++;
                }
            }
        }

        return image;
    }

    /**
     * generateVectorSpace - Generates a Test Image of the Vector Space
     *
     * @param width
     * @param height
     * @return BufferedImage
     */
    public BufferedImage generateVectorSpace(final int width, final int height)
    {
        // Initialize Compressed Image
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Iterate over Y coordinates of Image
        for(int y = 0; y < height; y++)
        {
            // Iterate over X coordinates of Image
            for(int x = 0; x < width; x++)
            {
                // Get Pixel Value
                byte r = (byte) 255;
                byte g = (byte) 255;
                byte b = (byte) 255;
                int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);

                // Set Pixel of Image to White
                img.setRGB(x,y,pix);
            }
        }

        // Iterate over all Input Vectors
        for(int i=0; i < _vectorList.size(); ++i)
        {
            // Get Pixel Value
            byte r = (byte) 0;
            byte g = (byte) 0;
            byte b = (byte) 0;
            int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);

            // Get (x,y) from Vector
            Vector v = _vectorList.get(i);
            final int x = v.getPixel1();
            final int y = v.getPixel2();

            // Update Pixel in Image
            img.setRGB(x, y, pix);
        }

        // Iterate over the Codebook
        for(int i=0; i < _codebook.size(); ++i)
        {
            // Get Pixel Value
            byte r = (byte) 255;
            byte g = (byte) 0;
            byte b = (byte) 0;
            int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);

            // Get (x,y) from Codebook
            Vector v = _codebook.get(i);
            final int x = v.getPixel1();
            final int y = v.getPixel2();

            // Update Pixel in Image
            img.setRGB(x, y, pix);
        }

        return img;
    }

    /**
     * getClosestCodeword
     *
     * @param vector - 
     * @param codeword
     */
    private Vector getClosestCodeword(final Vector vector)
    {
        // Initialize the Closest Codeword
        Vector closestCodeword = new Vector(0,0);

        // Initialize the Minimum Distance
        double minDist = Double.MAX_VALUE;

        // Iterate over Codewords
        for(final Vector codeword : _codebook)
        {
            // Calculate Differences
            int diff1 = codeword.getPixel1()-vector.getPixel1();
            int diff2 = codeword.getPixel2()-vector.getPixel2();

            // Get Distance between Codeword and Vector
            final double dist = Math.sqrt(Math.pow(diff1, 2) + Math.pow(diff2, 2));

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
     * getClosestCodeword
     *
     * @param vector - 
     * @param codeword
     */
    private Vector2by2 getClosestCodeword(final Vector2by2 vector)
    {
        // Initialize the Closest Codeword
        Vector2by2 closestCodeword = new Vector2by2(0,0,0,0);

        // Initialize the Minimum Distance
        double minDist = Double.MAX_VALUE;

        // Iterate over Codewords
        for(final Vector2by2 codeword : _codebook2by2)
        {
            // Calculate Differences
            int diff1 = codeword.getPixel1()-vector.getPixel1();
            int diff2 = codeword.getPixel2()-vector.getPixel2();
            int diff3 = codeword.getPixel3()-vector.getPixel3();
            int diff4 = codeword.getPixel4()-vector.getPixel4();

            // Get Distance between Codeword and Vector
            final double dist = Math.sqrt(Math.pow(diff1, 2) + Math.pow(diff2, 2) +
                                          Math.pow(diff3, 2) + Math.pow(diff4, 2));

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
     * getClosestCodeword
     *
     * @param vector - 
     * @param codeword
     */
    private Vector4by4 getClosestCodeword(final Vector4by4 vector)
    {
        // Initialize the Closest Codeword
        Vector4by4 closestCodeword = new Vector4by4(0,0,0,0,
                                                    0,0,0,0,
                                                    0,0,0,0,
                                                    0,0,0,0);

        // Initialize the Minimum Distance
        double minDist = Double.MAX_VALUE;

        // Iterate over Codewords
        for(final Vector4by4 codeword : _codebook4by4)
        {
            // Calculate Differences
            int diff1 = codeword.getPixel1()-vector.getPixel1();
            int diff2 = codeword.getPixel2()-vector.getPixel2();
            int diff3 = codeword.getPixel3()-vector.getPixel3();
            int diff4 = codeword.getPixel4()-vector.getPixel4();

            int diff5 = codeword.getPixel5()-vector.getPixel5();
            int diff6 = codeword.getPixel6()-vector.getPixel6();
            int diff7 = codeword.getPixel7()-vector.getPixel7();
            int diff8 = codeword.getPixel8()-vector.getPixel8();

            int diff9 = codeword.getPixel9()-vector.getPixel9();
            int diff10 = codeword.getPixel10()-vector.getPixel10();
            int diff11 = codeword.getPixel11()-vector.getPixel11();
            int diff12 = codeword.getPixel12()-vector.getPixel12();

            int diff13 = codeword.getPixel13()-vector.getPixel13();
            int diff14 = codeword.getPixel14()-vector.getPixel14();
            int diff15 = codeword.getPixel15()-vector.getPixel15();
            int diff16 = codeword.getPixel16()-vector.getPixel16();

            // Get Distance between Codeword and Vector
            final double dist = Math.sqrt(Math.pow(diff1, 2) + Math.pow(diff2, 2) + Math.pow(diff3, 2) + Math.pow(diff4, 2) +
                                          Math.pow(diff5, 2) + Math.pow(diff6, 2) + Math.pow(diff7, 2) + Math.pow(diff8, 2) +
                                          Math.pow(diff9, 2) + Math.pow(diff10, 2) + Math.pow(diff11, 2) + Math.pow(diff12, 2) +
                                          Math.pow(diff13, 2) + Math.pow(diff14, 2) + Math.pow(diff15, 2) + Math.pow(diff16, 2));

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
     * getCluster
     *
     * @param codeword
     * @return 
     */
    private ArrayList<Vector> getCluster(final Vector codeword)
    {
        // Create List of Vectors in Cluster
        ArrayList<Vector> vectorsInCluster = new ArrayList<Vector>();

        synchronized(_clusterMap)
        {
            // Iterate over Input Vector Keys in Cluster Mapping
            for(final Vector inputVector : _clusterMap.keySet())
            {
                // Get Codeword Value from Map
                final Vector cv = _clusterMap.get(inputVector);

                // Get Codeword Value of Input Vector Key
                if(cv.getPixel1() == codeword.getPixel1() && cv.getPixel2() == codeword.getPixel2())
                {
                    vectorsInCluster.add(inputVector);
                }
            }
        }

        return vectorsInCluster;
    }

    /**
     * getCluster
     *
     * @param codeword
     * @return ArrayList<Vector4by4>
     */
    private ArrayList<Vector4by4> getCluster(final Vector4by4 codeword)
    {
        // Create List of Vectors in Cluster
        ArrayList<Vector4by4> vectorsInCluster = new ArrayList<Vector4by4>();

        synchronized(_cluster4by4Map)
        {
            // Iterate over Input Vector Keys in Cluster Mapping
            for(final Vector4by4 inputVector : _cluster4by4Map.keySet())
            {
                // Get Codeword Value from Map
                final Vector4by4 cv = _cluster4by4Map.get(inputVector);

                // Get Codeword Value of Input Vector Key
                if(cv.getPixel1() == codeword.getPixel1() && 
                   cv.getPixel2() == codeword.getPixel2() &&
                   cv.getPixel3() == codeword.getPixel3() &&
                   cv.getPixel4() == codeword.getPixel4() &&
                   cv.getPixel5() == codeword.getPixel5() &&
                   cv.getPixel6() == codeword.getPixel6() &&
                   cv.getPixel7() == codeword.getPixel7() &&
                   cv.getPixel8() == codeword.getPixel8() &&
                   cv.getPixel9() == codeword.getPixel9() && 
                   cv.getPixel10() == codeword.getPixel10() &&
                   cv.getPixel11() == codeword.getPixel11() &&
                   cv.getPixel12() == codeword.getPixel12() &&
                   cv.getPixel13() == codeword.getPixel13() &&
                   cv.getPixel14() == codeword.getPixel14() &&
                   cv.getPixel15() == codeword.getPixel15() &&
                   cv.getPixel16() == codeword.getPixel16())
                {
                    vectorsInCluster.add(inputVector);
                }
            }
        }

        return vectorsInCluster;
    }

    /**
     * getCluster
     *
     * @param codeword
     * @return ArrayList<Vector2by2>
     */
    private ArrayList<Vector2by2> getCluster(final Vector2by2 codeword)
    {
        // Create List of Vectors in Cluster
        ArrayList<Vector2by2> vectorsInCluster = new ArrayList<Vector2by2>();

        synchronized(_cluster2by2Map)
        {
            // Iterate over Input Vector Keys in Cluster Mapping
            for(final Vector2by2 inputVector : _cluster2by2Map.keySet())
            {
                // Get Codeword Value from Map
                final Vector2by2 cv = _cluster2by2Map.get(inputVector);

                // Get Codeword Value of Input Vector Key
                if(cv.getPixel1() == codeword.getPixel1() && 
                   cv.getPixel2() == codeword.getPixel2() &&
                   cv.getPixel3() == codeword.getPixel3() &&
                   cv.getPixel4() == codeword.getPixel4())
                {
                    vectorsInCluster.add(inputVector);
                }
            }
        }

        return vectorsInCluster;
    }
}