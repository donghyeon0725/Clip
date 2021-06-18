import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Map.Entry;

/**
 * GUI 없이 키 이벤트를 지속적으로 추적하기 위한 방법입니다.
 * https://stackoverflow.com/questions/12177416/how-can-i-write-a-key-listener-to-track-all-keystrokes-in-java
 * GUI 없이 키 이벤트를 지속적으로 추적하기 위한 system hook 라이브러리 입니다.
 * https://github.com/kristian/system-hook
 * 로봇을 사용해서 키를 입력해주는 프로그램입니다.
 * https://stackoverflow.com/questions/11442471/press-a-key-with-java
 * */
public class GlobalKeyboardExample {

    private static ClipBoardManager clipBoardManager = new ClipBoardManager();

    private static boolean run = true;

    public static void main(String[] args) throws AWTException {
        // Might throw a UnsatisfiedLinkError if the native library fails to load or a RuntimeException if hooking fails
        GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook(true); // Use false here to switch to hook instead of raw input

        System.out.println("Global keyboard hook successfully started, press [escape] key to shutdown. Connected keyboards:");

        for (Entry<Long, String> keyboard : GlobalKeyboardHook.listKeyboards().entrySet()) {
            System.out.format("%d: %s\n", keyboard.getKey(), keyboard.getValue());
        }

        keyboardHook.addKeyListener(new GlobalKeyAdapter() {



            Keyboard keyboard = new Keyboard();

            @Override
            public void keyPressed(GlobalKeyEvent event) {
                System.out.println(event);

                // VK_ESCAPE => 27번으로 esc를 누르면 프로세스를 종료합니다.ㅌㅋㅌ
                if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_ESCAPE) {
                    run = false;
                }
            }

            @Override
            public void keyReleased(GlobalKeyEvent event) {
                System.out.println(event);
            }
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
