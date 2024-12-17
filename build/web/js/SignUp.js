
async function SignUp() {

    const user_data = {
        first_name: document.getElementById("firstname").value,
        last_name: document.getElementById("lastname").value,
        email: document.getElementById("email").value,
        password: document.getElementById("password").value
    };

    const response = await fetch(
            "SignUp",
            {
                method: "POST",
                body: JSON.stringify(user_data),
                headers: {
                    "Content-Type": "application/json"
                }


            }
    );
    
     if(response.ok){
         const json = await response.json();
         
         const popup = Notification();
       
       if(json.success){
            window.location = "verification.html";
                            popup.success({
                                
                                message:json.content
                            });

       }else{
           
//           document.getElementById("message").innerHTML = json.content;
                            popup.error({
                                
                                message:json.content
                            });
           
       }
        
        
        
        
    }else{
//        document.getElementById("message").innerHTML = "Please Tyr again";
        
                              popup.error({
                                
                                message:json.content
                            });
    }


}

