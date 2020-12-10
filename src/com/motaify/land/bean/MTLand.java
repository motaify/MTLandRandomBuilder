package com.motaify.land.bean;


import java.util.List;

/** 地形图 */
public class MTLand {
  /** X轴长度(含边界) */
  private int xw = 0;
  /** Y轴长度(含边界) */
  private int yh = 0;
  /** 地图元素 */
  private List<MTLandElement> elements;

  public int getXw() {
    return xw;
  }

  public MTLand setXw(int xw) {
    this.xw = xw;
    return this;
  }

  public int getYh() {
    return yh;
  }

  public MTLand setYh(int yh) {
    this.yh = yh;
    return this;
  }

  public List<MTLandElement> getElements() {
    return elements;
  }

  public MTLand setElements(List<MTLandElement> elements) {
    this.elements = elements;
    return this;
  }
}