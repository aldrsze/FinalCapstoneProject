import com.inventorysystem.gui.userFrame;
import com.inventorysystem.gui.Startup;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

// App
public class App {

    public static void main(String[] args) {
        
        // Show splash screen
        Startup splash = new Startup();
        splash.showSplash();

        SwingUtilities.invokeLater(() -> {
            try {
                splash.updateProgress(20, "Loading UI components...");
                Thread.sleep(250);

                // Native OS appearance
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                splash.updateProgress(50, "Initializing system...");
                Thread.sleep(250);

                // Smooth rendering
                System.setProperty("awt.useSystemAAFontSettings", "on");
                System.setProperty("swing.aatext", "true");

                splash.updateProgress(80, "Starting application...");
                Thread.sleep(250);

            } catch (Exception e) {
                com.inventorysystem.util.DebugLogger.error("Failed to set LookAndFeel or rendering properties", e);
            }

            splash.updateProgress(100, "Ready!");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                com.inventorysystem.util.DebugLogger.error("Splash screen delay interrupted", e);
            }

            // Close splash and show main window
            splash.closeSplash();
            userFrame frame = new userFrame();
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Start in full screen
            frame.setVisible(true);
        });
    }
}