/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.gui.filechooser;

import api.gui.Location;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Monil Gudhka
 */
public class FileChooser extends JFileChooser{
    
    private FileChooserListener listener;
    private JFrame frame;
    public FileChooser(){
        super();
        init();
    }

    public FileChooser(String string) {
        this();
        setTitle(string);
    }
    private void init(){
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FileChooser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex){
            java.util.logging.Logger.getLogger(FileChooser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex){
            java.util.logging.Logger.getLogger(FileChooser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex){
            java.util.logging.Logger.getLogger(FileChooser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        frame = new JFrame("File Chooser");
        frame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                if(listener!=null)
                    listener.close();
                else
                    System.exit(0);
            }
        });
        frame.add(this);
        frame.setSize(582, 397);
        Location.locateAtCenter(frame);
        
        this.setAcceptAllFileFilterUsed(false);
    }
    
    public void setTitle(String title){
        frame.setTitle(title);
    }
    @Override
    public void approveSelection(){
        super.approveSelection();
        if(listener!=null)
            listener.onApproval(this.getSelectedFile());
    }
    @Override
    public void cancelSelection(){
        super.cancelSelection();
        if(listener!=null)
            listener.cancel();
        else
            System.exit(0);
    }
    
    
    public void setFileChooserListener(FileChooserListener listener){
        if(listener != null){
            this.addChoosableFileFilter(new FileFilterImpl(listener));
            this.listener = listener;
        }
    }
    public FileChooserListener getFileChooserListener(){
        return listener;
    }
    
    
    @Override
    public void setVisible(boolean aFlag){
        this.frame.setVisible(aFlag);
        super.setVisible(aFlag);
    }
    public void dispose() {
        this.frame.dispose();
    }
    
    
    
    
    class FileFilterImpl extends FileFilter{
        private FileChooserListener listener;
        FileFilterImpl(FileChooserListener listener){
            this.listener = listener;
        }
        @Override
        public boolean accept(File f) {
            return this.listener.accept(f);
        }
        @Override
        public String getDescription() {
            return listener.getDescription();
        }
    }
    
    
    
    
    
    
    public static void main(String args[]){
        FileChooser file = new FileChooser();
        file.setVisible(true);
    }
}
