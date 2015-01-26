/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.gui.filechooser;

import java.io.File;
import java.util.EventListener;

/**
 *
 * @author Monil Gudhka
 */
public interface FileChooserListener extends EventListener{
    public boolean accept(File file);
    public String getDescription();
    public void onApproval(File file);
    public void cancel();
    public void close();
}
