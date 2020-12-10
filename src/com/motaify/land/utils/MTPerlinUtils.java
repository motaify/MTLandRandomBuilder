package com.motaify.land.utils;

import com.motaify.land.bean.MTRandom;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/** 柏林噪声工具类 */
public class MTPerlinUtils {
  private final static byte OCTAVES = 8;

  /**
   * 生成地图随机使用的噪声组
   *
   * @param xw          地图X轴宽度
   * @param yh          地图Y轴高度
   * @param prob        地图比例
   * @param persistence 地图复杂度
   * @param plSeed      噪声种子
   * @return 噪声地图
   */
  public static double[][] createPerlinMap(int xw, int yh, double prob, double persistence, int[] plSeed) {
    double[][] xyArr = new double[yh][xw];
    // 生成基础地貌
    for (int x = 0; x < xw; x++) {
      for (int y = 0; y < yh; y++) {
        final double pl = OctavePerlin(x * prob, y * prob, 1 * prob, OCTAVES, persistence, plSeed);
        xyArr[y][x] = pl;
      }
    }
    return xyArr;
  }

  /**
   * 对单个噪声值叠加倍频和复杂度的影响
   *
   * @param x           X轴
   * @param y           Y轴
   * @param z           Z轴
   * @param octaves     倍频
   * @param persistence 复杂度
   * @param plSeed      噪声种子
   * @return 变化后的噪声值
   */
  public static double OctavePerlin(double x, double y, double z, int octaves, double persistence, int[] plSeed) {

    double total = 0;
    double frequency = 1;
    double amplitude = 1;
    double maxValue = 0;  // Used for normalizing result to 0.0 - 1.0
    for (int i = 0; i < octaves; i++) {
      total += perlin(x * frequency, y * frequency, z * frequency, plSeed) * amplitude;

      maxValue += amplitude;

      amplitude *= persistence;
      frequency *= 2;
    }

    return total / maxValue;
  }

  /**
   * 计算单个基础噪声值
   *
   * @param x      X轴
   * @param y      Y轴
   * @param z      Z轴
   * @param plSeed 噪声种子
   * @return 单个噪声值
   */
  public static double perlin(double x, double y, double z, int[] plSeed) {
    int X = (int) Math.floor(x) & 255,                  // FIND UNIT CUBE THAT
        Y = (int) Math.floor(y) & 255,                  // CONTAINS POINT.
        Z = (int) Math.floor(z) & 255;
    x -= Math.floor(x);                                // FIND RELATIVE X,Y,Z
    y -= Math.floor(y);                                // OF POINT IN CUBE.
    z -= Math.floor(z);
    double u = fade(x),                                // COMPUTE FADE CURVES
        v = fade(y),                                // FOR EACH OF X,Y,Z.
        w = fade(z);
    int A = plSeed[X] + Y, AA = plSeed[A] + Z, AB = plSeed[A + 1] + Z,      // HASH COORDINATES OF
        B = plSeed[X + 1] + Y, BA = plSeed[B] + Z, BB = plSeed[B + 1] + Z;      // THE 8 CUBE CORNERS,

    return lerp(w, lerp(v, lerp(u, grad(plSeed[AA], x, y, z),  // AND ADD
        grad(plSeed[BA], x - 1, y, z)), // BLENDED
        lerp(u, grad(plSeed[AB], x, y - 1, z),  // RESULTS
            grad(plSeed[BB], x - 1, y - 1, z))),// FROM  8
        lerp(v, lerp(u, grad(plSeed[AA + 1], x, y, z - 1),  // CORNERS
            grad(plSeed[BA + 1], x - 1, y, z - 1)), // OF CUBE
            lerp(u, grad(plSeed[AB + 1], x, y - 1, z - 1),
                grad(plSeed[BB + 1], x - 1, y - 1, z - 1))));
  }

  static double fade(double t) {
    return t * t * t * (t * (t * 6 - 15) + 10);
  }

  static double lerp(double t, double a, double b) {
    return a + t * (b - a);
  }

  public static double grad(int hash, double x, double y, double z) {
    switch (hash & 0xF) {
      case 0x0:
        return x + y;
      case 0x1:
        return -x + y;
      case 0x2:
        return x - y;
      case 0x3:
        return -x - y;
      case 0x4:
        return x + z;
      case 0x5:
        return -x + z;
      case 0x6:
        return x - z;
      case 0x7:
        return -x - z;
      case 0x8:
        return y + z;
      case 0x9:
      case 0xD:
        return -y + z;
      case 0xA:
        return y - z;
      case 0xB:
      case 0xF:
        return -y - z;
      case 0xC:
        return y + x;
      case 0xE:
        return y - x;
      default:
        return 0; // never happens
    }
  }

  /**
   * 随机生成地形种子(主要用于测试)
   *
   * @param random 随机对象
   * @return 噪声种子
   */
  public static int[] randomPermutation(MTRandom random) {
    int maxLength = 512;
    int[] p = new int[maxLength];
    int[] permutation = new int[256];
    List<Integer> pool = new ArrayList<>();
    for (int i = 0; i < 256; i++) {
      pool.add(i);
    }

    int count = 0;
    while (pool.size() > 0) {
      int index = (int) (random.random() * pool.size());
      int temp = pool.get(index);
      pool.remove(index);
      permutation[count] = temp;
      count += 1;
    }
    for (int i = 0; i < 256; i++) p[256 + i] = p[i] = permutation[i];
    return p;
  }

  @Test
  public void test_randomPermutation() {
    int strLength = 5;
    ArrayList<Integer> pool = new ArrayList<>();
    for (int i = 0; i < 256; i++) {
      pool.add(i);
    }

    ArrayList<Integer> result = new ArrayList<>();
    while (pool.size() > 0) {
      int index = (int) (Math.random() * pool.size());
      int temp = pool.get(index);
      pool.remove(index);
      result.add(temp);
    }

    StringBuilder resultStr = new StringBuilder();
    for (int i = 0; i < result.size(); i++) {
      int temp = result.get(i);
      if (i % 8 == 0) {
        resultStr.append("\n");
      }
      StringBuilder str = new StringBuilder(temp + "");
      int tempLength = str.length();
      for (int si = 0; si < strLength - tempLength; si++) {
        str.insert(0, " ");
      }
      resultStr.append(str).append(", ");
    }

    System.out.println(resultStr);
  }
}
