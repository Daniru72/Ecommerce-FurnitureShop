


async function CheckSignIn(){
    
    const response =await fetch(
            "CheckSignIn"
            );
    
    
    
    if(response.ok){
        
        const json =await response.json();
        

        
        
        if(json.success){
            //SignIn
            
            const user = json.content;
            console.log(user);
            
           let tagMain = document.getElementById("tag-main");
           
           let accountTag = document.getElementById("tag-sp");
           accountTag.remove();
           let newAccountTag = document.createElement("span");
           newAccountTag.innerHTML = "Hi "+user.first_name;
           newAccountTag.className = "font-weight-bold";
            tagMain.appendChild(newAccountTag);
           
           
           let quicklink = document.getElementById("quick-link");
           
           let signInQuickLinka1 = document.getElementById("signIn-quickLink-a1");
           signInQuickLinka1.href = "SignOut";
           
           let signInQuickLink = document.getElementById("signIn-quickLink");
           signInQuickLink.innerHTML = "Sign Out";
           
           let registerquickLink = document.getElementById("register-quickLink");
           registerquickLink.remove();
           
           
            
        }else{
            //not SignIn
            
            console.log("Not Signed In");
        }
        
        
       
        
       
        
        
         

        
    }
    
    
}



async function loadSelect(){
    
     const response =await fetch(
            "LoadQuickMenue"
            );
    
    if(response.ok){
        
        const json =await response.json();
        
        const list = json.categoryList;
        console.log(list);
        const modelList = json.modelList;
        console.log(modelList);
        
        
    let quickMenueMain = document.getElementById("quick-menue-main");
     
         list.forEach(item => {
             // Create a new `li` and `a` for each item
        const newListItem = document.createElement("li");
        newListItem.classList.add("item", "parent");

        const newAnchor = document.createElement("a");
        newAnchor.href = "#";  // Set href for the link
        newAnchor.classList.add("hasicon");
        newAnchor.title = item.name;
        newAnchor.innerHTML = `<span class="icon"></span> ${item.name}`;  // Add icon and category name

        // Append the newly created anchor to the `li`
        newListItem.appendChild(newAnchor);

        // Optionally, add a dropdown (if needed, adjust this for dynamic subcategories)
        const dropdown = document.createElement("div");
        dropdown.classList.add("dropdown-menu");
        
        
        //sub category
        
        const menuItemsDiv = document.createElement("div");
        menuItemsDiv.classList.add("menu-items");

        const subcategoryList = document.createElement("ul");
        
        // Iterate over the subcategories of the current category
        modelList.forEach(subcategory => {
            if(subcategory.category.id == item.id){
                
                
                
                
            const subcategoryItem = document.createElement("li");
            subcategoryItem.classList.add("item");

            const subcategoryAnchor = document.createElement("a");
            subcategoryAnchor.href = "#";  // Set href for subcategory link
            subcategoryAnchor.title = subcategory.name;
            subcategoryAnchor.innerHTML = subcategory.name;  // Add subcategory name

            // Append the subcategory anchor to the `li`
            subcategoryItem.appendChild(subcategoryAnchor);
            
            // Append the `li` to the subcategory `ul`
            subcategoryList.appendChild(subcategoryItem);
            
            }
            
        });
        
        
         // Append subcategory list to menu items div
        menuItemsDiv.appendChild(subcategoryList);
        dropdown.appendChild(menuItemsDiv);

        // Append the dropdown to the main list item
        newListItem.appendChild(dropdown);

        // Append the new list item to the main menu
        quickMenueMain.appendChild(newListItem);
         });
        
        
        
        
    }else{
        
        console.log("not sucess");
        
    }
    
    
    
             
}





//load Home Products


async  function loadHomeProduct(){
    
   const response = await fetch(
            "LoadQuickMenue" 
            );
    
    if(response.ok){
        const json = await response.json();
        console.log(json);
 
        let livingRoomProducts = document.getElementById("livingRoomProducts");
        document.getElementById("livingRoomProductsmain").innerHTML = "";
         
         
         json.productList.forEach(item =>{
              
             let livingRoomProductsClone =  livingRoomProducts.cloneNode(true);
             
                livingRoomProductsClone.querySelector("#product-a1").href = "product-detail.html?id=" + item.id;
                livingRoomProductsClone.querySelector("#product-image").src = "product-images/" + item.id + "/image1.jpg";
                livingRoomProductsClone.querySelector("#product-image2").src = "product-images/" + item.id + "/image2.jpg";
                 livingRoomProductsClone.querySelector("#product-image").style.width = "260px"; // Example width
                livingRoomProductsClone.querySelector("#product-image").style.height = "250px";
                 livingRoomProductsClone.querySelector("#product-image2").style.width = "260px"; // Example width
                livingRoomProductsClone.querySelector("#product-image2").style.height = "250px";
                livingRoomProductsClone.querySelector("#product-a2").href = "product-detail.html?id=" + item.id;
                livingRoomProductsClone.querySelector("#product-title").innerHTML = item.title;
                livingRoomProductsClone.querySelector("#product-condition").innerHTML = item.condition_id.name;
                livingRoomProductsClone.querySelector("#product-price").innerHTML = "Rs. " + new Intl.NumberFormat(
                        "en-US",
                        {
                            minimumFractionDigits: 2
                        }

                ).format(item.price);
        
                 livingRoomProductsClone.querySelector("#add-to-cart-Product").addEventListener(
                        "click",
                        (e) => {
                    addToCart(item.id, 1);
                    e.preventDefault();
                }
                );
        
        document.getElementById("livingRoomProductsmain").appendChild(livingRoomProductsClone);
        
        
              
         });
         
          document.getElementById("livingRoomProductsmain").className = "row";
         
         
         
         
         
 //load living room models
 
      let litag = document.getElementById("li-tag");   
        document.getElementById("ul-tag").innerHTML = "";

         
                 json.modelList.forEach(model=>{
                     if(model.category.name == "Living Room"){
                        
                 let litagClone =  litag.cloneNode(true);
                       
                       litagClone.querySelector("#a1").innerHTML = model.name;
                       litagClone.querySelector("#a1").href = "product-grid-sidebar-left.html?id=" + model.id;
                       litagClone.querySelector("#a1").className ="dark";
                         
                      
                          document.getElementById("ul-tag").appendChild(litagClone);
                     }
                    
                 });
                 
           
//load living room models
        
//kitchen room load products


        let kitchenRoomProducts = document.getElementById("kitchenRoomProduct");
        document.getElementById("kitchenRoomProductsmain").innerHTML = "";
        
        
         
         json.kitchenproductList.forEach(item =>{
              
             let kitchenRoomProductsClone =  kitchenRoomProducts.cloneNode(true);
             
                kitchenRoomProductsClone.querySelector("#kitchen-product-a1").href = "product-detail.html?id=" + item.id;
                kitchenRoomProductsClone.querySelector("#kitchen-product-img1").src = "product-images/" + item.id + "/image1.jpg";
                kitchenRoomProductsClone.querySelector("#kitchen-product-img2").src = "product-images/" + item.id + "/image2.jpg";
                kitchenRoomProductsClone.querySelector("#kitchen-product-img1").style.width = "260px"; // Example width
                kitchenRoomProductsClone.querySelector("#kitchen-product-img1").style.height = "250px"; // Example height
                kitchenRoomProductsClone.querySelector("#kitchen-product-img2").style.width = "260px"; // Example width
                kitchenRoomProductsClone.querySelector("#kitchen-product-img2").style.height = "250px"; // Example height
                kitchenRoomProductsClone.querySelector("#kitchen-product-a2").href = "product-detail.html?id=" + item.id;
                kitchenRoomProductsClone.querySelector("#kitchen-product-title").innerHTML = item.title;
                kitchenRoomProductsClone.querySelector("#kitchen-product-condition").innerHTML = item.condition_id.name;
                kitchenRoomProductsClone.querySelector("#kitchen-product-price").innerHTML = "Rs. " + new Intl.NumberFormat(
                        "en-US",
                        {
                            minimumFractionDigits: 2
                        }

                ).format(item.price);
        
                 kitchenRoomProductsClone.querySelector("#add-to-cart-kitchenProduct").addEventListener(
                        "click",
                        (e) => {
                    addToCart(item.id, 1);
                    e.preventDefault();
                }
                );
        
        document.getElementById("kitchenRoomProductsmain").appendChild(kitchenRoomProductsClone);
        
        
              
         });
         
         document.getElementById("kitchenRoomProductsmain").className = "row";
        


//kitchen room load products


//load kitchen room models
 
      let kitchenlitag = document.getElementById("kitchen-litag");   
        document.getElementById("kitchen-ultag").innerHTML = "";

         
                 json.modelList.forEach(model=>{
                     if(model.category.name == "Kitchen Room"){
                        
                 let kitchenlitagClone =  kitchenlitag.cloneNode(true);
                       
                       kitchenlitagClone.querySelector("#a2").innerHTML = model.name;
                       kitchenlitagClone.querySelector("#a2").href = "product-grid-sidebar-left.html?id=" + model.id;
                       kitchenlitagClone.querySelector("#a2").className ="dark";
                         
                      
                          document.getElementById("kitchen-ultag").appendChild(kitchenlitagClone);
                     }
                    
                 });
                 
           
//load kitchen room models






 
 
 
    }else{
        
        console.log("no ");
        
    }  
        
        
}


//load living room Products







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









async function viewWishlist(){
    const response = await fetch("user-wishlist.html");
    
    if(response.ok){
         document.getElementById("index-header").className = "margin-bottom:50px;";
      const WishlistHtmlText =  await response.text();
        const paser = new DOMParser();
        const wishlistHtml = paser.parseFromString(WishlistHtmlText,"text/html");
        
       const wishlistMain = wishlistHtml.querySelector("#wishlist-main");
      
       document.querySelector("#index-content").innerHTML = wishlistMain.innerHTML;
    }
    
}





