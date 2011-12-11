package ca.wasabistudio.chat.support;

public interface UpdateWatcher {

	void pushUpdate(Object data);

	boolean isFinished();

}
