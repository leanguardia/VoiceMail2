import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Deleguard on 4/1/16.
 */
public class ConnectionTestMailboxMenu {


    Mailbox currentMailbox;
    MailSystem mailSystem;
    Telephone phone;
    Connection connection;

    private static String MAILBOX_MENU_TEXT = "Enter 1 to listen to your messages\n"
            + "Enter 2 to change your passcode\n"
            + "Enter 3 to change your greeting";

    @Before
    public void setup() {
        currentMailbox = mock(Mailbox.class);
        mailSystem = mock(MailSystem.class);
        phone = mock(Telephone.class);
        connection = new Connection(mailSystem, phone);
        when(mailSystem.findMailbox("1")).thenReturn(currentMailbox);
        when(currentMailbox.checkPasscode("1")).thenReturn(true);
        inMailboxLoggedIn();
    }

    @Test
    public void inMailboxMenuEnter1ForListenMessages() {
        String MESSAGE_MENU_TEXT = "Enter 1 to listen to the current message\n"
                + "Enter 2 to save the current message\n"
                + "Enter 3 to delete the current message\n"
                + "Enter 4 to return to the main menu";
        connection.dial("1");
        assert (connection.isInMessageMenu());
        verify(phone).speak(MESSAGE_MENU_TEXT);
    }

    @Test
    public void inMailboxMenuEnter2ForChangingPasscode() {

        connection.dial("2");
        assert (connection.isInChangePassword());
        verify(phone).speak("Enter new passcode followed by the # key");
    }

    @Test
    public void inMailboxMenuEnter3ForChangingGreeting() {

        connection.dial("3");
        assert (connection.isInChangeGreeting());
        verify(phone).speak("Record your greeting, then press the # key");
    }

    @Test
    public void inMailSystemMenuChangePasscode() {

        connection.dial("2");
        connection.dial("9");
        connection.dial("#");
        verify(currentMailbox).setPasscode("9");
        assert (connection.isInMailBoxMenu());
        verify(phone,times(2)).speak(MAILBOX_MENU_TEXT);
    }

    @Test
    public void inMailSystemMenuShouldChangeGreeting(){

        connection.dial("3");
        connection.record("Greeting");
        connection.dial("#");
        verify(currentMailbox).setGreeting("Greeting");
        assert(connection.isInMailBoxMenu());
        verify(phone,times(2)).speak(MAILBOX_MENU_TEXT);
    }

    private void inMailboxLoggedIn() {
        connection.dial("1");
        connection.dial("#");
        connection.dial("1");
        connection.dial("#");
    }
}