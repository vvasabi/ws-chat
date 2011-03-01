var url = '';
var pollInterval = 1500; // 1.5 seconds
var currentRoom = '';
var username = '';

// load config
jQuery(function() {
  jQuery.ajax({
    url: 'config.json',
    success: function(data) {
      url = data.url;

      var message = '<strong>Url:</strong> ' + url;
      jQuery('#status').html(message);
      setInterval(pollUpdate, pollInterval);
    },
    error: function(xhr) {
      var message = 'Config loading failed: ' + xhr.status;
      jQuery('#status').html(message);
    },
    dataType: 'json'
  });

  jQuery('#join-room').click(function() {
    var roomKey = jQuery('#room-key').val();
    username = jQuery('#client-username').val();
    if (!roomKey) {
      alert('Please enter the room to join.');
      return;
    }
    if (!username) {
      alert('Please enter your username.');
    }
    createUser(username, function() {
      joinRoom(roomKey, username, function(){
        var message = 'Joined room successfully.';
        jQuery('#current-room .status').text(message);

        currentRoom = roomKey;
        jQuery('#room-key').attr('disabled', 'disabled');
        jQuery('#client-username').attr('disabled', 'disabled');
        jQuery('#join-room').attr('disabled', 'disabled');
        jQuery('#message').removeAttr('disabled');
        jQuery('#send-button').removeAttr('disabled');
      });
    });
  });

  jQuery('#send-button').click(function() {
    var message = jQuery('#message').val();
    if (!message) {
      alert('Please enter something that you would like to say.');
      return;
    }
    jQuery('#message').val('');
    sendMessage(currentRoom, username, message);
  });
});

function createUser(username, success) {
  jQuery.ajax({
    contentType: 'application/json',
    type: 'POST',
    url: url + 'client/add',
    success: success,
    error: function(xhr, textStatus) {
      var message = 'Unable to join a room: ' + textStatus;
      jQuery('#current-room .status').text(message);
    },
    data: JSON.stringify({ username: username })
  });
}

function joinRoom(roomKey, username, success) {
  jQuery.ajax({
    type: 'GET',
    url: url + 'room/join/' + roomKey + '/' + username,
    success: success,
    error: function(xhr, textStatus) {
      var message = 'Unable to join a room: ' + textStatus;
      jQuery('#current-room .status').text(message);
    }
  });
}

function sendMessage(roomKey, username, message) {
  jQuery.ajax({
    contentType: 'application/json',
    type: 'POST',
    url: url + 'room/messages/add/' + roomKey,
    error: function(xhr, textStatus) {
      var message = 'Unable to send message: ' + textStatus;
      jQuery('#messages').append('<p>' + message + '</p>');
    },
    data: JSON.stringify({ body: message, client: username  })
  });
}

/**
 * Get updates from the server.
 */
function pollUpdate() {
  updateListOfRooms();
  updateListOfClients();
  updateMessages();
}

function updateListOfRooms() {
  jQuery.ajax({
    type: 'GET',
    url: url + 'room/list',
    success: function(data) {
      var element = jQuery('#rooms');
      if (!data.length) {
        element.html('<p>No rooms are found.</p>');
        return;
      }
      var list = jQuery('<ul />');
      for (var i in data) {
        var name = data[i].key;
        if (data[i].name) {
          name = data[i].name + ' (' + data[i].key + ')';
        }
        list.append('<li>' + name + '</li>');
      }
      element.html(list);
    },
    error: function() {
      jQuery('#rooms').html('<p>Room update failed. Will try again.</p>');
    },
    timeout: pollInterval,
    dataType: 'json'
  });
}

function updateListOfClients() {
  if (!currentRoom) {
    return;
  }
  jQuery.ajax({
    type: 'GET',
    url: url + 'room/' + currentRoom + '/clients',
    success: function(data) {
      var element = jQuery('#clients');
      if (!data.length) {
        element.html('<p>No clients are found.</p>');
        return;
      }
      var list = jQuery('<ul />');
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
      jQuery('#rooms').html('<p>Client update failed. Will try again.</p>');
    },
    timeout: pollInterval,
    dataType: 'json'
  });
}

function updateMessages() {
  if (!currentRoom) {
    return;
  }
  jQuery.ajax({
    type: 'GET',
    url: url + 'room/messages/list/' + currentRoom + '/' + username,
    success: function(data) {
      var element = jQuery('#messages');
      if (!data.length) {
        return;
      }
      for (var i in data) {
        var message = data[i].client + ": " + data[i].body;
        message += ' &mdash; ' + (new Date(data[i].createTime * 1000));
        element.append('<p>' + message + '</p>');
      }
    },
    error: function() {
      var message = 'Message update failed. Will try again...';
      jQuery('#messages').append('<p>' + message + '</p>');
    },
    timeout: pollInterval,
    dataType: 'json'
  }); 
}

