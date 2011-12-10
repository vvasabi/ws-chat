var username = '';
var url = '';
var longPollTimeout = 75000; // 75 seconds
var pollInterval = 250; // short because we use long poll
var participantPollInterval = 5000; // 5 seconds
var pollingMessages = false;
var currentRoom = '';
var username = '';
var session = ''; // for backend
var currentTab;
var windowFocused = true;
var failCount = 0;
var failThreshold = 5;
var syncMessageInterval;
var syncClientsInterval;

// message types
var MessageType = {
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

jQuery(function() {
	// no support for msie
	if (msie()) {
		jQuery('#ie-mask').css('display', 'block');
		jQuery('#ie-mask').css('height', jQuery(document).height() + 'px');
		return;
	}

	// load config
	jQuery.ajax({
		url: 'config.json',
		success: function(data) {
			url = data.url;
			var message = '正在登入聊天室: ' + url;
			postMessage(MessageType.NOTICE, null, message);
			getSessionId();
		},
		error: function(xhr) {
			var message = '無法讀取聊天室設定資料: ' + xhr.status;
			postMessage(MessageType.ERROR, null, message);
		},
		dataType: 'json'
	});

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
	enter(function(data) {
		username = data;
		jQuery('#client-username').val(username);

		var roomKey = '大廳';
		joinRoom(roomKey, function() {
			var message = '成功加入聊天室「' + roomKey + '」';
			postMessage(MessageType.NOTICE, null, message);

			currentRoom = roomKey;
			jQuery('#send-message-form input').removeAttr('disabled');

			syncClientsInterval = setInterval(updateListOfClients, participantPollInterval);
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
			var message = '登入聊天室失敗： ' + xhr.status;
			postMessage(MessageType.ERROR, null, message);
		}
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
		success: function() {
			// NOOP
		},
		error: function(xhr, textStatus) {
			var message = '無法送出訊息： ' + textStatus;
			postMessage(MessageType.ERROR, null, message);
		},
		data: JSON.stringify({ body: message })
	});
}

function appendSession(url) {
return url + ';jsessionid=' + session;
}

function postMessage(type, source, body, time) {
	var element = currentTab.find('.messages');
	var cssClass = '';
	switch (type) {
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

	var message = '';
	var escaped = body;
	while (escaped.search('<') != -1) {
		escaped = escaped.replace('<', '&lt;');
	}
	while (escaped.search('>') != -1) {
		escaped = escaped.replace('>', '&gt;');
	}
	if (type == MessageType.REGULAR) {
		var message = '<span class="source">' + source + ':</span> ';
		message += '<span class="body">' + escaped + '</span>';
	} else {
		var message = '<span class="body">' + escaped + '</span>';
	}

	var date = new Date();
	if (time) {
		date = new Date(time);
	}
	message += ' <span class="date">&mdash; ' + date.getHours() + ':';
	message += date.getMinutes() + ':' + date.getSeconds() + '</span>';
	element.append('<p class="' + cssClass + '">' + message + '</p>');
	element.each(function() {
		this.scrollTop = this.scrollHeight;
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
		timeout: participantPollInterval,
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
			pollingMessages = false;
			if (!data.length) {
				return;
			}
			for (var i in data) {
				postMessage(MessageType.REGULAR, data[i].client, data[i].body);
			}
			notify();
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

function notify() {
	if (!windowFocused && jQuery('#notification-toggle').is(':checked')) {
		jQuery('#notification')[0].play();
	}
}

function checkFailCount() {
	if (failCount >= failThreshold) {
		var message = '與聊天室失去連線，請重新整理頁面。';
		postMessage(MessageType.ERROR, null, message);

		clearInterval(syncMessageInterval);
		clearInterval(syncClientsInterval);
	}
}
