import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class CustomWatcher {
    //프로젝트 경로 => 이 위치에서만 작동을 함
    private static final String projPath = System.getProperty("user.dir");
    private WatchKey watchKey;

    public void init() throws IOException {
        //watchService 생성
        WatchService watchService = FileSystems.getDefault().newWatchService();
        System.out.println(projPath);
        //경로 생성
        Path path = Paths.get(projPath);
        //해당 디렉토리 경로에 와치서비스와 이벤트 등록
        path.register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.OVERFLOW);

        Thread thread = new Thread(()-> {
            while(true) {
                try {
                    watchKey = watchService.take();//이벤트가 오길 대기(Blocking)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<WatchEvent<?>> events = watchKey.pollEvents();//이벤트들을 가져옴
                for(WatchEvent<?> event : events) {
                    //이벤트 종류
                    WatchEvent.Kind<?> kind = event.kind();
                    //경로
                    Path paths = (Path)event.context();
                    System.out.println(paths.toAbsolutePath());//C:\...\...\test.txt
                    if(kind.equals(StandardWatchEventKinds.ENTRY_CREATE)) {
                        System.out.println("created something in directory");
                    }else if(kind.equals(StandardWatchEventKinds.ENTRY_DELETE)) {
                        System.out.println("delete something in directory");
                    }else if(kind.equals(StandardWatchEventKinds.ENTRY_MODIFY)) {
                        System.out.println("modified something in directory");
                    }else if(kind.equals(StandardWatchEventKinds.OVERFLOW)) {
                        System.out.println("overflow");
                    }else {
                        System.out.println("hello world");
                    }
                }
                if(!watchKey.reset()) {
                    try {
                        watchService.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    public static void main(String[] args) throws IOException {
        CustomWatcher customWatcher = new CustomWatcher();
        customWatcher.init();
    }



}
