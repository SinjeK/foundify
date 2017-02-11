package hu.berlin.dialog.io;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileDescriptor;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.ClosedByInterruptException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class is responsible for receiving the user's input.<br>
 * Use DialogInputDelegate to receive input events. <br>
 * Runs on its own thread.
 */
public class DialogInput implements Runnable {

    public interface DialogInputDelegate {
        void dialogInputReceivedMessage(DialogInput dialogInput, String message);
    }

    private DialogInputDelegate delegate;
    public ExecutorService delegateService;
    private ExecutorService inputService;
    private BufferedReader reader;
    private boolean running;


    public DialogInput() {
        new DialogInput(null);
    }

    public DialogInput(DialogInputDelegate delegate) {
        super();
        FileInputStream in = new FileInputStream(FileDescriptor.in);
        this.reader = new BufferedReader(new InputStreamReader(Channels.newInputStream(in.getChannel())));
        this.delegate = delegate;
        this.setRunning(false);
    }

    public boolean isRunning() {
        return this.running;
    }

    private void setRunning(boolean r) {
        this.running = r;
    }

    public DialogInputDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(DialogInputDelegate delegate) {
        this.delegate = delegate;
    }

    /**
     * Use this function to execute the dialog system so it can receive input
     * from the console and forward it to the delegate.
     */
    public void execute() {
        if (this.inputService == null) {
            this.inputService = Executors.newFixedThreadPool(2);
        }

        if (!this.isRunning()) {
            this.inputService.submit(this);
            this.setRunning(true);
        }
    }

    /**
     * Do NOT use this function to execute the dialog system. Instead use execute().
     * Calling this function might block the thread.<br>
     * However you can start an own thread and use this run method instead.
     */
    @Override
    public void run() {
        String input;
        try {
            while (this.isRunning()) {
                input = this.reader.readLine();

                if (input == null) {
                    System.out.println("Hard shut down");
                   System.exit(0);
                } else {
                    if (this.delegate != null) {
                        if (this.delegateService != null) {
                            final String finalInput = input;
                            this.delegateService.submit(() -> {
                                this.delegate.dialogInputReceivedMessage(this, finalInput);
                            });
                        } else {
                            this.delegate.dialogInputReceivedMessage(this, input);
                        }
                    }
                }
            }
        } catch (ClosedByInterruptException ioe) {
            // System.out.print(ioe.getMessage());
        } catch (IOException ioe) {
            // System.out.println(ioe.getMessage());
        } /*finally {
            this.inputService.shutdownNow();
        }*/
    }

    /**
     * Stops the input process and shutdowns the thread
     * on which the input reader runs.
     */
    public void stop() {
        this.inputService.submit(() -> {
            this.inputService.shutdownNow();
        });
    }
}
