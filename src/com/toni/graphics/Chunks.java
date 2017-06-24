package com.toni.graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Chunks {
	private static int rows = 1; 
	private static int cols = 1;
	private static int numThreads = 1;
	private static String fileName="C:\\Users\\4525s\\Desktop\\818A2694.jpg";
	private static String type="blur";
	
	
	public static void main(String[] args) throws IOException {
		 long startTimeProgram = System.nanoTime();
		parseArguments(args);

        File file = new File(fileName); 
        FileInputStream fis = new FileInputStream(file);
        BufferedImage image = ImageIO.read(fis); //reading the image file


        int chunks = rows * cols;
        // determines the chunk width and height
        int chunkWidth = image.getWidth() / cols; 
        int chunkHeight = image.getHeight() / rows;
        int count = 0;
        BufferedImage imgs[] = new BufferedImage[chunks]; //Image array to hold image chunks
        
        /*
         * Рразделя изображението на равни части и създава подизображения,
         * които да бъдат подадени в последствие на отделните нишки
         * за паралелна обработка
         */
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                //Initialize the image array with image chunks
                imgs[count] = new BufferedImage(chunkWidth, chunkHeight, image.getType());

                // draws the image chunk
                Graphics2D gr = imgs[count++].createGraphics();
                gr.drawImage(image, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight * x, chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight, null);
                gr.dispose();
                
                
                
            }
        }
        
        System.out.println("Splitting done");
        
        MyThread [] threads = new MyThread[numThreads];

        long startTime = System.nanoTime();
        
        /*
         * Подава всяко изображение на отделна нишка
         * и я стартира 
         */
        for (int i = 0; i < imgs.length; i++) {
        	threads[i] = new MyThread(imgs,i,type);
        	threads[i].start();
        }
        
        for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
        long endTime = System.nanoTime();
        
        int type = imgs[0].getType();
        
        BufferedImage finalImg = new BufferedImage(chunkWidth*cols, chunkHeight*rows,type);
        
        /*
         * Конкатенира финалното изображение от подизображенията, които вече са обработени 
         */
        int num = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                finalImg.createGraphics().drawImage(imgs[num], chunkWidth * j, chunkHeight * i, null);
                num++;
            }
        }
        
        long endTimePrograme = System.nanoTime();
        
        System.out.println("Image concatenated.....");
        ImageIO.write(finalImg, "jpg", new File("final.jpg"));


        long duration = (endTime - startTime);  
        System.out.print("Algorithm:"+ duration);
        
        System.out.println("Program:" +(endTimePrograme - startTimeProgram));
    }
	
	private static void parseArguments(String[] args) {
		for(int i = 0; i < args.length; i++) {
		 if(args[i].equals("-c")) {
				cols = Integer.parseInt(args[i + 1]);
				i++;
			}
			else if(args[i].equals("-t")) {
				numThreads = Integer.parseInt(args[i + 1]);
				i++;
			}
			else if(args[i].equals("-r")) {
				rows = Integer.parseInt(args[i + 1]);
				i++;
			}else if(args[i].equals("-f")) {
				fileName = args[i + 1];
				i++;
			}else if(args[i].equals("-g")) {
				type = args[i + 1];
				i++;
			}
			else {
				System.out.println("Illegal argument !!!");
			}
		}
	}	
	
}
