<?php
$cookie_name = 'phpbb3_c7y16_';
?>
<!DOCTYPE html>
<html>
  <head>
    <title>WS Chat Dev Tool</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <script type="text/javascript" src="jquery-1.5.1.min.js"></script>
    <script type="text/javascript" src="json2.js"></script>
    <script type="text/javascript" src="chat.js"></script>
    <script type="text/javascript">
    // <![CDATA[
    var sid = '<?php echo $_COOKIE[$cookie_name . 'sid']; ?>';
    // ]]>
    </script>
    <style type="text/css">
      body {
        font: 1em/1em Gill Sans, Gill Sans MT, helvetica, sans-serif;
      }
    
      .message-box {
        border: 1px solid #444;
        height: 300px;
        padding: 10px;
        overflow: scroll;
      }

      .message-box p {
        margin: 5px 0;
      }

      #message {
        width: 300px;
      }
      
      .date {
        color: #888;
        font-size: 0.75em;
      }
    </style>
  </head>
  <body>
    <h1>Chat Dev Tool</h1>

    <p id="status">Loading...</p>

    <h2>Rooms</h2>

    <div id="rooms"></div>

    <h2>Current Room</h2>

    <div id="current-room">
      <p class="status">
        Not in any room.
      </p>

      <form id="join-form">
        <dl>
          <dt>Room</dt>
          <dd><input id="room-key" type="text" /></dd>

          <dt>Username</dt>
          <dd><input id="client-username" type="text" disabled="disabled" /></dd>
        </dl>

        <p>
          <input type="submit" value="Join" />
        </p>
      </form>
    </div>

    <h2>Clients</h2>

    <div id="clients"></div>

    <h2>Messages</h2>

    <div id="messages" class="message-box"></div>

    <form id="send-message-form">
      Say something:
      <input id="message" type="text" disabled="disabled" />
      <input type="submit" value="Send" disabled="disabled" />
    </form>
  </body>
</html>