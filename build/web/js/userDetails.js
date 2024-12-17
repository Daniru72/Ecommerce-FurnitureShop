var districtsList;
var cityList;

async function LoadUserDetails(){
    
    const response = await fetch(
            "LoadUserDetails"
            );
    
    if(response.ok){
        const json = await response.json();
        
        if(json.success){
            
             const user = json.content;

             console.log(user);
             
             document.getElementById("fname").value = user.first_name;
             document.getElementById("lname").value = user.last_name;
             document.getElementById("email").value = user.email;
             
             if(json.AddressFound){
                 
                const address = json.address;
             console.log(address);
             

             const city = json.city;
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
                 
//             address not found

               
                 
                 cityList = json.cityList;
                 districtsList = json.districtsList;
                 const provinceList = json.provinceList;
                 
                loadSelect("provinceSelect",provinceList,"name"); 
                 
                 
             }
             
            
             
            
        }else{
            
            console.log("not success");
            
        }
        
    }
    
    
}






function loadSelect(selectTagId, list, property){
    
     const selectTag = document.getElementById(selectTagId);
         list.forEach(item => {
             let optionTag = document.createElement("option");
             optionTag.value = item.id;
             optionTag.innerHTML = item[property];
            
             selectTag.appendChild(optionTag);
         });
             
}




function updateDistricts(){
    
    
   let districteSelectTag = document.getElementById("districteSelect");
   districteSelectTag.length = 1; //palaveni option tag eka ithuru vii anith okkoma option tags tika remove kari
   let provinceSelectId = document.getElementById("provinceSelect").value;
    
    districtsList.forEach(model =>{
        if(model.province.id == provinceSelectId){
            let optionTag = document.createElement("option");
            optionTag.value = model.id;
            optionTag.innerHTML = model.name;
            districteSelectTag.appendChild(optionTag);
        }
    });
    
}





function updateCities(){
    
    
   let citySelectTag = document.getElementById("citySelect");
   citySelectTag.length = 1; //palaveni option tag eka ithuru vii anith okkoma option tags tika remove kari
   let districteSelectId = document.getElementById("districteSelect").value;
    
    cityList.forEach(model =>{
        if(model.district.id == districteSelectId){
            let optionTag = document.createElement("option");
            optionTag.value = model.id;
            optionTag.innerHTML = model.name;
            citySelectTag.appendChild(optionTag);
        }
    });
    
}






async  function insertAddress(){
    
    const email = document.getElementById("email").value;
    const mobile = document.getElementById("mobile").value;
    const address1 = document.getElementById("inputAddress").value;
    const address2 = document.getElementById("inputAddress2").value;

const provinceValue = document.getElementById('provinceSelect').value;
//console.log('Selected Province:', provinceValue);
  

const districtValue = document.getElementById('districteSelect').value;
//console.log('Selected District:', districtValue);

const cityValue = document.getElementById('citySelect').value;
//console.log('Selected City:', cityValue);
//  alert(cityValue);

    const postal = document.getElementById("inputZip").value;
    
    
    const addressData={
        email:email,
        mobile: mobile,
        address1:address1,
        address2:address2,
        provinceValue:provinceValue,
        districtValue:districtValue,
        cityValue:cityValue,
        postal:postal
    };
    
    
    const response =await fetch("InsertAddress",
    
    {
        method:"POST",
        body:JSON.stringify(addressData),
        headers:{
            "Content-Type": "application/json"
        }
    }
    
    
    );
    
    
    if(response.ok){
        const json =await response.json();
         const popup = Notification();
         
        if(json.success){
            
             popup.success({
                                
                                message:json.message
                            });
            
            
        }else{
              popup.success({
                                
                                message:json.message
                            });
            
        }
        
        
    }else{
        
         popup.success({
                                
                                message:"request error"
                            });
        
    }
    
    
}