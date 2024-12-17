
var modelList;

async function loadFeatures(){
    
    const response = await fetch(
            "LoadFeatures"
            );
    
    
    if(response.ok){
        
        const json = await response.json();
         
         console.log(json);
       
         const categoryList = json.categoryList;
          modelList = json.modelList;
         const materialList = json.materialList;
         const conditionList = json.conditionList;
         const colorList = json.colorList;
         
        loadSelect("categorySelect",categoryList,"name");
//        loadSelect("modelSelect",modelList,"name");
        loadSelect("materialSelect",materialList,"value");
        loadSelect("conditionSelect",conditionList,"name");
        loadSelect("colorSelect",colorList,"name");
        
        
    }else{
        
        document.getElementById("message").innerHTML = "Please Tyr again";
        
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




function updateModels(){
    
    
   let modelSelectTag = document.getElementById("modelSelect");
   modelSelectTag.length = 1; //palaveni option tag eka ithuru vii anith okkoma option tags tika remove kari
   let SelectCategoryId = document.getElementById("categorySelect").value;
    
    modelList.forEach(model =>{
        if(model.category.id == SelectCategoryId){
            let optionTag = document.createElement("option");
            optionTag.value = model.id;
            optionTag.innerHTML = model.name;
            modelSelectTag.appendChild(optionTag);
        }
    });
    
}






async function productListening(){
    
      const categorySelectTag = document.getElementById("categorySelect");
  const modelSelectTag = document.getElementById("modelSelect");
  const titleTag = document.getElementById("title");
  const descriptionTag = document.getElementById("description");
  const materialSelectTag = document.getElementById("materialSelect");
  const colorSelectTag = document.getElementById("colorSelect");
  const conditionSelectTag = document.getElementById("conditionSelect");
  const priceTag = document.getElementById("price");
  const qtyTag = document.getElementById("qty");
  const image1Tag = document.getElementById("image1");
  const image2Tag = document.getElementById("image2");
  const image3Tag = document.getElementById("image3");
    
    
    const data = new FormData();
    data.append("categoryId",categorySelectTag.value);
    data.append("modelId",modelSelectTag.value);
    data.append("titleId",titleTag.value);
    data.append("descriptionId",descriptionTag.value);
    data.append("materialId",materialSelectTag.value);
    data.append("colorId",colorSelectTag.value);
    data.append("conditionId",conditionSelectTag.value);
    data.append("priceId",priceTag.value);
    data.append("qtyId",qtyTag.value);
    data.append("image1",image1Tag.files[0]);
    data.append("image2",image2Tag.files[0]);
    data.append("image3",image3Tag.files[0]);
    
    
     const response =await fetch(
            "ProductListing",
                {
                    method:"POST",
                    body: data,
                    
                }
                       
    );
    
    
    if(response.ok){
        
        const json = await response.json();
         
         const messageTag = document.getElementById("message");
         const popup = Notification();
        
         if(json.success){
            
                            // Clear form fields correctly according to their types
            categorySelectTag.value = 0;  // Set to the first option (often the placeholder)
            modelSelectTag.length = 0;
            titleTag.value = "";
            descriptionTag.value = "";
            materialSelectTag.value = 0;
            colorSelectTag.value = 0;
            conditionSelectTag.value = 0;
            priceTag.value = "";
            qtyTag.value = 1;
            image1Tag.value = null;
            image2Tag.value = null;
            image3Tag.value = null;
            
//            messageTag.innerHTML=json.content;
//            messageTag.className ="text-success";
            
                            popup.success({
                                
                                message:json.content
                            });
            
            
        }else{
            
//            messageTag.innerHTML=json.content;
            
                             
                                     popup.error({
                                
                                message:json.content
                            });
            
            
        }
        
    }else{
        
        document.getElementById("message").innerHTML = "Please Tyr again";
        document.getElementById("message").className ="text-danger";
        
    }
    
    
}