package me.chanjar.weixin.common.util.http.apache;

import me.chanjar.weixin.common.WxType;
import me.chanjar.weixin.common.bean.result.WxMediaVideoInfoResult;
import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.util.http.MediaVideoInfoRequestExecutor;
import me.chanjar.weixin.common.util.http.RequestHttp;
import me.chanjar.weixin.common.util.json.WxGsonBuilder;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author songwendong
 * @date Created in 2020/3/19 09:50
 * @description 临时素材视频请求httpclient
 * @modified songwendong
 */
public class ApacheMediaVideoInfoRequestExecutor extends MediaVideoInfoRequestExecutor<CloseableHttpClient, HttpHost> {
  public ApacheMediaVideoInfoRequestExecutor(RequestHttp requestHttp) {
    super(requestHttp);
  }

  @Override
  public WxMediaVideoInfoResult execute(String uri, String materialId, WxType wxType) throws WxErrorException,
    IOException {
    HttpPost httpPost = new HttpPost(uri);
    if (requestHttp.getRequestHttpProxy() != null) {
      RequestConfig config = RequestConfig.custom().setProxy(requestHttp.getRequestHttpProxy()).build();
      httpPost.setConfig(config);
    }

    Map<String, String> params = new HashMap<>();
    params.put("media_id", materialId);
    httpPost.setEntity(new StringEntity(WxGsonBuilder.create().toJson(params)));
    try (CloseableHttpResponse response = requestHttp.getRequestHttpClient().execute(httpPost)) {
      String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);
      WxError error = WxError.fromJson(responseContent, WxType.MP);
      if (error.getErrorCode() != 0) {
        throw new WxErrorException(error);
      } else {
        return WxMediaVideoInfoResult.fromJson(responseContent);
      }
    } finally {
      httpPost.releaseConnection();
    }
  }
}
