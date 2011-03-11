var username = '';
var url = '';
var pollInterval = 1500; // 1.5 seconds
var currentRoom = '';
var username = '';
var refreshRequested = false;
var session = ''; // for backend

// message types
var MessageType = {
    REGULAR: 'Regular',
    NOTICE: 'Notice',
    ERROR: 'Error'
};

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
            setInterval(pollUpdate, pollInterval);
            userInit();
        },
        error: function(xhr) {
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
            updateMessages();
            refreshRequested = true;
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
    var element = jQuery('#messages');
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

/**
 * Get updates from the server.
 */
function pollUpdate() {
  updateListOfClients();
  updateMessages();
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
            var list = jQuery('#participants .list');
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
            element.html(list);
        },
        error: function() {
            var message = '無法更新聊天室成員，稍後會再試。';
            postMessage(MessageType.ERROR, null, message);
        },
        timeout: pollInterval,
        dataType: 'json'
    });
}

function updateMessages() {
    if (!currentRoom) {
        return;
    }
    if (refreshRequested) {
        refreshRequested = false;
        return;
    }
    var key = encodeURIComponent(currentRoom);
    jQuery.ajax({
        type: 'GET',
        url: appendSession(url + 'room/info/' + key + '/messages'),
        success: function(data) {
            var element = jQuery('#messages');
            if (!data.length) {
                return;
            }
            for (var i in data) {
                postMessage(MessageType.REGULAR, data[i].client, data[i].body);
            }
        },
        error: function() {
            var message = '更新訊息失敗，稍後會再嘗試。';
            postMessage(MessageType.ERROR, null, message);
        },
        timeout: pollInterval,
        dataType: 'json'
    });
}
