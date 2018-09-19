package vo;
//뷰페이지로 포워딩 시 필요한 정보를 담는 객체
public class ActionForward {
	private String url;
	private boolean redirect;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isRedirect() {
		return redirect;
	}
	public void setRedirect(boolean redirect) {
		this.redirect = redirect;
	}
}
