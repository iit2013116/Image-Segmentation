/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package image.segmentation;

import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author kittu
 */
public class TeseSegmentstion {
    
    public static void main(String[] args) throws IOException{
        
        GraphSegment g = new GraphSegment();
        int i;
        int n = 0;
        
        Scanner scan = new Scanner(System.in);
        n = scan.nextInt();
        int j, p, q;
        
        g.graph = new int [n][n];
        
        for(i = 0; i < n; i++){
            for(j = 0; j < n; j++){
                g.graph[i][j] = scan.nextInt();
            }
        }
        
        p = scan.nextInt();
        q = scan.nextInt();
        
        g.height = n;
        g.width = n;
        g.segment(n,p,q);
       
        /*
        11
0 7 0 0 0 0 0 0 0 9 0
7 0 2 0 0 0 0 0 0 0 3
0 2 0 3 0 0 0 0 0 0 0
0 0 3 0 4 0 0 0 0 0 0
0 0 0 4 0 5 0 0 0 0 0
0 0 0 0 5 0 13 0 0 0 2
0 0 0 0 0 13 0 12 0 0 0
0 0 0 0 0 0 12 0 11 0 0
0 0 0 0 0 0 0 11 0 10 0
9 0 0 0 0 0 0 0 10 0 0
0 3 0 0 0 2 0 0 0 0 0
0
5
        */
    }
}
