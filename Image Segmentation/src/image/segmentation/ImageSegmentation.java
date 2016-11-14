/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package image.segmentation;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

/**
 *
 * @author chintu
 */
public class ImageSegmentation {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        
        File f;
        BufferedImage bim, subimage;
        JFileChooser filechooser = new JFileChooser();
        ScreenCapture screen;
        Rectangle sub_rec;
        GraphSegment graphsegment = new GraphSegment();
        
        filechooser.setCurrentDirectory(new File("C:\\Users\\chintu\\Documents\\NetBeansProjects\\images"));
        int x = filechooser.showOpenDialog(filechooser);
        
        if(x == JFileChooser.APPROVE_OPTION){
            f = filechooser.getSelectedFile();
            System.out.println(f);
            bim = ImageIO.read(f);
            
            screen = new ScreenCapture(bim);
            sub_rec = screen.captureRect;
            
            System.out.println(sub_rec.x + " " + sub_rec.y + " " + sub_rec.width + " " + sub_rec.height);
            subimage = bim.getSubimage(sub_rec.x, sub_rec.y, sub_rec.width, sub_rec.height);
            
            graphsegment.creategraph(subimage);
        }
        
    }
    
}
