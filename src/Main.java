import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Main {
    
    public static String imagePath = "C:\\Users\\didic\\Desktop\\Thread Portrait Project\\pelusaTest.png";

    public static int maxIterations = 2000;
    public static int numNails = 400;

    public static boolean drawNails = false;
    
    public static void main(String[] args) throws IOException {
        //Imaginary im = new Imaginary(20,30,false);
        //im.printImaginary();

        threadMaking();
    }

    public static void threadMaking(){

        //Load model image
        BufferedImage image = ImageManager.loadImage(new File(imagePath));

        //Cuts said image into a circle
        int width = image.getWidth();

        //Gray scale Circular image
        BufferedImage circleBuffer = new BufferedImage(width, width, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2 = circleBuffer.createGraphics();
        g2.setClip(new Ellipse2D.Float(0, 0, width, width));
        g2.drawImage(image, 0, 0, width, width, null);

        Imaginary[] points = getNails(circleBuffer, g2, drawNails);

        //BufferedImage outputImage = getOutputImage(image);
        BufferedImage outputImage = getOutputImage(image);

        List<Integer> listOfNailsDone = lineDrawingAlgorithmBW(circleBuffer, points, outputImage);


        ImageManager.writeNailDataToFile(listOfNailsDone, "instructions-" + maxIterations + "-" + numNails);

        //Saves a given image
        ImageManager.saveImage(circleBuffer, "test");
        ImageManager.saveImage(outputImage, "output " + maxIterations + " " + numNails + " test");
    }

    private static List<Integer> lineDrawingAlgorithmBW(BufferedImage circleBuffer, Imaginary[] points, BufferedImage outputImage) {
        //Line Drawing Algorithm
        List<Integer> listOfNailsDone = new ArrayList<Integer>();

        int startingNail = new Random().nextInt(numNails);
        for (int i = 0; i < maxIterations; i++) {

            double startingDarkness = 100000;
            int pointNum = 0;

            //Check for a single line -----------------
            for (int j = 0; j < points.length; j++) {
                //Check Darkest line between starting nail and all others
                int x1 = (int)(points[startingNail].getX() + circleBuffer.getWidth() / 2d);
                int y1 = (int)(points[startingNail].getY() + circleBuffer.getHeight() / 2d);

                int x2 = (int)(points[j].getX() + circleBuffer.getWidth() / 2d);
                int y2 = (int)(points[j].getY() + circleBuffer.getHeight() / 2d);

                //get darkness Beneath a single given line
                double darkness = ImageManager.getDarknessBeneathLine(x1,y1,x2,y2, circleBuffer);

                if(darkness < startingDarkness && Math.abs(pointNum - j) > 10){
                    startingDarkness = darkness;
                    pointNum = j;
                }
            }

            int x1 = (int)(points[startingNail].getX() + circleBuffer.getWidth() / 2d);
            int y1 = (int)(points[startingNail].getY() + circleBuffer.getHeight() / 2d);

            int x2 = (int)(points[pointNum].getX() + circleBuffer.getWidth() / 2d);
            int y2 = (int)(points[pointNum].getY() + circleBuffer.getHeight() / 2d);

            //Draw Line Input Image
            ImageManager.drawLine(x1,y1,x2,y2,Color.white, circleBuffer);

            System.out.println("x1: " + x1 + " y1: " + y1 + " x2: " + x2 + " y2: " + y2); //-----> DEBUG POSITIONS

            //Draw Line Output Image
            ImageManager.drawLine(x1,y1,x2,y2,Color.black, outputImage);

            startingNail = pointNum;
            listOfNailsDone.add(startingNail);
            //-------------------------------------
        }
        return listOfNailsDone;
    }

    private static Imaginary[] getNails(BufferedImage circleBuffer, Graphics2D g2, boolean drawNails) {
        //DrawNails
        g2.setColor(Color.WHITE);
        int ovalW = 10;
        int ovalH = 10;
        Imaginary[] points = ImageManager.getPerimeterPoints(numNails, circleBuffer);

        if(drawNails){
            //draw nails to output image
            for (int i = 0; i < points.length; i++) {
                int x = (int)(points[i].getX() + circleBuffer.getWidth() / 2);
                int y = (int)(points[i].getY() + circleBuffer.getHeight() / 2);
                g2.fillOval(x-ovalW/2,y-ovalH/2,ovalW,ovalH);
            }
        }

        return points;
    }

    private static BufferedImage getOutputImage(BufferedImage image) {
        //--- Create Output Image ---
        BufferedImage outputImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D outputGraphics = outputImage.createGraphics();

        outputGraphics.setPaint(Color.black);
        outputGraphics.fillRect(0,0,outputImage.getWidth(), outputImage.getHeight());

        outputGraphics.setPaint(Color.white);
        outputGraphics.fillOval(0,0, outputImage.getWidth(), outputImage.getHeight());
        return outputImage;
    }

}
