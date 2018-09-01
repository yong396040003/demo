
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Description:
 * Date: 21:08 2018/8/29
 *
 * @author yong
 * @see
 */
public class AsciiPic {

    //当前工程路径
    private final static String app_dir=System.getProperty("user.dir");
    //图片转Ascii
    private String imgString="";
    //BinaryImage灰度图片
    BufferedImage BinaryImage;

    public String getImgString(){
        return imgString;
    }

    /**
     * 根据图片来源创建Ascii
     * @param file
     * @return
     */
    public void creatAsciiPic(File file){
        //用来存储Ascii
        StringBuffer sb = new StringBuffer();
        //字符串由复杂到简单
        final String base = "@#&$%*o!;.";

        try {
            //读取图片
            BufferedImage img = ImageIO.read(file);
            //压缩图片（100*100）
            img=compressImage(img);
            //设置灰度图片(压缩后的)
            BinaryImage=new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_BYTE_GRAY);
            //遍历图片,字符的高一般是宽的两倍,所以高度+=2
            for (int y = 0;y < img.getHeight();y += 2){
                for (int x = 0;x < img.getWidth();x++){
                    //根据x,y获取像素点
                    final int pixel = img.getRGB(x,y);
                    //red
                    final int r=(pixel & 0xff0000) >> 16;
                    //green
                    final int g=(pixel & 0xff00) >> 8;
                    //blue
                    final int b=(pixel & 0xff);
                    //加权算法（大佬们的研究结果）目的：设置图片灰度
                    final float gray = 0.299f * r + 0.578f * g + 0.114f * b;
                    //采用Math.round四舍五入的方式,base.length() + 1为了将白色凸显出来
                    final int index = Math.round(gray * (base.length() + 1) / 255);
                    //当index大于或等于base.length()时,用空白表示
                    sb.append(index >= base.length() ? " " : String.valueOf(base.charAt(index)));
                    //生成灰色图片
                    BinaryImage.setRGB(x, y, r | g | b);
                }
                //单行遍历完毕，换行
                sb.append("\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //保存图片
        try {
            ImageIO.write(BinaryImage, "jpg", new File(app_dir+"\\image\\1.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        imgString=sb.toString();
    }

    /*压缩图片到 200px*/
    private  BufferedImage compressImage(BufferedImage srcImg){
        int h =  srcImg.getHeight();
        int w = srcImg.getWidth();
        if(Math.max(h,w)<=200)
            return srcImg;
        int new_H;
        int new_W;
        if(w>h){
            new_W = 200;
            new_H = 200*h/w ;
        }else{
            new_H = 200;
            new_W = 200*w/h;
        }
        BufferedImage smallImg = new BufferedImage(new_W,new_H,srcImg.getType());
        Graphics g = smallImg.getGraphics();
        g.drawImage(srcImg,0,0,new_W,new_H,null);
        g.dispose();
        return smallImg;
    }

    /*将字符串保存为.txt文件*/
    public void saveAsTxt(String fileName){
        try{
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
            for(int i = 0;i<imgString.length();i++){
                out.print(imgString.charAt(i));
            }
            out.close();

        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    /*将字符串从txt文件中读取出来*/
    public void readTxt(String fileName){
        File file=new File(fileName);
        if (!file.isFile()) {
            System.out.println("this file is not a file..." + 2);
            return;
        }

        try {
            Reader in=new FileReader(file);
            BufferedReader reader=new BufferedReader(in);
            String s=null;
            while((s = reader.readLine()) != null){
                System.out.println(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        //Desktop桌面
        File file = new File(app_dir+"\\image\\bg.jpg");
        System.out.println(app_dir);
        if (!file.isFile()) {
            System.out.println("this file is not a file..." + 1);
            return;
        }
        AsciiPic asciiPic=new AsciiPic();
        asciiPic.creatAsciiPic(file);
        asciiPic.saveAsTxt(app_dir+"\\bg.txt");
        asciiPic.readTxt(app_dir+"\\bg.txt");
    }

}
