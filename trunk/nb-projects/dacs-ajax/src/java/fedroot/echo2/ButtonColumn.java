/* 
 * Created on April 26, 2006, 8:30 PM
 *
 * Copyright (c) 2005 Metalogic Software Corporation.
 * All rights reserved. See http://fedroot.com/licenses/metalogic.txt for redistribution information.
 */

package fedroot.echo2;

import nextapp.echo2.app.Button;
import nextapp.echo2.app.Column;
import nextapp.echo2.app.event.ActionListener;

public class ButtonColumn extends Column {
    
    public void addButton(String label, ActionListener actionListener) {
        Button button = new Button(label);
        button.addActionListener(actionListener);
        button.setStyleName("Default");
        add(button);
    }
}
