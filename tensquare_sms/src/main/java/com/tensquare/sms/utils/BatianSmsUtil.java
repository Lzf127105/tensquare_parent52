package com.tensquare.sms.utils;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * 芭田发送短信
 */
public class BatianSmsUtil {
	/**
	 * 开发者标示
	 */
	private static final String developerID = "8f6d914b430e42f4a9799354fb2a3f18";
	/**
	 * 秘钥
	 */
	private static final String secretKey = "25419afb9b1e4b599da7d42f9e7fef04";
	
	/**
	 * 4.1短信发送接口
	 */
	private static final String sendMsgUrl = "http://www.xinxinke.com/api/send";
	   
    // 转码
    private static String encode(String input) throws Exception {
		return URLEncoder.encode(input, "UTF-8");
	}

	// 发起 POST 请求
	public static boolean post(String uri,String param) {
		try{
			URL url = new URL(uri);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setUseCaches(false);
			conn.connect();
			// 输出参数
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(param);
			dos.flush();
			dos.close();
			// 读取响应
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line = br.readLine();
			br.close();
			// 关闭连接
			conn.disconnect();
			System.out.println(line);
			if(StringUtils.isNotBlank(line)){
				JSONObject data = JSONObject.fromObject(line);
				if("25010".equals(data.getString("code"))){
					return true;
				}
			}
		}catch(Exception e){
			System.err.println("短信平台post请求异常！");
		}
		return false;
	}
		
		
	/**
	 * 发送短信
	 * @param phones
	 * @param content
	 * @return
	 */
	public static boolean sendSMS(String phones,String content){
		String params = buildSendParam(phones,content);
		return post(sendMsgUrl,params);
	}
	
	private static String buildSendParam(String phones,String content){
		String sign = MD5.md5(developerID + secretKey + phones);
		StringBuffer param = new StringBuffer("");
		try{
			String sms_param = "{\"code\":\""+content+"\"}";
		    param.append("").append(encode("dev_id")).append("=").append(encode(developerID));
			param.append("&").append(encode("sms_template_code")).append("=").append(encode("sendMessage"));
			param.append("&").append(encode("rec_num")).append("=").append(encode(phones));
			param.append("&").append(encode("sms_param")).append("=").append(encode(sms_param));
			param.append("&").append(encode("sign")).append("=").append(encode(sign));
		}catch(Exception e){
			System.err.println("构建发送短信请求参数异常！");
		}
		return param.toString();
	}
	
	public static void main(String[] args) {
		//System.out.println(BatianSmsUtil.sendSMS("17688772722", "十次方短信测试！"));
	}
}
