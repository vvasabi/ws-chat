/**
 * Chat UI script.
 *
 * @author wasabi
 */

var JS_VERSION = 89757; // build system enters correct value
var username = '';
var url = '';
var longPollTimeout = 75000; // 75 seconds
var pollInterval = 250; // short because we use long poll
var pollingMessages = false;
var currentRoom = '';
var username = '';
var session = ''; // for backend
var currentTab;
var windowFocused = true;
var failCount = 0;
var failThreshold = 5;
var syncMessageInterval;

// message types
var MessageType = {
	ACTION: 'Action',
	REGULAR: 'Regular',
	NOTICE: 'Notice',
	ERROR: 'Error'
};

jQuery(window).focus(function() {
	windowFocused = true;
});

jQuery(window).blur(function() {
	windowFocused = false;
});

jQuery(window).load(function() {
	// no support for msie
	if (msie()) {
		jQuery('#wrapper').hide();
		jQuery('#ie-mask').css('display', 'block');
		jQuery('#ie-mask').css('height', jQuery(document).height() + 'px');
		return;
	}

	// check API version
	jQuery.ajax({
		url: 'config.json',
		success: function(data) {
			url = data.url;
			if (data.version > JS_VERSION) {
				var message = '已發現新版本，請重新整理頁面。';
				postMessage(MessageType.ERROR, null, message);
				return;
			}

			var message = '正在登入聊天室... ';
			postMessage(MessageType.NOTICE, null, message);
			getSessionId();
		},
		error: function(xhr) {
			var message = '無法讀取聊天室設定資料: ' + xhr.status;
			postMessage(MessageType.ERROR, null, message);
		},
		dataType: 'json'
	});

	// set up UI
	(function() {
		jQuery('#send-message-form').submit(function() {
			var message = jQuery('#message').val();
			if (!message.trim()) {
				postMessage(MessageType.ERROR, null, '請輸入要送出的訊息。');
				return false;
			}
			jQuery('#message').val('');
			sendMessage(currentRoom, message);
			return false;
		});

		// Madao no room tabs
		var tabs = jQuery('#room-tabs');
		var tab = jQuery('<li />');
		tab.addClass('current-room');

		var link = jQuery('<a href="#" />');
		link.attr('id', 'room-tab-link-1');
		link.text('大廳');
		link.click(function() {
			return false;
		});
		tab.append(link);
		tabs.append(tab);

		// actual room tab content
		var roomTabContent = jQuery('#room-tab-content');
		var roomTab = createRoomTab();
		currentTab = roomTab.attr('id', 'room-tab-1');
		roomTabContent.append(roomTab);

		// set up window size
		setUpWindow();
		jQuery(window).resize(function() {
			setUpWindow();
		});
	})();

	// room exit event
	jQuery(window).bind('beforeunload', function() {
		if (!currentRoom) {
			return;
		}

		var key = encodeURIComponent(currentRoom);
		jQuery.ajax({
			type: 'POST',
			url: appendSession(url + 'room/exit/' + key),
			async: false
		});
	});

	// @TODO: clean up madao's code
/*
	jQuery(function() {
		$("#tab-plus").append('<a href="javascript:;" class="tab-plus" id="plus">+</a>');
	});

	function creatNewRoom() {
		var roomName = prompt("Room Name:");
		joinRoom(roomName, function() {
				var message = '成功加入聊天室「' + roomName + '」';
				postMessage(MessageType.NOTICE, null, message);

				currentRoom = roomName;
				jQuery('#send-message-form input').removeAttr('disabled');
			});
		return roomName;
	}

	jQuery(document).ready(function(){
		$(".tab-plus").click(function() {
		var roomName = creatNewRoom().toString().trim();
		if(roomName != null || roomName != "") {
		var tab = '<a href="javascript:;" id="tab-'+tabCount+'">'+roomName+'</a>';
		var roomId = "tab-"+tabCount;
		var newRoom = tabCount-1;
		$("#tab").append(tab);
		$("#"+roomId).addClass("tabLinkRoom");
		$("#tab-room").append('<div id="'+roomId+'-1">'+
							'<div id="chat-box" class="clear-block">'+
								'<div id="participants">'+
									'<h2>聊天室成員</h2>'+
									'<ul class="list"></ul>'+
								'</div>'+
							'<div id="messages" class="message-box"></div></div></div>');
		$("#"+roomId+"-1").addClass("tabcontentRoom hideRoom paddingAll");
			$("#"+roomId).click(function() {
				$(".tabLinkRoom").removeClass("activeLinkRoom");
				$("#"+roomId).addClass("activeLinkRoom");
				$(".tabcontentRoom").addClass("hideRoom");
				$("#"+roomId+"-1").removeClass("hideRoom");
			});
			tabCount += 1;
			}
		});
	});*/
});

function msie() {
	var name = 'Internet Explorer';
	return navigator.appName.indexOf(name) != -1;
}

var elementsHeight = 0;
function setUpWindow() {
	// set the width
	var roomTabWidth = jQuery('.room-tab').first().width();
	var spacing = 220 + 15; // clinet list + right margin
	jQuery('.messages').width(roomTabWidth - spacing - 20); // 20 is padding
	jQuery('#message').width(roomTabWidth - spacing - 10); // 10 is padding

	// set the height
	if (!elementsHeight) {
		var wrapperHeight = jQuery('#wrapper').height();
		var messageBoxHeight = jQuery('.messages').first().height();
		elementsHeight = wrapperHeight - messageBoxHeight;
	}
	var windowHeight = jQuery(window).height();
	var height = windowHeight - elementsHeight + 43;
	jQuery('.messages').height(height - 30); // minus padding
	jQuery('.participants').height(height - 40); // minus padding
}

function getSessionId() {
	jQuery.ajax({
		type: 'POST',
		url: 'sid',
		success: function(data) {
		sid = data;
		setupServerSession();
		},
		error: function(xhr) {
			var message = '無法登入聊天室，請確定您已登入論壇。';
			postMessage(MessageType.ERROR, null, message);
		},
		dataType: 'text'
	});
}

function setupServerSession() {
	jQuery.ajax({
		type: 'GET',
		url: url + 'client/session',
		success: function(data) {
			session = data;
			userInit();
		},
		error: function(xhr) {
			if (!xhr.status) {
				postMessage(MessageType.ERROR, null, "無法連線至聊天室，請稍後再試。");
				return;
			}
			if (xhr.status == 403) {
				postMessage(MessageType.ERROR, null, "請先登入論壇。");
				return;
			}
			var message = '登入聊天室失敗： ' + xhr.status;
			postMessage(MessageType.ERROR, null, message);
		},
		dataType: 'text'
	});
}

function userInit() {
	enter(function(client) {
		username = client.username;
		jQuery('#client-username').val(username);

		var roomKey = '大廳';
		joinRoom(roomKey, function() {
			var message = '成功加入聊天室「' + roomKey + '」';
			postMessage(MessageType.NOTICE, null, message);

			currentRoom = roomKey;
			jQuery('#send-message-form input').removeAttr('disabled');

			syncMessageInterval = setInterval(updateMessages, pollInterval);
			updateListOfClients();
		});
	});
}

function enter(success) {
	jQuery.ajax({
		contentType: 'application/json',
		type: 'POST',
		url: appendSession(url + 'client/enter/' + sid),
		success: success,
		error: function(xhr, textStatus) {
			if (xhr.status == 403) {
				postMessage(MessageType.ERROR, null, "請先登入論壇。");
				return;
			}

			var message = '登入聊天室失敗： ' + xhr.status;
			postMessage(MessageType.ERROR, null, message);
		},
		dataType: 'json'
	});
}

function joinRoom(roomKey, success) {
	var key = encodeURIComponent(roomKey);
	jQuery.ajax({
		type: 'POST',
		url: appendSession(url + 'room/join/' + key),
		success: success,
		error: function(xhr, textStatus) {
			var message = '加入聊天室失敗： ' + xhr.status;
			postMessage(MessageType.ERROR, null, message);
		}
	});
}

function sendMessage(roomKey, message) {
	var key = encodeURIComponent(roomKey);
	jQuery.ajax({
		contentType: 'application/json',
		type: 'POST',
		url: appendSession(url + 'room/info/' + key + '/messages'),
		success: function(message) {
			postMessage(MessageType.REGULAR, username, message.body);
		},
		error: function(xhr, textStatus) {
			var message = '無法送出訊息： ' + textStatus;
			postMessage(MessageType.ERROR, null, message);
		},
		data: JSON.stringify({body: message}),
		dataType: 'json'
	});
}

function appendSession(url) {
return url + ';jsessionid=' + session;
}

function postMessage(type, source, body, time) {
	var element = currentTab.find('.messages');
	var cssClass = '';
	switch (type) {
		case MessageType.ACTION:
			cssClass = 'message-action';
		break;

		case MessageType.REGULAR:
			cssClass = 'message-regular';
		break;

		case MessageType.NOTICE:
			cssClass = 'message-notice';
		break;

		case MessageType.ERROR:
			cssClass = 'message-error';
		break;
	}

	// escape html
	var message;
	if (type == MessageType.REGULAR) {
		message = '<span class="source">' + source + ':</span> ';
		message += '<span class="body">' + body + '</span>';
	} else if (type == MessageType.ACTION) {
		message = '<span class="source">' + source + ' </span>';
		message += '<span class="body">' + body + '</span>';
	} else {
		message = '<span class="body">' + body + '</span>';
	}

	var date = new Date();
	if (time) {
		date = new Date(time);
	}
	message += ' <span class="date">&mdash; ' + date.getHours() + ':';
	message += date.getMinutes() + ':' + date.getSeconds() + '</span>';

	var paragraph = jQuery('<p class="' + cssClass + '">' + message + '</p>');
	element.append(paragraph);
	element.each(function() {
		this.scrollTop = this.scrollHeight;
	});

	// allow links to be clickable
	paragraph.find('a').click(function() {
		var href = jQuery(this).attr('href');
		if (!href) {
			return false;
		}

		if (window.opener) {
			window.opener.location = href;
			return false;
		}

		window.open(href);
		return false;
	});
}

function updateListOfClients() {
	if (!currentRoom) {
		return;
	}
	var key = encodeURIComponent(currentRoom);
	jQuery.ajax({
		type: 'GET',
		url: appendSession(url + 'room/info/' + key + '/clients'),
		success: function(data) {
			failCount = 0;
			var list = currentTab.find('.participants').children('.list');
			if (!data.length) {
				return;
			}
			list.empty();
			for (var i in data) {
				var name = data[i].username;
				if (data[i].status) {
					name += ' (' + data[i].status + ')';
				}
				list.append('<li>' + name + '</li>');
			}
		},
		error: function() {
			failCount++;
			checkFailCount();
		},
		dataType: 'json'
	});
}

function updateMessages() {
	if (!currentRoom) {
		return;
	}

	// do not poll if already doing so
	if (pollingMessages) {
		return;
	}
	pollingMessages = true;

	var key = encodeURIComponent(currentRoom);
	jQuery.ajax({
		type: 'GET',
		url: appendSession(url + 'room/info/' + key + '/messages'),
		success: function(data) {
			failCount = 0;

			var entranceNotify = false;
			var messageNotify = false;
			if (data.length) {
				for (var i in data) {
					switch (data[i].type) {
						case 'Entrance':
							handleEntranceMessage(data[i]);
							updateListOfClients();
							entranceNotify = true;
						break;

						case 'Exit':
							handleExitMessage(data[i]);
							updateListOfClients();
						break;

						case 'Regular':
							handleRegularMessage(data[i]);
							messageNotify = true;
						break;
					}
				}

				if (entranceNotify) {
					notifyEntrance();
				}
				if (messageNotify) {
					notifyMessage();
				}
			}

			// start over right away
			pollingMessages = false;
			updateMessages();
		},
		error: function() {
			pollingMessages = false;

			var message = '更新訊息失敗，稍後會再嘗試。';
			postMessage(MessageType.ERROR, null, message);
			failCount++;
			checkFailCount();
		},
		timeout: longPollTimeout,
		dataType: 'json'
	});
}

function handleEntranceMessage(message) {
	postMessage(MessageType.ACTION, message.client, "加入聊天室。");
}

function handleExitMessage(message) {
	postMessage(MessageType.ACTION, message.client, "離開聊天室。");
}

function handleRegularMessage(message) {
	postMessage(MessageType.REGULAR, message.client, message.body);
}

function createRoomTab() {
	var roomTab = jQuery('<div />');
	roomTab.addClass('room-tab');
	roomTab.addClass('clear-block');

	var participantBox = jQuery('<div />');
	participantBox.addClass('participants');
	participantBox.append('<h2>聊天室成員</h2>');
	participantBox.append('<ul class="list" />');
	roomTab.append(participantBox);
	roomTab.append('<div class="messages" />');
	return roomTab;
}

function notifyEntrance() {
	if (sendNotificationAudio()) {
		jQuery('#entrance-notification')[0].play();
	}
}

function notifyMessage() {
	if (sendNotificationAudio()) {
		jQuery('#message-notification')[0].play();
	}
}

function sendNotificationAudio() {
	return !windowFocused && jQuery('#notification-toggle').is(':checked');
}

function checkFailCount() {
	if (failCount >= failThreshold) {
		var message = '與聊天室失去連線，請重新整理頁面。';
		postMessage(MessageType.ERROR, null, message);

		clearInterval(syncMessageInterval);
	}
}
