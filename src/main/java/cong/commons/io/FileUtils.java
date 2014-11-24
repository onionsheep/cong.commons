package cong.commons.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class FileUtils {

  /**
   * 复制流的时候的默认buffer大小
   */
  public static int bufferSize        = 512 * 1024;

  /**
   * 读取文件成字符串时默认的StringBuilder大小
   */
  public static int stringBuilderSize = 1024;

  /**
   * 获取一个BufferedReader
   * 
   * @param f
   *          要读取的文件
   * @param charsetName
   *          用来打开文件的编码
   * @return 读取文件的BufferedReader
   * @throws FileNotFoundException
   *           如果文件是目录, 或者不存在但无法创建, 或者因其他原因打不开
   */
//  protected static BufferedReader getBufferedReader(File f, Charset charset)
//      throws FileNotFoundException, UnsupportedEncodingException {
//    FileInputStream fis = new FileInputStream(f);
//    InputStreamReader isr = new InputStreamReader(fis, charset);
//    BufferedReader br = new BufferedReader(isr);
//    return br;
//  }

  /**
   * 获取一个BufferedWriter
   * 
   * @param f
   *          要写入的文件
   * @param charset
   *          写入文件的编码
   * @param append
   *          是否追加
   * @return 写文件的BufferedWriter
   * @throws FileNotFoundException
   *           如果文件是目录, 或者不存在但无法创建, 或者因其他原因打不开
   */
//  protected static BufferedWriter getBufferedWriter(File f, Charset charset, boolean append)
//      throws FileNotFoundException {
//    FileOutputStream fos = new FileOutputStream(f, append);
//    OutputStreamWriter osw = new OutputStreamWriter(fos, charset);
//    BufferedWriter bw = new BufferedWriter(osw);
//    return bw;
//  }

  /**
   * 把整个文件读取为一个字符串
   * 
   * @param f
   *          要读取的文件
   * @param charset
   *          打开文件用的编码
   * @return 整个文件的一个字符串，异常返回null
   */
  public static String readFileToString(File f, Charset charset) {
    try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f),
                                                                      charset));) {
      if (br != null) {
        StringBuilder sb = new StringBuilder(stringBuilderSize);
        char[] buffer = new char[bufferSize];
        while (br.read(buffer) != -1) {
          sb.append(buffer);
        }
        // br.close();
        return sb.toString();
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * @see #readFileToString(File, Charset)
   */
  public static String readFileToString(String filePath, Charset charset) {
    return readFileToString(new File(filePath), charset);
  }

  /**
   * @see #readFileToString(File, Charset)
   */
  public static String readFileToString(String filePath, String charsetName) {
    return readFileToString(new File(filePath), Charset.forName(charsetName));
  }

  /**
   * @see #readFileToString(File, Charset)
   */
  public static String readFileToString(File f, String charsetName) {
    return readFileToString(f, Charset.forName(charsetName));
  }

  /**
   * 把文件读取成为一个ArrayList
   * 
   * @param f
   *          文件
   * @param charset
   *          打开文件用的编码
   * @return ArrayList<String>每个元素是一行，异常返回null
   */
  public static ArrayList<String> readLines(File f, Charset charset) {
    try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f),
                                                                      charset));) {
      // BufferedReader br = getBufferedReader(f, charset);
      if (br != null) {
        ArrayList<String> lines = new ArrayList<>();
        for (String line = br.readLine(); line != null; line = br.readLine()) {
          lines.add(line);
        }
        // br.close();
        return lines;
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * @see #readLines(File, Charset)
   */
  public static ArrayList<String> readLines(String filePath, String charsetName) {
    return readLines(new File(filePath), Charset.forName(charsetName));
  }

  /**
   * @see #readLines(File, Charset)
   */
  public static ArrayList<String> readLines(File f, String charsetName) {
    return readLines(f, Charset.forName(charsetName));
  }

  /**
   * @see #readLines(File, Charset)
   */
  public static ArrayList<String> readLines(String filePath) {
    return readLines(new File(filePath), Charset.defaultCharset());
  }

  /**
   * 把字符串写入文件
   * 
   * @param f
   *          文件
   * @param str
   *          字符串
   * @param charset
   *          使用的字符编码
   * @param append
   *          true追加，false覆盖
   */
  public static void writeStringToFile(File f, String str, Charset charset, boolean append) {
    try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f,
                                                                                            append),
                                                                       charset));) {
      // BufferedWriter bw = getBufferedWriter(f, charset, append);
      bw.write(str);
      // bw.close();
    }
    catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * @see #writeStringToFile(File, String, Charset, boolean)
   */
  public static void writeStringToFile(File f, String str, boolean append) {
    writeStringToFile(f, str, Charset.defaultCharset(), append);
  }

  /**
   * @see #writeStringToFile(File, String, Charset, boolean)
   */
  public static void writeStringToFile(File f, String str, String charsetName, boolean append) {
    writeStringToFile(f, str, Charset.forName(charsetName), append);
  }

  /**
   * @see #writeStringToFile(File, String, Charset, boolean)
   */
  public static void writeStringToFile(String filePath, String str, boolean append) {
    writeStringToFile(new File(filePath), str, Charset.defaultCharset(), append);
  }

  /**
   * @see #writeStringToFile(File, String, Charset, boolean)
   */
  public static void writeStringToFile(String filePath,
                                       String str,
                                       String charsetName,
                                       boolean append) {
    writeStringToFile(new File(filePath), str, Charset.forName(charsetName), append);
  }

  /**
   * 测试性质，不可用
   * @param filePath
   * @return
   */
  public static String readFileToStringNIO(String filePath) {
    try {
      FileChannel fch = FileChannel.open(Paths.get(filePath), StandardOpenOption.READ);
      ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
      Charset charset = Charset.defaultCharset(); 
      CharsetDecoder decoder = charset.newDecoder();
      StringBuilder sb = new StringBuilder(stringBuilderSize);
      while (fch.read(buffer) != -1) {
        //buffer.flip();
        
        CharBuffer charbuffer = decoder.decode(buffer);
        
        System.out.println(charbuffer);
        
        sb.append(charbuffer);
        //byte[] b = buffer.array();
        //sb.append();
        //sb.append(buffer.asCharBuffer());
        //buffer.reset();
      }
      return sb.toString();
    }
    catch (IOException e) {
      //do nothing
      e.printStackTrace();
    }
    return null;
  }
  
  public static boolean copyFileByChannel(File in, File out) {
    try (FileInputStream fis = new FileInputStream(in);
        FileOutputStream fos = new FileOutputStream(out);) {
      FileChannel infc = fis.getChannel();
      FileChannel outfc = fos.getChannel();
      outfc.transferFrom(infc, 0, infc.size());
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
}
