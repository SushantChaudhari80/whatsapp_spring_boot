<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>WhatsApp Dashboard</title>
  <link rel="icon" type="image/png" href="/public/logo_img.png">
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
  <!-- Bootstrap CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

  <!-- Bootstrap JS Bundle -->
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>


  <!-- jQuery -->
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  <jsp:include page="url.jsp" />
  <script>
	$(document).ready(function() {
		
		$.ajax({
		  url: prod_url + '/api/whatsapp/user/details',
		  method: 'GET',
		  success: function (user) {
		    console.log('User details:', user);

		    // Update upgrade text
		    $('.upgrade span').text('Expire date: ' + (user.planEndDate || 'N/A'));

		    // Populate modal fields
		    $('#modalFullName').text(user.fullName || 'N/A');
		    $('#modalEmail').text(user.email || 'N/A');
		    $('#modalMobile').text(user.mobile || 'N/A');
		    $('#modalPlanType').text(user.planType || 'N/A');
		    $('#modalPlanEndDate').text(user.planEndDate || 'N/A');

		    // Show user profile modal
		   
		  },
		  error: function (xhr) {
		    console.error('Error:', xhr.responseText);
		    alert("Invalid Session, please Login.");
		    window.location.href = '/login'; // Optional redirect
		  }
		});

		
		$.ajax({
		      url: prod_url +'/api/whatsapp/validate/session',
		      method: 'GET',
		      success: function(response) {
               if(response === "Invalid Session"){
				 alert("Invalid Session ,please Login.");
				 window.location.href = "/login";
			   }
		      },
		      error: function(xhr) {
		        console.error('Error:', xhr.responseText);
		        alert("Invalid Session ,please Login.");
		      }
		    });
			
			$.ajax({
			     url: '/api/stats/summary',
			     method: 'GET',
			     dataType: 'json',
			     success: function (response) {
			       // Example: { total_count: 87, current_month: "August", current_month_count: 12 }
                
				   const limit = response.count; 
				   
			       $('#currentMonth').text(response.current_month);
			       $('#currentMonthCount').text(response.current_month_count+'/'+limit);
			       $('#totalCount').text(response.total_count);
				   
				  
				   
				   const currentCount = response.current_month_count;
				   const percent = Math.min(100, Math.round((currentCount / limit) * 100));
                      // Update text and progress bar
				  $('#percentText').text(percent + '%');
				  $('#progressBar').css('width', percent + '%');
			
				      const count = response.total_count;
				       
				        const dashArray = percent + ", 100";
				        $('.circle').attr('stroke-dasharray', dashArray);
				        $('.percentage').text(percent + "%");	
						
						
						// Reset previous color classes
						      $('.circle').removeClass('red yellow green');

						      // Apply new color class based on percent
						      if (percent < 30) {
						        $('.circle').addClass('green');
						      } else if (percent < 70) {
						        $('.circle').addClass('yellow');
						      } else {
						        $('.circle').addClass('red');
						      }		  	  
				  
			     },
			     error: function (xhr, status, error) {
			       $('#currentMonth').text("Failed to load");
			       $('#currentMonthCount, #totalCount').text("N/A");
			       console.error("❌ Error fetching summary:", error);
			     }
			   });
			
			
			$('#whatsappIcon').click(function () {
			    $('.nav-icon').removeClass('active');
			    $(this).addClass('active');
			    $('#mainContent').load('/whatsapp');  // Not .jsp!
			});
			
			$('#home_logo').click(function () {
				$('.nav-icon').removeClass('active');
				window.location.href = window.location.href;
			});
		
			$('#logout-button').click(function () {
			    window.location.href = "/login";
			});
	
	});
	
	function showUserDetails(){
				 $('#userProfileModal').modal('show');
	}
  </script>
 
  <style>
    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
      font-family: 'Inter', sans-serif;
    }
    body {
      background-color: #f8f9fd;
      display: flex;
    }
    .sidebar {
      width: 70px;
      background-color: #fff;
      height: 100vh;
      box-shadow: 2px 0 5px rgba(0,0,0,0.07);
      padding-top: 20px;
    }
    .sidebar img {
      width: 35px;
      display: block;
      margin: 0 auto 20px;
    }
    .sidebar .nav-icon {
      font-size: 20px;
      color: #37F011;
      margin: 20px 0;
      text-align: center;
      cursor: pointer;
    }
    .main {
      flex: 1;
      padding: 20px 40px;
    }
    .header {
      display: flex;
      justify-content: space-between;
      align-items: center;
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
	  gap: 10px; /* space between items */
	}

	.header .profile-icon {
	  font-size: 22px;
	  color: #333;
	  cursor: pointer;
	  margin-left: 10px;
	}
    .report-section {
      margin-top: 40px;
    }
    .report-cards {
      display: flex;
      gap: 20px;
    }
    .card {
      flex: 1;
      background-color: white;
      border-radius: 10px;
      padding: 20px;
      box-shadow: 0 2px 8px rgba(0,0,0,0.05);
    }
    .card h3 {
      font-size: 16px;
      margin-bottom: 10px;
    }
    .card .count {
      font-size: 24px;
      font-weight: bold;
      color: #00ba94;
    }
    .card .bar-bg {
      background: #e5e7eb;
      height: 6px;
      border-radius: 10px;
      overflow: hidden;
      margin-top: 8px;
    }
    .card .bar-fill {
      background: #00ba94;
      height: 100%;
      width: 1%;
    }
    .progress-circle {
      width: 100px;
      height: 100px;
      border-radius: 50%;
      background: #d4f4d4;
      margin: 0 auto;
    }
    .whatsapp-btn {
      position: fixed;
      bottom: 20px;
      right: 20px;
      background-color: #25D366;
      color: white;
      border-radius: 50%;
      width: 50px;
      height: 50px;
      display: flex;
      justify-content: center;
      align-items: center;
      font-size: 24px;
      cursor: pointer;
      box-shadow: 0 4px 10px rgba(0,0,0,0.2);
    }
	.nav-icon {
	  font-size: 20px;
	  margin: 10px;
	  cursor: pointer;
	}
	.nav-icon:hover {
	  color: #0d6efd; /* Bootstrap primary color */
	}
	.nav-icon.active {
	  color: #0d6efd;
	  font-weight: bold;
	}

  </style>
  <style>
  .circular-chart {
    display: block;
    margin: 0 auto;
    max-width: 100px;
    max-height: 100px;
  }

  .circle-bg {
    fill: none;
    stroke: #eee;
    stroke-width: 3.8;
  }

  
  .circle {
    fill: none;
    stroke-width: 2.8;
    stroke-linecap: round;
    transition: stroke-dasharray 1s ease, stroke 0.5s ease;
    transform: rotate(-90deg);
    transform-origin: center;
  }
  
  .circle.red {
    stroke: #f44336;
  }
  .circle.yellow {
    stroke: #ff9800;
  }
  .circle.green {
    stroke: #4caf50;
  }

  .percentage {
    fill: #666;
    font-family: sans-serif;
    font-size: 0.5em;
    text-anchor: middle;
  }
  </style>

</head>
<body>
  <div class="sidebar">
    <img id="home_logo" src="/public/logo_img.png" alt="logo" />
    <div class="nav-icon" id="whatsappIcon"><i class="bi bi-whatsapp"></i></div>  
    <div class="nav-icon"><i class="bi bi-share-fill"></i></div> 
    <div class="nav-icon"><i class="bi bi-folder2-open"></i></div> 
    <div class="nav-icon"><i class="bi bi-tools"></i></div>  
  </div>

  <div class="main" id="mainContent">
    <div class="header">
      <div class="title">WhatsApp report</div>
	  <div class="upgrade">
	    <span>Expire date: NULL</span>
	    <button class="upgrade-btn">Upgrade</button>
	    <div class="profile-icon" onclick="showUserDetails()"><i class="bi bi-person-circle"></i></div> 
	  </div>
    </div>

    <div class="report-section">
      <div class="report-cards">
        <div class="card">
          <h3>Message by month</h3>
          <p>Limit messages by month</p>
		  <p id="currentMonth">Loading..</p>
          <div id="currentMonthCount" class="count"></div>
          <!--<div class="bar-bg">
            <div class="bar-fill"></div>
          </div>
          <div style="margin-top: 8px;">Percent: 1%</div>-->
		  <div style="margin-top: 8px;">Percent: <span id="percentText">0%</span></div>

		  <div style="width: 100%; background-color: #ddd; height: 20px; border-radius: 5px; margin-top: 4px;">
		    <div id="progressBar" style="width: 0%; height: 100%; background-color: #4CAF50; border-radius: 5px;"></div>
		  </div>
        </div>


		<div class="card">
		  <h3>Total message sent</h3>
		  <div class="progress-circle" data-percent="0">
		    <svg viewBox="0 0 36 36" class="circular-chart">
		      <path class="circle-bg"
		            d="M18 2.0845
		               a 15.9155 15.9155 0 0 1 0 31.831
		               a 15.9155 15.9155 0 0 1 0 -31.831" />
		      <path class="circle"
		            stroke-dasharray="0, 100"
		            d="M18 2.0845
		               a 15.9155 15.9155 0 0 1 0 31.831
		               a 15.9155 15.9155 0 0 1 0 -31.831" />
		      <text x="18" y="20.35" class="percentage">0%</text>
		    </svg>
		  </div>
		  <div id="totalCount" class="count"></div>
		  <p>Messages</p>
		</div>

		
      </div>
    </div>
  </div>

  <div class="whatsapp-btn">🖊️
  </div>
  <!-- User Profile Modal -->
  <div class="modal fade" id="userProfileModal" tabindex="-1" aria-labelledby="userProfileModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="userProfileModalLabel">User Profile</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <p><strong>Full Name:</strong> <span id="modalFullName"></span></p>
          <p><strong>Email:</strong> <span id="modalEmail"></span></p>
          <p><strong>Mobile:</strong> <span id="modalMobile"></span></p>
          <p><strong>Plan Type:</strong> <span id="modalPlanType"></span></p>
          <p><strong>Plan End Date:</strong> <span id="modalPlanEndDate"></span></p>
        </div>
        <div class="modal-footer">
			<button id="logout-button" type="button" class="btn btn-primary" >Lagout</button>	
          <!--<button type="button" class="btn btn-warning" data-bs-dismiss="modal">Close</button>-->
        </div>
      </div>
    </div>
  </div>

</body>
</html>
