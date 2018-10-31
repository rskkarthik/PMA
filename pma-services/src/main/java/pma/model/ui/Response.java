package pma.model.ui;

public class Response<T> {

	public static final int SUCCESS = 200;
	public static final int BAD_REQUEST = 202;
	public static final int EXCEPTION = 500;
	
	private int status;
	private T data;
	private String message;
	
	public Response(int status, T data, String errorMsg) {
		this.status = status;
		this.data = data;
		this.message = errorMsg;
	}
	
	public int getStatus() {
		return status;
	}

	public T getData() {
		return data;
	}

	public String getMessage() {
		return message;
	}
}
