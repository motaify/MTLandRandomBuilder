package com.motaify.land.utils;

/** 常量 */
public interface MTMapConst {
  /** 土地 */
  byte RES_ID_MAP_GROUND = 0;
  /** 山体(1级山) */
  byte RES_ID_MAP_HILL = 1;
  /** 岩石(2级山) */
  byte RES_ID_MAP_STONE = 2;
  /** 砂砾(3级山) */
  byte RES_ID_MAP_GRAVEL = 3;
  /** 占位不绘制4级山 */
  byte RES_ID_MAP_TO_S_WATER = 4;
  /** 深水(1级水) */
  byte RES_ID_MAP_D_WATER = 5;
  /** 浅水(2级水) */
  byte RES_ID_MAP_S_WATER = 6;
  /** 泥地(3级水) */
  byte RES_ID_MAP_MUDDY = 7;
  /** 地貌特征1 */
  byte RES_ID_MAP_GROUND1 = 8;
  /** 地貌特征2 */
  byte RES_ID_MAP_GROUND2 = 9;
  /** 地貌特征3 */
  byte RES_ID_MAP_GROUND3 = 10;

  /** 像素单位 */
  int PIXEL_SIZE = 4;
}
