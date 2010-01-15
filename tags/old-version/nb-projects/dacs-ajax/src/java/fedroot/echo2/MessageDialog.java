/* 
 * Created on April 26, 2006, 8:30 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package fedroot.echo2;

import fedroot.echo2.demo.Messages;
import java.util.EventListener;

import nextapp.echo2.app.Button;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.Label;
import nextapp.echo2.app.Row;
import nextapp.echo2.app.SplitPane;
import nextapp.echo2.app.WindowPane;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;

/**
 * A generic modal dialog that displays a message.
 */
public class MessageDialog extends WindowPane {

    public static final int TYPE_ERROR = 1;    
    public static final int TYPE_CONFIRM = 1;    
    
    public static final int CONTROLS_OK = 1;
    public static final int CONTROLS_YES_NO = 2;
    
    public static final String COMMAND_OK = "ok";
    public static final String COMMAND_CANCEL = "cancel";
    
    private ActionListener actionProcessor = new ActionListener() {

        /**
         * @see nextapp.echo2.app.event.ActionListener#actionPerformed(nextapp.echo2.app.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            getParent().remove(MessageDialog.this);
            EventListener[] listeners = getEventListenerList().getListeners(ActionListener.class);
            ActionEvent outgoingEvent = new ActionEvent(this, e.getActionCommand());
            for (int i = 0; i < listeners.length; ++i) {
                ((ActionListener) listeners[i]).actionPerformed(outgoingEvent);
            }
        }
    };
    
    /**
     * Creates a new <code>MessageDialog</code>.
     * 
     * @param title the dialog title
     * @param message the message to display
     * @param type the type of dialog, one of the following values:
     *        <ul>
     *         <li><code>TYPE_ERROR</code></li>
     *         <li><code>TYPE_CONFIRM</code></li>
     *        </ul>
     * @param controlConfiguration the control configuration, one of the 
     *        following values:
     *        <ul>
     *         <li><code>CONTROLS_OK</code></li>
     *         <li><code>CONTROLS_YES_NO</code></li>
     *        </ul>
     */
    public MessageDialog(String title, String message, int type, int controlConfiguration) {
        super(title, new Extent(320), new Extent(240));
        setStyleName("Default");
        setModal(true);

        SplitPane splitPane = new SplitPane(SplitPane.ORIENTATION_VERTICAL_BOTTOM_TOP, new Extent(32));
        add(splitPane);
        
        Row controlsRow = new Row();
        controlsRow.setStyleName("ControlPane");
        splitPane.add(controlsRow);

        Button button;
        switch (controlConfiguration) {
        case CONTROLS_OK:
            button = new Button(Messages.getString("Generic.Ok"), Styles.ICON_24_YES);
            button.setStyleName("ControlPane.Button");
            button.setActionCommand(COMMAND_OK);
            button.addActionListener(actionProcessor);
            controlsRow.add(button);
            break;
        case CONTROLS_YES_NO:
            button = new Button(Messages.getString("Generic.Yes"), Styles.ICON_24_YES);
            button.setStyleName("ControlPane.Button");
            button.setActionCommand(COMMAND_OK);
            button.addActionListener(actionProcessor);
            controlsRow.add(button);
            button = new Button(Messages.getString("Generic.No"), Styles.ICON_24_NO);
            button.setStyleName("ControlPane.Button");
            button.setActionCommand(COMMAND_CANCEL);
            button.addActionListener(actionProcessor);
            controlsRow.add(button);
            break;
        }
        
        Label contentLabel = new Label(message);
        contentLabel.setStyleName("MessageDialog.ContentLabel");
        splitPane.add(contentLabel);
        
        setModal(true);
    }
    
    /**
     * Adds an <code>ActionListener</code> to receive notification when the
     * user selects a choice.  The fired <code>command</code> of the fired 
     * <code>ActionEvent</code> will contain be one of the 
     * <code>COMMAND_XXX</code> constants.
     * 
     * @param l the <code>ActionListener</code> to add
     */
    public void addActionListener(ActionListener l) {
        getEventListenerList().addListener(ActionListener.class, l);
    }
    
    /**
     * Removes an <code>ActionListener</code> from receiving notification 
     * when the user selects a choice.
     * 
     * @param l the <code>ActionListener</code> to remove
     */
    public void removeActionListener(ActionListener l) {
        getEventListenerList().removeListener(ActionListener.class, l);
    }
}
