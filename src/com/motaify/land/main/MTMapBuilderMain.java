package com.motaify.land.main;

import com.motaify.land.bean.MTLand;
import com.motaify.land.bean.MTLandElement;
import com.motaify.land.bean.MTLandElementTier;
import com.motaify.land.bean.MTRandom;
import com.motaify.land.test.MTMapBuilderTest;
import com.motaify.land.utils.MTMapBuilderUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.motaify.land.utils.MTMapConst.*;
import static com.motaify.land.utils.MTMapConst.RES_ID_MAP_TO_S_WATER;

public class MTMapBuilderMain {
  public static void main(String[] args) {
    // 水域参数
    double waterProb = 0.028;// 缩放,值越小形状越大
    double waterPersistence = 0.4; // 复杂度,数量越大越复杂
    double waterPercentage = 0.05; // 占比,针对于整个地图当前地形所占的比例

    // 山体参数
    double hillProb = 0.018;
    double hillPersistence = 0.45;
    double hillPercentage = 0.2;

    // 生成地图各层级的配置
    // * 由于存在将深水临山区域转化为浅水的逻辑,所以山一定要在水后绘制
    List<MTLandElement> elements = new ArrayList<>();
    // 额外地形配置项
    elements.add(new MTLandElement().setType(RES_ID_MAP_GROUND1).setProb(0.1).setPercentage(0.1).setPersistence(0.4));
    elements.add(new MTLandElement().setType(RES_ID_MAP_GROUND2).setProb(0.03).setPercentage(0.1).setPersistence(0.4));
    elements.add(new MTLandElement().setType(RES_ID_MAP_GROUND3).setProb(0.06).setPercentage(0.1).setPersistence(0.4));

    // 水地形
    elements.add(new MTLandElement(RES_ID_MAP_D_WATER, waterProb, waterPersistence, waterPercentage, Arrays.asList(new MTLandElementTier(RES_ID_MAP_S_WATER, 0.7), new MTLandElementTier(RES_ID_MAP_MUDDY, 0.4))));
    // 山地形
    elements.add(new MTLandElement(RES_ID_MAP_HILL, hillProb, hillPersistence, hillPercentage, Arrays.asList(new MTLandElementTier(RES_ID_MAP_STONE, 0.5), new MTLandElementTier(RES_ID_MAP_GRAVEL, 0.25), new MTLandElementTier(RES_ID_MAP_TO_S_WATER, 0))));

    MTRandom random = new MTRandom(100);
    
    System.out.println("开始生成地形...");
    long now = System.currentTimeMillis();
    MTLand land = MTMapBuilderUtils.createLand(random, 256, 256, elements);
    System.out.println("地形生成完毕.耗时" + (System.currentTimeMillis() - now) + "ms.");
    
    try {
      MTMapBuilderTest.showMap(land, "test_land" + random.getSeed() + ".png");
    } catch (IOException e) {
      System.err.println("绘制地形时出现IO异常");
      e.printStackTrace();
    }
  }
}
