package org.example;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class PrimaryController {

    @FXML
    private TextField inputTime;

    @FXML
    private Label timerLabel;

    @FXML
    private TextArea infoBoksTextArea;

    private long interval;
    private static Timer timer;

    private boolean running = false;

    private int delay = 1000;
    private int period = 1000;

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getPeriod() {
        return period;
    }

    // This is the stopwatch
    public void countdown() {

        setRunning(true);

        String secs = this.inputTime.getText();

        // checking wether the countdown needs to be in seconds, minutes or hours
        if (secs.contains("s") || secs.contains("S")) {
            setDelay(1000);
            setPeriod(1000);
        } else if (secs.contains("m") || secs.contains("M")) {
            setDelay(1000);
            setPeriod(60000);
        } else if (secs.contains("h") || secs.contains("H")) {
            setDelay(1000);
            setPeriod(3600000);
        }

        timer = new Timer();

        /*
        Making sure if you input a space or any of the required characters that it is not read, since
        the program only needs to read characters for the desired stopwatchtype, see above ( L52 - L61 )
        */
        interval = Integer.parseInt(secs.replace("s","").replace("S","")
        .replace("m","").replace("M","").replace("h","")
        .replace("H","").replace(" ", ""));



        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //Updating the label to count down in real time
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (getPeriod() == 1000) {
                            timerLabel.setText("Time left: " + setInterval() + " second(s)");
                        } else if (getPeriod() == 60000) {
                            timerLabel.setText("Time left: " + setInterval() + " minute(s)");
                        } else if (getPeriod() == 3600000) {
                            timerLabel.setText("Time left: " + setInterval() + " hour(s)");
                        }
                    }
                });
                }
            }, delay, period);
        infoBoksTextArea.setText("Countdown Startet");
        }

    // The command to check if interval hits 0 (timer runs out) and then shutting down pc
    private long setInterval() {
        if (interval == 0) {
            try {
            shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return interval--;
    }


    public static void shutdown() throws RuntimeException, IOException {
        Runtime runtime = Runtime.getRuntime();
        Process proc = runtime.exec("shutdown -s -t 0");
        System.exit(0);

    }
    
    /*
    stopping the timer and making sure the the boolean "Running" is stopped aswell so it is possible
    to create a new timer
    */
    private void stopTimer() {
        timer.cancel();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                timerLabel.setText("Time left: " + inputTime.getText());
                infoBoksTextArea.setText("The timer has been stopped");
            }
        });
        setRunning(false);
    }

    public void startProgram () {
        /*
        If the program aint running, it will start a countdown, else you will get an error
        that states that the program is already running (Error is the message to the user)
        This makes sure that 2 instances of a countdown cant run and therefor mess with the program
        */
        if (running == false) {
            countdown();
        } else {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    infoBoksTextArea.setText("Program is already running.\n" +
                            "Press cancel to stop the timer and to start a new timer");
                }
            });

        }
    }

    public void stopProgram () {
    stopTimer();
    }
}