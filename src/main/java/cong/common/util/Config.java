package cong.common.util;
/**
 * Created by cong on 2014/3/19.
 */

import jodd.props.Props;
import jodd.props.PropsEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Iterator;

/**
 * @author cong onion_sheep@163.com|onionsheep@gmail.com
 */
public class Config {
  private static final Logger log = LoggerFactory.getLogger(Config.class);
  public static final String DEFAULT_PROP_FILE_PATH = "config.props";

  public static Props load(String filePath) {
    return load(new File(filePath));
  }

  public static Props load() {
    return load(Config.class.getClassLoader().getResourceAsStream(DEFAULT_PROP_FILE_PATH));
  }

  public static Props load(File configFile) {
    if (configFile.isFile() && configFile.canRead()) {
      try {
        return load(new FileInputStream(configFile));
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }
    return new Props();
  }

  public static Props load(InputStream is) {
    Props props = new Props();
    try {
      props.load(is);
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (log.isDebugEnabled()) {
      log.debug("props loaded.");
      final Iterator<PropsEntry> iterator = props.iterator();
      while (iterator.hasNext()) {
        log.debug("{}", iterator.next());
      }
    }
    return props;
  }


}
