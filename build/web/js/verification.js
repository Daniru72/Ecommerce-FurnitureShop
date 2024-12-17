

async function verifiAccount(){
    
    const jsObj = {
        
       verification: document.getElementById("verification").value
    };
    
    
   const respopnse = await fetch (
           "Verification",
                        {
                            method : "POST",
                            body : JSON.stringify(jsObj),
                            headers:{
                                "Content-Type":"application/json"
                            }
                        }
            );
   
   
   if(respopnse.ok){
       
      const json =await  respopnse.json();
      
      const popup = Notification();
      
      if(json.success){
          window.location = "index.html";
          
      }else{
          
          if(json.content == "Unverified"){
               window.location = "verification.html";
              
          }else{
//              document.getElementById("message").innerHTML = json.content;

                    popup.error({
                                
                                message:json.content
                            });
              
          }
          
          
      }
       
       
       
   }else{
//       document.getElementById("message").innerHTML = "Please Tyr again";
                    popup.error({
                                
                                message:"Please Try again"
                            });
       
   }
    
}


