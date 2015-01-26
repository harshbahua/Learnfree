/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.gui;

import java.awt.Component;
import java.awt.Frame;
import javax.swing.JOptionPane;

/**
 *
 * @author Monil Gudhka
 */
public class MessageBox {
    
    public static final int OK_CANCEL_OPTION = JOptionPane.OK_CANCEL_OPTION;
    public static final int YES_NO_CANCEL_OPTION = JOptionPane.YES_NO_CANCEL_OPTION;
    public static final int YES_NO_OPTION = JOptionPane.YES_NO_OPTION;
    
    public static final int PLAIN_MESSAGE = JOptionPane.PLAIN_MESSAGE;
    public static final int ERROR_MESSAGE = JOptionPane.ERROR_MESSAGE;
    public static final int WARNING_MESSAGE = JOptionPane.WARNING_MESSAGE;
    public static final int QUESTION_MESSAGE = JOptionPane.QUESTION_MESSAGE;
    public static final int INFORMATION_MESSAGE = JOptionPane.INFORMATION_MESSAGE;
    
    
    //Allow user to give an answer in the form yes, no, ok, cancel
    public static int Box(Component comp, String message, String title, int optionType, int messageType){
        init();
        int result = JOptionPane.showConfirmDialog(comp, message, title, optionType, messageType);
        return result;
    }
    public static boolean ok_cancel_Box(Component comp, String message, String title, int messageType){
        int result = Box(comp, message, title, OK_CANCEL_OPTION, messageType);
        return (result == JOptionPane.OK_OPTION);
    }
    public static boolean yes_no_Box(Component comp, String message, String title, int messageType){
        int result = Box(comp, message, title, YES_NO_OPTION, messageType);
        return (result == JOptionPane.YES_OPTION);
    }
    public static int yes_no_cancel_Box(Component comp, String message, String title, int messageType){
        return Box(comp, message, title, YES_NO_CANCEL_OPTION, messageType);
    }
    
    
    
    
    
    //Allow user to give an answer to a question
    public static String prompt(Component comp, String message, String title){
        init();
        return JOptionPane.showInputDialog(comp, message, title, QUESTION_MESSAGE);
    }
    public static String password(Component comp, String message, String title){
        init();
        Login n = new Login((Frame)comp, true, true);
        n.setTitle(title);
        n.setPasswordMessage(message);
        n.show();
        n.dispose();
        return n.getPassword();
    }
    
    
    
    //Displays the Message
    public static void Message(Component comp, String message, String title, int messageType){
        init();
        JOptionPane.showMessageDialog(comp, message, title, messageType);
    }
    public static void error(Component comp, String message, String title){
        Message(comp, message, title, ERROR_MESSAGE);
    }
    public static void warn(Component comp, String message, String title){
        Message(comp, message, title, WARNING_MESSAGE);
    }
    public static void inform(Component comp, String message, String title){
        Message(comp, message, title, INFORMATION_MESSAGE);
    }
    
    
    
    
    private static void init(){
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
        } catch (ClassNotFoundException  ex) {
            java.util.logging.Logger.getLogger(MessageBox.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex){
            java.util.logging.Logger.getLogger(MessageBox.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch(IllegalAccessException ex){
            java.util.logging.Logger.getLogger(MessageBox.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch(javax.swing.UnsupportedLookAndFeelException ex){
            java.util.logging.Logger.getLogger(MessageBox.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
    }
}
