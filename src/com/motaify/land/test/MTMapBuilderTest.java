package com.motaify.land.test;

import com.motaify.land.bean.MTLand;
import com.motaify.land.bean.MTLandElement;
import com.motaify.land.bean.MTLandElementTier;
import com.motaify.land.utils.MTMapBuilderUtils;
import com.motaify.land.bean.MTRandom;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.motaify.land.utils.MTMapConst.*;

public class MTMapBuilderTest {

  @Test
  public void test_createLand() throws IOException {
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

    MTLand land = MTMapBuilderUtils.createLand(random, 256, 256, elements);
    showMap(land, "test_land" + random.getSeed() + ".png");
  }

  public static BufferedImage showMap(MTLand land) {

    int xw = land.getXw();
    int yh = land.getYh();
    BufferedImage bi = new BufferedImage(xw * PIXEL_SIZE, yh * PIXEL_SIZE, BufferedImage.TYPE_4BYTE_ABGR);

    // 合并地表
    byte[][] landform = new byte[yh][xw];

    // 生成地图
    for (short y = 0; y < yh; y++) {
      for (short x = 0; x < xw; x++) {
        // 生成地图元素
        for (MTLandElement element : land.getElements()) {
          byte[][] site = element.getLandform();
          byte type = site[y][x];
          if (type != RES_ID_MAP_GROUND) {
            if (type != RES_ID_MAP_TO_S_WATER) {
              landform[y][x] = type;
            } else {
              // 山水转化类型需要特殊处理
              // 如果4级山位置是深水,转化为浅水
              if (landform[y][x] == RES_ID_MAP_D_WATER) {
                landform[y][x] = RES_ID_MAP_S_WATER;
              }
            }
          }
        }
      }
    }

    Graphics2D g = (Graphics2D) bi.getGraphics();
    // 绘制基础地表
    for (int y = 0; y < yh; y++) {
      for (int x = 0; x < xw; x++) {
        int landformId = landform[y][x];
        y = yh - 1 - y;

        if (landformId == RES_ID_MAP_GROUND) {
          drawPoint(g, new Color(146, 106, 57, 255), x, y);
        } else if (landformId == RES_ID_MAP_MUDDY) {
          drawPoint(g, new Color(108, 86, 60, 255), x, y);
        } else if (landformId == RES_ID_MAP_GRAVEL) {
          drawPoint(g, new Color(130, 95, 52, 255), x, y);
        } else if (landformId == RES_ID_MAP_STONE) {
          drawPoint(g, new Color(85, 85, 85, 255), x, y);
        } else if (landformId == RES_ID_MAP_HILL) {
          drawPoint(g, new Color(134, 134, 134, 255), x, y);
        } else if (landformId == RES_ID_MAP_S_WATER) {
          drawPoint(g, new Color(126, 187, 215, 255), x, y);
        } else if (landformId == RES_ID_MAP_D_WATER) {
          drawPoint(g, new Color(81, 138, 160, 255), x, y);
        } else if (landformId == RES_ID_MAP_GROUND1) {
          drawPoint(g, new Color(63, 111, 44, 255), x, y);
        } else if (landformId == RES_ID_MAP_GROUND2) {
          drawPoint(g, new Color(113, 102, 78, 255), x, y);
        } else if (landformId == RES_ID_MAP_GROUND3) {
          drawPoint(g, new Color(131, 137, 48, 255), x, y);
        }
      }
    }

    return bi;
  }

  public static void showMap(MTLand star, String fileName) throws IOException {
    BufferedImage bi = showMap(star);
    ImageIO.write(bi, "PNG", new File(fileName));
  }

  private static void drawPoint(Graphics g, Color c, int x, int y) {
    g.setColor(c);
    g.fillRect(x * PIXEL_SIZE, y * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);
  }
}
