import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;
import java.util.UUID;

public class SystemManager {

    private static ClipBoardManager clipBoardManager = new ClipBoardManager();

    private static FileManager fileManager = new FileManager();

    private static String selectedPath = null;

    private static String file = null;

    private static JFileChooser fFileChooser = new JFileChooser();



//    private static Keyboard keyboard = new Keyboard();

    private static boolean run = true;

    public static String getUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static void main(String[] args) throws AWTException {
        // Might throw a UnsatisfiedLinkError if the native library fails to load or a RuntimeException if hooking fails
        GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook(true); // Use false here to switch to hook instead of raw input

        System.out.println("Global keyboard hook successfully started, press [escape] key to shutdown. Connected keyboards:");

        for (Map.Entry<Long, String> keyboard : GlobalKeyboardHook.listKeyboards().entrySet()) {
            System.out.format("%d: %s\n", keyboard.getKey(), keyboard.getValue());
        }

        keyboardHook.addKeyListener(new GlobalKeyAdapter() {

            Keyboard keyboard = new Keyboard();

            @Override
            public void keyPressed(GlobalKeyEvent event) {

                // 이미지 추가하기
                // alt + shift + e
                if (event.getVirtualKeyCode() == 69 && event.isShiftPressed() && event.isMenuPressed()) {
                    keyboard.releaseKey(KeyEvent.VK_CONTROL);
                    keyboard.releaseKey(KeyEvent.VK_SHIFT);
                    keyboard.releaseKey(KeyEvent.VK_ALT);
                    // 이미지가 있다면 이미지 업로드
                    BufferedImage image = clipBoardManager.getClipImage();

                    file = getUID() + ".png";

                    if (selectedPath != null && image != null)
                        fileManager.uploadImage(image, selectedPath + File.separator + file);

                    // 선택 경로가 있다면 경로와 함께 키 입력
                    if (selectedPath != null)
                        keyboard.type("![default](./img/" + file + ")");
                }

                // 방금 업로드 한 이미지 삭제하기
                // alt + shift + d
                if (event.getVirtualKeyCode() == 69 && event.isShiftPressed() && event.isMenuPressed()) {
                    fileManager.deleteFile(selectedPath + File.separator);
                }


                // 경로 선택하기
                // ctrl + shift + z
                if (event.getVirtualKeyCode() == 90 && event.isShiftPressed() && event.isControlPressed()) {
                    keyboard.releaseKey(KeyEvent.VK_CONTROL);
                    keyboard.releaseKey(KeyEvent.VK_SHIFT);
                    keyboard.releaseKey(KeyEvent.VK_ALT);

                    fFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    fFileChooser.showSaveDialog(null);

                    if (!fFileChooser.getSelectedFile().isDirectory())
                        return;

                    selectedPath = fFileChooser.getSelectedFile().getAbsolutePath();
                }

                // VK_ESCAPE => 27번으로 esc를 누르면 프로세스를 종료합니다.ㅌㅋㅌ
                if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_ESCAPE) {
                    run = false;
                }
            }

            @Override
            public void keyReleased(GlobalKeyEvent event) {}
        });

        try {
            while(run) {
                Thread.sleep(128);
            }
        } catch(InterruptedException e) {
            //Do nothing
        } finally {
            keyboardHook.shutdownHook();
        }
    }
}
