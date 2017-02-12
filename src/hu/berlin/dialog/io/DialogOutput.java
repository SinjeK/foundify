package hu.berlin.dialog.io;
import java.util.concurrent.*;

public class DialogOutput {

    private ExecutorService queue;

    public DialogOutput() {
        super();
        this.queue = Executors.newScheduledThreadPool(1);
    }

    public void put(String output) {
        String lines[] = output.split("\\n");

        /*this.queue.submit(() -> {
            for (int i=0; i<lines.length; i++) {
                final int j = i;
                this.queue.submit(() -> {
                    int delay = j==0 ? ThreadLocalRandom.current().nextInt(50, 500 ) : ThreadLocalRandom.current().nextInt(900, 1600);;
                    try {
                        Thread.sleep(delay);
                        System.out.println(lines[j]);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }

        });*/

        System.out.println(output);
    }
}
