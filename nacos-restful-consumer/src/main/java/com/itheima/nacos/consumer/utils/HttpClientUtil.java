package com.itheima.nacos.consumer.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * httpClient工具类
 * 
 * @author liuzla
 *
 */
public class HttpClientUtil
{

    private static String EMPTY_STR = "";
    private static String UTF_8 = "UTF-8";

    /**
     * 通过连接池获取HttpClient
     * 
     * @return
     */
    private static CloseableHttpClient getHttpClient()
    {
	return HttpClientBuilder.create().build();
    }

    /**
     * 
     * @param url
     * @return
     */
    public static String httpGetRequest(String url)
    {
	HttpGet httpGet = new HttpGet(url);
	return getResult(httpGet);
    }

    public static String httpGetRequest(String url, Map<String, Object> params) throws URISyntaxException
    {
	URIBuilder ub = new URIBuilder();
	ub.setPath(url);

	ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
	ub.setParameters(pairs);

	HttpGet httpGet = new HttpGet(ub.build());
	return getResult(httpGet);
    }

    public static String httpGetRequest(String url, Map<String, Object> headers, Map<String, Object> params)
	    throws URISyntaxException
    {
	URIBuilder ub = new URIBuilder();
	ub.setPath(url);

	ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
	ub.setParameters(pairs);

	HttpGet httpGet = new HttpGet(ub.build());
	for (Map.Entry<String, Object> param : headers.entrySet())
	{
	    httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));
	}
	return getResult(httpGet);
    }

    public static String httpPostRequest(String url)
    {
	HttpPost httpPost = new HttpPost(url);
	return getResult(httpPost);
    }

    public static String httpPostRequest(String url, Map<String, Object> params) throws UnsupportedEncodingException
    {
	HttpPost httpPost = new HttpPost(url);
	ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
	httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
	return getResult(httpPost);
    }

    public static String httpPostRequest(String url, Map<String, Object> headers, Map<String, Object> params)
	    throws UnsupportedEncodingException
    {
	HttpPost httpPost = new HttpPost(url);

	for (Map.Entry<String, Object> param : headers.entrySet())
	{
	    httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
	}

	ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
	httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));

	return getResult(httpPost);
    }

    public static String httpPostRequest(String url, String body)
    {
	// 设置HTTP请求超时时间
	HttpPost httpPost = new HttpPost(url);
	StringEntity entity = new StringEntity(body, "utf-8");// 解决中文乱码问题
	entity.setContentEncoding("UTF-8");
	entity.setContentType("application/json");
	httpPost.setEntity(entity);
	return getResult(httpPost);
    }

    public static String httpPutRequest(String url, String body)
    {
	HttpPut httpPut = new HttpPut(url);

	StringEntity entity = new StringEntity(body, "utf-8");// 解决中文乱码问题
	entity.setContentEncoding("UTF-8");
	entity.setContentType("application/json");
	httpPut.setEntity(entity);
	return getResult(httpPut);
    }

    public static String httpPostRequestWithJson(String url, String body, Map<String, Object> headers)
	    throws UnsupportedEncodingException
    {
	HttpPost httpPost = new HttpPost(url);

	for (Map.Entry<String, Object> param : headers.entrySet())
	{
	    httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
	}

	StringEntity entity = new StringEntity(body, "utf-8");// 解决中文乱码问题
	entity.setContentEncoding("UTF-8");
	entity.setContentType("application/json");
	httpPost.setEntity(entity);
	return getResult(httpPost);
    }

    public static String httpPutRequestWithJson(String url, String body, Map<String, Object> headers)
	    throws UnsupportedEncodingException
    {
	HttpPut httpPut = new HttpPut(url);

	for (Map.Entry<String, Object> param : headers.entrySet())
	{
	    httpPut.addHeader(param.getKey(), String.valueOf(param.getValue()));
	}

	StringEntity entity = new StringEntity(body, "utf-8");// 解决中文乱码问题
	entity.setContentEncoding("UTF-8");
	entity.setContentType("application/json");
	httpPut.setEntity(entity);
	return getResult(httpPut);
    }

    public static String httpDeleteRequest(String url)
    {
	HttpDelete httpDelete = new HttpDelete(url);
	return getResult(httpDelete);
    }

    private static ArrayList<NameValuePair> covertParams2NVPS(Map<String, Object> params)
    {
	ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
	for (Map.Entry<String, Object> param : params.entrySet())
	{
	    pairs.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));
	}

	return pairs;
    }

    /**
     * 处理Http请求
     * 
     * @param request
     * @return
     */
    private static String getResult(HttpRequestBase request)
    {
	// CloseableHttpClient httpClient = HttpClients.createDefault();
	CloseableHttpClient httpClient = getHttpClient();
	try
	{
	    // 设置HTTP请求超时时间
	    Builder customReqConf = RequestConfig.custom();
	    customReqConf.setConnectTimeout(10000);
	    customReqConf.setSocketTimeout(10000);
	    customReqConf.setConnectionRequestTimeout(10000);
	    request.setConfig(customReqConf.build());

	    CloseableHttpResponse response = httpClient.execute(request);
	    // response.getStatusLine().getStatusCode();
	    HttpEntity entity = response.getEntity();
	    if (entity != null)
	    {
		// long len = entity.getContentLength();// -1 表示长度未知
		String result = EntityUtils.toString(entity);
		response.close();
		// httpClient.close();
		return result;
	    }
	} catch (ClientProtocolException e)
	{
	    e.printStackTrace();
	} catch (IOException e)
	{
	    e.printStackTrace();
	} finally
	{

	}

	return EMPTY_STR;
    }

}