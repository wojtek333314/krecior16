package mapsUpdater;

public class DownloadFile {
	private Param param;
	private Request request;
	private RequestListener requestListener;
	private boolean isEnd = false;
	private String fileNumber;
	
	public DownloadFile(String fileNumber){
		this.fileNumber = fileNumber;
		request = new Request("download.php",new RequestListener() {
				
				@Override
				public void onResponse(String response) {
					isEnd = true;
                    if(response.length()>0 && response.charAt(0)==' ')
                        response = response.substring(1);
					onEnd(response);
					if(requestListener!=null)
						requestListener.onResponse(response);
				}
				
				@Override
				public void onError(String error) {
					isEnd = true;
					onEnd(error);
					if(requestListener!=null)
						requestListener.onError(error);
				}
			});
			
		param = new Param("number",fileNumber);
	}
	
	public String getFileNumber(){
		return fileNumber;
	}
	
	public void setRequestListener(RequestListener rl){
		this.requestListener = rl;
	}
	
	public boolean isEnd(){
		return isEnd;
	}
	
	
	public void start(){
		request.execute(param);
	}
	
	public void onEnd(String out){
		
	}
	
}
