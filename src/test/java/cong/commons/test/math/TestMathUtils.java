package cong.commons.test.math;

import java.util.ArrayList;
import java.util.Collections;

import cong.commons.math.MathUtils;

public class TestMathUtils {

  /**
   * @param args
   */
  public static void main(String[] args) {
    for(int i = 0; i < 1000; i++){
      tTopK();
    }
    
  }

  public static void tTopK(){
    ArrayList<Integer> l = new ArrayList<Integer>();
    for(int i = 0; i < 100; i++){
      l.add(java.util.concurrent.ThreadLocalRandom.current().nextInt(1000));
    }
    int tk = MathUtils.topK(l, 10);
    Collections.sort(l);
    int tkk = l.get(90);
    if(tk != tkk){
      System.out.println("BUG");
    }
  }
  public static void tTopKPerformance(){
    ArrayList<Integer> l = new ArrayList<Integer>();
    for(int i = 0; i < 100; i++){
      l.add(java.util.concurrent.ThreadLocalRandom.current().nextInt(1000));
    }
    int tk = MathUtils.topK(l, 10);
    Collections.sort(l);
    int tkk = l.get(90);
    if(tk != tkk){
      System.out.println("BUG");
    }
  }
}
