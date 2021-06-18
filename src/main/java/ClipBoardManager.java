import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class ClipBoardManager implements ClipboardOwner {
    private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    /**
     * String을 클립보드에 넣어 줍니다.
     * */
    public void copyToClipBoard(String string) {
        StringSelection data = new StringSelection(string);
        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(data, data);
    }

    /**
     * 클립보드에 복사된 String을 input Stream에 넣어 줍니다.
     * */
    public byte[] getByteFromClipBoard() {
        try {
            Transferable data = clipboard.getContents(clipboard);
            String result = (String) data.getTransferData(DataFlavor.stringFlavor);
            return result.getBytes("UTF-8");
        } catch (IOException e) {
            System.out.println("입출력 에러");
        } catch (UnsupportedFlavorException e) {
            System.out.println("지원하지 않는 타입");
        }
        return null;
    }

    /**
     * 클립보드에 담긴 이미지를 업로드 합니다.
     * */
    public BufferedImage getClipImage() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        try {
            Transferable trans = clipboard.getContents(this);
            if (trans.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                return (BufferedImage) trans.getTransferData(DataFlavor.imageFlavor);
            }
        } catch (Exception e) {
            System.out.println("복사된 자료가 없어요!");
        }

        return null;
    }

    /**
     * 풀 스크린 화면의 이미지를 클립보드에 담습니다.
     * */
    public void copyFullScreenToClipBoard() {
        try {
            Robot robot = new Robot();
            Dimension screenSize  = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle screen = new Rectangle( screenSize );
            BufferedImage i = robot.createScreenCapture( screen );
            TransferableImage trans = new TransferableImage( i );
            Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
            c.setContents( trans, this );
        }
        catch ( AWTException x ) {
            x.printStackTrace();
            System.exit( 1 );
        }
    }

    public static void main( String[] arg ) {
        ClipBoardManager ci = new ClipBoardManager();
//        ClipBoardManager clipBoardManager = new ClipBoardManager();
//        clipBoardManager.copyToClipBoard("asd");
        ci.copyFullScreenToClipBoard();
        BufferedImage image = ci.getClipImage();
    }

    public void lostOwnership( Clipboard clip, Transferable trans ) {
        System.out.println( "Lost Clipboard Ownership" );
    }

    private class TransferableImage implements Transferable {

        Image i;

        public TransferableImage( Image i ) {
            this.i = i;
        }

        public Object getTransferData( DataFlavor flavor )
                throws UnsupportedFlavorException, IOException {
            if ( flavor.equals( DataFlavor.imageFlavor ) && i != null ) {
                return i;
            }
            else {
                throw new UnsupportedFlavorException( flavor );
            }
        }

        public DataFlavor[] getTransferDataFlavors() {
            DataFlavor[] flavors = new DataFlavor[ 1 ];
            flavors[ 0 ] = DataFlavor.imageFlavor;
            return flavors;
        }

        public boolean isDataFlavorSupported( DataFlavor flavor ) {
            DataFlavor[] flavors = getTransferDataFlavors();
            for ( int i = 0; i < flavors.length; i++ ) {
                if ( flavor.equals( flavors[ i ] ) ) {
                    return true;
                }
            }

            return false;
        }
    }

}
