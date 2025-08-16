<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>WhatsApp Platform - Login</title>
  <link rel="icon" type="image/png" href="/public/logo_img.png">
  <!-- jQuery -->
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  <!-- Spinner Styles -->
      <style>
          .spinner-container {
              display: none;
              position: fixed;
              top: 0; left: 0;
              width: 100%; height: 100%;
              background: rgba(0, 0, 0, 0.5);
              backdrop-filter: blur(4px);
              z-index: 999;
              display: flex;
              justify-content: center;
              align-items: center;
          }

          .spinner {
              border: 6px solid #f3f3f3;
              border-top: 6px solid #3498db;
              border-radius: 50%;
              width: 40px;
              height: 40px;
              animation: spin 1s linear infinite;
          }

          @keyframes spin {
              0% { transform: rotate(0deg); }
              100% { transform: rotate(360deg); }
          }
      </style>
  <style>
    body {
      font-family: Arial, sans-serif;
      background: #fff;
      margin: 0;
      padding: 0;
    }
    .container {
      min-height: 100vh;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      background: #fff;
    }
    #login-form {
      background: #fff;
      padding: 1.2rem 1.5rem;
      border-radius: 8px;
      box-shadow: 0 8px 32px 0 rgba(3, 5, 26, 0.4);
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 240px;
    }
    #login-form h2 {
      margin-bottom: 0.7rem;
      color: #222;
      font-size: 1.1rem;
    }
    #login-form input {
      margin: 0.3rem 0;
      padding: 0.5rem;
      width: 100%;
      border-radius: 4px;
      border: 1px solid #ccc;
      font-size: 0.95rem;
    }
    #login-form button {
      margin-top: 0.7rem;
      padding: 0.5rem;
      width: 100%;
      background: #007bff;
      color: #fff;
      border: none;
      border-radius: 4px;
      font-size: 0.95rem;
      cursor: pointer;
      transition: background 0.2s;
    }
    #login-form button:hover {
      background: #0056b3;
    }
    .form-msg {
      margin-top: 0.3rem;
      color: #d9534f;
      font-size: 0.85rem;
      min-height: 1em;
    }
    .copyright {
      color: #222;
      text-align: center;
      margin-top: 1.2rem;
      font-size: 0.9rem;
      letter-spacing: 1px;
    }
  </style>
  <jsp:include page="url.jsp" />
    <script>
		$(document).ready(function() {
			
			$.ajax({
			      url: prod_url +'/api/whatsapp/reset/session',
			      method: 'POST',
			      success: function() {
			        console.log('Session has been reset.')			        
			      },
			      error: function(xhr) {
			        console.error('Error resetting session:', xhr.responseText);
			        alert('Failed to reset session.');
			      }
			    });
			
			
		   $('.spinner-container').hide();
		   
		   $('#login-form').on('submit', function(e) {
		           e.preventDefault(); // prevent default form submission

		           const username = $('#login-username').val();
		           const password = $('#login-password').val();

		           $('.spinner-container').show();

		           $.ajax({
					url: prod_url +'/api/whatsapp/login',
					    type: 'POST',
					    contentType: 'application/json',
					    data: JSON.stringify({ username: username, password: password }),
		               success: function(response) {
		                   $('.spinner-container').hide();
		                   if (response === "Success") {
		                       window.location.href = prod_url +"/home";
		                   } else {
		                       $('#login-msg').text(response).css("color", "red");
		                   }
		               },
		               error: function(xhr, status, error) {
		                   $('.spinner-container').hide();
		                   $('#login-msg').text("Login failed. Please try again.").css("color", "red");
		                   console.error("Error:", error);
		               }
		           });
		       });
		});
	
	</script>
</head>
<body>
	<!-- Spinner -->
	<div class="spinner-container">
	    <div class="spinner"></div>
	</div>
  <div class="container">
    <form id="login-form">
      <h2>Login</h2>
      <input type="text" id="login-username" placeholder="Username" required />
      <input type="password" id="login-password" placeholder="Password" required />
      <button type="submit">Login</button>
      <div class="form-msg" id="login-msg"></div>
    </form>
    <div class="copyright">
      &copy; Tanvi Software Solution
    </div>
  </div>
  
</body>
</html>