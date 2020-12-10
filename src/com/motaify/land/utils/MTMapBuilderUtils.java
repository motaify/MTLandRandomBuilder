package com.motaify.land.utils;

import com.motaify.land.bean.MTLand;
import com.motaify.land.bean.MTLandElement;
import com.motaify.land.bean.MTLandElementTier;
import com.motaify.land.bean.MTRandom;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MTMapBuilderUtils {

  /** 创建一个地形图,为其中的每个地形元素生成实际的地形图 */
  public static MTLand createLand(MTRandom random, int xw, int yh, List<MTLandElement> elements) {
    MTLand starMap = new MTLand()
        .setXw(xw)
        .setYh(yh)
        .setElements(new ArrayList<>());

    // 生成基础地形
    // 根据每种元素生成他们对应的地形图
    for (MTLandElement element : elements) {
      byte[][] perlinMap = createElementMap(xw, yh, MTPerlinUtils.randomPermutation(random), element);
      element.setLandform(perlinMap);
      starMap.getElements().add(element);
    }

    return starMap;
  }

  /** 根据地图元素描述填充元素的地形属性 */
  public static byte[][] createElementMap(int xw, int yh, int[] seed, MTLandElement element) {
    byte type = element.getType();
    double prob = element.getProb();
    double persistence = element.getPersistence();
    double percentage = element.getPercentage();
    double[][] perlinMap = MTPerlinUtils.createPerlinMap(xw, yh, prob, persistence, seed);
    byte[][] result = new byte[perlinMap.length][perlinMap[0].length];
    // 寻找分界点
    double top = findDividingPoint(perlinMap, percentage);

    for (int y = 0; y < yh; y++) {
      for (int x = 0; x < xw; x++) {
        double temp = perlinMap[y][x];
        if (temp > top) {// 真正的山
          result[y][x] = type;
        } else {
          if (element.getTiers() != null) {
            for (MTLandElementTier nestItem : element.getTiers()) {
              if (temp > top * nestItem.getNest()) {// 山边缘的石头
                result[y][x] = nestItem.getType();
                break;
              }
            }
          }
        }
      }
    }
    return result;
  }

  /** 根据地形对整个地图的占比,寻找地形分界点 */
  public static double findDividingPoint(double[][] perlinMap, double percentage) {

    // 将double放在一起进行排序，然后按比例找到临界点的值
    List<Double> tempAllBaseMapValue = new ArrayList<>();
    for (double[] tempArr : perlinMap) {
      for (double temp : tempArr) {
        tempAllBaseMapValue.add(temp);
      }
    }

    // 对集合从大到小进行排序
    tempAllBaseMapValue.sort(Comparator.reverseOrder());

    // 根据覆盖度读取对应索引的值
    int index = BigDecimal.valueOf(tempAllBaseMapValue.size()).multiply(BigDecimal.valueOf(percentage)).intValue();

    return tempAllBaseMapValue.get(index);
  }
}
