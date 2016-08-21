package uk.co.real_logic;

import static java.lang.System.out;

/*
Original exercise did during "Lock Free Workshop" by Martin Thompson: http://www.real-logic.co.uk/training.html
 */
public final class PingPong {
    private static final int REPETITIONS = 100_000_000;

    private static volatile long pingValue = -1;
    private static volatile long pongValue = -1;

    public static void main(final String[] args) throws Exception {
        final Thread pongThread = new Thread(new PongRunner());
        final Thread pingThread = new Thread(new PingRunner());
        pongThread.setName("pong-thread");
        pongThread.setName("ping-thread");
        pongThread.start();
        pingThread.start();

        final long start = System.nanoTime();

        pingThread.join();
        pongThread.join();

        final long duration = System.nanoTime() - start;

        out.printf("duration %,d (ns)%n", duration);
        out.printf("%,d ns/op%n", duration / (REPETITIONS * 2L));
        out.printf("%,d ops/s%n", (REPETITIONS * 2L * 1_000_000_000L) / duration);
        out.println("pingValue = " + pingValue + ", pongValue = " + pongValue);

        main(args);
    }

    public static class PingRunner implements Runnable {
        public void run() {
            for(int i = 0; i < REPETITIONS; ++i){
                pingValue = i;
                while(i != pongValue){
                }
            }
        }
    }

    public static class PongRunner implements Runnable {
        public void run() {
            for(int i = 0; i < REPETITIONS; ++i) {
                while (i != pingValue) {
                }
                pongValue = i;
            }
        }
    }
}
