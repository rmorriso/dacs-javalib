/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fedroot.dacs.events;

import fedroot.dacs.client.DacsCheckRequest;
import fedroot.dacs.exceptions.DacsException;
import fedroot.util.Publisher;

/**
 *
 * @author rmorriso
 */
public class DacsEventNotifier  {

    public enum Status {signon, signout}

    public interface Listener {
        void status(Status status, String message);

        void notify(DacsException ex, DacsCheckRequest checkRequest);
    }

    private Publisher publisher = new Publisher();

    public void addEventListener(Listener l) {
        publisher.subscribe(l);
    }

    public void removeEventListener(Listener l) {
        publisher.unsubscribe(l);
    }

    public void status(final Status status, final String message) {
        publisher.publish(new Publisher.Distributor() {

            @Override
            public void deliverTo(Object subscriber) {
                ((Listener) subscriber).status(status, message);
            }
        });
    }

    public void notify(final DacsException ex, final DacsCheckRequest checkRequest) {
        publisher.publish(new Publisher.Distributor() {

            @Override
            public void deliverTo(Object subscriber) {
                ((Listener) subscriber).notify(ex, checkRequest);
            }
        });
    }

}
