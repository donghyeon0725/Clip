import java.awt.*;

public enum Ext {
    TXT("txt");

    Ext(String ext) {
        this.ext = ext;
    }

    private String ext;

    public String getExt() {
        return ext;
    }

    public static void main(String[] args) throws AWTException {
        Keyboard keyboard = new Keyboard();
        System.out.println("![default](C:\\Users\\ehdgu\\IdeaProjects\\jpa-basic1\\src\\main\\java\\First\\27519885b48c4c9baec327ab9ba5d99e.png)");
        keyboard.type("![default](C:\\Users\\ehdgu\\IdeaProjects\\jpa-basic1\\src\\main\\java\\First\\27519885b48c4c9baec327ab9ba5d99e.png)");
    }
}
