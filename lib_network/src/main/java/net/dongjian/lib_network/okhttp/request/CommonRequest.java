package net.dongjian.lib_network.okhttp.request;

import java.io.File;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 对外提供get / post / 文件上传请求
 */
public class CommonRequest {

    /**
     * 一个请求要考虑 url 、 请求头 、 请求体
     * 然后开始构建请求对象、
     * 但请求头一般很少去自定义，所以可以进行一个重载操作
     *
     */

    /**
     * 创建一个get类型的Request，但不需要自定义请求头
     * @param url
     * @param params
     * @return
     */
    public static Request createGetRequest(String url, RequestParams params) {
        return createGetRequest(url, params, null);
    }

    /**
     * 创建一个get类型的request
     * @param url
     * @param params
     * @param header
     * @return
     */
    public static Request createGetRequest(String url , RequestParams params , RequestParams header){
        //和post不同的是，get请求的url和请求体是连在一起的，要用到StringBuilder
        StringBuilder stringBuilder = new StringBuilder(url).append("?");
        if(params != null){
            for(Map.Entry<String,String> entry : params.urlParams.entrySet()){
                stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        //添加请求头
        Headers.Builder headerBuilder = new Headers.Builder();
        if(header != null){
            for(Map.Entry<String,String> entry : header.urlParams.entrySet()){
                headerBuilder.add(entry.getKey(),entry.getValue());
            }
        }
        Headers mHeader = headerBuilder.build();
        return new Request.Builder().url(stringBuilder.substring(0,stringBuilder.length() - 1))
                .get()
                .headers(mHeader)
                .build();
    }

    /**
     * 创建一个post类型的Request，但不需要自定义请求头
     * @param url
     * @param params
     * @return
     */
    public static Request createPostRequest(String url, RequestParams params) {
        return createPostRequest(url, params, null);
    }

    /**
     * 创建一个post Request
     * @param url
     * @param params 请求体内容
     * @param header 请求头内容
     * @return
     */
    public static Request createPostRequest(String url , RequestParams params , RequestParams header ){
        //1、创建请求头和请求体 并进行填充
        Headers.Builder headers = new Headers.Builder();
        FormBody.Builder formBody = new FormBody.Builder();
        if(params != null){
            for (Map.Entry<String,String> entry : params.urlParams.entrySet()){
                formBody.add(entry.getKey(),entry.getValue());
            }
        }
        if(header != null){
            for (Map.Entry<String,String> entry : header.urlParams.entrySet()){
                headers.add(entry.getKey(),entry.getValue());
            }
        }
        //2、真正的请求头、请求体
        FormBody mFormBody = formBody.build();
        Headers mHeader = headers.build();
        Request request = new Request.Builder()
                .url(url)
                .post(mFormBody)
                .headers(mHeader)
                .build();
        return request;
    }

    //指 上传的类型是文件类型
    private static final MediaType FILE_TYPE = MediaType.parse("application/octet-stream");
    /**
     * 创建一个文件上传请求
     * @param url
     * @param params
     * @return
     */
    public static Request createMultiPostRequest(String url , RequestParams params){
        //创建请求体的builder  和url的一样，只是那个叫formBody，文件的叫MultipartBody
        MultipartBody.Builder requestBody = new MultipartBody.Builder();
        //文件上传请求，指定用表单提交
        requestBody.setType(MultipartBody.FORM);
        //进行数据的填充
        if(params != null){
            for (Map.Entry<String,Object> entry : params.fileParams.entrySet()){
                //如果数据类型是文件
                if (entry.getValue() instanceof File){
                    //addPart需要传入Headers,RequestBody
                    //请求头Headers.of，然后用key做name交给服务端获取文件；
                    // 请求体RequestBody.create，说明类型和值
                    requestBody.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + entry.getKey() + "\""),
                            RequestBody.create(FILE_TYPE, (File) entry.getValue()));
                }else if(entry.getValue() instanceof String){
                //不是文件类型的话就是json格式
                    requestBody.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + entry.getKey() + "\""),
                            RequestBody.create(null, (String) entry.getValue()));
                }
            }
        }
        return new Request.Builder().url(url).post(requestBody.build()).build();
    }

}
