package me.chanjar.weixin.common.util.http;

import me.chanjar.weixin.common.WxType;
import me.chanjar.weixin.common.bean.result.WxMediaVideoInfoResult;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.util.http.apache.ApacheMediaVideoInfoRequestExecutor;
import me.chanjar.weixin.common.util.http.jodd.JoddHttpMediaVideoInfoRequestExecutor;
import me.chanjar.weixin.common.util.http.okhttp.OkhttpMediaVideoInfoRequestExecutor;

import java.io.IOException;

/**
 * @author songwendong
 * @date Created in 2020/3/18 23:26
 * @description 临时素材视频信息
 * @modified songwendong
 */
public abstract class MediaVideoInfoRequestExecutor<H, P> implements RequestExecutor<WxMediaVideoInfoResult, String> {
  protected RequestHttp<H, P> requestHttp;

  public MediaVideoInfoRequestExecutor(RequestHttp requestHttp) {
    this.requestHttp = requestHttp;
  }

  @Override
  public void execute(String uri, String data, ResponseHandler<WxMediaVideoInfoResult> handler, WxType wxType) throws WxErrorException, IOException {
    handler.handle(this.execute(uri, data, wxType));
  }

  public static RequestExecutor<WxMediaVideoInfoResult, String> create(RequestHttp requestHttp) {
    switch (requestHttp.getRequestType()) {
      case APACHE_HTTP:
        return new ApacheMediaVideoInfoRequestExecutor(requestHttp);
      case JODD_HTTP:
        return new JoddHttpMediaVideoInfoRequestExecutor(requestHttp);
      case OK_HTTP:
        return new OkhttpMediaVideoInfoRequestExecutor(requestHttp);
      default:
        return null;
    }
  }
}
