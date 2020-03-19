package me.chanjar.weixin.common.util.http.okhttp;

import com.google.common.collect.ImmutableMap;
import me.chanjar.weixin.common.WxType;
import me.chanjar.weixin.common.bean.result.WxMediaVideoInfoResult;
import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.util.http.MediaVideoInfoRequestExecutor;
import me.chanjar.weixin.common.util.http.RequestHttp;
import me.chanjar.weixin.common.util.json.WxGsonBuilder;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author songwendong
 * @date Created in 2020/3/19 10:07
 * @description 临时素材视频请求okHttp
 * @modified songwendong
 */
public class OkhttpMediaVideoInfoRequestExecutor extends MediaVideoInfoRequestExecutor<OkHttpClient,
  OkHttpProxyInfo> {
  public OkhttpMediaVideoInfoRequestExecutor(RequestHttp requestHttp) {
    super(requestHttp);
  }

  @Override
  public WxMediaVideoInfoResult execute(String uri, String materialId, WxType wxType) throws WxErrorException,
    IOException {

    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
      WxGsonBuilder.create().toJson(ImmutableMap.of("media_id", materialId)));
    Request request = new Request.Builder().url(uri).post(requestBody).build();
    Response response = requestHttp.getRequestHttpClient().newCall(request).execute();
    String responseContent = response.body().string();
    WxError error = WxError.fromJson(responseContent, WxType.MP);
    if (error.getErrorCode() != 0) {
      throw new WxErrorException(error);
    } else {
      return WxMediaVideoInfoResult.fromJson(responseContent);
    }
  }
}
