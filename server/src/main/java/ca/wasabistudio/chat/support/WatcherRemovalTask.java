package ca.wasabistudio.chat.support;

import java.util.concurrent.Callable;

/**
 * A callable task that removes the given watcher from the queue.
 *
 * @author wasabi <wasabi@wasabistudio.ca>
 */
public class WatcherRemovalTask implements Callable<Boolean> {

	private final UpdateQueue queue;
	private UpdateWatcher watcher;
	private Callable<Boolean> eventHandler;

	public WatcherRemovalTask(UpdateQueue queue) {
		this.queue = queue;
	}

	public void setUpdateWatcher(UpdateWatcher watcher) {
		this.watcher = watcher;
	}

	@Override
	public Boolean call() throws Exception {
		if (watcher != null) {
			queue.removeWatcher(watcher);
			if (eventHandler != null) {
				return eventHandler.call();
			}
			return true;
		}
		return false;
	}

	public void setRemovalEventHandler(Callable<Boolean> handler) {
		eventHandler = handler;
	}

}
