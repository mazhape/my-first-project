import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Random;
import java.util.Scanner;

public class AlarmClock implements Runnable {
    private final LocalTime alarmTime;
    private final String filePath;
    private final Scanner scanner;
    private final String label;

    // Constructor
    AlarmClock(LocalTime alarmTime, String filePath, Scanner scanner, String label) {
        this.alarmTime = alarmTime;
        this.filePath = filePath;
        this.scanner = scanner;
        this.label = label;
    }

    @Override
    public void run() {
        while (LocalTime.now().isBefore(alarmTime)) {
            try {
                Thread.sleep(1000);
                LocalTime now = LocalTime.now();
                System.out.printf("\r%02d:%02d:%02d", now.getHour(), now.getMinute(), now.getSecond());
            } catch (InterruptedException e) {
                System.out.println("Thread was interrupted");
            }
        }
        triggerAlarm();
    }

    // Method to trigger the alarm with vibration and volume increase
    private void triggerAlarm() {
        System.out.println("\n⏰ *ALARM NOISES* [" + label + "] ⏰");

        // Start "vibration" animation
        new Thread(this::simulateVibration).start();

        // Gradually increase volume while playing sound
        playSoundWithGradualVolume(filePath);

        System.out.print("Press ENTER to stop the alarm...");
        scanner.nextLine();
        System.out.println("Alarm [" + label + "] stopped.");
    }

    // Simulate vibration effect by printing shaking text
    private void simulateVibration() {
        String text = "⏰ WAKE UP! ⏰";
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int spaces = random.nextInt(10);
            System.out.print("\r" + " ".repeat(spaces) + text);
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {}
        }
        System.out.println();
    }

    // Play alarm sound without volume control (Fix for UnsupportedControlTypeException)
    private void playSoundWithGradualVolume(String filePath) {
        File audioFile = new File(filePath);
        try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile)) {
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            clip.loop(Clip.LOOP_CONTINUOUSLY); // Keep looping until stopped

            System.out.println("\nPress ENTER to stop the alarm...");
            scanner.nextLine();
            clip.stop();
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Audio file format is not supported");
        } catch (LineUnavailableException e) {
            System.out.println("Audio is unavailable");
        } catch (IOException e) {
            System.out.println("Error playing audio file");
        }
    }
}
