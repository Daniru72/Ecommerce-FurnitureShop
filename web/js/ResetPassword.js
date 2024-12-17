
async function changePassword(){
    
    const jsObject = {
        
       newPassword : document.getElementById("newPassword").value,
       confirmPassword : document.getElementById("confirmPassword").value
    };
    
    
    const response =await fetch(
            "ResetPassword",
    {
         method: "POST",
                body: JSON.stringify(jsObject),
                headers: {
                    "Content-Type": "application/json"
                }
    }
             
    );
    
    
    if(response.ok){
         const json =await response.json();
         const popup = Notification();
         
         if(json.success){
                            popup.success({
                                  
                                message:json.content
                            });
                  window.location = "user-login.html";
             
         }else{
             
              popup.error({
                                
                                message:json.content
                            });
         }
        
    }else{
        
                          popup.error({
                                
                                message:"Please Try again"
                            });
    }
    
    
    
    
}


