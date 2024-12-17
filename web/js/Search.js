

async  function loadData(){
    
    const response = await fetch(
            "LoadSearchData" 
            );
    
    if(response.ok){
        const json = await response.json();
        console.log(json);
        
        //load category list

       let category_option = document.getElementById("category-options");
       let category_li = document.getElementById("category-li");
       category_option.innerHTML = "";
       
        let categoryList = json.categoryList;
        categoryList.forEach(category =>{
           let category_li_clone = category_li.cloneNode(true);
            category_li_clone.querySelector("#category-a").innerHTML = category.name;
               
            category_option.appendChild(category_li_clone);
            
        });
        
        //Template js
         const categoryOptions = document.querySelectorAll('#category-options li');
        categoryOptions.forEach(option => {
            option.addEventListener('click', function() {
                categoryOptions.forEach(opt => opt.classList.remove('current-cat'));
                this.classList.add('current-cat');
            });
        });
        
        //load category list
        
        
        
        //load condition list

       let condition_option = document.getElementById("condition-options");
       let condition_li = document.getElementById("condition-li");
       condition_option.innerHTML = "";
       
        let conditionList = json.conditionList;
        conditionList.forEach(condition =>{
           let condition_li_clone = condition_li.cloneNode(true);
            condition_li_clone.querySelector("#condition-a").innerHTML = condition.name;
            condition_option.appendChild(condition_li_clone);
        });
        
        
        
          const conditionOptions = document.querySelectorAll('#condition-options li');
        conditionOptions.forEach(option => {
            option.addEventListener('click', function() {
                conditionOptions.forEach(opt => opt.classList.remove('chosen'));
                this.classList.add('chosen');
            });
        });
        //load condition list
        
        
         //load material list

       let material_option = document.getElementById("material-options");
       let material_li = document.getElementById("material-li");
       material_option.innerHTML = "";
       
        let MaterialList = json.MaterialList;
        MaterialList.forEach(material =>{
           let material_li_clone = material_li.cloneNode(true);
            material_li_clone.querySelector("#material-a").innerHTML = material.value;
            material_option.appendChild(material_li_clone);
        });
        
         const storageOptions = document.querySelectorAll('#material-options li');
        storageOptions.forEach(option => {
            option.addEventListener('click', function() {
                storageOptions.forEach(opt => opt.classList.remove('chosen'));
                this.classList.add('chosen');
            });
        });
        
        //load material list
        
        
        
          //load color list

       let color_option = document.getElementById("color-options");
       let color_li = document.getElementById("color-li");
       color_option.innerHTML = "";
       
        let colorList = json.colorList;
        colorList.forEach(color =>{
           let color_li_clone = color_li.cloneNode(true);
            color_li_clone.querySelector("#color-name").innerHTML = color.name;
            
            
            color_li_clone.querySelector("#color-a").style.backgroundColor = color.name;
            color_li_clone.querySelector("#color-a").style.borderColor = color.name;
//            color_li_clone.querySelector("#color-name").style.backgroundColor = color.name;
//            color_li_clone.querySelector("#color-name").style.borderColor = color.name;
            color_option.appendChild(color_li_clone);
        });
        
        
          const colorOptions = document.querySelectorAll('#color-options li');
        colorOptions.forEach(option => {
            option.addEventListener('click', function() {
                colorOptions.forEach(opt => opt.classList.remove('chosen'));
                this.classList.add('chosen');
            });
        });
        
        //load color list
        
        
        updateProductView(json);
        
        
        
    }else{
        
        console.log("Invalide data");
        
    }
    
}



async function searchProducts(firstResult){
    
    
    
    //get search data
   let category_name = document.getElementById("category-options")
         .querySelector(".current-cat") 
           ?.querySelector("#category-a").innerHTML;
 
   let condition_name = document.getElementById("condition-options")
         .querySelector(".chosen")
         ?.querySelector("#condition-a").innerHTML;
 
  let color_name = document.getElementById("color-options")
         .querySelector(".chosen")
         ?.querySelector("#color-a").style.backgroundColor;
 
  let material_name = document.getElementById("material-options")
         .querySelector(".chosen")
         ?.querySelector("#material-a").innerHTML;
 
  

   let sort_text = document.getElementById("st-sort").value;
   //end get search data
 
   
    
   
    
     const popup = Notification();
     
    const data = {
        firstResult:firstResult,
        category_name: category_name,
        condition_name:condition_name,
        color_name: color_name,
        material_name: material_name,
        sort_text: sort_text
    };
    
    console.log(data);
      const response = await fetch(
            "SearchProducts",
            {
                method : "POST",
                body : JSON.stringify(data),
                headers : {
                    "Content-Type":"application/json"
                }
            }
            );
    
    
    
    
      if(response.ok){
        
        const json = await response.json();
        console.log(json);
        
        
        if(json.success){
            
            updateProductView(json);
           
            
             popup.success({
                     message:"Search Completed"
                            });
            
        }else{
            popup.error({
                 message:"Search Unsuccess"
                            });
            
        }
        
                       
        
        
        
    }else{
        
                          popup.error({
                                
                                message:"Please Try again"
                            });
        
    }
    
    
    
}




var st_product = document.getElementById("st-product");
var st_pagination_button = document.getElementById("st-pagination-button");

var currentPage = 0;

function updateProductView(json){
     //load Products
       
       let st_product_container = document.getElementById("st-product-container");
       
        st_product_container.innerHTML = "";
        
        json.ProductList.forEach(product =>{
            let st_product_clone = st_product.cloneNode(true);
            
            //update product data
            st_product_clone.querySelector("#st-product-a1").href = "product-detail.html?id="+product.id;
            st_product_clone.querySelector("#st-product-img1").src = "product-images/" + product.id + "/image1.jpg";
            st_product_clone.querySelector("#st-product-img2").src = "product-images/" + product.id + "/image2.jpg";
             st_product_clone.querySelector("#st-product-a2").href = "product-detail.html?id="+product.id;
             st_product_clone.querySelector("#st-product-title1").innerHTML = product.title;
             st_product_clone.querySelector("#st-product-condition").innerHTML = product.condition_id.name;
             st_product_clone.querySelector("#st-product-price1").innerHTML ="Rs."+ new Intl.NumberFormat(
                    "en-US",
                    {
                        minimumFractionDigits: 2
                    }
            ).format(product.price);
    
             st_product_clone.querySelector("#add-to-cart-Product").addEventListener(
                        "click",
                        (e) => {
                    addToCart(product.id, 1);
                    e.preventDefault();
                }
                );
    
             
            //update product data
            
            st_product_container.appendChild(st_product_clone);
        });
        
        //load Products
        
        
        //set Pagination
        
        let st_pagination_container = document.getElementById("st-pagination-container");
        

        st_pagination_container.innerHTML = "";
        
       
        
        let product_count = json.allProductCount;
        const product_per_page = 6;  
        
        let pages = Math.ceil(product_count/product_per_page);
        
        
        //Add previous button
         if(currentPage != 0){
             let st_pagination_button_clone_Prev = st_pagination_button.cloneNode(true);
 
            st_pagination_button_clone_Prev.innerHTML = "Prev";
             st_pagination_button_clone_Prev.addEventListener("click",e=>{
                currentPage--;
                searchProducts(currentPage * 6);
                
            }) ;
            st_pagination_container.appendChild(st_pagination_button_clone_Prev);
         }
        
        
        //add main button
        for(let i = 0;i < pages; i++){
            let st_pagination_button_clone = st_pagination_button.cloneNode(true);
           
            
            st_pagination_button_clone.innerHTML = i+1;
            st_pagination_button_clone.addEventListener("click",e=>{
                currentPage = i;
                searchProducts(i *6);
                
            }) ;
            
            if(i==currentPage){
                st_pagination_button_clone.className = "axil-btn btn-bg-secondary ml--10";
            }else{
                st_pagination_button_clone.className = "axil-btn btn-bg-primary  ml--10";
            }
            
            st_pagination_container.appendChild(st_pagination_button_clone);
            
        }
        
        
        //next button
        if(currentPage != (pages - 1)){
            let st_pagination_button_clone_Next = st_pagination_button.cloneNode(true);

            st_pagination_button_clone_Next.innerHTML = "Next";
            st_pagination_button_clone_Next.addEventListener("click",e=>{
                currentPage++;
                searchProducts(currentPage * 6);
                
            }) ;
            st_pagination_container.appendChild(st_pagination_button_clone_Next);
        }
         
        
       
        
    
}








async function addToCart(id, qty) {

    const response = await fetch(
            "AddToCart?id=" + id + "&qty=" + qty
            );

    

    if (response.ok) {
        const json =await response.json();
        const popup = Notification();

        if (json.success) {

            popup.success({

                message: json.content
            });

        } else {

            popup.error({

                message: json.content
            });

        }

    } else {

        popup.error({

            message: "Unable to Process Your Request"
        });

    }


}






function resetpage(){
    
    location.reload();
}