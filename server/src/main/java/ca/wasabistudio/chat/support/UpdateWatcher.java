package ca.wasabistudio.chat.support;

public interface UpdateWatcher {

	void pushUpdate(Object data);

	void cancel(Object data);

	boolean isFinished();

}
