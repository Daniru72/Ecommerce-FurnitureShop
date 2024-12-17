// Payment completed. It can be a successful failure.
    payhere.onCompleted = function onCompleted(orderId) {
        const popup = Notification();
                   popup.success({
                            message:"Order Placed. Thank You"
                            });
                            window.location = "index.html";
    };

    // Payment window closed
    payhere.onDismissed = function onDismissed() {
        // Note: Prompt user to pay again or show an error page
        console.log("Payment dismissed");
    };

    // Error occurred
    payhere.onError = function onError(error) {
        // Note: show an error page
        console.log("Error:"  + error);
    };




async function loadData(){
    
    const response = await fetch(
            "LoadCheckout" 
            );
    
    if(response.ok){
           const popup = Notification();
        const json = await response.json();
        console.log(json);
        
        var city;
        if(json.success){
            
            
            
            if(json.foundAddress){
                
                const address = json.address;
           console.log(address);
           
            city = json.city;
             console.log(city);
            
             document.getElementById("mobile").value = address.mobile;
             document.getElementById("inputAddress").value = address.line1;
             document.getElementById("inputAddress2").value = address.line2;
            
             document.getElementById("cityOption").value = city.id;
            document.getElementById("cityOption").innerHTML = city.name;
            
              document.getElementById("districteOption").value = city.district.id;
            document.getElementById("districteOption").innerHTML = city.district.name;
            
              document.getElementById("provinceOption").value = city.district.province.id;
            document.getElementById("provinceOption").innerHTML = city.district.province.name;
            
            document.getElementById("inputZip").value = address.postal_code; 
                
                
                
            }else{
                
                         popup.error({
                                
                                message:"Please Fill Your Address Details in your Account"
                            }); 
                
            }
            
             const cartList = json.cartList;
             console.log(cartList);
             
            //load cart details
            let item_body = document.getElementById("item-body");
            let st_item = document.getElementById("st-item");
            let productList_Subtotal = document.getElementById("productListSubtotal");
            let product_Shipping = document.getElementById("productShipping");
            let final_Total = document.getElementById("finalTotal");
            item_body.innerHTML = "";
            
            let subTotal = 0;
            
            cartList.forEach(item =>{
                      let st_item_clone =  st_item.cloneNode(true);
                      st_item_clone.querySelector("#product-name").innerHTML = item.product.title;
                      st_item_clone.querySelector("#product-qty").innerHTML ="X "+item.qty;
                      
                  let item_sub_total = item.product.price * item.qty;
                  subTotal += item_sub_total;
                  
                  st_item_clone.querySelector("#product-subtotal").innerHTML = new Intl.NumberFormat(
                                                            "en-US",
                                                        {
                                                         minimumFractionDigits: 2
                                                        }
                                                      ).format(item_sub_total);
                      
                      item_body.appendChild(st_item_clone);
            });
            
            productList_Subtotal.querySelector("#totalsubTotal").innerHTML = new Intl.NumberFormat(
                                                            "en-US",
                                                        {
                                                         minimumFractionDigits: 2
                                                        }
                                                      ).format(subTotal);
                                              
             item_body.appendChild(productList_Subtotal);
             
             
             //update Shipping charges
             let item_count = cartList.length;
             let shipping_amount = 0;
             
             if(city.district.province.id== 1){
                 //Western Province
                  shipping_amount = item_count*1000;
             }else{
               //  Out of Western Province
                 shipping_amount = item_count*1500;
                 
             }
             
             
             product_Shipping.querySelector("#shippingamount").innerHTML = new Intl.NumberFormat(
                                                            "en-US",
                                                        {
                                                         minimumFractionDigits: 2
                                                        }
                                                      ).format(shipping_amount);
                    
              item_body.appendChild( product_Shipping);
              
              
              //update Total
              let total = subTotal + shipping_amount;
              final_Total.querySelector("#totalAmount").innerHTML = new Intl.NumberFormat(
                                                            "en-US",
                                                        {
                                                         minimumFractionDigits: 2
                                                        }
                                                      ).format(total);
                                              
              item_body.appendChild( final_Total);
              
              
        }else{
            
            window.location = "user-login.html";
            
        }
        
    }
    
}




async function checkout(){
    
    //get address data
   let mobile = document.getElementById("mobile");
   let address1 = document.getElementById("inputAddress");
   let address2 = document.getElementById("inputAddress2");
   let province = document.getElementById("provinceSelect");
   let districte = document.getElementById("districteSelect");
   let city = document.getElementById("citySelect");
   let zipcode = document.getElementById("inputZip");
   
   //request data
   const data = {
       mobile : mobile.value,
       address1 : address1.value,
       address2 : address2.value,
       province : province.value,
       districte : districte.value,
       city : city.value,
       zipcode : zipcode.value
   };
  
    const response = await fetch(
            "CheckOut",
            {
                method : "POST",
                body : JSON.stringify(data),
                headers : {
                    "Content-Type":"application/json"
                }
            }
            );
    
    
    
    
    if(response.ok){
         const popup = Notification();
        const json = await response.json();
        
        if(json.success){
            
            //Start PayHere Payment
            console.log(json.payhereJson);
            payhere.startPayment(json.payhereJson);
            
           
            
                       
            
        }else{
                             popup.error({
                                
                                message:json.message
                            });
            
            
        }
        
        
    }else{
        
          popup.error({
                                
                message:"Try again"
               });
        
    }
    
}



