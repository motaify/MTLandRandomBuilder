package com.motaify.land.bean;

import java.util.List;

/** 地图元素 */
public class MTLandElement {
  /** 基础元素类型 */
  private byte type;
  /** 噪声缩放比例,值越小形状越大 */
  private double prob;// 占比
  /** 噪声复杂度,数量越大越复杂 */
  private double persistence;
  /** 元素占比,针对于整个地图当前地形所占的比例 */
  private double percentage;
  /** 子元素层占比 */
  private List<MTLandElementTier> tiers;
  /** 实际地形,在地图生成后填充该属性 */
  private byte[][] landform;

  public MTLandElement() {
    super();
  }

  public MTLandElement(byte type, double prob, double persistence, double percentage, List<MTLandElementTier> tiers) {
    this.type = type;
    this.prob = prob;
    this.persistence = persistence;
    this.percentage = percentage;
    this.tiers = tiers;
  }

  public byte getType() {
    return type;
  }

  public MTLandElement setType(byte type) {
    this.type = type;
    return this;
  }

  public double getProb() {
    return prob;
  }

  public MTLandElement setProb(double prob) {
    this.prob = prob;
    return this;
  }

  public double getPersistence() {
    return persistence;
  }

  public MTLandElement setPersistence(double persistence) {
    this.persistence = persistence;
    return this;
  }

  public double getPercentage() {
    return percentage;
  }

  public MTLandElement setPercentage(double percentage) {
    this.percentage = percentage;
    return this;
  }

  public List<MTLandElementTier> getTiers() {
    return tiers;
  }

  public MTLandElement setTiers(List<MTLandElementTier> tiers) {
    this.tiers = tiers;
    return this;
  }

  public byte[][] getLandform() {
    return landform;
  }

  public MTLandElement setLandform(byte[][] landform) {
    this.landform = landform;
    return this;
  }
}