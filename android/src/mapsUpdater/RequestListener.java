package mapsUpdater;

public interface RequestListener {
	void onResponse(String response);
	void onError(String error);
}
