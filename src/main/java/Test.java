public class Test {
    public static void main(String[] args) {
        ClipBoardManager clipBoardManager = new ClipBoardManager();
        FileManager fileManager = new FileManager();

        clipBoardManager.copyFullScreenToClipBoard();
        fileManager.uploadImage(clipBoardManager.getClipImage(), "C:\\Users\\ehdgu\\IdeaProjects\\jpa-basic1\\src\\main\\java\\First\\img\\826a0edbeff44698b943e723b48cbcfa.png");
    }
}
