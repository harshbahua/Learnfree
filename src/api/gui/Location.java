/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;

/**
 *
 * @author Monil Gudhka
 */
public class Location {
    public static final int CENTER        =   0;
    public static final int TOP           =   1;
    public static final int LEFT          =   2;
    public static final int BOTTOM        =   3;
    public static final int RIGHT         =   4;
    public static final int TOP_LEFT      =   5;
    public static final int BOTTOM_LEFT   =   6;
    public static final int BOTTOM_RIGHT  =   7;
    public static final int TOP_RIGHT     =   8;
    private static final int[][] position = {{-1,-1},{-1,0},{0,-1},{-1,-2},{-2,-1},{0,0},{0,-2},{-2,-2},{-2,0}};
    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    
    
    public static void locateAtCenter(Window frame){
        locate(frame, CENTER);
    }
    public static void locate(Window frame, int location){
        if(!frame.isVisible()){
            Point frame_size = new Point();
            frame_size.x = calculate(position[location][0], SCREEN_SIZE.getWidth(), frame.getWidth());
            frame_size.y = calculate(position[location][1], SCREEN_SIZE.getHeight(), frame.getHeight());
            frame.setLocation(frame_size);
            frame.setVisible(true);
        }
        frame.requestFocus();
    }
    private static int calculate(int p, double screen, int frame){
        int n = 0;
        switch(p){
            case -1  :   n = (int) ((screen - frame) / 2);      break;
            case -2  :   n = (int)  (screen - frame);           break;
        }
        return n;
    }
    
    
    
    public static void minimize(Frame frame){
        state(frame, Frame.ICONIFIED);
    }
    public static void maximize(Frame frame){
        state(frame, Frame.MAXIMIZED_BOTH);
    }
    private static void state(Frame frame, int state){
        if(!frame.isVisible()){
            frame.setExtendedState(state);
            frame.setVisible(true);
        }
        frame.requestFocus();
    }
}