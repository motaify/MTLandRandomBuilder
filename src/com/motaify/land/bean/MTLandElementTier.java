package com.motaify.land.bean;

/**
 * 子元素层<br/>
 * 为保证基础元素层的高度恒定,子元素层将位于基础元素层以下,子元素层占比也是针对于基础元素层的一个相对占比
 */
public class MTLandElementTier {
  /** 子元素层类型 */
  private byte type;
  /** 子元素层占比 */
  private double nest;

  public MTLandElementTier() {
    super();
  }

  public MTLandElementTier(byte type, double nest) {
    this.type = type;
    this.nest = nest;
  }

  public byte getType() {
    return type;
  }

  public MTLandElementTier setType(byte type) {
    this.type = type;
    return this;
  }

  public double getNest() {
    return nest;
  }

  public MTLandElementTier setNest(double nest) {
    this.nest = nest;
    return this;
  }
}
