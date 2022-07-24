import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ImageManager {

    public static BufferedImage loadImage(File file){
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bi;
    }

    public static void showPicture(BufferedImage img){
        JFrame frame = new JFrame();
        ImageIcon icon = new ImageIcon(img);
        JLabel label = new JLabel(icon);
        frame.add(label);
        frame.setDefaultCloseOperation
                (JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void saveImage(BufferedImage img, String name){
        try{
            File outputFile = new File(name + ".png");
            ImageIO.write(img, "png", outputFile);
        }
        catch (IOException e){
            System.out.println("Something Went Wrong!");
        }

    }

    public static void saveImage(BufferedImage img){
        try{
            File outputFile = new File("NoName.png");
            ImageIO.write(img, "png", outputFile);
        }
        catch (IOException e){
            System.out.println("Something Went Wrong!");
        }

    }

    public static void writeNailDataToFile(List<Integer> nailList, String fileName){
        try{
            FileWriter fw = new FileWriter(fileName + ".txt");

            int[] arrayOfNails = nailList.stream().mapToInt(Integer::intValue).toArray();

            for (int i = 0; i < arrayOfNails.length; i++) {

                fw.write(arrayOfNails[i] + " ");

                if(i % 30 == 0){
                    fw.write("\n");
                    fw.write("==========================================================================================\n");
                }
            }

            fw.close();
        }
        catch (IOException e){
            System.out.println("Error When Trying To Write To NailData File!");
        }
    }

    public static Imaginary[] getPerimeterPoints(int numPoints, BufferedImage img){
        Imaginary[] result = new Imaginary[numPoints];

        double angle = 0;
        double offset = (2*Math.PI) / numPoints;

        for (int i = 0; i < result.length; i++) {
            result[i] = new Imaginary(img.getWidth() / 2d, angle,true);
            angle += offset;
        }

        return result;
    }

    public static double getDarknessBeneathLine(int x, int y, int x2, int y2, BufferedImage img){

        List<Integer> listOfPixels = new ArrayList<Integer>();

        int w = x2 - x ;
        int h = y2 - y ;
        int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0 ;
        if (w<0) dx1 = -1 ; else if (w>0) dx1 = 1 ;
        if (h<0) dy1 = -1 ; else if (h>0) dy1 = 1 ;
        if (w<0) dx2 = -1 ; else if (w>0) dx2 = 1 ;
        int longest = Math.abs(w) ;
        int shortest = Math.abs(h) ;
        if (!(longest>shortest)) {
            longest = Math.abs(h) ;
            shortest = Math.abs(w) ;
            if (h<0) dy2 = -1 ; else if (h>0) dy2 = 1 ;
            dx2 = 0 ;
        }
        int numerator = longest >> 1 ;
        for (int i=0;i<=longest;i++) {
            if(x < img.getWidth() && y < img.getHeight()){ // To avoid out of bounds error
                listOfPixels.add(img.getRGB(x,y));
            }
            numerator += shortest ;
            if (!(numerator<longest)) {
                numerator -= longest ;
                x += dx1 ;
                y += dy1 ;
            } else {
                x += dx2 ;
                y += dy2 ;
            }
        }

        int[] arrayOfPixels = listOfPixels.stream().mapToInt(Integer::intValue).toArray();

        return meanValueOfColorArray(arrayOfPixels);
    }

    public static double getAverageColorBeneathLine(int x, int y, int x2, int y2, BufferedImage img){

        List<Integer> listOfPixels = new ArrayList<Integer>();

        int w = x2 - x ;
        int h = y2 - y ;
        int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0 ;
        if (w<0) dx1 = -1 ; else if (w>0) dx1 = 1 ;
        if (h<0) dy1 = -1 ; else if (h>0) dy1 = 1 ;
        if (w<0) dx2 = -1 ; else if (w>0) dx2 = 1 ;
        int longest = Math.abs(w) ;
        int shortest = Math.abs(h) ;
        if (!(longest>shortest)) {
            longest = Math.abs(h) ;
            shortest = Math.abs(w) ;
            if (h<0) dy2 = -1 ; else if (h>0) dy2 = 1 ;
            dx2 = 0 ;
        }
        int numerator = longest >> 1 ;
        for (int i=0;i<=longest;i++) {
            if(x < img.getWidth() && y < img.getHeight()){ // To avoid out of bounds error
                listOfPixels.add(img.getRGB(x,y));
            }
            numerator += shortest ;
            if (!(numerator<longest)) {
                numerator -= longest ;
                x += dx1 ;
                y += dy1 ;
            } else {
                x += dx2 ;
                y += dy2 ;
            }
        }

        int[] arrayOfPixels = listOfPixels.stream().mapToInt(Integer::intValue).toArray();

        return meanColorOfColorArray(arrayOfPixels);
    }

    private static double meanValueOfColorArray(int[] arr){
        int blue = 0;
        int green = 0;
        int red = 0;

        for (int i = 0; i < arr.length; i++) {
            blue += arr[i] & 0xff;
            green += (arr[i] & 0xff00) >> 8;
            red += (arr[i] & 0xff0000) >> 16;
        }

        blue /= arr.length;
        green /= arr.length;
        red /= arr.length;

        return (blue+green+red) / 3d;
    }

    private static double meanColorOfColorArray(int[] arr){
        int blue = 0;
        int green = 0;
        int red = 0;

        for (int i = 0; i < arr.length; i++) {
            blue += arr[i] & 0xff;
            green += (arr[i] & 0xff00) >> 8;
            red += (arr[i] & 0xff0000) >> 16;
        }

        blue /= arr.length;
        green /= arr.length;
        red /= arr.length;

        int rgb = red;
        rgb = (rgb << 8) + green;
        rgb = (rgb << 8) + blue;

        System.out.println("R: " + red + " G: " + green + " B: " + blue);

        return rgb;
    }

    //drawing line using bresenham algorithm •♦•♦•
    public static void drawLine(int x,int y,int x2, int y2, Color color, BufferedImage img) {
        int w = x2 - x ;
        int h = y2 - y ;
        int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0 ;
        if (w<0) dx1 = -1 ; else if (w>0) dx1 = 1 ;
        if (h<0) dy1 = -1 ; else if (h>0) dy1 = 1 ;
        if (w<0) dx2 = -1 ; else if (w>0) dx2 = 1 ;
        int longest = Math.abs(w) ;
        int shortest = Math.abs(h) ;
        if (!(longest>shortest)) {
            longest = Math.abs(h) ;
            shortest = Math.abs(w) ;
            if (h<0) dy2 = -1 ; else if (h>0) dy2 = 1 ;
            dx2 = 0 ;
        }
        int numerator = longest >> 1 ;
        for (int i=0;i<=longest;i++) {
            //putpixel(x,y,color);
            if(x < img.getWidth() && y < img.getHeight()){ // To avoid out of bounds error
                img.setRGB(x,y,color.getRGB());
            }
            numerator += shortest ;
            if (!(numerator<longest)) {
                numerator -= longest ;
                x += dx1 ;
                y += dy1 ;
            } else {
                x += dx2 ;
                y += dy2 ;
            }
        }
    }

}
