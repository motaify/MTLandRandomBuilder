package com.motaify.land.bean;

/**
 * 随机工具类
 */
public class MTRandom {
  /** 随机种子 */
  private long seed;
  /** 随机步进 */
  private int step;

  /** 无参构造，一般无用，用于反射 */
  public MTRandom() {
    super();
    step = 0;
    seed = System.currentTimeMillis();
  }

  public MTRandom(long seed) {
    step = 0;
    this.seed = seed;
  }

  public int getStep() {
    return step;
  }

  public void setStep(int step) {
    this.step = step;
  }

  public long getSeed() {
    return seed;
  }

  public void setSeed(long seed) {
    this.seed = seed;
  }

  /**
   * 随机数生成策略(0 ~ 1)
   */
  public double random() {
    double _random = _getRandom();
    double result = Math.abs(_random);
    // logger.info("非负随机数:" + result);
    return result;
  }

  /**
   * 随机数生成策略(-1 ~ 1)
   */
  private double _getRandom() {
    int _step = this.step;
    long _seed = this.seed;

    this.step++;

    return random(_step, _seed);
  }

  public static double random(int step, long seed) {
    long n = step + seed * 57;
    n = (n << 13) ^ n;

    double random = 1.0 - ((n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824.0;
    return (1 + random) / 2;
  }
}
