/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 *
 * @author Monil Gudhka
 */
public class ListPanel extends JPanel{
    public static final int FLEXIBLE = 0;
    public static final int X_AXIS = BoxLayout.X_AXIS;
    public static final int Y_AXIS = BoxLayout.Y_AXIS;
    
    private GridLayout layout;
    private BoxLayout box_layout;
    private boolean horizontal_flexibility = false;
    private boolean vertical_flexibility = false;
    
    public ListPanel(){
        this(FLEXIBLE, 1);
    }
    public ListPanel(int axis){
        super();
        addListener();
        setBoxLayout(axis);
    }
    public ListPanel(int row, int column){
        this(row, column, 5, 5);
    }
    public ListPanel(int row, int column, int hgap, int vgap){
        super();
        addListener();
        setLayout(row, column, hgap, vgap);
    }
    
    public void setHorizontalResizable(boolean resizable){
        horizontal_flexibility = resizable && !layout.column_flexible;
    }
    public void setVerticalResizable(boolean resizable){
        vertical_flexibility = resizable;
    }
    public boolean isHorizontalResizable(){
        return horizontal_flexibility;
    }
    public boolean isVerticalResizable(){
        return vertical_flexibility;
    }
    
    
    public void setLayout(int row, int column){
        this.box_layout = null;
        layout = new GridLayout(row, column);
        this.setLayout(layout);
    }
    public void setLayout(int row, int column, int hgap, int vgap){
        this.setLayout(row, column);
        layout.setHgap(hgap);
        layout.setVgap(vgap);
    }
    private void setBoxLayout(int axis){
        box_layout = new BoxLayout(this, axis);
        this.setLayout(box_layout);
        this.layout = null;
    }
    
    @Override
    public void setAlignmentX(float alignmentX){
        layout.setAlignmentX(alignmentX);
    }
    @Override
    public float getAlignmentX(){
        return layout.getAlignmentX();
    }
    @Override
    public void setAlignmentY(float alignmentY){
        layout.setAlignmentY(alignmentY);
    }
    @Override
    public float getAlignmentY(){
        return layout.getAlignmentY();
    }
    
    
    private void addListener(){
        this.addContainerListener(new EventHandler());
    }
    
    private int index = 0;
    private Component getComponent(boolean down){
        int total = getComponentCount();
        if(down){
            index++;
            if(index >= total)
                index = 0;
        }
        else{
            index = (index==0)?total-1:index-1;
        }
        return this.getComponent(index);
    }
    
    class EventHandler implements ContainerListener{
        @Override
        public void componentAdded(ContainerEvent e) {
            e.getChild().addKeyListener(new ListComponentListener());
            e.getContainer().validate();
            try{
                e.getContainer().getParent().validate();
            } catch(NullPointerException ev){}
        }
        @Override
        public void componentRemoved(ContainerEvent e) {
            Component comp = e.getChild();
            KeyListener[] k = comp.getKeyListeners();
            for(int i=0; i<k.length; i++){
                if(k[i] instanceof ListComponentListener)
                    comp.removeKeyListener(k[i]);
            }
            e.getContainer().validate();
            try{
                e.getContainer().getParent().validate();
            } catch(NullPointerException ev){}
        }
    }
    class ListComponentListener implements KeyListener{
        @Override
        public void keyTyped(KeyEvent e) {}
        @Override
        public void keyReleased(KeyEvent e) {}
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()){
                case 40: getComponent(true).requestFocus(); break;
                case 38: getComponent(false).requestFocus(); break;
                default: break;
            }
        }
    }
    
    class GridLayout implements LayoutManager, java.io.Serializable {
        //private static final long serialVersionUID = -7411804673224730901L;
        private int hgap;
        private int vgap;
        private int rows;
        private int cols;
        private boolean column_flexible;
        private boolean row_flexible;
        private float X;
        private float Y;

        public GridLayout() {
            this(1, 0, 0, 0);
        }
        public GridLayout(int rows, int cols) {
            this(rows, cols, 0, 0);
        }
        public GridLayout(int rows, int cols, int hgap, int vgap) {
            this.setAlignmentX(CENTER_ALIGNMENT);
            this.setAlignmentY(CENTER_ALIGNMENT);
            this.setRows(rows);
            this.setColumns(cols);
            this.setHgap(hgap);
            this.setVgap(vgap);
        }

        public int getRows() {
            return rows;
        }
        public void setRows(int rows) {
            if(rows <= FLEXIBLE){
                row_flexible = true;
                this.rows = 1;
            } else{
                this.rows = rows;
            }
        }

        public int getColumns() {
            return cols;
        }
        public void setColumns(int cols) {
            if(cols <= FLEXIBLE){
                column_flexible = true;
                this.cols = 1;
            } else{
                this.cols = cols;
            }
        }

        public int getHgap() {
            return hgap;
        }
        public void setHgap(int hgap) {
            this.hgap = hgap;
        }

        public int getVgap() {
            return vgap;
        }
        public void setVgap(int vgap) {
            this.vgap = vgap;
        }
        
        public void setAlignmentX(float alignment){
            this.X = alignment;
        }
        public float getAlignmentX(){
            return this.X;
        }
        public void setAlignmentY(float alignment){
            this.Y = alignment;
        }
        public float getAlignmentY(){
            return this.Y;
        }

        @Override
        public void addLayoutComponent(String name, Component comp) {}
        @Override
        public void removeLayoutComponent(Component comp) {}

        @Override
        public Dimension preferredLayoutSize(Container parent) {
          synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int ncomponents = parent.getComponentCount();
            int nrows = rows;
            int ncols = cols;

            int w = 1;
            int h = 1;
            for (int i = 0 ; i < ncomponents ; i++) {
                Component comp = parent.getComponent(i);
                Dimension d = comp.getPreferredSize();
                if (w < d.width) {
                    w = d.width;
                }
                if (h < d.height) {
                    h = d.height;
                }
            }
            int widthWOInsets = parent.getParent().getWidth() - (insets.left + insets.right);
            if(!column_flexible){
                nrows = (ncomponents + ncols - 1) / ncols;
            } else {
                if(row_flexible){
                    ncols = (widthWOInsets + hgap) / (w + hgap);
                    ncols = (ncols<=0)? 1: ncols;
                    nrows = (ncomponents + ncols - 1) / ncols;
                } else {
                    ncols = (ncomponents + nrows - 1) / nrows;
                }
            }
            return new Dimension(insets.left + insets.right + ncols*w + (ncols-1)*hgap,insets.top + insets.bottom + nrows*h + (nrows-1)*vgap);
          }
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
          synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int ncomponents = parent.getComponentCount();
            int nrows = rows;
            int ncols = cols;

            int w = 1;
            int h = 1;
            for (int i = 0 ; i < ncomponents ; i++) {
                Component comp = parent.getComponent(i);
                Dimension d = comp.getMinimumSize();
                if (w < d.width) {
                    w = d.width;
                }
                if (h < d.height) {
                    h = d.height;
                }
            }
            int widthWOInsets = parent.getParent().getWidth() - (insets.left + insets.right);
            if(!column_flexible){
                nrows = (ncomponents + ncols - 1) / ncols;
            } else {
                if(row_flexible){
                    ncols = (widthWOInsets + hgap) / (w + hgap);
                    ncols = (ncols<=0)? 1: ncols;
                    nrows = (ncomponents + ncols - 1) / ncols;
                } else {
                    ncols = (ncomponents + nrows - 1) / nrows;
                }
            }
            return new Dimension(insets.left + insets.right + ncols*w + (ncols-1)*hgap,
                                 insets.top + insets.bottom + nrows*h + (nrows-1)*vgap);
          }
        }
        
        @Override
        public void layoutContainer(Container parent) {
          synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int ncomponents = parent.getComponentCount();
            int nrows = rows;
            int ncols = cols;
            boolean ltr = parent.getComponentOrientation().isLeftToRight();

            if (ncomponents == 0) {
                return;
            }
            
            
            int w = 1;
            int h = 1;
            for (int i = 0 ; i < ncomponents ; i++) {
                Component comp = parent.getComponent(i);
                Dimension d = comp.getMinimumSize();
                if (w < d.width) {
                    w = d.width;
                }
                if (h < d.height) {
                    h = d.height;
                }
            }
            
            int widthWOInsets = parent.getParent().getWidth() - (insets.left + insets.right);
            if(!column_flexible){
                nrows = (ncomponents + ncols - 1) / ncols;
            } else {
                if(row_flexible){
                    ncols = (widthWOInsets + hgap) / (w + hgap);
                    ncols = (ncols<=0)? 1: ncols;
                    nrows = (ncomponents + ncols - 1) / ncols;
                } else {
                    ncols = (ncomponents + nrows - 1) / nrows;
                }
            }
            
            int totalGapsWidth = (ncols - 1) * hgap;
            int widthOnComponent =  w;
            int extraWidthAvailable = 0;
            if(horizontal_flexibility){
                widthOnComponent = Math.max((widthWOInsets - totalGapsWidth) / ncols, w);
            } else{
                extraWidthAvailable = (int) ((widthWOInsets - (widthOnComponent * ncols + totalGapsWidth)) * this.X);
                extraWidthAvailable = (extraWidthAvailable<0)? 0: extraWidthAvailable;
            }
            
            int totalGapsHeight = (nrows - 1) * vgap;
            int heightWOInsets = parent.getHeight() - (insets.top + insets.bottom);
            int heightOnComponent = (vertical_flexibility)? (heightWOInsets - totalGapsHeight) / nrows  : h;
            int extraHeightAvailable = (int) ((vertical_flexibility)? 0 : (heightWOInsets - (heightOnComponent * nrows + totalGapsHeight)) * Y);
            
            if (ltr) {
                for (int c = 0, x = insets.left + extraWidthAvailable; c < ncols ; c++, x += widthOnComponent + hgap) {
                    for (int r = 0, y = insets.top + extraHeightAvailable; r < nrows ; r++, y += heightOnComponent + vgap) {
                        int i = r * ncols + c;
                        if (i < ncomponents) {
                            parent.getComponent(i).setBounds(x, y, widthOnComponent, heightOnComponent);
                        }
                    }
                }
            } else {
                for (int c = 0, x = (parent.getWidth() - insets.right - widthOnComponent) - extraWidthAvailable; c < ncols ; c++, x -= widthOnComponent + hgap) {
                    for (int r = 0, y = insets.top + extraHeightAvailable; r < nrows ; r++, y += heightOnComponent + vgap) {
                        int i = r * ncols + c;
                        if (i < ncomponents) {
                            parent.getComponent(i).setBounds(x, y, widthOnComponent, heightOnComponent);
                        }
                    }
                }
            }
          }
        }

        @Override
        public String toString() {
            return getClass().getName() + "[hgap=" + hgap + ",vgap=" + vgap +
                                           ",rows=" + rows + ",cols=" + cols + "]";
        }
    }
}