/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package image.segmentation;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import javax.imageio.ImageIO;

/**
 *
 * @author chintu
 */
public class GraphSegment {
    
    static int graph[][];
    static int pixel[][];
    static int path[] = new int[10000];
    static int height;
    static int width;
    
    public static void creategraph(BufferedImage bim) throws IOException {
        
        height = bim.getHeight();
        width = bim.getWidth();
        pixel = new int[height][width];
        int i;
        int j;
        Color c;
        int pix;
        int total;
        int pos;
        int pos1;
        int pos2;
        int pos3;
        int pos4;
        int image_average;
        int background_average;
        
        
        for(i = 0; i < height; i++){
            for(j = 0; j < width; j++){
                pix = bim.getRGB(j, i);
                c = new Color(pix);
                
                pixel[i][j] = (c.getRed() + c.getBlue() + c.getGreen())/3;
                System.out.print(pixel[i][j] + " ");
            }
            System.out.println("");
        }
        
        
        
        // calculating image average
        i = height/2;
        j = width/2;
        
        image_average = (pixel[i][j] + pixel[i+2][j] + pixel[i][j+2] + pixel[i][j-2] + pixel[i-2][j])/5;
        
        // calculating object average
        background_average = (pixel[1][1] + pixel[1][width - 2] + pixel[height - 2][1] + pixel[height - 2][width - 2])/4;
        
        System.out.println("Image Average :- " + image_average + " Background Average :- " + background_average);
        
        
        total = height*width;
        graph = new int[total+2][total+2];
        
        for(i = 0; i < height; i++){            
            for(j = 0; j < width; j++){
                
                pos = i*height + j;
                pos1 = (i-1)*height + j;
                pos2 = i*height + j + 1;
                pos3 = (i+1)*height + j;
                pos4 = i*height + j - 1;
                
                if(pos >= 0 && pos < total + 2){
                    
                    if(i > 0 && pos1 >= 0 && pos1 < total){
                        graph[pos][pos1] = 255 - Math.abs(pixel[i][j] - pixel[i-1][j]) + 1;
                    }
                    
                    if(j < width - 1 && pos2 >= 0 && pos2 < total){
                        graph[pos][pos2] = 255 - Math.abs(pixel[i][j] - pixel[i][j + 1]) + 1;
                    }
                    
                    if(i < height - 1 && pos3 >= 0 && pos3 < total){
                        graph[pos][pos3] = 255 - Math.abs(pixel[i][j] - pixel[i + 1][j]) + 1;
                    }
                    
                    if(j > 0 && pos4 >= 0 && pos4 < total){
                        graph[pos][pos4] = 255 - Math.abs(pixel[i][j] - pixel[i][j - 1]) + 1;
                    }
                    
                    // teminal weights
                    graph[total][pos] = Math.abs(1 - ((pixel[i][j] - background_average)/(background_average + 1))); // object
                    
                    graph[total+1][pos] = Math.abs(1 - ((pixel[i][j] - image_average)/(image_average + 1)));
                }
            }
        }
        
        for(i = 0; i < total+2; i++){
            System.out.print(i + " --- > ");
            for(j = 0; j < total+2; j++){
                System.out.print(graph[i][j] + " ");
            }
            System.out.println("");
        }
        
        segment(total + 2,total, total+1);
    }
    
    public static void segment(int total, int p, int q) throws IOException{
        
        int i, j;
        path[0] = p;
        boolean visited[] = new boolean[total];
        
        for(i = 0; i < total; i++)
            visited[i] = false;
        
        dfs(p,q,1, visited, total);
        
        for(i = 0; i < total; i++){
            System.out.print(i + " --- > ");
            for(j = 0; j < total; j++){
                System.out.print(graph[i][j] + " ");
            }
            System.out.println("");
        }
        
        createimage(p,q,total);
    }
    
    public static void dfs(int p, int q, int pos, boolean visited[], int total){
        //System.out.println("Node :- " + p);
        if(visited[p] == false){
            int i;
            int bottleneck = Integer.MAX_VALUE;
            
            if(p == q){
                System.out.println("Path Found !!!");
                
                for(i = 0; i < pos - 1; i++){
                    System.out.println(path[i] + "  " + path[i+1]);
                    if(graph[path[i]][path[i+1]] < bottleneck)
                        bottleneck = graph[path[i]][path[i+1]];
                }
                
                System.out.println(bottleneck);
                for(i = 0; i < pos - 1; i++){
                    graph[path[i]][path[i+1]] -= bottleneck;
                    graph[path[i+1]][path[i]] -= bottleneck;
                }
                
                return ;                
            }
                        visited[p] = true;

            for(i = 0; i < total; i++){
                if(graph[p][i] > 0 && visited[i] == false){
                    path[pos] = i;
                    dfs(i,q,pos+1,visited,total);
                }
            }
        }
    }
    
    public static void createimage(int p, int q, int total) throws IOException{
        
        Set<Integer> Source;
        Set<Integer> Sink;
        
        int i;
        int j;
        int temp;
        
        Source = reachable(p,total);
        Sink = reachable(q, total);
        
        System.out.println("\nSource --- > ");
        for(int s:Source)
            System.out.print(s + " ");
        
        System.out.println("\nSink --- > ");
        for(int s:Sink)
            System.out.print(s + " ");
        
        BufferedImage newimage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        
        for(i = 0; i < height; i++){
            for(j = 0; j < width; j++){
                temp = i*height + j;
                
                if(Source.contains(temp))
                    newimage.setRGB(j, i, 255);
                else if(Sink.contains(temp))
                    newimage.setRGB(j, i, 0);
            }
        }
        
        File image_out = new File("/home/kittu/Pictures/anvesh/newimage.jpg");
        ImageIO.write(newimage, "jpg", image_out);
        
    }
    public static Set<Integer> reachable(int p, int total) {
        
        Set<Integer> elements = new HashSet<>();
        Deque<Integer> que = new ArrayDeque<>();
        que.add(p);
        int temp;
        int i;
        boolean visited[] = new boolean[total];
        
        visited[p] = true;

        while(!que.isEmpty()){
            temp = que.element();
            que.remove();
            for(i = 0; i < total; i++){
                if(graph[temp][i] > 0 && visited[i] == false){
                    visited[i] = true;
                    elements.add(i);
                    que.add(i);
                }
            }
        }
        
        return elements;
    }
}
