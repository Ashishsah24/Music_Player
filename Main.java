import java.io.*;
import java.util.*;
import javax.sound.sampled.*;
import javazoom.jl.decoder.*;
import javazoom.jl.player.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) throws UnsupportedAudioFileException, IOException {
        Scanner sc = new Scanner(System.in);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select an audio file");
        int userSelection = fileChooser.showOpenDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            try {
                FileInputStream fis = new FileInputStream(selectedFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
               Player player = new Player(bis);

           boolean isPlaying = false;
final Player[] playerRef = { player }; // Wrap in an array to make it effectively final

                while (true) {
                    System.out.println("P = Play, S = Stop, Q = Quit, R = Restart");
                    System.out.println("Enter your choice: ");
                    String response = sc.next();
                    response = response.toUpperCase();

                    switch (response) {
                        case "P":
                            if (!isPlaying) {
                                isPlaying = true;
                                Thread audioThread = new Thread(() -> {
                                    try {
                                        playerRef[0].play();
                                    } catch (JavaLayerException e) {
                                        System.out.println("Error playing the audio file: " + e.getMessage());
                                    }
                                });
                                audioThread.start();
                            } else {
                                System.out.println("Music is already playing!");
                            }
                            break;
                        case "S":
                            if (isPlaying) {
                                playerRef[0].close();
                                isPlaying = false;
                            } else {
                                System.out.println("No music is currently playing!");
                            }
                            break;
                        case "Q":
                            if (isPlaying) {
                                playerRef[0].close();
                            }
                            System.out.println("Byeee!!");
                            return;
                        case "R":
                            if (isPlaying) {
                                playerRef[0].close();
                                fis = new FileInputStream(selectedFile);
                                bis = new BufferedInputStream(fis);
                                playerRef[0] = new Player(bis); // Update the playerRef with a new instance
                                isPlaying = true;
                                Thread audioThread = new Thread(() -> {
                                    try {
                                        playerRef[0].play();
                                    } catch (JavaLayerException e) {
                                        System.out.println("Error playing the audio file: " + e.getMessage());
                                    }
                                });
                                audioThread.start();
                            } else {
                                System.out.println("No music is currently playing!");
                            }
                            break;
                        default:
                            System.out.println("Not a Valid Response!");
                    }

                    // Consume the newline character left in the input buffer
                    sc.nextLine();
                }
            } catch (JavaLayerException e) {
                System.out.println("Error playing the audio file: " + e.getMessage());
            }
        } else {
            System.out.println("No file selected. Exiting...");
        }
    }
}
/* compile and run the code with classpath of javazoom JLayer Library
    For Windows:
    compile :- javac -cp .;lib/jl1.0.1.jar Main.java
    run :- java .;lib/jl1.0.1.jar Main
    For Mac/Linux: 
    use : in place of ;
*/
