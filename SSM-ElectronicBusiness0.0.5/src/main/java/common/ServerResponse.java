package common;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

//对返回的json数据含有null值进行过滤(如果值为null，键也会消失)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable{
	
	private int status;
	private String msg;
	private T data;
	
	private ServerResponse(int status){
		this.status=status;
	}
	private ServerResponse(int status,T data){
		this.status=status;
		this.data=data;
	}
	private ServerResponse(int status,String msg,T data){
		this.msg=msg;
		this.status=status;
		this.data=data;
	}
	private ServerResponse(int status,String msg){
		this.msg=msg;
		this.status=status;
	}
	
	//使之不在json序列化结果当中
	@JsonIgnore
	public boolean isSuccess(){
		return this.status == ResponseCode.SUCCESS.getCode();
	}
	
	public int getStatus() {
		return status;
	}
	public T getData() {
		return data;
	}
	public String getMsg() {
		return msg;
	}
	
	public static <T> ServerResponse<T> createBySuccess() {
		return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
	}
	
	public static <T> ServerResponse<T> createBySuccessMessage(String msg) {
		return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
	}
	
	public static <T> ServerResponse<T> createBySuccess(T data) {
		return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data);
	}
	
	public static <T> ServerResponse<T> createBySuccessMessage(String msg,T data) {
		return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
	}
	
	public static <T> ServerResponse<T> createByError() {
		return new ServerResponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
	}
	
	public static <T> ServerResponse<T> createByErrorMessage(String erroMessage) {
		return new ServerResponse<T>(ResponseCode.ERROR.getCode(),erroMessage);
	}
	
	public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode,String erroMessage) {
		return new ServerResponse<T>(errorCode,erroMessage);
	}
	
	
}
