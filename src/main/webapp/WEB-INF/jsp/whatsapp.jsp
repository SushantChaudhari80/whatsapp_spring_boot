<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>WhatsApp Integration</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        .status {
            font-weight: bold;
            font-size: 18px;
            margin-bottom: 20px;
        }
        #qrImage {
            max-width: 300px;
            margin: 20px 0;
        }
        .form-group {
            margin-bottom: 15px;
        }
        .btn {
            padding: 8px 16px;
            background-color: #0d6efd;
            color: white;
            border: none;
            cursor: pointer;
        }
        .btn:disabled {
            background-color: #aaa;
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom:40px;
        }
        .header .title {
            font-size: 20px;
            font-weight: 600;
        }
        .header .upgrade-btn {
            background-color: #0061ff;
            color: white;
            padding: 8px 16px;
            border: none;
            border-radius: 20px;
            cursor: pointer;
        }
        .header .upgrade {
            display: flex;
            align-items: center;
            gap: 10px;
        }
        .header .profile-icon {
            font-size: 22px;
            color: #333;
            cursor: pointer;
            margin-left: 10px;
        }
    </style>
</head>
<body>
    <div class="header">
        <div class="title">WhatsApp Account Manager</div>
        <div class="upgrade">
            <span>Expire date: NULL</span>
            <button class="upgrade-btn">Upgrade</button>
            <div class="profile-icon" onclick="showUserDetails()"><i class="bi bi-person-circle"></i></div>
        </div>
    </div>

    <div id="whatsappStatusContainer">
        <div class="status">Checking WhatsApp status...</div>
        <img id="qrImage" src="" style="display:none;" />
    </div>

    <div id="send_message_container">
        <h3>Send WhatsApp Message</h3>
        <div class="form-group">
            <label>Group ID:</label>
            <input type="text" id="groupId" placeholder="Enter group ID" />
        </div>
        <div class="form-group">
            <label>Message:</label>
            <textarea id="messageText" placeholder="Enter your message"></textarea>
        </div>
        <button id="sendMsgBtn" class="btn">Send Message</button>
    </div>
	<div style="margin-bottom: 20px;">
	    <button id="resetSessionBtn" class="btn">🔄 Reset Session</button>
	    <button id="downloadContactsBtn" class="btn" style="background-color: green;">📥 Download Contacts</button>
	</div>
	<jsp:include page="url.jsp" />
	<script>
	    // ✅ Reset Session
	    $('#resetSessionBtn').click(function () {
	        if (!confirm('Are you sure you want to reset the WhatsApp session?')) return;

	        $.ajax({
	            url: prod_url +'/api/whatsapp/reset/',
	            method: 'GET',
	            success: function (res) {
	                alert('✅ ' + (res.message || 'Session reset successfully.'));
	                checkWhatsappStatus();
	            },
	            error: function () {
	                alert('❌ Failed to reset session.');
	            }
	        });
	    });

	    // ✅ Download Contacts
	    $('#downloadContactsBtn').click(function () {
	        // This will trigger a file download
	        window.location.href = prod_url +'/api/whatsapp/downloadContacts/';
	    });
	
    function checkWhatsappStatus() {
        $.ajax({
            url: prod_url +'/api/whatsapp/status/',
            method: 'GET',
            success: function (res) {
                if (res.status === 'ready') {
                    $('#send_message_container').show();
                    $('.status').text('✅ WhatsApp is connected.');
                    $('#qrImage').hide();
                } else if (res.status === 'not_ready') {
                    $('#send_message_container').hide();
                    $('.status').text('📱 Scan this QR code to connect your WhatsApp:');
                    $('#qrImage').attr('src', res.qr_base64).show();
                } else {
                    $('#send_message_container').hide();
                    $('.status').text('⏳ WhatsApp is initializing...');
                    $('#qrImage').hide();
                }
            },
            error: function () {
                $('#send_message_container').hide();
                $('.status').text('❌ Failed to check WhatsApp status.');
            }
        });
    }

    $('#sendMsgBtn').click(function () {
        const groupId = $('#groupId').val();
        const message = $('#messageText').val();

        if (!groupId || !message) {
            alert('Please fill out both fields.');
            return;
        }

        $.ajax({
            url: prod_url +'/api/whatsapp/send',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ groupId, message }),
            success: function (res) {
                if (res.success) {
                    alert(res.message);
                } else {
                    alert('❌ Failed to send: ' + res.error);
                }
            },
            error: function (err) {
                try {
                    const errorResponse = JSON.parse(err.responseText);
                    alert(errorResponse.message || '❌ An unknown error occurred.');
                } catch (e) {
                    alert('❌ Unexpected error occurred while sending the message.');
                    console.error('Parsing error:', e, 'Original response:', err.responseText);
                }
            }
        });
    });

    $(document).ready(function () {
        checkWhatsappStatus();
        setInterval(checkWhatsappStatus, 60000); // refresh every 1 minute
    });
</script>



</body>
</html>
