

async function loadProduct() {

    const parameter = new URLSearchParams(window.location.search);

    if (parameter.has("id")) {

        const productId = parameter.get("id");

        const response = await fetch("LoadSingleProduct?id=" + productId);


        if (response.ok) {

            const json = await response.json();
            console.log(json.product);
            console.log(json.productList);


            const id = json.product.id;
            document.getElementById("image1").src = "product-images/" + id + "/image1.jpg";
            document.getElementById("image2").src = "product-images/" + id + "/image2.jpg";
            document.getElementById("image3").src = "product-images/" + id + "/image3.jpg";

            document.getElementById("thumb-image1").src = "product-images/" + id + "/image1.jpg";
            document.getElementById("thumb-image2").src = "product-images/" + id + "/image2.jpg";
            document.getElementById("thumb-image3").src = "product-images/" + id + "/image3.jpg";


            document.getElementById("zoom-image1").src = "product-images/" + id + "/image1.jpg";
            document.getElementById("zoom-image2").src = "product-images/" + id + "/image2.jpg";
            document.getElementById("zoom-image3").src = "product-images/" + id + "/image3.jpg";

            document.getElementById("zoom-sub-image1").src = "product-images/" + id + "/image1.jpg";
            document.getElementById("zoom-sub-image2").src = "product-images/" + id + "/image2.jpg";
            document.getElementById("zoom-sub-image3").src = "product-images/" + id + "/image3.jpg";


            document.getElementById("product-title").innerHTML = json.product.title;
            document.getElementById("product-publish").innerHTML = json.product.date_time;
            document.getElementById("product-price").innerHTML = new Intl.NumberFormat(
                    "en-US",
                    {
                        minimumFractionDigits: 2
                    }
            ).format(json.product.price);

            document.getElementById("product-brand").innerHTML = json.product.model.category.name;
            document.getElementById("product-model").innerHTML = json.product.model.name;
            document.getElementById("product-condition").innerHTML = json.product.condition_id.name;
            document.getElementById("product-qty").innerHTML = json.product.qty;

            document.getElementById("color-boder").style.backgroundColor = json.product.color.name;
            document.getElementById("product-material").innerHTML = json.product.material.value;
            document.getElementById("product-description").innerHTML = json.product.description;



            document.getElementById("add-to-cart-main").addEventListener(
                    "click",
                    (e) => {
                addToCart(json.product.id, document.getElementById("add-to-cart-qty").value);
                e.preventDefault();
            }
            );

            let productHtml = document.getElementById("similer-product");
            document.getElementById("similer-product-main").innerHTML = "";


            json.productList.forEach(item => {

                let productCloneHtml = productHtml.cloneNode(true);

                productCloneHtml.querySelector("#similer-product-a1").href = "product-detail.html?id=" + item.id;
                productCloneHtml.querySelector("#similer-product-image").src = "product-images/" + item.id + "/image1.jpg";
                productCloneHtml.querySelector("#similer-product-image2").src = "product-images/" + item.id + "/image2.jpg";
                productCloneHtml.querySelector("#similer-product-a2").href = "product-detail.html?id=" + item.id;
                productCloneHtml.querySelector("#similer-product-title").innerHTML = item.title;
                productCloneHtml.querySelector("#similer-product-condition").innerHTML = item.condition_id.name;
                productCloneHtml.querySelector("#similer-product-price").innerHTML = "Rs. " + new Intl.NumberFormat(
                        "en-US",
                        {
                            minimumFractionDigits: 2
                        }

                ).format(item.price);



                productCloneHtml.querySelector("#add-to-cart-similerProduct").addEventListener(
                        "click",
                        (e) => {
                    addToCart(item.id, 1);
                    e.preventDefault();
                }
                );


                document.getElementById("similer-product-main").appendChild(productCloneHtml);



            });



        } else {
            window.location = "index.html";
        }



    } else {
        window.location = "index.html";
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
