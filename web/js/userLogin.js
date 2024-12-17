async function SignIn(){
    
    const jsObject = {
        
       email : document.getElementById("email").value,
       password : document.getElementById("password").value
    };
    
    
    const response = await fetch(
            "SignIn",
                    {
                        method : "POST",
                        body : JSON.stringify(jsObject),
                        header : {
                            "Content-Type":"application/json"
                        }
                    }
            );
    
    
    
    
    if(response.ok){
       const Json =await response.json();
        
        const popup = Notification();
        
        if(Json.success){
            
            window.location = "index.html";
            
        }else{
            
               if(Json.content=="Unverified") {
                   window.location = "verification.html";
                   
               }else{
                          popup.error({
                                
                                message:Json.content
                            });
                   
                   
               }       
            
        }
        
        
        
    }else{
        
                       popup.error({
                                
                                message:"Please Try again"
                            });
        
    }
    
    
}






async function forgotPassword(){
    
    const jsObject = {
        
       email : document.getElementById("email").value
    };
    
     const response =await fetch(
            "ForgotPassword",
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
                     document.getElementById("emailEmpty").innerHTML = "";
                     document.getElementById("emailEmpty").className ="";
                    document.getElementById("verificationCode").disabled = false;
             
         }else{
             
                 if(json.content == "EnterEmail"){
                     document.getElementById("emailEmpty").innerHTML = "Please Enter Your Email in the Email Field";
                     document.getElementById("emailEmpty").className ="text-danger";
                     document.getElementById("verificationCode").disabled = true;
                     
                 }else{
                     popup.error({
                                
                                message:json.content
                            });
                     
                     
                 }           
             
         }
        
    }else{
        
                          popup.error({
                                
                                message:"Please Try again"
                            });
        
        
    }
    
    
}






async function valideVerification(){
   
   const jsObject = {
       verification : document.getElementById("verificationCode").value
   };
   
   const response =await fetch(
            "VerifyVerification",
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
             window.location ="user-reset-password.html";
             
         }else{
              document.getElementById("emailEmpty").innerHTML = json.content;
                     document.getElementById("emailEmpty").className ="text-danger";
                    
             
         }
        
        
    }else{
        
                         popup.error({
                                
                                message:"Please Try again"
                            });
        
    }
   
   
   
}