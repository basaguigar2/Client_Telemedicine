package Bitalino;

public class BITalinoException extends java.lang.Exception {
	
	private static final long serialVersionUID = 1L;
	public String code;
	public BITalinoException(BITalinoErrorTypes errorType) {
		super(errorType.getName());
	    code = errorType.getName();
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
