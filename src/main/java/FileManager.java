import org.apache.commons.lang3.ArrayUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    /**
     * 경로의 파일을 읽어 List<Byte>로 반환 합니다.
     * */
    private List<Byte> copyContentOfInputFile(String filePath) {
        FileInputStream inputStream = null;

        try {
            List<Byte> result = new ArrayList();
            inputStream = new FileInputStream(filePath);// 파일 입력 스트림 생성
            byte[] readBuffer = new byte[1024]; // 버퍼
            int size = 0;

            while ((size = inputStream.read(readBuffer, 0, readBuffer.length)) != -1)
                for (int i=0; i<size; i++)
                    result.add(readBuffer[i]);

            return result;
        } catch (Exception e) {
            System.out.println("파일 입출력 에러!!" + e);
            return null;
        } finally {
            try {
                // 파일 닫기. 여기에도 try/catch가 필요하다.
                inputStream.close();
            } catch (Exception e) {
                System.out.println("닫기 실패" + e);
            }
        }
    }

    /**
     * byte의 데이터를 파일 내부에 넣어 줍니다.
     * */
    private void writeContentIntoFile(String path, List<Byte> content) {
        FileOutputStream outputStream = null; // 파일 쓰기 스트림

        try
        {
            outputStream = new FileOutputStream(path);// 파일 출력 스트림 생성

            // Apache ArrayUtil 사용해서 변환
            Byte[] bytes = content.toArray(new Byte[content.size()]);
            byte[] readBuffer = ArrayUtils.toPrimitive(bytes);

            int size = content.size();

            //버퍼 크기만큼 읽을 때마다 출력 스트림에 써준다.
            // stream.read() 는 버퍼 내부에 들어있는 값의 크기를 리턴해주는데 그 크기 만큼만 써야 합니다.
            outputStream.write(readBuffer, 0, size);
        } catch (Exception e)
        {
            System.out.println("파일 입출력 에러!!" + e);
        } finally {
            try {
                outputStream.close();
            }
            catch (Exception e) {
                System.out.println("닫기 실패" + e);
            }
        }
    }

    /**
     * 주어진 타입에 맞는 파일을 씁니다.
     * */
    public void makeFile(String path, String filename, Ext ext, List<Byte> contents) {
        mkDir(path);
        String fullname = filename.concat("." + ext.getExt());
        mkFile(path, fullname);
        writeContentIntoFile(path.concat(File.separator + fullname), contents);
    }


    /**
     * 경로에 폴더를 생성합니다.
     * */
    private void mkDir(String path) {
        // 디렉토리가 없다면 생성
        File dir = new File(path);
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }
    }

    public boolean isDir(String path) {
        File dir = new File(path);
        return dir.isDirectory();
    }

    /**
     * 경로에 파일을 생성 합니다.
     * */
    private void mkFile(String path, String fullname) {
        mkDir(path);
        File serverFile = new File(path + File.separator + fullname);
    }

    public void uploadImage(BufferedImage image, String fullpath) {
        File file = new File(fullpath);
        try {
            // 상위 경로 생성
            mkDir(file.getParent());

            if (!file.exists()) {
                // png 파일 포멧으로 생성
                ImageIO.write(image, "png", file);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteFile(String fullpath) {
        File file = new File(fullpath);
        if (file.exists())
            file.delete();
    }

    public static void main(String[] args) {

        // 애플리 케이션이 실행되는 곳의 디렉토리
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        /*FileManager fileManager = new FileManager();

        String filePath = "C:/Users/ehdgu/OneDrive/바탕 화면/자료/orm 강의 자료/input.txt"; // 대상 파일
        String outputFilePath = "C:/Users/ehdgu/OneDrive/바탕 화면/자료/orm 강의 자료/test"; // 대상 파일
        String filename = "out";

        List<Byte> list = fileManager.copyContentOfInputFile(filePath);

        fileManager.makeFile(outputFilePath, filename, Ext.TXT, list);*/
        try {


            Path path = Paths.get("C:/Users/ehdgu/IdeaProjects");


            WatchService watchService = FileSystems.getDefault().newWatchService();

            path.register(
                    watchService, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);

            while(true) {
                // 위 이벤트가 일어나면 WatchKey 를 반 반환 합니다.
                WatchKey key = watchService.take();
                // WatchEvent 이벤트 리스트를 키로부터 얻어냅니다.
                List<WatchEvent<?>> eventList = key.pollEvents();

                System.out.println("키를 얻어냄");
                for (WatchEvent<?> event : eventList) {
                    //이벤트의 종류
                    WatchEvent.Kind<?> kind = event.kind();


                    //감지된 Path
//                    Path path = (Path)event.context();

                    //이벤트 종류별로 처리
                    if (kind.equals(StandardWatchEventKinds.ENTRY_CREATE)) {
                        // 생성되었을 경우, 실행할 코드
                    } else if(kind.equals(StandardWatchEventKinds.ENTRY_DELETE)) {
                        // 삭제되었을 경우, 실행할 코드
                    } else if (kind.equals(StandardWatchEventKinds.ENTRY_MODIFY)) {
                        System.out.println("수정 발생");
                        // 수정되었을경우, 실행할 코드
                    } else if (kind.equals(StandardWatchEventKinds.OVERFLOW)) {
                        //운영체제에서 이벤트가 소실되었거나 버려진 경우에 발생
                    }
                    boolean valid = key.reset();
                    if (!valid) {
                        break;
                    }
                }
                watchService.close();
            }




        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
