import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime alarmTime = null;
        String filePath = "short-rooster-crowing.wav";

        System.out.println("🚀 Welcome to Smart Alarm Clock!");

        while (alarmTime == null) {
            try {
                System.out.print("Enter an alarm time (HH:MM:SS): ");
                String inputTime = scanner.nextLine();
                alarmTime = LocalTime.parse(inputTime, formatter);

                System.out.print("Enter a label for this alarm (e.g., 'Wake Up', 'Meeting'): ");
                String label = scanner.nextLine().trim();

                System.out.println("✅ Alarm set for " + alarmTime + " [" + label + "]");

                AlarmClock alarmClock = new AlarmClock(alarmTime, filePath, scanner, label);
                Thread alarmThread = new Thread(alarmClock);
                alarmThread.start();

            } catch (DateTimeParseException e) {
                System.out.println("❌ Invalid format. Please use HH:MM:SS.");
            }
        }
    }
}
