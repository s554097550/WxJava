package me.chanjar.weixin.common.bean.result;

import lombok.Data;
import me.chanjar.weixin.common.util.json.WxGsonBuilder;

/**
 * @author songwendong
 * @date Created in 2020/3/18 23:01
 * @description 微信视频临时素材下载链接
 * @modified songwendong
 */
@Data
public class WxMediaVideoInfoResult {
  private String videoUrl;

  public static WxMediaVideoInfoResult fromJson(String json) {
    return WxGsonBuilder.create().fromJson(json, WxMediaVideoInfoResult.class);
  }

  @Override
  public String toString() {
    return WxGsonBuilder.create().toJson(this);
  }
}
