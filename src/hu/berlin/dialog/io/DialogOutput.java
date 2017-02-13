package hu.berlin.dialog.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class DialogOutput {

    private ScheduledExecutorService queue;

    public DialogOutput() {
        super();
        this.queue = Executors.newScheduledThreadPool(1);
    }

    public void putOnSameLine(String output) {
        System.out.print(output);
    }

    public void put(String output, boolean delayflag) {

        if (output == null) {
            System.out.println("Error: try to print null");
            System.exit(-1);
            return;
        }

        if (delayflag) {
            if (output.equals("")) {
                System.out.println();
                return;
            }

            String lines[] = output.split("\\n");

            this.queue.submit(() -> {
                for (int i=0; i<lines.length; i++) {
                    final int j = i;
                    this.queue.submit(() -> {
                        String comp = lines[j];
                        int delay = 0;

                        if (j>0) {
                            String[] whitespaces = lines[j-1].split("\\s+");
                            delay = (whitespaces.length+1) * 200;
                        }

                        try {
                            if (delay > 0) Thread.sleep(delay);
                            System.out.println(comp);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                }
            });
        } else {
            System.out.println(output);
        }
    }

    public void put(String output) {
        put(output, false);
    }
}
