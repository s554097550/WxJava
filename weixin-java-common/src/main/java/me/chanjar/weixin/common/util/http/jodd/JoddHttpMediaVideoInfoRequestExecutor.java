package me.chanjar.weixin.common.util.http.jodd;

import jodd.http.HttpConnectionProvider;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import jodd.http.ProxyInfo;
import jodd.util.StringPool;
import me.chanjar.weixin.common.WxType;
import me.chanjar.weixin.common.bean.result.WxMediaVideoInfoResult;
import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.util.http.MediaVideoInfoRequestExecutor;
import me.chanjar.weixin.common.util.http.RequestHttp;

import java.io.IOException;

/**
 * @author songwendong
 * @date Created in 2020/3/19 10:04
 * @description 临时素材视频请求joddHttp
 * @modified songwendong
 */
public class JoddHttpMediaVideoInfoRequestExecutor extends MediaVideoInfoRequestExecutor<HttpConnectionProvider,
  ProxyInfo> {
  public JoddHttpMediaVideoInfoRequestExecutor(RequestHttp requestHttp) {
    super(requestHttp);
  }

  @Override
  public WxMediaVideoInfoResult execute(String uri, String materialId, WxType wxType) throws WxErrorException,
    IOException {
    HttpRequest request = HttpRequest.post(uri);
    if (requestHttp.getRequestHttpProxy() != null) {
      requestHttp.getRequestHttpClient().useProxy(requestHttp.getRequestHttpProxy());
    }
    request.withConnectionProvider(requestHttp.getRequestHttpClient());

    request.query("media_id", materialId);
    HttpResponse response = request.send();
    response.charset(StringPool.UTF_8);
    String responseContent = response.bodyText();
    WxError error = WxError.fromJson(responseContent, WxType.MP);
    if (error.getErrorCode() != 0) {
      throw new WxErrorException(error);
    } else {
      return WxMediaVideoInfoResult.fromJson(responseContent);
    }
  }
}
